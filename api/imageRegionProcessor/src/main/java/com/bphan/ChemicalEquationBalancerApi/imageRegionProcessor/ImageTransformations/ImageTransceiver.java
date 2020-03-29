package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.amazonaws.services.s3.model.S3Object;
import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsS3Client;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor.ImageRegionExtractor;

import org.springframework.beans.factory.annotation.Autowired;

public class ImageTransceiver {
    
    @Autowired
    private AwsS3Client s3Client;

    private Logger logger = Logger.getLogger(ImageRegionExtractor.class.getName());

    public BufferedImage download(String filename) {
        S3Object imageObject = s3Client.getObjectFromS3Bucket(filename);
        BufferedImage image = convertToBufferedImage(imageObject);

        closeS3Object(imageObject);
        
        return image;
    }

    public String upload(String filename, BufferedImage image) {
        return this.s3Client.uploadImage(filename, image);
    }

    private BufferedImage convertToBufferedImage(S3Object imageObject) {
        try {
            return ImageIO.read(imageObject.getObjectContent());
        } catch (Exception e) {
            logger.warning(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void closeS3Object(S3Object imageObject) {
        try {
            imageObject.close();
        } catch (Exception e) {
            logger.warning(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
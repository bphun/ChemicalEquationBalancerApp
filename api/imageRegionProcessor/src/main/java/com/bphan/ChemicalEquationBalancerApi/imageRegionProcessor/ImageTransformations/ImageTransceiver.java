package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.amazonaws.services.s3.model.S3Object;
import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsS3Client;

import org.springframework.beans.factory.annotation.Autowired;

public class ImageTransceiver {
    @Autowired
    private AwsS3Client s3Client;

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
            e.printStackTrace();
        }
        return null;
        // try {
        // ImageInputStream iis =
        // ImageIO.createImageInputStream(imageObject.getObjectContent());
        // Iterator readers = ImageIO.getImageReaders(iis);
        // ImageReader reader = (ImageReader) readers.next();
        // reader.setInput(iis, false);
        // BufferedImage img = reader.read(0);
        // return img;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return null;

        // try (S3ObjectInputStream s3ObjectInputStream =
        // imageObject.getObjectContent()) {
        // Long fileSize = imageObject.getObjectMetadata().getContentLength();
        // byte[] bytes = new byte[fileSize.intValue()];
        // try {
        // s3ObjectInputStream.read(bytes);
        // } catch (Exception e) {
        // e.printStackTrace();
        // } finally {
        // while (s3ObjectInputStream != null && s3ObjectInputStream.read() != -1) {
        // s3ObjectInputStream.read(bytes);
        // }
        // }
        // } catch (Exception e) {

        // }

        // iis = ImageIO.createImageInputStream(s3ObjectInputStream);
        // image = ImageIO.read(iis);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return image;
        // try {
        // ImageInputStream iis =
        // ImageIO.createImageInputStream(imageObject.getObjectContent());
        // return ImageIO.read(iis);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    private void closeS3Object(S3Object imageObject) {
        try {
            imageObject.close();
        } catch (Exception e) {

        }
    }
}
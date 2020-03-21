package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations;

import java.awt.image.BufferedImage;

import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsS3Client;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models.ImageTransformerResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class ImageTransformer {

    @Autowired
    private AwsS3Client s3Client;

    private ImageOperations imageOperations = new ImageOperations();

    public ImageTransformerResponse rotate(String requestId, BufferedImage image, String newFilename, double radians) {
        BufferedImage rotatedImage = this.imageOperations.rotate(image, radians);
        String fileName = newFilename != null ? newFilename : requestId;
        String fileUrl = this.s3Client.uploadImage(fileName, rotatedImage);

        boolean success = fileUrl != null;

        return new ImageTransformerResponse(success ? "success" : "error", "", requestId, fileUrl);
    }

    public ImageTransformerResponse scale(String requestId, BufferedImage image, String newFilename, int scaleWidth,
            int scaleHeight, double scaleOriginX, double scaleOriginY) {
        BufferedImage scaledImage = this.imageOperations.scale(image, scaleOriginX, scaleOriginY, scaleWidth,
                scaleHeight);
        String fileName = newFilename != null ? newFilename : requestId;
        String fileUrl = this.s3Client.uploadImage(fileName, scaledImage);

        boolean success = fileUrl != null;

        return new ImageTransformerResponse(success ? "success" : "error", "", requestId, fileUrl);
    }
}
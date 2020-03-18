package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageOperations {

    public ImageOperations() {
    }

    public BufferedImage rotate(BufferedImage image, double radians) {
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = image.getWidth();
        int height = image.getHeight();
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, image.getType());
        AffineTransform at = new AffineTransform();

        at.translate((newWidth - width) / 2, (newHeight - height) / 2);
        at.rotate(radians, width / 2, height / 2);

        AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        rotateOp.filter(image, rotatedImage);

        return rotatedImage;
    }

    public BufferedImage scale(BufferedImage image, double scaleOriginX, double scaleOriginY, int scaledWidth,
            int scaledHeight) {
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
        AffineTransform at = new AffineTransform();

        at.translate(-scaleOriginX, -scaleOriginY);

        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        scaleOp.filter(image, scaledImage);

        return scaledImage;
    }

}
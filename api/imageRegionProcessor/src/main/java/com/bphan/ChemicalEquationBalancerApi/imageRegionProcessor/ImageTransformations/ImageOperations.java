package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageOperations {

  public ImageOperations() {}

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

  public BufferedImage scale(
      BufferedImage image,
      double scaleOriginX,
      double scaleOriginY,
      int scaledWidth,
      int scaledHeight) {
    BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
    AffineTransform at = new AffineTransform();

    at.translate(-scaleOriginX, -scaleOriginY);
    // at.scale(scaledWidth, scaledHeight);

    AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

    scaleOp.filter(image, scaledImage);

    return scaledImage;
  }

  public BufferedImage resize(BufferedImage image, int scaledWidth, int scaledHeight) {
    // Make sure the aspect ratio is maintained, so the image is not distorted
    double thumbRatio = (double) scaledWidth / (double) scaledHeight;
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    double aspectRatio = (double) imageWidth / (double) imageHeight;

    if (thumbRatio < aspectRatio) {
      scaledHeight = (int) (scaledWidth / aspectRatio);
    } else {
      scaledWidth = (int) (scaledHeight * aspectRatio);
    }

    // Draw the scaled image
    BufferedImage newImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
    Graphics2D graphics2D = newImage.createGraphics();
    graphics2D.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);

    return newImage;
  }
}

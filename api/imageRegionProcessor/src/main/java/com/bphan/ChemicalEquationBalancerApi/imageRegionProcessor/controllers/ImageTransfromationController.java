package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.controllers;

import java.awt.image.BufferedImage;

import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageOperations;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageTransceiver;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models.ImageTransformerResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("imageProcessor/transform")
public class ImageTransfromationController {

    @Autowired
    private ImageOperations imageOperations;

    @Autowired
    private ImageTransceiver imageTransceiver;

    private final String frontendHostname = "*";
    
    @CrossOrigin(origins = frontendHostname)
    @RequestMapping("/rotate")
    public ImageTransformerResponse rotateImage(@RequestParam(value = "rid", required = true) String requestId,
            @RequestParam(value = "r", required = true) double radians,
            @RequestParam(value = "n", required = false) String newFilename) {
        BufferedImage image = imageTransceiver.download(requestId + ".png");

        BufferedImage rotatedImage = imageOperations.rotate(image, radians);

        String fileName = newFilename != null ? newFilename : requestId;
        String fileUrl = imageTransceiver.upload(fileName, rotatedImage);

        boolean success = fileUrl != null;

        return new ImageTransformerResponse(success ? "success" : "error", "", requestId, fileUrl);
    }

    @CrossOrigin(origins = frontendHostname)
    @RequestMapping("/scale")
    public ImageTransformerResponse scaleImage(@RequestParam(value = "rid", required = true) String requestId,
            @RequestParam(value = "sx", required = true) int scaleWidth,
            @RequestParam(value = "sy", required = true) int scaleHeight,
            @RequestParam(value = "sox", required = false, defaultValue = "0") int scaleOriginX,
            @RequestParam(value = "soy", required = false, defaultValue = "0") int scaleOriginY,
            @RequestParam(value = "n", required = false) String newFilename) {

        BufferedImage image = imageTransceiver.download(requestId + ".png");

        BufferedImage scaledImage = imageOperations.scale(image, scaleOriginX, scaleOriginY, scaleWidth, scaleHeight);

        String fileName = newFilename != null ? newFilename : requestId;
        String fileUrl = imageTransceiver.upload(fileName, scaledImage);

        boolean success = fileUrl != null;

        return new ImageTransformerResponse(success ? "success" : "error", "", requestId, fileUrl);
    }
}
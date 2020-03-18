package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.controllers;

import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageTransformer;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models.ImageTransformerResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("imageProcessor/transform")
public class ImageTransfromationController {

    @Autowired
    private ImageTransformer imageTransformer;

    @RequestMapping("/rotate")
    public ImageTransformerResponse rotateImage(@RequestParam(value = "rid", required = true) String requestId,
            @RequestParam(value = "r", required = true) double radians,
            @RequestParam(value = "n", required = false) String newFilename) {

        return this.imageTransformer.rotate(requestId, newFilename, radians);
    }

    @RequestMapping("/scale")
    public ImageTransformerResponse scaleImage(@RequestParam(value = "rid", required = true) String requestId,
            @RequestParam(value = "sx", required = true) int scaleWidth,
            @RequestParam(value = "sy", required = true) int scaleHeight,
            @RequestParam(value = "sox", required = false, defaultValue = "0") int scaleOriginX,
            @RequestParam(value = "soy", required = false, defaultValue = "0") int scaleOriginY,
            @RequestParam(value = "n", required = false) String newFilename) {

        return this.imageTransformer.scale(requestId, newFilename, scaleWidth, scaleHeight, scaleOriginX, scaleOriginY);
    }
}
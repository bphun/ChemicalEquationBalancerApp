package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageOperations;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageTransceiver;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.apiInterfaces.ImageProcessorApiInterface;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models.ImageTransformerResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class ImageRegionExtractor {

    @Autowired
    private ImageTransceiver imageTransceiver;

    @Autowired
    private ImageOperations imageOperations;

    @Autowired
    private ImageProcessorApiInterface imageProcessorApiInterface;
    
    public ImageRegionExtractor() {
    }

    public Map<String, List<ImageTransformerResponse>> createRegionImages(List<ImageRegion> regions,
            boolean includeRegionInResponse) {
        Map<String, List<ImageTransformerResponse>> response = new HashMap<>();
        Map<String, BufferedImage> imageCache = new HashMap<>(); // This is going to require a lot of memory once there
                                                                 // are alot of images

        regions.parallelStream().forEach(region -> {
            String requestId = region.getRequestInfoId();
            BufferedImage regionParentImage = imageCache.get(requestId);

            if (regionParentImage == null) {
                regionParentImage = imageTransceiver.download(requestId + ".png");
                imageCache.put(requestId, regionParentImage);
            }

            if (response.get(requestId) == null) {
                response.put(requestId, new ArrayList<>());
            }

            BufferedImage regionImage = createRegionImage(requestId, regionParentImage, region);

            boolean success = regionImage != null;
            String newFileName = requestId + "_" + region.getId();
            String regionImageUrl = "";

            if (success) {
                regionImageUrl = imageTransceiver.upload(newFileName, regionImage);
                imageProcessorApiInterface.setImageUrlForRegion(region.getId(), regionImageUrl);
            }

            ImageTransformerResponse transformerResponse = new ImageTransformerResponse(success ? "success" : "error",
                    "", requestId, region.getId(), regionImageUrl);

            if (includeRegionInResponse) {
                transformerResponse.setRegion(region);
            }

            response.get(requestId).add(transformerResponse);
        });

        return response;
    }

    public BufferedImage createRegionImage(String requestId, BufferedImage image, ImageRegion region) {
        // Have to do some transformations of the region origin and dimensions since the
        // image displayed in the management console is not the full size image.

        double parentImageWidth = region.getParentImageWidth();
        double parentImageHeight = region.getParentImageHeight();

        double viewportWidth = region.getViewportWidth();
        double viewportHeight = region.getViewportHeight();

        double widthScaleFactor = parentImageWidth / viewportWidth;
        double heightScaleFactor = parentImageHeight / viewportHeight;

        int regionWidth = (int) (widthScaleFactor * region.getWidth());
        int regionHeight = (int) (heightScaleFactor * region.getHeight());

        double scaleOriginX = widthScaleFactor * region.getOriginX();
        double scaleOriginY = (heightScaleFactor * region.getOriginY());

        BufferedImage resultImage = null;

        if (parentImageWidth > 0 && parentImageHeight > 0 && viewportWidth > 0 && viewportHeight > 0 && regionWidth > 0
                && regionHeight > 0) {
            resultImage = imageOperations.scale(image, scaleOriginX, scaleOriginY, regionWidth, regionHeight);
            if ((regionWidth > regionHeight) && (regionWidth > 600 || regionHeight > 100)) {
                resultImage = imageOperations.resize(resultImage, (int)(regionWidth / widthScaleFactor), (int)(regionHeight / heightScaleFactor));
            } else if ((regionWidth < regionHeight) && (regionWidth > 100 || regionHeight > 600)) {
                resultImage = imageOperations.resize(resultImage, (int)(regionWidth / widthScaleFactor), (int)(regionHeight / heightScaleFactor));
            }
        }

        return resultImage;
    }

}
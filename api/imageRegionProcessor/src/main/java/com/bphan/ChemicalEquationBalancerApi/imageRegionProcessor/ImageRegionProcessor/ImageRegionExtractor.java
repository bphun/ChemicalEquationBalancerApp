package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageTransformations.ImageTransformer;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models.ImageTransformerResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class ImageRegionExtractor {

    @Autowired
    private ImageTransformer imageTransformer;

    public ImageRegionExtractor() {
    }

    public Map<String, List<ImageTransformerResponse>> createRegionImages(List<ImageRegion> regions) {
        Map<String, List<ImageTransformerResponse>> response = new HashMap<>();
        
        regions.parallelStream().forEach(region -> {
            String requestId = region.getRequestInfoId();

            if (response.get(requestId) == null) {
                List<ImageTransformerResponse> responses = new ArrayList<>();
                response.put(requestId, responses);
            }
            
            response.get(requestId).add(createRegionImage(requestId, region));
        });

        return response;
    }

    public ImageTransformerResponse createRegionImage(String requestId, ImageRegion region) {

        String newFileName = requestId + "_" + region.getId();

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

        ImageTransformerResponse scaleResponse;

        if (parentImageWidth <= 0 || parentImageHeight <= 0 || viewportWidth <= 0 || viewportHeight <= 0
                || regionWidth <= 0 || regionHeight <= 0) {
            scaleResponse = new ImageTransformerResponse("error", "Inalid viewport, parent image, or region dimension",
                    requestId, "");
        } else {
            scaleResponse = imageTransformer.scale(requestId, newFileName, regionWidth, regionHeight, scaleOriginX,
                    scaleOriginY);
        }

        scaleResponse.setRegionId(region.getId());

        return scaleResponse;
    }

}
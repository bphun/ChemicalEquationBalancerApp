package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.ImageRegionProcessor.ImageRegionExtractor;
import com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models.ImageTransformerResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("imageProcessor/extract")
public class ImageRegionProcessorController {

    @Autowired
    ImageRegionExtractor imageRegionExtractor;

    @Value("${requestsApi.hostname}")
    private String requestsApiHostname;

    @Value("${requestsApi.allRegionsEndpoint}")
    private String allRegionsEndpoint;

    @Value("${requestsApi.selectRegionsEndpoint}")
    private String selectRegionsEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    private String selectRegionFetchUrl = "";
    private String allRegionsFetchUrl = "";

    @PostConstruct
    public void init() {
        selectRegionFetchUrl = requestsApiHostname + selectRegionsEndpoint;
        allRegionsFetchUrl = requestsApiHostname + allRegionsEndpoint;
    }

    @GetMapping("/regions")
    public Map<String, List<ImageTransformerResponse>> getCroppedRegionsForRequest(
            @RequestParam(value = "rid", required = true) String requestId) {
        String url = selectRegionFetchUrl + "?rid=" + requestId;
        List<ImageRegion> regions = fetchRegionsFromUrl(url);

        return extractRegionImagesFromRegionList(regions);
    }

    @GetMapping("/regions/all")
    public Map<String, List<ImageTransformerResponse>> getAllCroppedRegions() {
        List<ImageRegion> regions = fetchRegionsFromUrl(allRegionsFetchUrl);

        return extractRegionImagesFromRegionList(regions);
    }

    private Map<String, List<ImageTransformerResponse>> extractRegionImagesFromRegionList(List<ImageRegion> regions) {
        if (regions.size() == 0) {
            return null;
        }

        return imageRegionExtractor.createRegionImages(regions);
    }

    private List<ImageRegion> fetchRegionsFromUrl(String url) {
        return Arrays.asList(this.restTemplate.getForObject(url, ImageRegion[].class));
    }
}
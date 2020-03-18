package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.controllers;

import java.util.List;

import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.jdbc.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels.RegionDiff;
import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
public class RegionsController {

    @Autowired
    private ImageProcessorRequestRepository imageProcessorRequestRepository;

    private final String frontendHostname = "*";

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/")
    public List<ImageRegion> regionsForRequest(@RequestParam(value = "rid", required = true) String requestId) {
        return imageProcessorRequestRepository.getRegionsForRequest(requestId);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/all/")
    public List<ImageRegion> allRegions() {
        return imageProcessorRequestRepository.getAllRegions();
    }

    // @CrossOrigin(origins = frontendHostname)
    // @GetMapping("/all/")
    // public HashMap<String, List<ImageRegion>> allRegions() {
    // List<String> requestIds = imageProcessorRequestRepository.getRequestIdList();
    // HashMap<String, List<ImageRegion>> regions = new HashMap<String,
    // List<ImageRegion>>();

    // for (String requestId : requestIds) {
    // regions.put(requestId,
    // imageProcessorRequestRepository.getRegionsForRequest(requestId));
    // }

    // return regions;
    // }

    @CrossOrigin(origins = frontendHostname)
    @PostMapping("/update/")
    public ApiResponse updateRegions(@RequestBody RegionDiff regionDiff) {
        return imageProcessorRequestRepository.updateRegions(regionDiff);
    }
}
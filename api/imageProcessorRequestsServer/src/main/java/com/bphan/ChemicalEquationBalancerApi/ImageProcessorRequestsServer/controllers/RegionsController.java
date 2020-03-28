package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

    @GetMapping("/updateValue")
    public ApiResponse setS3ImageUrl(@RequestParam(value = "rid", required = true) String id,
            @RequestParam(value = "vid", required = true) String valueId,
            @RequestParam(value = "v", required = true) String value, HttpServletResponse response) {
        ApiResponse responseBody;

        switch (valueId) {
            case "imgUrl":
                responseBody = imageProcessorRequestRepository.updateS3ImageUrlForRegion(id, value);
                break;
            case "equStr":
                responseBody = imageProcessorRequestRepository.updateEquationStrForRegion(id, value);
            default:
                responseBody = new ApiResponse("error", "Invalid value ID");
                break;
        }

        return responseBody;
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
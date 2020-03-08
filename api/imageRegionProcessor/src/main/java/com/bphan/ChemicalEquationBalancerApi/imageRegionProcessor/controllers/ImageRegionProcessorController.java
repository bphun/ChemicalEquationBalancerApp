package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regionProcessor")
public class ImageRegionProcessorController {

    @GetMapping("/croppedRegions")
    public void getCroppedRegionsForRequest(@RequestParam(value = "rid", required = true) String requestId) {
        
    }
}
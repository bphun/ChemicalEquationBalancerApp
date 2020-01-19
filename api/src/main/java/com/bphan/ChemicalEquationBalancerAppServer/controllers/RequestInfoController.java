package com.bphan.ChemicalEquationBalancerAppServer.controllers;

import java.util.List;

import com.bphan.ChemicalEquationBalancerAppServer.DatabaseConnector.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.StoredRequestInfo;
import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.StoredRequestInfoApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storedRequests")
public class RequestInfoController {

    @Autowired
    private ImageProcessorRequestRepository imageProcessorRequestRepository;

    @RequestMapping("/list")
    public List<StoredRequestInfo> getStoredRequestInfoList() {
        return imageProcessorRequestRepository.getRequestList();
    }

    @RequestMapping("/updateValue")
    public StoredRequestInfoApiResponse updateValue(@RequestParam(value = "rid", required = true) String id,
            @RequestParam(value = "vid", required = true) String valueId,
            @RequestParam(value = "v", required = true) String value) {
        StoredRequestInfoApiResponse response;
        switch (valueId) {
        case "userInputtedChemicalEquationString":
            response = imageProcessorRequestRepository.updateUserInputtedChemicalEquationString(id, value);
            break;
        case "verifiedChemicalEquationString":
            response = imageProcessorRequestRepository.updateVerifiedChemicalEquationString(id, value);
            break;
        case "gcpIdentifiedChemicalEquationString":
            response = imageProcessorRequestRepository.updateGcpIdentifiedChemicalEquationString(id, value);
            break;
        case "onDeviceImageProcessStartTime":
            response = imageProcessorRequestRepository.updateOnDeviceImageProcessStartTime(id, value);
            break;
        case "onDeviceImageProcessEndTime":
            response = imageProcessorRequestRepository.updateOnDeviceImageProcessEndTime(id, value);
            break;
        case "onDeviceImageProcessDeviceName":
            response = imageProcessorRequestRepository.updateOnDeviceImageProcessDeviceName(id, value);
            break;
        default:
            response = new StoredRequestInfoApiResponse("error", "Invalid value ID");
            break;
        }

        return response;
    }

    @RequestMapping("/updateVerification")
    public StoredRequestInfoApiResponse updateVerification(@RequestParam(value = "rid", required = true) String id,
            @RequestParam(value = "vs", required = true) boolean verificationStatus) {
        return imageProcessorRequestRepository.updateVerificationStatus(id, verificationStatus);
    }
}
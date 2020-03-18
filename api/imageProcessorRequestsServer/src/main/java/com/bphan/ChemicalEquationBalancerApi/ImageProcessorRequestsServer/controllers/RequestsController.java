package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.jdbc.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels.StoredRequestInfo;
import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsS3Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests")
public class RequestsController {

    @Autowired
    private ImageProcessorRequestRepository imageProcessorRequestRepository;

    @Autowired
    private AwsS3Client amazonClient;

    private final String frontendHostname = "*";

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/list")
    public List<StoredRequestInfo> getStoredRequestInfoList() {
        return imageProcessorRequestRepository.getRequestList();
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/info")
    public StoredRequestInfo getStoredRequestInfoList(@RequestParam(value = "rid", required = true) String id) {
        return imageProcessorRequestRepository.getRequestWithId(id);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/updateValue")
    public ApiResponse updateValue(@RequestParam(value = "rid", required = true) String id,
            @RequestParam(value = "vid", required = true) String valueId,
            @RequestParam(value = "v", required = true) String value, HttpServletResponse response) {
        ApiResponse responseBody;
        switch (valueId) {
            case "userInputtedChemicalEquationString":
                responseBody = imageProcessorRequestRepository.updateUserInputtedChemicalEquationString(id, value);
                break;
            case "verifiedChemicalEquationString":
                responseBody = imageProcessorRequestRepository.updateVerifiedChemicalEquationString(id, value);
                break;
            case "gcpIdentifiedChemicalEquationString":
                responseBody = imageProcessorRequestRepository.updateGcpIdentifiedChemicalEquationString(id, value);
                break;
            case "onDeviceImageProcessStartTime":
                responseBody = imageProcessorRequestRepository.updateOnDeviceImageProcessStartTime(id, value);
                break;
            case "onDeviceImageProcessEndTime":
                responseBody = imageProcessorRequestRepository.updateOnDeviceImageProcessEndTime(id, value);
                break;
            case "onDeviceImageProcessDeviceName":
                responseBody = imageProcessorRequestRepository.updateOnDeviceImageProcessDeviceName(id, value);
                break;
            case "labelingStatus":
                responseBody = imageProcessorRequestRepository.updateLabelingStatusForRequest(id, value);
                break;
            default:
                responseBody = new ApiResponse("error", "Invalid value ID");
                break;
        }

        return responseBody;
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/next")
    public ApiResponse nextRequest(@RequestParam(value = "t", required = true) String timestamp) {
        return imageProcessorRequestRepository.getNextRequestIdAfterTimestamp(timestamp);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/previous")
    public ApiResponse previousRequest(@RequestParam(value = "t", required = true) String timestamp) {
        return imageProcessorRequestRepository.getPreviousRequestIdBeforeTimestamp(timestamp);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/delete")
    public ApiResponse deleteRequest(@RequestParam(value = "rid", required = true) String requestId) {
        ApiResponse response = imageProcessorRequestRepository.deleteRequest(requestId);

        if (response.getStatus() == "success") {
            String deleteImageResponse = amazonClient.deleteImageByName(requestId);
            if (deleteImageResponse != "success") {
                response.setStatus("error");
                response.setDescription("Unable to delete image");
            }
        } else {
            response.setStatus("error");
            response.setDescription("Unable to delete request");
        }

        return response;
    }
}
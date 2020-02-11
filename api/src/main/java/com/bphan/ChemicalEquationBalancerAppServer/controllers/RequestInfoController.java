package com.bphan.ChemicalEquationBalancerAppServer.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.bphan.ChemicalEquationBalancerAppServer.DatabaseConnector.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.BoundingBox;
import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.BoundingBoxDiff;
import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.StoredRequestInfo;
import com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels.StoredRequestInfoApiResponse;
import com.bphan.ChemicalEquationBalancerAppServer.Services.AmazonClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storedRequests")
public class RequestInfoController {

    @Autowired
    private ImageProcessorRequestRepository imageProcessorRequestRepository;

    @Autowired
    private AmazonClient amazonClient;

    private final String frontendHostname = "${crossOrigin.frontendHostname}";
    
    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/list")
    public List<StoredRequestInfo> getStoredRequestInfoList() {
        return imageProcessorRequestRepository.getRequestList();
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/view")
    public StoredRequestInfo getStoredRequestInfoList(@RequestParam(value = "rid", required = true) String id) {
        return imageProcessorRequestRepository.getRequestWithId(id);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/updateValue")
    public StoredRequestInfoApiResponse updateValue(@RequestParam(value = "rid", required = true) String id,
            @RequestParam(value = "vid", required = true) String valueId,
            @RequestParam(value = "v", required = true) String value,
            HttpServletResponse response) {
        StoredRequestInfoApiResponse responseBody;
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
        default:
        responseBody = new StoredRequestInfoApiResponse("error", "Invalid value ID");
            break;
        }

        return responseBody;
    }

    @CrossOrigin(origins = frontendHostname)
    @PostMapping("/updateBoundingBoxes")
    public StoredRequestInfoApiResponse addBoundingBox(@RequestBody BoundingBoxDiff boundingBoxes) {
        return imageProcessorRequestRepository.updateBoundingBoxes(boundingBoxes);
    }

    @CrossOrigin(origins = frontendHostname)
    @PostMapping("/addBoundingBox")
    public StoredRequestInfoApiResponse addBoundingBox(@RequestBody BoundingBox boundingBox) {
        return imageProcessorRequestRepository.addBoundingBox(boundingBox);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/getBoundingBoxes")
    public List<BoundingBox> addBoundingBox(@RequestParam(value = "rid", required = true) String requestId) {
        return imageProcessorRequestRepository.getBoundingBoxesForRequest(requestId);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/nextRequest")
    public StoredRequestInfoApiResponse nextRequest(@RequestParam(value = "t", required = true) String timestamp) {
        return imageProcessorRequestRepository.getNextRequestIdAfterTimestamp(timestamp);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/previousRequest")
    public StoredRequestInfoApiResponse previousRequest(@RequestParam(value = "t", required = true) String timestamp) {
        return imageProcessorRequestRepository.getPreviousRequestIdBeforeTimestamp(timestamp);
    }

    @CrossOrigin(origins = frontendHostname)
    @GetMapping("/deleteRequest")
    public StoredRequestInfoApiResponse deleteRequest(@RequestParam(value = "rid", required = true) String requestId) {
        StoredRequestInfoApiResponse response = imageProcessorRequestRepository.deleteRequest(requestId);

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
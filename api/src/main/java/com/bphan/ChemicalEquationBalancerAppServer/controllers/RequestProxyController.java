package com.bphan.ChemicalEquationBalancerAppServer.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.bphan.ChemicalEquationBalancerAppServer.DatabaseConnector.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerAppServer.Models.RequestModels.ImageProcessorRequest;
import com.bphan.ChemicalEquationBalancerAppServer.Models.ResponseModels.ImageProcessorResponse;
import com.bphan.ChemicalEquationBalancerAppServer.Services.AmazonClient;
import com.bphan.ChemicalEquationBalancerAppServer.Threads.RequestInfoUploadRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class RequestProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyCjcvFqGbD6kaQWy9g5kGLxWK0wvhr4l6k";

    @Autowired
    private ImageProcessorRequestRepository imageProcessorRequestRepository;

    @Autowired
    private AmazonClient amazonClient;

    @RequestMapping(value = "/proxy", method = RequestMethod.POST)
    public ResponseEntity<ImageProcessorResponse> proxy(@RequestBody ImageProcessorRequest requestBody,
            @RequestHeader("Content-Length") String contentLength,
            @RequestParam(value = "uploadImg", required = false) boolean shouldUploadImg,
            HttpServletResponse response) {

        HttpHeaders gcpApiRequestHeaders = new HttpHeaders();
        HttpEntity<ImageProcessorRequest> postEntity;
        ImageProcessorResponse imageProcessorResponse;
        long requestStartTime, requestEndTime;
        String requestId = UUID.randomUUID().toString();
        String base64EncodedImage = requestBody.getRequests().get(0).getImage().getContent();
        RequestInfoUploadRunner requestInfoUploadRunner;

        gcpApiRequestHeaders.setContentLength(Integer.parseInt(contentLength));
        gcpApiRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
        postEntity = new HttpEntity<>(requestBody, gcpApiRequestHeaders);

        requestStartTime = System.currentTimeMillis();
        imageProcessorResponse = this.restTemplate.postForObject(URL, postEntity, ImageProcessorResponse.class);
        requestEndTime = System.currentTimeMillis();

        imageProcessorResponse.setRequestId(requestId);

        // if (shouldUploadImg) {
        requestInfoUploadRunner = new RequestInfoUploadRunner(imageProcessorRequestRepository, amazonClient, requestId,
                base64EncodedImage, requestStartTime, requestEndTime);
        new Thread(requestInfoUploadRunner).start();
        // }

        return ResponseEntity.ok(imageProcessorResponse);
    }
}
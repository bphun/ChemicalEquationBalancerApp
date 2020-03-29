package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.jdbc.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.requestModels.ImageProcessorRequest;
import com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.responseModels.ImageProcessorResponse;
import com.bphan.ChemicalEquationBalancerApi.common.amazon.AwsS3Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    private String url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyDNKE28nRi-Qt1QeOtsPUBAFHBDN8ikhcI";

    private final String frontendHostname = "*";
    
    @Autowired
    private ImageProcessorRequestRepository imageProcessorRequestRepository;

    @Autowired
    private AwsS3Client amazonClient;

    @CrossOrigin(origins = frontendHostname)
    @RequestMapping(value = "/proxy", method = RequestMethod.POST)
    public ResponseEntity<ImageProcessorResponse> proxy(@RequestBody ImageProcessorRequest requestBody,
            @RequestHeader("Content-Length") String contentLength,
            @RequestParam(value = "upload", required = false) boolean shouldUploadImg,
            @RequestParam(value = "eq", required = false, defaultValue = "") String equationString,
            HttpServletResponse response) {

        HttpHeaders gcpApiRequestHeaders = new HttpHeaders();
        HttpEntity<ImageProcessorRequest> postEntity;
        ImageProcessorResponse imageProcessorResponse;
        long requestStartTime, requestEndTime;
        String requestId = UUID.randomUUID().toString();
        String base64EncodedImage = requestBody.getRequests().get(0).getImage().getContent();

        gcpApiRequestHeaders.setContentLength(Integer.parseInt(contentLength));
        gcpApiRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
        postEntity = new HttpEntity<>(requestBody, gcpApiRequestHeaders);

        requestStartTime = System.currentTimeMillis();
        imageProcessorResponse = this.restTemplate.postForObject(url, postEntity, ImageProcessorResponse.class);
        requestEndTime = System.currentTimeMillis();

        imageProcessorResponse.setRequestId(requestId);

        if (shouldUploadImg) {
            String imageUrl = amazonClient.uploadImage(requestId, base64EncodedImage);
            this.imageProcessorRequestRepository.uploadRequestInfo(requestId, imageUrl, requestStartTime, requestEndTime, equationString);
        }

        return ResponseEntity.ok(imageProcessorResponse);
    }

    // Decodes a URL encoded string using `UTF-8`
    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
        }
        return value;
    }

}
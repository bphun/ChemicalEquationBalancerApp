package com.bphan.ChemicalEquationBalancerAppServer.Threads;

import com.bphan.ChemicalEquationBalancerAppServer.DatabaseConnector.ImageProcessorRequestRepository;
import com.bphan.ChemicalEquationBalancerAppServer.Services.AmazonClient;

public class RequestInfoUploadRunner implements Runnable {
    private String requestId, base64EncodedImage, equationString;
    private long requestStartTime, requestEndTime;
    private ImageProcessorRequestRepository imageProcessorRequestRepository;
    private AmazonClient amazonClient;

    public RequestInfoUploadRunner(ImageProcessorRequestRepository imageProcessorRequestRepository,
            AmazonClient amazonClient, String requestId, String base64EncodedImage, long requestStartTime,
            long requestEndTime, String equationString) {
        this.imageProcessorRequestRepository = imageProcessorRequestRepository;
        this.amazonClient = amazonClient;
        this.requestId = requestId;
        this.base64EncodedImage = base64EncodedImage;
        this.requestStartTime = requestStartTime;
        this.requestEndTime = requestEndTime;
        this.equationString = equationString;
    }

    public void run() {
        String s3ImageUrl = amazonClient.uploadImage(requestId, base64EncodedImage);
        this.imageProcessorRequestRepository.uploadRequestInfo(requestId, s3ImageUrl, requestStartTime, requestEndTime, equationString);
    }
}
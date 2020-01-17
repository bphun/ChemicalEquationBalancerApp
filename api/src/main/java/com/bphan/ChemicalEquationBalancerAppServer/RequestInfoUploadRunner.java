package com.bphan.ChemicalEquationBalancerAppServer;

import com.bphan.ChemicalEquationBalancerAppServer.DatabaseConnector.ImageProcessorRequestRepository;

public class RequestInfoUploadRunner implements Runnable {
    private String requestId, base64EncodedImage;
    private long requestStartTime, requestEndTime;
    ImageProcessorRequestRepository imageProcessorRequestRepository;

    public RequestInfoUploadRunner(ImageProcessorRequestRepository imageProcessorRequestRepository, AmazonClient amazonClient, String requestId, String base64EncodedImage, long requestStartTime, long requestEndTime) {
        this.imageProcessorRequestRepository = imageProcessorRequestRepository;
        this.requestId = requestId;
        this.base64EncodedImage = base64EncodedImage;
        this.requestStartTime = requestStartTime;
        this.requestEndTime = requestEndTime;
    }

    public void run() {
        this.imageProcessorRequestRepository.uploadRequestInfo(requestId, base64EncodedImage, requestStartTime, requestEndTime);
    }
}
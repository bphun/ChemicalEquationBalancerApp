package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.requestModels;

import java.util.List;

public class ImageProcessorRequest {
    private List<Request> requests;

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> value) {
        this.requests = value;
    }
}
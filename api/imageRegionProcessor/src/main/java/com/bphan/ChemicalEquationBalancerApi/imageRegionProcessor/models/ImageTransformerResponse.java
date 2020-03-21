package com.bphan.ChemicalEquationBalancerApi.imageRegionProcessor.models;

import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageTransformerResponse extends ApiResponse {
    
    private String fileUrl, requestId, regionId;

    public ImageTransformerResponse(String status, String description, String requestId, String fileUrl) {
        super(status, description);
        this.fileUrl = fileUrl;
        this.requestId = requestId;
        this.regionId = "";
    }

    public ImageTransformerResponse(String status, String description, String requestId, String regionId, String fileUrl) {
        super(status, description);
        this.fileUrl = fileUrl;
        this.requestId = requestId;
        this.regionId = regionId;
    }

    @JsonProperty("fileUrl")
    public String getFileUrl() {
        return this.fileUrl;
    }

    @JsonProperty("fileUrl")
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @JsonProperty("requestId")
    public String getRequestId() {
        return this.requestId;
    }

    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("regionId")
    public String getRegionId() {
        return this.regionId;
    }

    @JsonProperty("regionId")
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

}
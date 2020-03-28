package com.bphan.ChemicalEquationBalancerApi.common.ResponseModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {

    String status;
    String description;
    
    public ApiResponse() {
        this.status = "";
        this.description = "";
    }

    public ApiResponse(String status, String description) {
        this.status = status;
        this.description = description;
    }

    @JsonProperty("status")
    public String getStatus() {
        return this.status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("description")
    public String getDescription() {
        return this.description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

}
package com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class StoredRequestInfoApiResponse {
    String status, description;

    public StoredRequestInfoApiResponse(String status, String description) {
        this.status = status;
        this.description = description;
    }

    @JsonProperty
    String getstatus() {
        return status;
    }

    @JsonProperty
    void setstatus(String status) {
        this.status = status;
    }
    
    @JsonProperty
    String getdescription() {
        return description;
    }

    @JsonProperty
    void setdescription(String description) {
        this.description = description;
    }
    

}
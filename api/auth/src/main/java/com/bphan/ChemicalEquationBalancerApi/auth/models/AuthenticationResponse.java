package com.bphan.ChemicalEquationBalancerApi.auth.models;

import java.util.Calendar;
import java.util.Date;

import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse extends ApiResponse {

    private Date timestamp;

    public AuthenticationResponse(String status, String description) {
        super(status, description);
        this.timestamp = Calendar.getInstance().getTime();
    }

    @JsonProperty("timestamp")
    public Date getTimestamp() {
        return this.timestamp;
    }

}
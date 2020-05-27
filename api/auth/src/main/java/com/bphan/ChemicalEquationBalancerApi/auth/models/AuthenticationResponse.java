package com.bphan.ChemicalEquationBalancerApi.auth.models;

import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Calendar;
import java.util.Date;

public class AuthenticationResponse extends ApiResponse {

  private Date timestamp;
  private String authToken;

  public AuthenticationResponse(String status, String description, String authToken) {
    super(status, description);
    this.timestamp = Calendar.getInstance().getTime();
    this.authToken = authToken;
  }

  public AuthenticationResponse(String status, String description) {
    super(status, description);
    this.timestamp = Calendar.getInstance().getTime();
    this.authToken = "";
  }

  @JsonProperty("timestamp")
  public Date getTimestamp() {
    return this.timestamp;
  }

  @JsonProperty("token")
  public String getAuthToken() {
    return this.authToken;
  }
}

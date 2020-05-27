package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels;

import com.bphan.ChemicalEquationBalancerApi.common.ResponseModels.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredRequestInfoId extends ApiResponse {
  private String id;

  public StoredRequestInfoId(String id) {
    super("success", "");
    this.id = checkNull(id);
  }

  public StoredRequestInfoId(String id, String status, String success) {
    super(status, success);
    this.id = checkNull(id);
  }

  @JsonProperty
  public String getId() {
    return id;
  }

  @JsonProperty
  public void setId(String id) {
    this.id = id;
  }

  private String checkNull(String str) {
    return str != null ? str : "";
  }
}

package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels;

import com.bphan.ChemicalEquationBalancerApi.common.models.ImageRegion;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionDiff {
    String requestId;
    ImageRegion[] modified;
    ImageRegion[] deleted;

    public RegionDiff(String requestId, ImageRegion[] modified, ImageRegion[] deleted) {
        this.requestId = requestId;
        this.modified = modified;
        this.deleted = deleted;
    }

    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("requestId")
    public String getRequestId() {
        return this.requestId;
    }

    @JsonProperty("modified")
    public void setModified(ImageRegion[] modified) {
        this.modified = modified;
    }

    @JsonProperty("modified")
    public ImageRegion[] getModified() {
        return modified;
    }

    @JsonProperty("deleted")
    public void setDeleted(ImageRegion[] deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("deleted")
    public ImageRegion[] getDeleted() {
        return deleted;
    }
}
package com.bphan.ChemicalEquationBalancerApi.common.models.storedRequestInfoModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoundingBoxDiff {
    String requestId;
    BoundingBox[] modified;
    BoundingBox[] deleted;

    BoundingBoxDiff(String requestId, BoundingBox[] modified, BoundingBox[] deleted) {
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
    public void setModified(BoundingBox[] modified) {
        this.modified = modified;
    }

    @JsonProperty("modified")
    public BoundingBox[] getModified() {
        return modified;
    }

    @JsonProperty("deleted")
    public void setDeleted(BoundingBox[] deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("deleted")
    public BoundingBox[] getDeleted() {
        return deleted;
    }
}
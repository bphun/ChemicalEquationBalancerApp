package com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoundingBoxDiff {
    BoundingBox[] modified;
    BoundingBox[] deleted;

    BoundingBoxDiff(BoundingBox[] modified, BoundingBox[] deleted) {
        this.modified = modified;
        this.deleted = deleted;
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
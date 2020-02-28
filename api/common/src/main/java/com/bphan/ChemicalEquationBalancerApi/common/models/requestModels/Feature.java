package com.bphan.ChemicalEquationBalancerApi.common.models.requestModels;

public class Feature {
    private long maxResults;
    private String type;

    public long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(long value) {
        this.maxResults = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }
}
package com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels;

public enum RequestLabelingStatus {
    INCOMPLETE("INCOMPLETE"), IN_PROGRESS("IN_PROGRESS"), LABELED("LABELED");

    private String status;

    private RequestLabelingStatus(String status) {
        this.status = status;
    }

    public String toStr() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
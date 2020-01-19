package com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class StoredRequestInfo {
    String id;
    String s3ImageUrl;
    String userInputtedChemicalEquationString;
    long gcpRequestStartTimeMs;
    long gcpRequestEndTimeMs;
    String verifiedChemicalEquationString;
    String gcpIdentifiedChemicalEquationString;
    long onDeviceImageProcessStartTime;
    long onDeviceImageProcessEndTime;
    String onDeviceImageProcessDeviceName;
    boolean chemicalEquationStringVerified;

    public StoredRequestInfo(String id, String s3ImageUrl, String userInputtedChemicalEquationString,
            long gcpRequestStartTimeMs, long gcpRequestEndTimeMs, String verifiedChemicalEquationString,
            String gcpIdentifiedChemicalEquationString, long onDeviceImageProcessStartTime,
            long onDeviceImageProcessEndTime, String onDeviceImageProcessDeviceName,
            boolean chemicalEquationStringVerified) {

        this.id = id;
        this.s3ImageUrl = s3ImageUrl;
        this.userInputtedChemicalEquationString = userInputtedChemicalEquationString;
        this.gcpRequestStartTimeMs = gcpRequestStartTimeMs;
        this.gcpRequestEndTimeMs = gcpRequestEndTimeMs;
        this.verifiedChemicalEquationString = verifiedChemicalEquationString;
        this.gcpIdentifiedChemicalEquationString = gcpIdentifiedChemicalEquationString;
        this.onDeviceImageProcessStartTime = onDeviceImageProcessStartTime;
        this.onDeviceImageProcessEndTime = onDeviceImageProcessEndTime;
        this.onDeviceImageProcessDeviceName = onDeviceImageProcessDeviceName;
        this.chemicalEquationStringVerified = chemicalEquationStringVerified;
    }

    @JsonProperty
    String getId() {
        return id;
    }

    @JsonProperty
    void setId(String id) {
        this.id = id;
    }

    @JsonProperty
    String gets3ImageUrl() {
        return s3ImageUrl;
    }

    @JsonProperty
    void sets3ImageUrl(String s3ImageUrl) {
        this.s3ImageUrl = s3ImageUrl;
    }

    @JsonProperty
    String getUserInputtedChemicalEquationString() {
        return userInputtedChemicalEquationString;
    }

    @JsonProperty
    void setUserInputtedChemicalEquationString(String userInputtedChemicalEquationString) {
        this.userInputtedChemicalEquationString = userInputtedChemicalEquationString;
    }

    @JsonProperty
    long getGcpRequestStartTimeMs() {
        return gcpRequestStartTimeMs;
    }

    @JsonProperty
    void setGcpRequestStartTimeMs(long gcpRequestStartTimeMs) {
        this.gcpRequestStartTimeMs = gcpRequestStartTimeMs;
    }

    @JsonProperty
    long getGcpRequestEndTimeMs() {
        return gcpRequestEndTimeMs;
    }

    @JsonProperty
    void setGcpRequestEndTimeMs(long gcpRequestEndTimeMs) {
        this.gcpRequestEndTimeMs = gcpRequestEndTimeMs;
    }

    @JsonProperty
    String getVerifiedChemicalEquationString() {
        return verifiedChemicalEquationString;
    }

    @JsonProperty
    void setVerifiedChemicalEquationString(String verifiedChemicalEquationString) {
        this.verifiedChemicalEquationString = verifiedChemicalEquationString;
    }

    @JsonProperty
    String getGcpIdentifiedChemicalEquationString() {
        return gcpIdentifiedChemicalEquationString;
    }

    @JsonProperty
    void setGcpIdentifiedChemicalEquationString(String gcpIdentifiedChemicalEquationString) {
        this.gcpIdentifiedChemicalEquationString = gcpIdentifiedChemicalEquationString;
    }

    @JsonProperty
    long getOnDeviceImageProcessStartTime() {
        return onDeviceImageProcessStartTime;
    }

    @JsonProperty
    void setOnDeviceImageProcessStartTime(long onDeviceImageProcessStartTime) {
        this.onDeviceImageProcessStartTime = onDeviceImageProcessStartTime;
    }

    @JsonProperty
    long getOnDeviceImageProcessEndTime() {
        return onDeviceImageProcessEndTime;
    }

    @JsonProperty
    void setOnDeviceImageProcessEndTime(long onDeviceImageProcessEndTime) {
        this.onDeviceImageProcessEndTime = onDeviceImageProcessEndTime;
    }

    @JsonProperty
    String getOnDeviceImageProcessDeviceName() {
        return onDeviceImageProcessDeviceName;
    }

    @JsonProperty
    void setOnDeviceImageProcessDeviceName(String onDeviceImageProcessDeviceName) {
        this.onDeviceImageProcessDeviceName = onDeviceImageProcessDeviceName;
    }

    @JsonProperty
    boolean getChemicalEquationStringVerified() {
        return chemicalEquationStringVerified;
    }

    @JsonProperty
    void setChemicalEquationStringVerified(boolean chemicalEquationStringVerified) {
        this.chemicalEquationStringVerified = chemicalEquationStringVerified;
    }
}
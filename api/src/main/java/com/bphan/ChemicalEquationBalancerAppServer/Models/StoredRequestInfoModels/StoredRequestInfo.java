package com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class StoredRequestInfo {
    private String id;
    private String s3ImageUrl;
    private String userInputtedChemicalEquationString;
    private long gcpRequestStartTimeMs;
    private long gcpRequestEndTimeMs;
    private String verifiedChemicalEquationString;
    private String gcpIdentifiedChemicalEquationString;
    private long onDeviceImageProcessStartTime;
    private long onDeviceImageProcessEndTime;
    private String onDeviceImageProcessDeviceName;

    public StoredRequestInfo(String id, String s3ImageUrl, String userInputtedChemicalEquationString,
            long gcpRequestStartTimeMs, long gcpRequestEndTimeMs, String verifiedChemicalEquationString,
            String gcpIdentifiedChemicalEquationString, long onDeviceImageProcessStartTime,
            long onDeviceImageProcessEndTime, String onDeviceImageProcessDeviceName) {

        this.id = checkNull(id);
        this.s3ImageUrl = checkNull(s3ImageUrl);
        this.userInputtedChemicalEquationString = checkNull(userInputtedChemicalEquationString);
        this.gcpRequestStartTimeMs = gcpRequestStartTimeMs;
        this.gcpRequestEndTimeMs = gcpRequestEndTimeMs;
        this.verifiedChemicalEquationString = checkNull(verifiedChemicalEquationString);
        this.gcpIdentifiedChemicalEquationString = checkNull(gcpIdentifiedChemicalEquationString);
        this.onDeviceImageProcessStartTime = onDeviceImageProcessStartTime;
        this.onDeviceImageProcessEndTime = onDeviceImageProcessEndTime;
        this.onDeviceImageProcessDeviceName = checkNull(onDeviceImageProcessDeviceName);
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty
    public String gets3ImageUrl() {
        return s3ImageUrl;
    }

    @JsonProperty
    public void sets3ImageUrl(String s3ImageUrl) {
        this.s3ImageUrl = s3ImageUrl;
    }

    @JsonProperty
    public String getUserInputtedChemicalEquationString() {
        return userInputtedChemicalEquationString;
    }

    @JsonProperty
    public void setUserInputtedChemicalEquationString(String userInputtedChemicalEquationString) {
        this.userInputtedChemicalEquationString = userInputtedChemicalEquationString;
    }

    @JsonProperty
    public long getGcpRequestStartTimeMs() {
        return gcpRequestStartTimeMs;
    }

    @JsonProperty
    public void setGcpRequestStartTimeMs(long gcpRequestStartTimeMs) {
        this.gcpRequestStartTimeMs = gcpRequestStartTimeMs;
    }

    @JsonProperty
    public long getGcpRequestEndTimeMs() {
        return gcpRequestEndTimeMs;
    }

    @JsonProperty
    public void setGcpRequestEndTimeMs(long gcpRequestEndTimeMs) {
        this.gcpRequestEndTimeMs = gcpRequestEndTimeMs;
    }

    @JsonProperty
    public String getVerifiedChemicalEquationString() {
        return verifiedChemicalEquationString;
    }

    @JsonProperty
    public void setVerifiedChemicalEquationString(String verifiedChemicalEquationString) {
        this.verifiedChemicalEquationString = verifiedChemicalEquationString;
    }

    @JsonProperty
    public String getGcpIdentifiedChemicalEquationString() {
        return gcpIdentifiedChemicalEquationString;
    }

    @JsonProperty
    public void setGcpIdentifiedChemicalEquationString(String gcpIdentifiedChemicalEquationString) {
        this.gcpIdentifiedChemicalEquationString = gcpIdentifiedChemicalEquationString;
    }

    @JsonProperty
    public long getOnDeviceImageProcessStartTime() {
        return onDeviceImageProcessStartTime;
    }

    @JsonProperty
    public void setOnDeviceImageProcessStartTime(long onDeviceImageProcessStartTime) {
        this.onDeviceImageProcessStartTime = onDeviceImageProcessStartTime;
    }

    @JsonProperty
    public long getOnDeviceImageProcessEndTime() {
        return onDeviceImageProcessEndTime;
    }

    @JsonProperty
    public void setOnDeviceImageProcessEndTime(long onDeviceImageProcessEndTime) {
        this.onDeviceImageProcessEndTime = onDeviceImageProcessEndTime;
    }

    @JsonProperty
    public String getOnDeviceImageProcessDeviceName() {
        return onDeviceImageProcessDeviceName;
    }

    @JsonProperty
    public void setOnDeviceImageProcessDeviceName(String onDeviceImageProcessDeviceName) {
        this.onDeviceImageProcessDeviceName = onDeviceImageProcessDeviceName;
    }

    private String checkNull(String str) {
        return str != null ? str : "";
    }
}
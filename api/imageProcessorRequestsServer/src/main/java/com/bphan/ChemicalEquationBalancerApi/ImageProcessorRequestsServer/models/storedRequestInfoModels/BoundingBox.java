package com.bphan.ChemicalEquationBalancerApi.ImageProcessorRequestsServer.models.storedRequestInfoModels;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class BoundingBox {
    private String id;
    private String requestInfoId;
    private String regionClass;
    private double originX;
    private double originY;
    private double width;
    private double height;
    private List<String> tags;

    public BoundingBox() {

    }

    public BoundingBox(String requestInfoId, double originX, double originY, double width, double height, List<String> tags) {
        this.id = "";
        this.requestInfoId = requestInfoId;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.tags = tags;
    }

    public BoundingBox(String id, String requestInfoId, String regionClass, double originX, double originY, double width, double height, String tags) {
        this.id = id;
        this.requestInfoId = requestInfoId;
        this.regionClass = regionClass;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.tags = Arrays.asList(tags.split(","));
    }

    public BoundingBox(String id, String requestInfoId, String regionClass, double originX, double originY, double width, double height, String[] tags) {
        this.id = id;
        this.requestInfoId = requestInfoId;
        this.regionClass = regionClass;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.tags = Arrays.asList(tags);
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("requestInfoId")
    public String getRequestInfoId() {
        return requestInfoId;
    }

    @JsonProperty("requestInfoId")
    public void setRequestInfoId(String requestInfoId) {
        this.requestInfoId = requestInfoId;
    }

    @JsonProperty("regionClass")
    public String getRegionClass() {
        return regionClass;
    }

    @JsonProperty("regionClass")
    public void setRegionClass(String regionClass) {
        this.regionClass = regionClass;
    }

    @JsonProperty("originX")
    public double getOriginX() {
        return originX;
    }

    @JsonProperty("originX")
    public void setOriginX(double originX) {
        this.originX = originX;
    }

    @JsonProperty("originY")
    public double getOriginY() {
        return originY;
    }

    @JsonProperty("originY")
    public void setOriginY(double originY) {
        this.originY = originY;
    }

    @JsonProperty("width")
    public double getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(double width) {
        this.width = width;
    }

    @JsonProperty("height")
    public double getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(double height) {
        this.height = height;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String tagStr() {
        StringJoiner joiner = new StringJoiner(",", "", "");
        tags.forEach(joiner::add);
        return joiner.toString();
    }
}
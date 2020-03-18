package com.bphan.ChemicalEquationBalancerApi.common.models;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class ImageRegion {
    private String id;
    private String requestInfoId;
    private String regionClass;
    private double originX;
    private double originY;
    private double width;
    private double height;
    private double viewportWidth;
    private double viewportHeight;
    private int parentImageWidth;
    private int parentImageHeight;
    private List<String> tags;

    public ImageRegion() {
    }

    public ImageRegion(String requestInfoId, double originX, double originY, double width, double height,
            List<String> tags) {
        this.id = "";
        this.requestInfoId = requestInfoId;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.tags = tags;
    }

    public ImageRegion(String id, String requestInfoId, String regionClass, double originX, double originY,
            double width, double height, String tags) {
        this.id = id;
        this.requestInfoId = requestInfoId;
        this.regionClass = regionClass;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.tags = Arrays.asList(tags.split(","));
    }

    public ImageRegion(String id, String requestInfoId, String regionClass, double originX, double originY,
            double width, double height, double viewportWidth, double viewportHeight, int parentImageWidth, int parentImageHeight, String tags) {
        this.id = id;
        this.requestInfoId = requestInfoId;
        this.regionClass = regionClass;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.parentImageWidth = parentImageWidth;
        this.parentImageHeight = parentImageHeight;
        this.tags = Arrays.asList(tags.split(","));
    }

    public ImageRegion(String id, String requestInfoId, String regionClass, double originX, double originY,
            double width, double height, double viewportWidth, double viewportHeight, int parentImageWidth, int parentImageHeight, String[] tags) {
        this.id = id;
        this.requestInfoId = requestInfoId;
        this.regionClass = regionClass;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.parentImageWidth = parentImageWidth;
        this.parentImageHeight = parentImageHeight;
        this.tags = Arrays.asList(tags);
    }

    public ImageRegion(String id, String requestInfoId, String regionClass, double originX, double originY,
            double width, double height, String[] tags) {
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

    @JsonProperty("viewportWidth")
    public double getViewportWidth() {
        return viewportWidth;
    }

    @JsonProperty("viewportWidth")
    public void setViewportWidth(double viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    @JsonProperty("viewportHeight")
    public double getViewportHeight() {
        return viewportHeight;
    }

    @JsonProperty("viewportHeight")
    public void setViewportHeight(double viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    @JsonProperty("parentImageWidth")
    public int getParentImageWidth() {
        return parentImageWidth;
    }

    @JsonProperty("parentImageWidth")
    public void setParentImageWidth(int parentImageWidth) {
        this.parentImageWidth = parentImageWidth;
    }

    @JsonProperty("parentImageHeight")
    public int getParentImageHeight() {
        return parentImageHeight;
    }

    @JsonProperty("parentImageHeight")
    public void setParentImageHeight(int parentImageHeight) {
        this.parentImageHeight = parentImageHeight;
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
package com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class BoundingBox {
    private String id;
    private String requestInfoId;
    private int originX;
    private int originY;
    private int width;
    private int height;
    private List<String> tags;

    public BoundingBox() {

    }

    public BoundingBox(String requestInfoId, int originX, int originY, int width, int height, List<String> tags) {
        this.id = "";
        this.requestInfoId = requestInfoId;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.tags = tags;
    }

    public BoundingBox(String id, String requestInfoId, int originX, int originY, int width, int height, String tags) {
        this.id = id;
        this.requestInfoId = requestInfoId;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.tags = Arrays.asList(tags.split(","));
    }

    public BoundingBox(String id, String requestInfoId, int originX, int originY, int width, int height, String[] tags) {
        this.id = id;
        this.requestInfoId = requestInfoId;
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

    @JsonProperty("originX")
    public int getOriginX() {
        return originX;
    }

    @JsonProperty("originX")
    public void setOriginX(int originX) {
        this.originX = originX;
    }

    @JsonProperty("originY")
    public int getOriginY() {
        return originY;
    }

    @JsonProperty("originY")
    public void setOriginY(int originY) {
        this.originY = originY;
    }

    @JsonProperty("width")
    public int getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(int width) {
        this.width = width;
    }

    @JsonProperty("height")
    public int getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(int height) {
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
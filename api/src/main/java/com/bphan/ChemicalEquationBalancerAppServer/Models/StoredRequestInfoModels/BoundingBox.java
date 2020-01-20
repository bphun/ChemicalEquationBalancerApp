package com.bphan.ChemicalEquationBalancerAppServer.Models.StoredRequestInfoModels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class BoundingBox {
    private String id;
    private String imageProcessorRequestInfoId;
    private int originX;
    private int originY;
    private int width;
    private int height;

    public BoundingBox() {
        
    }

    public BoundingBox(String imageProcessorRequestInfoId, int originX, int originY,
            int width, int height) {
        this.id = "";
        this.imageProcessorRequestInfoId = imageProcessorRequestInfoId;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
    }

    public BoundingBox(String id, String imageProcessorRequestInfoId, int originX, int originY,
            int width, int height) {
        this.id = id;
        this.imageProcessorRequestInfoId = imageProcessorRequestInfoId;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("imageProcessorRequestInfoId")
    public String getImageProcessorRequestInfoId() {
        return imageProcessorRequestInfoId;
    }

    @JsonProperty("imageProcessorRequestInfoId")
    public void setImageProcessorRequestInfoId(String imageProcessorRequestInfoId) {
        this.imageProcessorRequestInfoId = imageProcessorRequestInfoId;
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
}
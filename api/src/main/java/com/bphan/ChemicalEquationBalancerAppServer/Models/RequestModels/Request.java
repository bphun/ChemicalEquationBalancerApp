package com.bphan.ChemicalEquationBalancerAppServer.Models.RequestModels;

import java.util.List;

public class Request {
    private Image image;
    private List<Feature> features;

    public Image getImage() {
        return image;
    }

    public void setImage(Image value) {
        this.image = value;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> value) {
        this.features = value;
    }
}
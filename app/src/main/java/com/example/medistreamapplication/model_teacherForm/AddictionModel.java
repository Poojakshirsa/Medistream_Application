package com.example.medistreamapplication.model_teacherForm;



public class AddictionModel {
    private String addictedNow;
    private String addictedPast;
    private String addictionType;
    private String addictionDuration;
    private String frequency;

    // Empty constructor (required for Firebase)
    public AddictionModel() {
    }


    // Getter and Setter methods
    public String getAddictedNow() {
        return addictedNow;
    }

    public void setAddictedNow(String addictedNow) {
        this.addictedNow = addictedNow;
    }

    public String getAddictedPast() {
        return addictedPast;
    }

    public void setAddictedPast(String addictedPast) {
        this.addictedPast = addictedPast;
    }

    public String getAddictionType() {
        return addictionType;
    }

    public void setAddictionType(String addictionType) {
        this.addictionType = addictionType;
    }

    public String getAddictionDuration() {
        return addictionDuration;
    }

    public void setAddictionDuration(String addictionDuration) {
        this.addictionDuration = addictionDuration;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}


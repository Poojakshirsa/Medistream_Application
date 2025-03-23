package com.example.medistreamapplication.model_teacherForm;

public class ExaminationModel {
    private String weight;
    private String waistHipRatio;
    private String bloodPressure;
    private String height;
    private String bmi;
    private String pulse;
    private String condition;
    private String cvs;
    private String rs;
    private String bslRandom;
    private String remark;
    private String referredDoctor;
    private String imageUrl;

    // Empty Constructor (Required for Firebase)
    public ExaminationModel() {}

    // Constructor with parameters
    public ExaminationModel(String weight, String waistHipRatio, String bloodPressure, String height,
                            String bmi, String pulse, String condition, String cvs, String rs, String bslRandom) {
        this.weight = weight;
        this.waistHipRatio = waistHipRatio;
        this.bloodPressure = bloodPressure;
        this.height = height;
        this.bmi = bmi;
        this.pulse = pulse;
        this.condition = condition;
        this.cvs = cvs;
        this.rs = rs;
        this.bslRandom = bslRandom;
    }

    // Getters and Setters
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getWaistHipRatio() { return waistHipRatio; }
    public void setWaistHipRatio(String waistHipRatio) { this.waistHipRatio = waistHipRatio; }

    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getBmi() { return bmi; }
    public void setBmi(String bmi) { this.bmi = bmi; }

    public String getPulse() { return pulse; }
    public void setPulse(String pulse) { this.pulse = pulse; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getCvs() { return cvs; }
    public void setCvs(String cvs) { this.cvs = cvs; }

    public String getRs() { return rs; }
    public void setRs(String rs) { this.rs = rs; }

    public String getBslRandom() { return bslRandom; }
    public void setBslRandom(String bslRandom) { this.bslRandom = bslRandom; }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReferredDoctor() {
        return referredDoctor;
    }

    public void setReferredDoctor(String referredDoctor) {
        this.referredDoctor = referredDoctor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

package com.example.medistreamapplication.model_teacherForm;

import java.util.List;

public class HistoryExaminationModel {
    private boolean diabetes;
    private boolean hypertension;
    private boolean ischemicHeartDisease;
    private boolean tuberculosis;
    private boolean asthma;
    private boolean anyOther;

    private String sinceYears;
    private String medicationDetails;
    private String regularity;

    private boolean familyDiabetes;
    private boolean familyHypertension;
    private boolean familyIschemicHeartDisease;
    private boolean familyTuberculosis;
    private boolean familyAsthma;
    private boolean familyAnyOther;

    // Getters and Setters for each field
    public boolean isDiabetes() { return diabetes; }
    public void setDiabetes(boolean diabetes) { this.diabetes = diabetes; }

    public boolean isHypertension() { return hypertension; }
    public void setHypertension(boolean hypertension) { this.hypertension = hypertension; }

    public boolean isIschemicHeartDisease() { return ischemicHeartDisease; }
    public void setIschemicHeartDisease(boolean ischemicHeartDisease) { this.ischemicHeartDisease = ischemicHeartDisease; }

    public boolean isTuberculosis() { return tuberculosis; }
    public void setTuberculosis(boolean tuberculosis) { this.tuberculosis = tuberculosis; }

    public boolean isAsthma() { return asthma; }
    public void setAsthma(boolean asthma) { this.asthma = asthma; }

    public boolean isAnyOther() { return anyOther; }
    public void setAnyOther(boolean anyOther) { this.anyOther = anyOther; }

    public String getSinceYears() { return sinceYears; }
    public void setSinceYears(String sinceYears) { this.sinceYears = sinceYears; }

    public String getMedicationDetails() { return medicationDetails; }
    public void setMedicationDetails(String medicationDetails) { this.medicationDetails = medicationDetails; }

    public String getRegularity() { return regularity; }
    public void setRegularity(String regularity) { this.regularity = regularity; }

    public boolean isFamilyDiabetes() { return familyDiabetes; }
    public void setFamilyDiabetes(boolean familyDiabetes) { this.familyDiabetes = familyDiabetes; }

    public boolean isFamilyHypertension() { return familyHypertension; }
    public void setFamilyHypertension(boolean familyHypertension) { this.familyHypertension = familyHypertension; }

    public boolean isFamilyIschemicHeartDisease() { return familyIschemicHeartDisease; }
    public void setFamilyIschemicHeartDisease(boolean familyIschemicHeartDisease) { this.familyIschemicHeartDisease = familyIschemicHeartDisease; }

    public boolean isFamilyTuberculosis() { return familyTuberculosis; }
    public void setFamilyTuberculosis(boolean familyTuberculosis) { this.familyTuberculosis = familyTuberculosis; }

    public boolean isFamilyAsthma() { return familyAsthma; }
    public void setFamilyAsthma(boolean familyAsthma) { this.familyAsthma = familyAsthma; }

    public boolean isFamilyAnyOther() { return familyAnyOther; }
    public void setFamilyAnyOther(boolean familyAnyOther) { this.familyAnyOther = familyAnyOther; }
}

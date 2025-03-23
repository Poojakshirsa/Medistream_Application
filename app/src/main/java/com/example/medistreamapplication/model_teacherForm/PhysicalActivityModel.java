package com.example.medistreamapplication.model_teacherForm;

public class PhysicalActivityModel {
    private String exerciseAtPresent;
    private String exerciseFrequency;
    private String reasonForNotExercising;

    public PhysicalActivityModel() {
        // Default constructor (Required for Firebase)
    }

    public PhysicalActivityModel(String exerciseAtPresent, String exerciseFrequency, String reasonForNotExercising) {
        this.exerciseAtPresent = exerciseAtPresent;
        this.exerciseFrequency = exerciseFrequency;
        this.reasonForNotExercising = reasonForNotExercising;
    }

    public String getExerciseAtPresent() {
        return exerciseAtPresent;
    }

    public void setExerciseAtPresent(String exerciseAtPresent) {
        this.exerciseAtPresent = exerciseAtPresent;
    }

    public String getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(String exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }

    public String getReasonForNotExercising() {
        return reasonForNotExercising;
    }

    public void setReasonForNotExercising(String reasonForNotExercising) {
        this.reasonForNotExercising = reasonForNotExercising;
    }
}

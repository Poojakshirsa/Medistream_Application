package com.example.medistreamapplication.model_teacherForm;

public class Demographic_Form_Model {
    private String employeeName;
    private String dateOfBirth;
    private String age;
    private String schoolName;
    private String schoolAddress;
    private String designation;
    private String education;
    private String workingExperience;
    private String schoolTiming;
    private String monthlyIncome;
    private String gender;
    private String maritalStatus;
    private String UniqueId;

    // Constructor
    public Demographic_Form_Model() {}

    // Getters and Setters
    public String getEmployeeName() { return employeeName; }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    public String getSchoolAddress() { return schoolAddress; }
    public void setSchoolAddress(String schoolAddress) { this.schoolAddress = schoolAddress; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getWorkingExperience() { return workingExperience; }
    public void setWorkingExperience(String workingExperience) { this.workingExperience = workingExperience; }

    public String getSchoolTiming() { return schoolTiming; }
    public void setSchoolTiming(String schoolTiming) { this.schoolTiming = schoolTiming; }

    public String getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(String monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }
}

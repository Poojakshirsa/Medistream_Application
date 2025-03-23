package com.example.medistreamapplication.model_form2;

import com.google.gson.Gson;

public class SchoolDataSecondForm {
    private String schoolName;
    private String schoolAddress;
    private String dateOfCheckup;
    private String childName;

    private String dateOfBirth;
    private String childAge;
    private String gender;
    private String UniqueId;
    private  String section;
    private String teacherInchargeName;
    private String teacherInchargeContact;
    private String fatherName;
    private String motherName;
    private String parentContact;
    private String childWeight;
    private String childHeight;
    private String BMI;
    private String BMI_Classification;
    private String Bp_layout;

    public String getSchoolName() {
        return schoolName;
    }
    // Method to convert the object to JSON
    public String toJson() {
        return new Gson().toJson(this);
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getDateOfCheckup() {
        return dateOfCheckup;
    }

    public void setDateOfCheckup(String dateOfCheckup) {
        this.dateOfCheckup = dateOfCheckup;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTeacherInchargeName() {
        return teacherInchargeName;
    }

    public void setTeacherInchargeName(String teacherInchargeName) {
        this.teacherInchargeName = teacherInchargeName;
    }

    public String getTeacherInchargeContact() {
        return teacherInchargeContact;
    }

    public void setTeacherInchargeContact(String teacherInchargeContact) {
        this.teacherInchargeContact = teacherInchargeContact;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getParentContact() {
        return parentContact;
    }

    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }

    public String getChildWeight() {
        return childWeight;
    }

    public void setChildWeight(String childWeight) {
        this.childWeight = childWeight;
    }

    public String getChildHeight() {
        return childHeight;
    }

    public void setChildHeight(String childHeight) {
        this.childHeight = childHeight;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getBMI_Classification() {
        return BMI_Classification;
    }

    public void setBMI_Classification(String BMI_Classification) {
        this.BMI_Classification = BMI_Classification;
    }

    public String getBp_layout() {
        return Bp_layout;
    }

    public void setBp_layout(String bp_layout) {
        Bp_layout = bp_layout;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }
}

package com.example.medistreamapplication.model_form1;

import com.google.gson.Gson;

public class SchoolData {


    private String schoolName;
    private String schoolAddress;
    private String dateOfCheckup;
    private String childName;

    private String dateOfBirth; // New field for DOB
    private String childAge;
    private String gender;
    private String teacherIncharge;
    private String fatherName;
    private String motherName;
    private String parentContact;
    private String childWeight;
    private String childHeight;
    private String headCircumference;
    private String muacValue;
    private String UniqueId;

    // Constructor

    public SchoolData() {
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getSchoolName() {
        return schoolName;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getTeacherIncharge() {
        return teacherIncharge;
    }

    public void setTeacherIncharge(String teacherIncharge) {
        this.teacherIncharge = teacherIncharge;
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

    public String getHeadCircumference() {
        return headCircumference;
    }

    public void setHeadCircumference(String headCircumference) {
        this.headCircumference = headCircumference;
    }

    public String getMuacValue() {
        return muacValue;
    }

    public void setMuacValue(String muacValue) {
        this.muacValue = muacValue;
    }

    // Method to convert the object to JSON
    public String toJson() {
        return new Gson().toJson(this);
    }


}

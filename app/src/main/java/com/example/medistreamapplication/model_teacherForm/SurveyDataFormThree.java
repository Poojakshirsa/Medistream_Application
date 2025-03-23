package com.example.medistreamapplication.model_teacherForm;

import com.example.medistreamapplication.model_form2.AdolescentFormModel;
import com.example.medistreamapplication.model_form2.DefectDataFormTwo;
import com.example.medistreamapplication.model_form2.DevelopmentDelayModelFormTwo;
import com.example.medistreamapplication.model_form2.PreliminaryFindingsFormTwo;
import com.example.medistreamapplication.model_form2.SchoolDataSecondForm;

public class SurveyDataFormThree {
    private String id;
    private Demographic_Form_Model demographicFormModel;
    private HistoryExaminationModel historyExaminationModel;
    private DietaryHistoryModel dietaryHistoryModel;
    private PhysicalActivityModel physicalActivityModel;
    private AddictionModel addictionModel;
    private ExaminationModel examinationModel;

    public SurveyDataFormThree() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Demographic_Form_Model getDemographicFormModel() {
        return demographicFormModel;
    }

    public void setDemographicFormModel(Demographic_Form_Model demographicFormModel) {
        this.demographicFormModel = demographicFormModel;
    }

    public HistoryExaminationModel getHistoryExaminationModel() {
        return historyExaminationModel;
    }

    public void setHistoryExaminationModel(HistoryExaminationModel historyExaminationModel) {
        this.historyExaminationModel = historyExaminationModel;
    }

    public DietaryHistoryModel getDietaryHistoryModel() {
        return dietaryHistoryModel;
    }

    public void setDietaryHistoryModel(DietaryHistoryModel dietaryHistoryModel) {
        this.dietaryHistoryModel = dietaryHistoryModel;
    }

    public PhysicalActivityModel getPhysicalActivityModel() {
        return physicalActivityModel;
    }

    public void setPhysicalActivityModel(PhysicalActivityModel physicalActivityModel) {
        this.physicalActivityModel = physicalActivityModel;
    }

    public AddictionModel getAddictionModel() {
        return addictionModel;
    }

    public void setAddictionModel(AddictionModel addictionModel) {
        this.addictionModel = addictionModel;
    }

    public ExaminationModel getExaminationModel() {
        return examinationModel;
    }

    public void setExaminationModel(ExaminationModel examinationModel) {
        this.examinationModel = examinationModel;
    }
}

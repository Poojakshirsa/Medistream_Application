package com.example.medistreamapplication.model_form1;

import com.example.medistreamapplication.model_form1.DefectData;
import com.example.medistreamapplication.model_form1.DeficiencyData;
import com.example.medistreamapplication.model_form1.DevelopmentalDelaysData;
import com.example.medistreamapplication.model_form1.PreliminaryFinding;
import com.example.medistreamapplication.model_form1.SchoolData;

public class SurveyData {

    private  String id;
    private SchoolData schoolData;
    private DefectData defectsData;
    private DeficiencyData deficienciesData;
    private DevelopmentalDelaysData developmentalDelaysData;
    private PreliminaryFinding preliminaryFinding;

    public SurveyData() {
    }

    // Getters and setters
    public SchoolData getSchoolData() {
        return schoolData;
    }

    public void setSchoolData(SchoolData schoolData) {
        this.schoolData = schoolData;
    }

    public DefectData getDefectsData() {
        return defectsData;
    }

    public void setDefectsData(DefectData defectsData) {
        this.defectsData = defectsData;
    }

    public DeficiencyData getDeficienciesData() {
        return deficienciesData;
    }

    public void setDeficienciesData(DeficiencyData deficienciesData) {
        this.deficienciesData = deficienciesData;
    }

    public DevelopmentalDelaysData getDevelopmentalDelaysData() {
        return developmentalDelaysData;
    }

    public void setDevelopmentalDelaysData(DevelopmentalDelaysData developmentalDelaysData) {
        this.developmentalDelaysData = developmentalDelaysData;
    }

    public PreliminaryFinding getPreliminaryFinding() {
        return preliminaryFinding;
    }

    public void setPreliminaryFinding(PreliminaryFinding preliminaryFinding) {
        this.preliminaryFinding = preliminaryFinding;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

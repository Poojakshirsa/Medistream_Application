package com.example.medistreamapplication.model_form2;

import com.example.medistreamapplication.model_form2.AdolescentFormModel;
import com.example.medistreamapplication.model_form2.DefectDataFormTwo;
import com.example.medistreamapplication.model_form2.DevelopmentDelayModelFormTwo;
import com.example.medistreamapplication.model_form2.PreliminaryFindingsFormTwo;
import com.example.medistreamapplication.model_form2.SchoolDataSecondForm;

public class SurveyDataFormTwo {
    private  String id;
    private SchoolDataSecondForm schoolData;
    private DefectDataFormTwo defectsData;
    private DevelopmentDelayModelFormTwo developmentalDelaysData;
    private AdolescentFormModel adolescentFormModel;
    private PreliminaryFindingsFormTwo preliminaryFinding;

    public SurveyDataFormTwo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SchoolDataSecondForm getSchoolData() {
        return schoolData;
    }

    public void setSchoolData(SchoolDataSecondForm schoolData) {
        this.schoolData = schoolData;
    }

    public DefectDataFormTwo getDefectsData() {
        return defectsData;
    }

    public void setDefectsData(DefectDataFormTwo defectsData) {
        this.defectsData = defectsData;
    }

    public DevelopmentDelayModelFormTwo getDevelopmentalDelaysData() {
        return developmentalDelaysData;
    }

    public void setDevelopmentalDelaysData(DevelopmentDelayModelFormTwo developmentalDelaysData) {
        this.developmentalDelaysData = developmentalDelaysData;
    }

    public AdolescentFormModel getAdolescentFormModel() {
        return adolescentFormModel;
    }

    public void setAdolescentFormModel(AdolescentFormModel adolescentFormModel) {
        this.adolescentFormModel = adolescentFormModel;
    }

    public PreliminaryFindingsFormTwo getPreliminaryFinding() {
        return preliminaryFinding;
    }

    public void setPreliminaryFinding(PreliminaryFindingsFormTwo preliminaryFinding) {
        this.preliminaryFinding = preliminaryFinding;
    }
}

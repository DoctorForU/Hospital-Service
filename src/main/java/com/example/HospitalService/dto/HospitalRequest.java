package com.example.HospitalService.dto;

import lombok.Data;

@Data
public class HospitalRequest {
    private String primaryOption = "";
    private String secondaryOption = "";
    private String selectedCity = "";
    private String selectedDistrict = "";
    private String hospitalName = "";

    // Getters and Setters
    public String getPrimaryOption() {
        return primaryOption;
    }

    public void setPrimaryOption(String primaryOption) {
        this.primaryOption = primaryOption;
    }

    public String getSecondaryOption() {
        return secondaryOption;
    }

    public void setSecondaryOption(String secondaryOption) {
        this.secondaryOption = secondaryOption;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public String getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(String selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}

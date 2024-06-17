package com.example.HospitalService.dto;

import lombok.Data;

@Data
public class FavoriteHospitalResponse {
    private String hpid;
    private String dutyName;
    private String dutyAddr; // 병원주소
    private String dutyTel1; // 병원번호
}

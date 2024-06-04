package com.example.HospitalService.dto;

import lombok.Data;

@Data
public class HospitalToMypageResponse {
    private String hpid; // 병원 코드
    private String dutyName; // 병원명
    private String dutyAddr; // 주소
    private String dutyTel1; // 대표전화
    private String dutyTel3; // 응급실 전화
    private String dgidIdName; // 진료과목

    private String dutyTime1c; // 진료시간(월요일)C
    private String dutyTime2c; // 진료시간(화요일)C
    private String dutyTime3c; // 진료시간(수요일)C
    private String dutyTime4c; // 진료시간(목요일)C
    private String dutyTime5c; // 진료시간(금요일)C
    private String dutyTime6c; // 진료시간(토요일)C
    private String dutyTime7c; // 진료시간(일요일)C
    private String dutyTime8c; // 진료시간(공휴일)C
    private String dutyTime1s; // 진료시간(월요일)S
    private String dutyTime2s; // 진료시간(화요일)S
    private String dutyTime3s; // 진료시간(수요일)S
    private String dutyTime4s; // 진료시간(목요일)S
    private String dutyTime5s; // 진료시간(금요일)S
    private String dutyTime6s; // 진료시간(토요일)S
    private String dutyTime7s; // 진료시간(일요일)S
    private String dutyTime8s; // 진료시간(공휴일)S
}

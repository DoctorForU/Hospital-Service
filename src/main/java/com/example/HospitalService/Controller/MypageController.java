package com.example.HospitalService.Controller;

import com.example.HospitalService.Service.MypageRegisterService;
import com.example.HospitalService.dto.AvailableTimes;
import com.example.HospitalService.dto.DutyTimes;
import com.example.HospitalService.dto.HpidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/hospital-service3")
public class MypageController {
    private static final Logger logger = LoggerFactory.getLogger(HospitalController.class);

    @Autowired
    private MypageRegisterService mypageRegisterService;

    @GetMapping("/toMypageForDate")
    public ResponseEntity<?> toMypageRegister(@RequestParam String hpid) {
        logger.info("Received hpid: " + hpid); // 요청받은 병원 코드를 로그에 출력
        HpidRequest newHpid = new HpidRequest();
        newHpid.setHpid(hpid);

        // 서비스에서 진료 시간과 병원 정보를 가져옴
        DutyTimes dutyTimes = mypageRegisterService.registerDate(newHpid);

        // 진료 시간을 바탕으로 예약 가능한 시간대를 생성
        AvailableTimes availableTimes = generateAvailableTimes(dutyTimes);

        // 병원 정보를 설정
        availableTimes.setHpid(dutyTimes.getHpid());
        availableTimes.setDutyName(dutyTimes.getDutyName());
        availableTimes.setDutyAddr(dutyTimes.getDutyAddr());
        availableTimes.setDutyTel1(dutyTimes.getDutyTel1());
        availableTimes.setDutyTel3(dutyTimes.getDutyTel3());
        availableTimes.setDgidIdName(dutyTimes.getDgidIdName());

        return ResponseEntity.ok(availableTimes);
    }

    // 예약 가능한 시간대를 생성하는 메서드
    private AvailableTimes generateAvailableTimes(DutyTimes dutyTimes) {
        AvailableTimes availableTimes = new AvailableTimes();

        // 각 요일에 대해 예약 가능한 시간대를 생성하여 설정
        availableTimes.setMonday(getAvailableTimeSlots(dutyTimes.getDutyTime1s(), dutyTimes.getDutyTime1c()));
        availableTimes.setTuesday(getAvailableTimeSlots(dutyTimes.getDutyTime2s(), dutyTimes.getDutyTime2c()));
        availableTimes.setWednesday(getAvailableTimeSlots(dutyTimes.getDutyTime3s(), dutyTimes.getDutyTime3c()));
        availableTimes.setThursday(getAvailableTimeSlots(dutyTimes.getDutyTime4s(), dutyTimes.getDutyTime4c()));
        availableTimes.setFriday(getAvailableTimeSlots(dutyTimes.getDutyTime5s(), dutyTimes.getDutyTime5c()));
        availableTimes.setSaturday(Collections.emptyList()); // 예약 불가
        availableTimes.setSunday(Collections.emptyList()); // 예약 불가
        availableTimes.setHoliday(Collections.emptyList()); // 예약 불가

        return availableTimes;
    }

    // 주어진 시작 시간과 종료 시간으로 예약 가능한 30분 간격의 시간대를 생성하는 메서드
    private List<String> getAvailableTimeSlots(String start, String end) {
        List<String> timeSlots = new ArrayList<>();

        // 시작 시간과 종료 시간을 정수로 변환
        int startTime = Integer.parseInt(start);
        int endTime = Integer.parseInt(end);

        // 점심시간 설정 (예: 1300-1400)
        int lunchStart = 1300;
        int lunchEnd = 1400;

        // 30분 간격의 시간대를 생성
        for (int time = startTime; time < endTime; time += 50) {
            if (time >= lunchStart && time < lunchEnd) {
                // 점심시간 동안은 예약 불가로 설정
                continue;
            }
            String startSlot = String.format("%04d", time);
            String endSlot = String.format("%04d", time + 50);
            timeSlots.add(startSlot + "-" + endSlot); // 예약 가능한 시간대를 리스트에 추가
        }

        return timeSlots;
    }
}

package com.example.HospitalService.Controller;

import com.example.HospitalService.Service.FavoriteHospitalService;
import com.example.HospitalService.Service.HospitalDetailService;
import com.example.HospitalService.Service.MypageRegisterService;
import com.example.HospitalService.dto.FavoriteHospitalResponse;
import com.example.HospitalService.dto.HospitalRequest;
import com.example.HospitalService.Service.HospitalService;
import com.example.HospitalService.dto.HpidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/hospital-service")
public class HospitalController {

    private static final Logger logger = LoggerFactory.getLogger(HospitalController.class);
    private final HospitalServiceClient hospitalServiceClient;
    @Autowired
    public HospitalController(HospitalServiceClient hospitalServiceClient) {
        this.hospitalServiceClient = hospitalServiceClient;
    }

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalDetailService hospitalDetailService;

    @Autowired
    private MypageRegisterService mypageRegisterService;

    @Autowired
    private FavoriteHospitalService favoriteHospitalService;

//    @GetMapping("/health-check")
//    public String status(){
//        return String.format("It's Working in Hospital Service"
//            + ", port(local.server.port)=  " + env.getProperty("local.server.port")
//                        + ", port(local.server.port)=  " + env.getProperty("local.server.port")
//
//                )"It's Working in Hospital Service"port(local.server.port)= "
//    }

    @PostMapping("/hospitalsList")
    public ResponseEntity<?> searchHospitals(@RequestBody HospitalRequest request) { // FE에서 날린 데이터들 ( 병원명이라던가, 위치)
        logger.info("Received request: " + request);
        return ResponseEntity.ok(hospitalService.searchHospitals(request));
    }

    @GetMapping("/hospitalDetail")
    public ResponseEntity<?> getHospitalDetail(@RequestParam String hpid){
        HpidRequest NewHpid = new HpidRequest();
        NewHpid.setHpid(hpid);
        logger.info("Received HPID: " + hpid);
        return ResponseEntity.ok(hospitalDetailService.getHospitalDetail(NewHpid));
    }

    @GetMapping("/toMypageForDate")
    public ResponseEntity<?> toMypageRegister(@RequestParam String hpid){
        logger.info("Received hpid: " +hpid);
        HpidRequest NewHpid = new HpidRequest();
        NewHpid.setHpid(hpid);
        return ResponseEntity.ok(mypageRegisterService.registerDate(NewHpid));
    }

    @GetMapping("/registerFavoriteHospital")
    public ResponseEntity<FavoriteHospitalResponse> registerFavoriteHospital(@RequestParam String hpid){
        HpidRequest NewHpid = new HpidRequest();
        NewHpid.setHpid(hpid);
        logger.info("Received HPID: " + hpid);
        return ResponseEntity.ok(favoriteHospitalService.registerFavoriteHospital(NewHpid));
    }







}


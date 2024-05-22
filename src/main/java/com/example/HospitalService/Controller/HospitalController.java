package com.example.HospitalService.Controller;

import com.example.HospitalService.dto.HospitalRequest;
import com.example.HospitalService.Service.HospitalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospital-service")
public class HospitalController {

    private static final Logger logger = LoggerFactory.getLogger(HospitalController.class);

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/hospitalsList")
    public ResponseEntity<?> searchHospitals(@RequestBody HospitalRequest request) {
        logger.info("Received request: " + request);
        return ResponseEntity.ok(hospitalService.searchHospitals(request));
    }
}

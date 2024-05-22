package com.example.HospitalService.Service;

import com.example.HospitalService.dto.HospitalRequest;
import com.example.HospitalService.dto.HospitalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HospitalService {

    private static final Logger logger = LoggerFactory.getLogger(HospitalService.class);

    @Autowired
    private RestTemplate restTemplate;

    private static final String PUBLIC_DATA_API_URL = "https://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";
    //private static final String SERVICE_KEY = "K9t4%2FMS1InyhHxC7oJtTEGncK1mWLav7ML0G5XcgX7k37YyN6sL7owPZDulwsO7m0jyVwvEqeoiFQp3c7C%2BKuQ%3D%3D"; // 인코딩된 서비스 키 사용
    private static final String SERVICE_KEY = "K9t4/MS1InyhHxC7oJtTEGncK1mWLav7ML0G5XcgX7k37YyN6sL7owPZDulwsO7m0jyVwvEqeoiFQp3c7C+KuQ==";
    private static final String NUM_OF_ROWS = "3000";
    private static final String PAGE_NO = "1";

    public List<HospitalData> searchHospitals(HospitalRequest request) {
        String apiUrl = buildApiUrl(request);
        logger.info("Constructed API URL: " + apiUrl);
        String response = restTemplate.getForObject(apiUrl, String.class);
        logger.info("Response: " + response);

        return parseXmlResponse(response);
    }

    private String buildApiUrl(HospitalRequest request) {
        StringBuilder apiUrl = new StringBuilder(PUBLIC_DATA_API_URL);
        apiUrl.append("?pageNo=").append(PAGE_NO);
        apiUrl.append("&numOfRows=").append(NUM_OF_ROWS);
        apiUrl.append("&serviceKey=").append(URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8));

        if (request.getPrimaryOption() != null && !request.getPrimaryOption().isEmpty()) {
            apiUrl.append("&QZ=").append(encodeValue(request.getPrimaryOption()));
        }
        if (request.getSecondaryOption() != null && !request.getSecondaryOption().isEmpty()) {
            apiUrl.append("&QD=").append(encodeValue(request.getSecondaryOption()));
        }
        if (request.getSelectedCity() != null && !request.getSelectedCity().isEmpty()) {
            apiUrl.append("&Q0=").append(encodeValue(request.getSelectedCity()));
        }
        if (request.getSelectedDistrict() != null && !request.getSelectedDistrict().isEmpty()) {
            apiUrl.append("&Q1=").append(encodeValue(request.getSelectedDistrict()));
        }
        if (request.getHospitalName() != null && !request.getHospitalName().isEmpty()) {
            apiUrl.append("&QN=").append(encodeValue(request.getHospitalName()));
        }

        String apiUrlStr = apiUrl.toString();
        logger.info("Final API URL: " + apiUrlStr);
        return apiUrlStr;
    }

    private String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private List<HospitalData> parseXmlResponse(String response) {
        List<HospitalData> hospitals = new ArrayList<>();
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode root = xmlMapper.readTree(response.getBytes(StandardCharsets.UTF_8));
            JsonNode items = root.path("body").path("items").path("item");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    HospitalData hospital = new HospitalData();
                    hospital.setDutyAddr(item.path("dutyAddr").asText());
                    hospital.setDutyDiv(item.path("dutyDiv").asText());
                    hospital.setDutyDivNam(item.path("dutyDivNam").asText());
                    hospital.setDutyName(item.path("dutyName").asText());
                    hospital.setDutyTel1(item.path("dutyTel1").asText());
                    hospital.setDutyTime1c(item.path("dutyTime1c").asText());
                    hospital.setDutyTime1s(item.path("dutyTime1s").asText());
                    hospital.setWgs84Lat(item.path("wgs84Lat").asText());
                    hospital.setWgs84Lon(item.path("wgs84Lon").asText());
                    hospitals.add(hospital);
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing XML response", e);
        }
        return hospitals;
    }
}

package com.example.HospitalService.Service;

import com.example.HospitalService.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageRegisterService {
    private static final Logger logger = LoggerFactory.getLogger(HospitalService.class);
    private static final String PUBLIC_DATA_API_URL = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlBassInfoInqire";
    @Value("${api.service.key}")
    private String SERVICE_KEY;
    private static final String NUM_OF_ROWS = "200";
    private static final String PAGE_NO = "1";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    public String callExternalService() {
        // 특정 회로 차단기 설정을 사용 (여기서는 'circuitBreaker1')
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");

        return circuitBreaker.run(() -> restTemplate.getForObject("http://external-service/api", String.class),
                throwable -> fallbackMethod(throwable));
    }

    public String fallbackMethod(Throwable throwable) {
        // 외부 서비스 호출 실패 시 대체 로직
        return "Fallback response";
    }

    private String buildApiUrl(HpidRequest hpid) { // api 호출 url 만들기

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(PUBLIC_DATA_API_URL)
                .queryParam("pageNo", PAGE_NO)
                .queryParam("numOfRows", NUM_OF_ROWS)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("HPID", hpid.getHpid())
                .encode()
                .build();

        StringBuilder apiUrl = new StringBuilder(builder.toString());

        String apiUrlStr = apiUrl.toString();
        logger.info("Final API URL: " + apiUrlStr);
        return apiUrlStr;
    }
    public DutyTimes registerDate(HpidRequest hpid) { // api 호출 보내기 함수
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(); // building 하는 요소들 제어
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); // 인코딩 자체를 멈추기 막아버리기
        restTemplate.setUriTemplateHandler(uriBuilderFactory); // 레스트 템플릿 빌딩 잡기

        String apiUrl = buildApiUrl(hpid); //hpid 보내기
        logger.info("Constructed API URL: " + apiUrl);

        // 서킷달기
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");

        String response = circuitBreaker.run(() -> restTemplate.getForObject(apiUrl.replace("%25","%"), String.class),
                throwable -> fallbackMethod(throwable));

        //String response = restTemplate.getForObject(apiUrl.replace("%25","%"), String.class); // 혹시 몰라서 한번 더 25 제거
        String utf8EncodedResponse = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // 한글 깨지는거 잡기

        logger.info("Response: " + utf8EncodedResponse);

        return parseXmlResponse(utf8EncodedResponse);
    }

    private DutyTimes parseXmlResponse(String response) {
        DutyTimes dataDate  = new DutyTimes();
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode root = xmlMapper.readTree(response.getBytes(StandardCharsets.UTF_8));
            JsonNode item = root.path("body").path("items").path("item");

            // 여기 처리 필요함
            dataDate.setHpid(item.path("hpid").asText());
            dataDate.setDutyAddr(item.path("dutyAddr").asText());
            dataDate.setDutyName(item.path("dutyName").asText());
            dataDate.setDutyTel1(item.path("dutyTel1").asText());
            dataDate.setDutyTel3(item.path("dutyTel3").asText());
            dataDate.setDutyTime1c(item.path("dutyTime1c").asText());
            dataDate.setDutyTime1s(item.path("dutyTime1s").asText());
            dataDate.setDutyTime2c(item.path("dutyTime2c").asText());
            dataDate.setDutyTime2s(item.path("dutyTime2s").asText());
            dataDate.setDutyTime3c(item.path("dutyTime3c").asText());
            dataDate.setDutyTime3s(item.path("dutyTime3s").asText());
            dataDate.setDutyTime4c(item.path("dutyTime4c").asText());
            dataDate.setDutyTime4s(item.path("dutyTime4s").asText());
            dataDate.setDutyTime5c(item.path("dutyTime5c").asText());
            dataDate.setDutyTime5s(item.path("dutyTime5s").asText());
            dataDate.setDutyTime6c(item.path("dutyTime6c").asText());
            dataDate.setDutyTime6s(item.path("dutyTime6s").asText());
            dataDate.setDutyTime7c(item.path("dutyTime7c").asText());
            dataDate.setDutyTime7s(item.path("dutyTime7s").asText());
            dataDate.setDutyTime8c(item.path("dutyTime8c").asText());
            dataDate.setDutyTime8s(item.path("dutyTime8s").asText());

            dataDate.setDgidIdName(item.path("dgidIdName").asText());



        } catch (Exception e) {
            logger.error("Error parsing XML response", e);
        }
        logger.info("Parsed hospital detail data: " + dataDate.toString());
        return dataDate;
    }
}

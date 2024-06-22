package com.example.HospitalService.Service;

import com.example.HospitalService.dto.FavoriteHospitalResponse;
import com.example.HospitalService.dto.HospitalDetailData;
import com.example.HospitalService.dto.HpidRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Service
public class FavoriteHospitalService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteHospitalService.class);

    @Autowired
    private RestTemplate restTemplate;

    private static final String PUBLIC_DATA_API_URL = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlBassInfoInqire";

    @Value("${api.service.key}")
    private String SERVICE_KEY;
    private static final String NUM_OF_ROWS = "300";
    private static final String PAGE_NO = "1";

    public FavoriteHospitalResponse registerFavoriteHospital(HpidRequest hpid) { // api 호출 보내기 함수
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(); // building 하는 요소들 제어
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); // 인코딩 자체를 멈추기 막아버리기
        restTemplate.setUriTemplateHandler(uriBuilderFactory); // 레스트 템플릿 빌딩 잡기

        String apiUrl = buildApiUrl(hpid); //hpid 보내기
        logger.info("Constructed API URL: " + apiUrl);
        String response = restTemplate.getForObject(apiUrl.replace("%25","%"), String.class); // 혹시 몰라서 한번 더 25 제거
        String utf8EncodedResponse = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // 한글 깨지는거 잡기

        logger.info("Response: " + utf8EncodedResponse);

        return parseXmlResponse(utf8EncodedResponse);
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


    private FavoriteHospitalResponse parseXmlResponse(String response) {
        FavoriteHospitalResponse favoriteHospitalResponse  = new FavoriteHospitalResponse();
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode root = xmlMapper.readTree(response.getBytes(StandardCharsets.UTF_8));
            JsonNode item = root.path("body").path("items").path("item");

            // mypage 엔티티에 저장할 요소 호출
            favoriteHospitalResponse.setHpid(item.path("hpid").asText());
            favoriteHospitalResponse.setDutyAddr(item.path("dutyAddr").asText());
            favoriteHospitalResponse.setDutyName(item.path("dutyName").asText());
            favoriteHospitalResponse.setDutyTel1(item.path("dutyTel1").asText());

        } catch (Exception e) {
            logger.error("Error parsing XML response", e);
        }
        logger.info("Parsed hospital detail data: " + favoriteHospitalResponse.toString());
        return favoriteHospitalResponse;
    }
}

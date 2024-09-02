package com.mobalpa.api.service;

import com.mobalpa.api.dto.forecast.ForecastDTO;
import com.mobalpa.api.dto.forecast.SummaryDTO;
import com.mobalpa.api.dto.forecast.SalesDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

@Service
public class ForecastService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${forecast.base-url}")
    private String forecastUrl;

    @Value("${forecast.api-key}")
    private String apiKey;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        return headers;
    }

    public List<ForecastDTO> getForecast(String reportType) {
        String url = String.format("%s?report_type=%s", forecastUrl + "/forecast", reportType);
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<ForecastDTO[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, ForecastDTO[].class);
        return List.of(response.getBody());
    }

    public List<SummaryDTO> getSummary(String reportType) {
        String url = String.format("%s?report_type=%s", forecastUrl + "/summary", reportType);
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<SummaryDTO[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, SummaryDTO[].class);
        return List.of(response.getBody());
    }

    public List<SalesDTO> getSales(String startDate, String endDate) {
        String url = String.format("%s?start_date=%s&end_date=%s", forecastUrl + "/sales", startDate, endDate);
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<SalesDTO[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, SalesDTO[].class);
        System.out.println(List.of(response.getBody()));
        return List.of(response.getBody());
    }

    public byte[] downloadCsv(String endpoint, String reportTypeOrStartDate, String endDate) {
        String url;
        if (endpoint.equals("forecast")) {
            url = String.format("%s/csv?report_type=%s", forecastUrl + "/forecast", reportTypeOrStartDate);
        } else if (endpoint.equals("summary")) {
            url = String.format("%s/csv?report_type=%s", forecastUrl + "/summary", reportTypeOrStartDate);
        } else if (endpoint.equals("sales")) {
            url = String.format("%s/csv?start_date=%s&end_date=%s", forecastUrl + "/sales", reportTypeOrStartDate, endDate);
        } else {
            throw new IllegalArgumentException("Invalid endpoint");
        }

        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
        return response.getBody();
    }
}
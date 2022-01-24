package com.loamoa.loamoa.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class ItemService {
    public String checkItemInfo(String username) {
        RestTemplate restTemplate = new RestTemplate();
        URI calculateURI = buildURI(username);
        String result = restTemplate.getForObject(calculateURI, String.class);
        return result;
    }

    private URI buildURI(String username) {
        String endpointURI = "http://apis.iptime.org/LostArk/Character/Character-Item";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpointURI)
                .queryParam("NickName", username);
        return builder.build().encode().toUri();
    }
}

package hr.algebra.iis_api_service.services;

import hr.algebra.iis_api_service.models.Photo;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AuthService {

    public AuthService(){
    }

    public static final String API_URL = "http://localhost:8081/";

    public static void getPhotos()
    {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(API_URL + "photos", String.class);
        //List<Photo>
        System.out.println(result);
    }


}

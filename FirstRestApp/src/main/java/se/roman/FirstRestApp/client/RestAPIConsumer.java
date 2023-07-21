package se.roman.FirstRestApp.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.roman.FirstRestApp.models.Measurement;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RestAPIConsumer {
    public static void main(String[] args) {
        final String sensorName = "new sensor57";
        registerSensor(sensorName);

        Random random = new Random();


        Float maxTemperature = 100F;
        for (int i = 0; i < 100; i++) {
            sendMeasurement(random.nextFloat()*maxTemperature, random.nextBoolean(), sensorName );
        }
    }

    private static void registerSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors/register";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJsonData(url, jsonData);
    }

    private static void makePostRequestWithJsonData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);
        try{
            restTemplate.postForObject(url, request, String.class);
        }
        catch (HttpClientErrorException e){
            System.out.println("ERROR");
            System.out.println(e.getMessage());
        }
    }

    private static void sendMeasurement(Float value, Boolean raining, String sensorName){
        final String url = "http://localhost:8080/measurements/add";

        Map<String, Object> measurement = new HashMap<>();
        measurement.put("value", value);
        measurement.put("raining", raining);
        measurement.put("sensor", Map.of("name", sensorName));

        makePostRequestWithJsonData(url, measurement);
    }
}

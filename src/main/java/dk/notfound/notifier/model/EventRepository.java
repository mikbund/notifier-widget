package dk.notfound.notifier.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import dk.notfound.notifier.model.Event;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dk.notfound.notifier.config.ConfigLoader;


public class EventRepository {


    private ConfigLoader configLoader = new ConfigLoader();

    public Collection<Event> getEvents() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();

        //"http://localhost:8080/event",
        String url = configLoader.getListAllEvents();

        ResponseEntity<List<Event>> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Event>>() {}
                );

        List<Event> events = responseEntity.getBody();

        return events;
    }

    public Collection<Event> getUnhandledEvents() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();

        String url = configLoader.getListUnhandledEvents();

        ResponseEntity<List<Event>> responseEntity =
                restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>() {}
        );

        List<Event> events = responseEntity.getBody();

        return events;
    }


    public Collection<Event> acknowledgeEvent(Event event)  {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        // http://localhost:8080/event/acknowledge/
        String url = configLoader.getAcknowledgeEvent();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String eventAsJson = objectMapper.writeValueAsString(event);


            HttpEntity<String> request =
                    new HttpEntity<String>(eventAsJson, headers);

            ResponseEntity<String> responseEntityStr = restTemplate.
                    postForEntity(url+"/"+event.getId(), request, String.class);

            //JsonNode root = objectMapper.readTree(responseEntityStr.getBody());


        } catch (Exception e) {
            System.out.println("Exception" + e.toString());
        }










        return null;
    }


    public Collection<Event> acknowledgeEvent(Long id) {

        return null;
    }



}

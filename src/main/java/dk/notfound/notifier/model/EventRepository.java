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
import java.util.HashMap;
import java.util.List;

import dk.notfound.notifier.config.ConfigLoader;


public class EventRepository {


    private ConfigLoader configLoader = new ConfigLoader();

    public Collection<Event> getEvents() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();


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

    public Event getEvent(Long id) {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();

        String url = configLoader.getListAllEvents() + "/" + id.toString();

        ResponseEntity<Event> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Event>() {}
                );

        Event event = responseEntity.getBody();
        return event;
    }



    public Collection<Event> getAcknowledgedEvents() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();

        String url = configLoader.getListAcknowledgedEvents();

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


    public void acknowledgeEvent(Event event)  {

        Long eventId = event.getId();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

    //    String url = configLoader.getAcknowledgeEvent() + "/" + eventId;
        String url = configLoader.getAcknowledgeEvent().replace("{id}",eventId.toString());
        System.out.println("URL acknowledge event: " + url);
        event.setEventResponsible(configLoader.getEventResponsible());

        restTemplate.put(url, event, Event.class);

    }

}

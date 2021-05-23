package dk.notfound.notifier.model;

import dk.notfound.notifier.model.Event;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EventRepository {





    public Collection<Event> getEvents() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Event>> responseEntity =
                restTemplate.exchange(
                        "http://localhost:8080/event",
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

        ResponseEntity<List<Event>> responseEntity =
                restTemplate.exchange(
                "http://localhost:8080/event/unhandled",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>() {}
        );

        List<Event> events = responseEntity.getBody();

        return events;
    }


    public Collection<Event> acknowledgeEvent(Event event) {

        return null;
    }


    public Collection<Event> acknowledgeEvent(Long id) {

        return null;
    }



}

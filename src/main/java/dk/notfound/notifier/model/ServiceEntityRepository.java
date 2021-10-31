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




public class ServiceEntityRepository {



    private ConfigLoader configLoader = new ConfigLoader();


    public Collection<ServiceEntity> getServiceEntities() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();

        String url = configLoader.getListServiceEntities();

        ResponseEntity<List<ServiceEntity>> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ServiceEntity>>() {}
                );


        List<ServiceEntity> serviceEntities = responseEntity.getBody();

        return serviceEntities;
    }


}

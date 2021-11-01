package dk.notfound.notifier.model;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
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




    public ServiceEntity getServiceEntityById(Long id) {

        ServiceEntity serviceEntity;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();

        String url = configLoader.getListServiceEntities() + "/" + id;

        ResponseEntity<ServiceEntity> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ServiceEntity>() {}
                );

        serviceEntity = responseEntity.getBody();

        return serviceEntity;
    }

    public void patchServiceEntity(ServiceEntity serviceEntity) {

        Long id = serviceEntity.getId();
        String url = configLoader.getListServiceEntities() + "/" + id.toString();
        //System.out.println("Patch serviceEntity: " + url);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println("URL: " + url);
        restTemplate.put(url, serviceEntity, Event.class);

    }


/*
    public void patchServiceEntity(ServiceEntity serviceEntity)  {

        Long id = serviceEntity.getId();

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = configLoader.getListServiceEntities() + "/" + id.toString();
        System.out.println("Patch serviceEntity: " + url);

        final HttpEntity<ServiceEntity> entity = new HttpEntity<ServiceEntity>(serviceEntity);

//        final HttpEntity<String> entity = new HttpEntity<Serv>(JSON.toJSONString(serviceEntity), headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
          //  String statusCode =  response.getStatusCode();
        } catch (HttpClientErrorException e) {
            // handle exception here
        }
    }


 */





}

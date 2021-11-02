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
        restTemplate.put(url, serviceEntity, ServiceEntity.class);
        //restTemplate.postForObject(url,serviceEntity,ServiceEntity.class);

    }



    public void createServiceEntity(ServiceEntity serviceEntity) {

        String url = configLoader.getListServiceEntities();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println("URL: " + url + "String:" + serviceEntity.toString());
        restTemplate.postForObject(url,serviceEntity,ServiceEntity.class);
    }




    public void deleteServiceEntity(Long id) {

        String url = configLoader.getListServiceEntities() + "/" + id.toString();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println("Delete ressource: URL: " + url);
        restTemplate.delete(url);

    }




}

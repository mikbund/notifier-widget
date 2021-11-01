package dk.notfound.notifier.controller;

import dk.notfound.notifier.model.ServiceEntity;
import dk.notfound.notifier.model.ServiceEntityRepository;

import javax.validation.constraints.Null;
import java.security.Provider;
import java.util.Collection;
import java.util.HashMap;

public class ServiceEntityHandler {

    ServiceEntityRepository serviceEntityRepository = new ServiceEntityRepository();

    private HashMap<Long, ServiceEntity> serviceEntityHashMap= new HashMap<>();

    public Collection<ServiceEntity> getServiceEntityCollection() {
        return serviceEntityHashMap.values();
    }

    public HashMap<Long, ServiceEntity> getServiceEntityHashMap() {
        return this.serviceEntityHashMap;
    }

    public void fetchServiceEntities() {
        Collection<ServiceEntity> serviceEntities;
        serviceEntities= serviceEntities=serviceEntityRepository.getServiceEntities();

        serviceEntityHashMap.clear();
        for(ServiceEntity se: serviceEntities) {
            this.serviceEntityHashMap.put(se.getId(), se);
        }
    }


    public ServiceEntity getServiceEntityById(Long id)  {

        return serviceEntityHashMap.get(id);
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        serviceEntityHashMap.remove(serviceEntity.getId());
        serviceEntityHashMap.put(serviceEntity.getId(), serviceEntity);
    }

    public void saveServiceEntity(ServiceEntity se) {
            serviceEntityRepository.patchServiceEntity(se);
    }

}

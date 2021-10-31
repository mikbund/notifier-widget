package dk.notfound.notifier.controller;


import dk.notfound.notifier.model.ServiceEntity;
import dk.notfound.notifier.model.ServiceEntityRepository;

import java.util.Collection;

public class ServiceEntityHandler {

    ServiceEntityRepository serviceEntityRepository = new ServiceEntityRepository();

    public Collection<ServiceEntity> getServiceEntityCollection() {

        return serviceEntityRepository.getServiceEntities();
    }

}

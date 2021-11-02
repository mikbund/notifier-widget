package dk.notfound.notifier.controller;

import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.model.EventRepository;
import dk.notfound.notifier.model.ServiceEntity;
import dk.notfound.notifier.model.ServiceEntityRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.sql.Timestamp;
import java.util.HashSet;

public class ServiceEntityHandler {

    ServiceEntityRepository serviceEntityRepository = new ServiceEntityRepository();
    EventRepository eventRepository = new EventRepository();


    private HashMap<Long, ServiceEntity> serviceEntityHashMap= new HashMap<>();

    public Collection<ServiceEntity> getServiceEntityCollection() {
        return serviceEntityHashMap.values();
    }

    public HashMap<Long, ServiceEntity> getServiceEntityHashMap() {
        return this.serviceEntityHashMap;
    }


    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    public LocalDateTime convertTsToLocalDateTime(Timestamp ts) {
        return ts.toLocalDateTime();
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

    public void addMissingServiceEntities() {

        fetchServiceEntities();

        HashSet<String> serviceIdentitySet = new HashSet<>();

        for(Event event: eventRepository.getUnhandledEvents()) {
            serviceIdentitySet.add("".concat(event.getServiceIdentifier()) );
        }

        for(ServiceEntity se: this.getServiceEntityCollection()) {
            serviceIdentitySet.remove("".concat(se.getServiceIdentifier()));
        }




        for(String s: serviceIdentitySet) {
            System.out.println(s + "     " + s.hashCode() ) ;

            ServiceEntity se = new ServiceEntity();
            se.setServiceIdentifier(s);
            se.setAutoAcknowledgeEventOnReception(false);
            se.setEventAcknowledgeTimer(0L);
            se.setAutoAcknowledgeEventOnTimer(false);


            serviceEntityRepository.createServiceEntity(se);

        }





    }




}
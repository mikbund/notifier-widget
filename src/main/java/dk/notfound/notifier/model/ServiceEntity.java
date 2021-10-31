package dk.notfound.notifier.model;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;



public class ServiceEntity {

    private Long id;

    String serviceIdentifier;


    private Long eventAcknowledgeTimer;


    private Boolean autoAcknowledgeEventOnReception = false;



    Timestamp autoAcknowledgeEventOnReceptionUntilTs;



    private Boolean autoAcknowledgeEventOnTimer = false;


    private Timestamp created_ts;


    private Timestamp updated_ts;


    public Long getId() {
        return id;
    }

    public void setEventAcknowledgeTimer(Long eventAcknowledgeTimer) {
        this.eventAcknowledgeTimer = eventAcknowledgeTimer;
    }

    public Long getEventAcknowledgeTimer() {
        return eventAcknowledgeTimer;
    }


    public String getServiceIdentifier() {
        return serviceIdentifier;
    }

    public void setAutoAcknowledgeEventOnTimer(Boolean autoAcknowledgeEventOnTimer) {
        this.autoAcknowledgeEventOnTimer = autoAcknowledgeEventOnTimer;
    }

    public Boolean getAutoAcknowledgeEventOnTimer() {
        return autoAcknowledgeEventOnTimer;
    }


    public Boolean getAutoAcknowledgeEventOnReception() {
        return autoAcknowledgeEventOnReception;
    }

    public void setAutoAcknowledgeEventOnReception(Boolean autoAcknowledgeEventOnReception) {
        this.autoAcknowledgeEventOnReception = autoAcknowledgeEventOnReception;
    }

    public Timestamp getAutoAcknowledgeEventOnReceptionUntilTs() {
        return autoAcknowledgeEventOnReceptionUntilTs;
    }

    public void setAutoAcknowledgeEventOnReceptionUntilTs(Timestamp autoAcknowledgeEventOnReceptionUntilTs) {
        this.autoAcknowledgeEventOnReceptionUntilTs = autoAcknowledgeEventOnReceptionUntilTs;
    }




}

package dk.notfound.notifier.model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Event {

    Long id;
    String serviceIdentifier;
    Boolean acknowledged;
    String eventRaw;
    String eventResponsible;
    Timestamp created_ts;
    Timestamp updated_ts;

    Group group;

    public Long getId() {
        return id;
    }

    public String getServiceIdentifier() {
        return serviceIdentifier;
    }

    public Boolean getAcknowledged() {
        return acknowledged;
    }

    public Boolean setAcknowledged(Boolean acknowledged) {
        this.acknowledged=acknowledged;
        return acknowledged;
    }

    public String getEventRaw() {
        return eventRaw;
    }
    //         "updated_ts": "2021-05-23T08:48:09.952096",

    public String getEventResponsible() {
        return eventResponsible;
    }

    public void setEventResponsible(String eventResponsible) {
        this.eventResponsible = eventResponsible;
    }

    public Timestamp getCreated_ts() {
        return created_ts;
    }

    public Timestamp getUpdated_ts() {
        return updated_ts;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group=group;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + "\nAcknowledged: " + getAcknowledged() + "\nEvent: " + getEventRaw() + "\nEventResponsible: " + getEventResponsible();
    }




}

package dk.notfound.notifier.model;

import java.sql.Timestamp;

public class Event {

    Long id;
    String serviceIdentifier;
    Boolean acknowledged;
    String eventRaw;
    Timestamp created_ts;
    Timestamp updated_ts;


    public Long getId() {
        return id;
    }

    public String getServiceIdentifier() {
        return serviceIdentifier;
    }

    public Boolean getAcknowledged() {
        return acknowledged;
    }

    public String getEventRaw() {
        return eventRaw;
    }
    //         "updated_ts": "2021-05-23T08:48:09.952096",

    @Override
    public String toString() {
        return "Id: " + getId() + "\nAcknowledged: " + getAcknowledged() + "\nEvent: " + getEventRaw();
    }

}

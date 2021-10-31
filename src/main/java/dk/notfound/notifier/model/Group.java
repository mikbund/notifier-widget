

package dk.notfound.notifier.model;


import java.sql.Timestamp;
import java.time.LocalDateTime;



public class Group {

    private Long id;

    String groupName;

    Boolean enabled;

    private Timestamp created_ts;

    private Timestamp updated_ts;

    public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Boolean getEnabled() { return enabled; }

    public Timestamp getCreated_ts() {
        return created_ts;
    }

    public Timestamp getUpdated_ts() {
        return updated_ts;
    }


    public String toString() {
        return "id: " + id + " groupName: " + groupName + " enabled: " + enabled;
    }
}


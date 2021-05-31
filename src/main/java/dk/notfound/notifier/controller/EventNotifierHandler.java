package dk.notfound.notifier.controller;


import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.model.EventRepository;
import dk.notfound.notifier.widget.EventViewerWidget;

import java.util.Collection;


public class EventNotifierHandler {

    EventRepository eventRepository = new EventRepository();
    EventViewerWidget eventViewerWidget;


    public EventNotifierHandler(EventViewerWidget eventViewerWidget) {
        this.eventViewerWidget = eventViewerWidget;
    }



    public void acknowledgeEvent(Event e) {

            e.setAcknowledged(true);
            eventRepository.acknowledgeEvent(e);

    }


    public Collection<Event> getUnhandledEvents() {
        return eventRepository.getUnhandledEvents();
    }


    public void acknowledgeEventById(Long id) {

        Event event = eventRepository.getEvent(id);
        this.acknowledgeEvent(event);
    }



    public void acknowledgeAllEvents() {

        for(Event e: eventRepository.getUnhandledEvents()) {
            e.setAcknowledged(true);
            eventRepository.acknowledgeEvent(e);
        }


    }

}

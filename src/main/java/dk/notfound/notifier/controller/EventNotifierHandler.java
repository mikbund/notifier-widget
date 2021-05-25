package dk.notfound.notifier.controller;


import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.model.EventRepository;
import dk.notfound.notifier.widget.EventViewerWidget;


public class EventNotifierHandler {

    EventRepository eventRepository = new EventRepository();
    EventViewerWidget eventViewerWidget;


    public EventNotifierHandler(EventViewerWidget eventViewerWidget) {
        this.eventViewerWidget = eventViewerWidget;
    }


    public void acknowledgeAllEvents() {

        for(Event e: eventRepository.getUnhandledEvents()) {
            e.setAcknowledged(true);
            eventRepository.acknowledgeEvent(e);

            eventViewerWidget.updateEventTable(eventRepository.getUnhandledEvents());

        }

    }

}

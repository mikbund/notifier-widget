package dk.notfound.notifier.controller;

import dk.notfound.notifier.config.ConfigLoader;
import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.model.EventRepository;
import dk.notfound.notifier.widget.EventViewerWidget;

import java.util.Collection;

public class NotifierUpdater extends Thread {

    private ConfigLoader configloader = new ConfigLoader();
    private EventViewerWidget eventViewerWidget;




    public NotifierUpdater(EventViewerWidget eventViewerWidget) {
        this.eventViewerWidget = eventViewerWidget;



    }

    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            checkUnhandledEventsAndNotify();

            try {



                sleep(configloader.getPollFrequency()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void checkUnhandledEventsAndNotify() {

        EventRepository eventRepository = new EventRepository();
        Collection<Event> eventCollection = eventRepository.getUnhandledEvents();

        eventViewerWidget.updateEventTable(eventCollection);


    }



}

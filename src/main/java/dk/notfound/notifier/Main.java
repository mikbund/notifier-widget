package dk.notfound.notifier;

import dk.notfound.notifier.config.ConfigLoader;
import dk.notfound.notifier.controller.EventNotifierHandler;
import dk.notfound.notifier.controller.NotifierUpdater;
import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.model.EventRepository;
import dk.notfound.notifier.widget.EventViewerWidget;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Main {





    public static void main(String[] args) {

        String dir = System.getProperty("user.dir");
        System.out.println("Working directory: " + dir);


        ConfigLoader configLoader = new ConfigLoader();
        configLoader.printConfiguration();
        EventViewerWidget eventViewerWidget = new EventViewerWidget();
        NotifierUpdater notifierUpdater = new NotifierUpdater(eventViewerWidget);

        eventViewerWidget.runWidget();

        notifierUpdater.run();



    }
}

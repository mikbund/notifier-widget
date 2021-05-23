package dk.notfound.notifier;

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
	// write your code here



        EventViewerWidget eventViewerWidget = new EventViewerWidget();
        eventViewerWidget.runWidget();




    }
}

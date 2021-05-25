package dk.notfound.notifier.widget;

import dk.notfound.notifier.model.Event;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import dk.notfound.notifier.controller.EventNotifierHandler;


public class EventViewerWidget {

    private JFrame frame;
    private JPanel panel;
    private JTable jTableEventList;
    private JTableHeader jTableEventHeader;
    private JButton jButtonAcknowledgeEvent;
    private JButton jButtonMuteEvents;
    private JLabel labelLastUpdated;
    private JPanel labelEvents;
    private JPanel panelStatus;
    private JLabel labelStatus;
    private EventNotifierHandler eventNotifierHandler = new EventNotifierHandler(this);

    private Boolean muteNotification = false;
    DefaultTableModel model = new DefaultTableModel();
    private Collection<Event> eventCollection = new ArrayList<Event>();

    public EventViewerWidget() {

        jButtonAcknowledgeEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventNotifierHandler.acknowledgeAllEvents();
            }
        });


        jButtonMuteEvents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                muteNotification=!muteNotification;
                setjButtonMuteEventState();
            }
        });
    }

    public void runWidget() {

        jTableEventList.setModel(model);
        String[] columns = {"Id", "Created" , "Service", "Event"};
        model.addColumn(columns[0]);
        model.addColumn(columns[1]);
        model.addColumn(columns[2]);
        model.addColumn(columns[3]);



        frame = new JFrame("Widget");
        frame.setContentPane(panel);

        frame.setBounds(300,300,300,300);
        frame.setTitle("Event notifier");
        frame.setSize(600,300);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }


    public void clearEventTable() {
        while(model.getRowCount()>0) // remove rows if any
        {
            model.removeRow(0);
        }
    }

    public void updateTableWithEvents() {
        for(Event e: eventCollection) { // fills with new data
            String[] row= {e.getId().toString(), e.getCreated_ts().toString(), e.getServiceIdentifier(), e.getEventRaw()};
            model.addRow(row);
        }
    }

    public void setUpdateInformationLabel() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatDateTime = now.format(format);
        labelLastUpdated.setText(formatDateTime);

        if (this.eventCollection.isEmpty()) {
            panelStatus.setForeground(Color.GREEN);
            panelStatus.setBackground(Color.GREEN);
        } else
        {
           panelStatus.setForeground(Color.RED);
           panelStatus.setBackground(Color.RED);
         }



    }

    //Notify user if queue is not empty and user has not set ignore
    public void notifyUserAndSetVisibility() {

        if (eventCollection.isEmpty()) {
            muteNotification = false;
        }

        //queue is not empty user has not responded - set frame visible
        if(!eventCollection.isEmpty() && muteNotification ==false) {
            bringToFront();
        }
    }

    public void bringToFront() {
        frame.setExtendedState(JFrame.NORMAL);
        frame.setAlwaysOnTop(true);
        frame.requestFocus();

    }

    public void updateEventTable(Collection<Event> eventCollection) {

        this.eventCollection.removeAll(this.eventCollection);
        this.eventCollection.addAll(eventCollection);

        notifyUserAndSetVisibility();

        setUpdateInformationLabel();
        clearEventTable();
        updateTableWithEvents();
        setjButtonMuteEventState();


    }


    public void setjButtonMuteEventState() {
        if(eventCollection.isEmpty()) {
            jButtonMuteEvents.setEnabled(false);
            muteNotification=false;
        } else {
            jButtonMuteEvents.setEnabled(true);
        }

        if (muteNotification==true) {
            jButtonMuteEvents.setText("Unmute events");

        } else
        {
            jButtonMuteEvents.setText("Mute events");
        }
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

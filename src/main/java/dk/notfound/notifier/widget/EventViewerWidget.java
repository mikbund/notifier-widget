package dk.notfound.notifier.widget;

import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.config.ConfigLoader;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import dk.notfound.notifier.controller.EventNotifierHandler;

public class EventViewerWidget {

    private ConfigLoader configLoader = new ConfigLoader();
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
    private JButton jButtonAcknowledgeSelected;
    private JButton jButtonOpenLink;
    private EventNotifierHandler eventNotifierHandler = new EventNotifierHandler(this);

    private Boolean muteNotification = false;
    DefaultTableModel model = new DefaultTableModel();
    private Collection<Event> eventCollection = new ArrayList();

    public EventViewerWidget() {

        jButtonAcknowledgeEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acknowledgeAllEvents();
            }
        });
        jButtonMuteEvents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                muteNotification=!muteNotification;
                setjButtonMuteEventState();
            }
        });
        jButtonAcknowledgeSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acknowledgeSelectedEvent();

            }
        });

        jButtonOpenLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLinkInBrowser();
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


    public synchronized void addEventToEventTable(Event event) {

        String[] row = {event.getId().toString(), event.getCreated_ts().toString(), event.getServiceIdentifier(), event.getEventRaw()};
        this.model.addRow(row);

    }

    public void setUpdateInformationLabel() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatDateTime = now.format(format);
        labelLastUpdated.setText(formatDateTime);

        if (eventCollection.isEmpty()) {
            panelStatus.setForeground(Color.GREEN);
            panelStatus.setBackground(Color.GREEN);
        } else
        {
           panelStatus.setForeground(Color.RED);
           panelStatus.setBackground(Color.RED);

           if(muteNotification==false) {
               frame.setExtendedState(JFrame.NORMAL);
               frame.setAlwaysOnTop(true);
           }


        }
    }

    // prunes jTable model
    private synchronized void pruneEventsFromTable() {
        Collection<Long> eventList = new ArrayList();

        //Creates a hashTable with mapping ID from  eventCollection
        for(Event e: this.eventCollection) {
            eventList.add(e.getId());
        }

        Integer row= 0;
        Long cellIdValue;

        while (row<model.getRowCount()) {

            cellIdValue = Long.valueOf((String) model.getValueAt(row,0));

            if(eventList.contains(cellIdValue)==false) {
                model.removeRow(row);
                row=0;
            } else
                row++;
        }



        /*while(row<this.model.getRowCount()) {
            cellIdValue = Long.valueOf((String) model.getValueAt(row,0));

            if(eventHashmap.containsKey(cellIdValue)==false) {
                // removes row in table that does not exist in eventCollection
                // we also removes them newEventHashMap - else we will get dublicates. This list contains new events not present in table
                //this.model.removeRow(row);
                deleteRowNum.add(row);
            }
                row++;
        }*/

    }


    private synchronized void addNewEventsToTable() {

        Collection<Long> tableId = new ArrayList();

        int row = 0;
        Long cellValue;


          while(row<model.getRowCount()) {
            cellValue = Long.valueOf((String) model.getValueAt(row,0));
            tableId.add(cellValue);
            row++;
        }

        for(Event e: this.eventCollection) {
            if(tableId.contains(e.getId())==false) {
                addEventToEventTable(e);
            }
        }

    }

    public synchronized void updateEventTable(Collection<Event> eventCollection) {
            this.eventCollection.clear();
            this.eventCollection.addAll(eventCollection);

            pruneEventsFromTable();
            addNewEventsToTable();
            setUpdateInformationLabel();
    }

    public void acknowledgeAllEvents() {
        eventNotifierHandler.acknowledgeAllEvents();
        updateEventTable(eventNotifierHandler.getUnhandledEvents());


    }

    public void acknowledgeSelectedEvent() {


        Integer id;
        String cell;


            cell = (String) jTableEventList.getModel().getValueAt(jTableEventList.getSelectedRow(), 0);
            id = Integer.valueOf(cell);

            eventNotifierHandler.acknowledgeEventById(Long.valueOf(id));
            updateEventTable(eventNotifierHandler.getUnhandledEvents());
    }





    public void openLinkInBrowser() {
        //Long id;
        String cell;
        String url;
        cell = (String) jTableEventList.getModel().getValueAt(jTableEventList.getSelectedRow(), 2);
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        url=configLoader.getWiki()+"/"+cell;


        try {

            System.out.println("Running which OS " + os.toString());


            if (os.toString().startsWith("win")) {
                System.out.println("Run: rundll32 url.dll,FileProtocolHandler " + url);

                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
                //rt.exec("start /max " + url);
            } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

                String[] browsers = {"firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};

                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    if (i == 0)
                        cmd.append(String.format("%s \"%s\"", browsers[i], url));
                    else
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                // If the first didn't work, try the next browser and so on

                System.out.println("opening " + cmd.toString());
                rt.exec(new String[]{"sh", "-c", cmd.toString()});

            }
        }catch(Exception e) {
            e.printStackTrace();
        }
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

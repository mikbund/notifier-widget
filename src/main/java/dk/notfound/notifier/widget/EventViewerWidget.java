package dk.notfound.notifier.widget;

import dk.notfound.notifier.controller.ServiceEntityHandler;
import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.config.ConfigLoader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import dk.notfound.notifier.model.ServiceEntity;

public class EventViewerWidget {

    private ConfigLoader configLoader = new ConfigLoader();
    private JFrame frame;
    private JPanel alertPanel;
    private JTable jTableEventList;
    private JTableHeader jTableEventHeader;
    private JButton jButtonAcknowledgeEvent;
    private JButton jButtonMuteEvents;
    private JLabel labelLastUpdated;
    private JPanel panelStatus;
    private JLabel labelStatus;
    private JButton jButtonAcknowledgeSelected;
    private JButton jButtonOpenLink;
    private JTabbedPane tabbedPane;
    private JTable jTableResolvedEvents;
    private JPanel jPanelResolved;
    private JPanel panelEvents;
    private JPanel panelServiceEntities;
    private JTable jTableServiceEntities;
    private JButton saveButton;
    private JButton reloadButton;
    private JCheckBox acknowledgeEventsOnTimerCheckBox;
    private JCheckBox acknowledgeOnReceptionCheckBox;
    private JFormattedTextField formattedTextField2;
    private JFormattedTextField formattedTextField1;
    private EventNotifierHandler eventNotifierHandler = new EventNotifierHandler(this);
    private ServiceEntityHandler serviceEntityHandler = new ServiceEntityHandler();


    private Boolean muteNotification = false;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel modelResolvedEvents = new DefaultTableModel();
    DefaultTableModel modelServiceEntities = new DefaultTableModel();
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

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabbedPane.getSelectedIndex()) {
                    case 0:
                        ;
                    case 1:
                        displayAcknowledgedEvents();
                        ;
                    case 2:
                        displayServiceEntities();
                        ;
                }
            }
        });

    }

    public void runWidget() {


        String[] columns = {"Id", "Created" , "Service", "Event"};
        jTableEventList.setModel(model);
        for(String s: columns) {
            model.addColumn(s);
        }

        String[] columnsResolvedTbl = {"Id", "Created", "Resolved", "Group", "Service", "Responsible", "Event"};
        jTableResolvedEvents.setModel(modelResolvedEvents);
        for(String s: columnsResolvedTbl) {
            modelResolvedEvents.addColumn(s);
        }

        String[] ColumnServiceEntityTbl = {"Id",  "ServiceIdentifier", "eventAcknowledgeTimer", "autoAcknowledgeEventOnReception", "autoAcknowledgeEventOnReceptionUntilTs", "autoAcknowledgeEventOnTimer"};
        jTableServiceEntities.setModel(modelServiceEntities);

        for(String s: ColumnServiceEntityTbl) {
            modelServiceEntities.addColumn(s);
        }

        frame = new JFrame("Widget");
        frame.setContentPane(alertPanel);
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



    public void displayServiceEntities() {

//        String[] ColumnServiceEntityTbl = {"Id",  "ServiceIdentifier", "eventAcknowledgeTimer", "autoAcknowledgeEventOnReception", "autoAcknowledgeEventOnReceptionUntilTs", "autoAcknowledgeEventOnTimer"};

        Collection<ServiceEntity> serviceEntities = serviceEntityHandler.getServiceEntityCollection();

        modelServiceEntities.setRowCount(0);

        for(ServiceEntity se: serviceEntities) {

                    String[] row = {
                                        se.getId().toString(),
                                        se.getServiceIdentifier(),
                                        se.getAutoAcknowledgeEventOnReception().toString(),
                                        se.getAutoAcknowledgeEventOnReceptionUntilTs().toString(),
                                        se.getEventAcknowledgeTimer().toString(),
                                        se.getAutoAcknowledgeEventOnTimer().toString()
                                    };
            modelServiceEntities.addRow(row);
        }

    }

    public void displayAcknowledgedEvents() {

        Collection<Event> events = eventNotifierHandler.getAcknowledgedEvents();
        String groupName;



        modelResolvedEvents.setRowCount(0);
        for(Event event: events) {

                try {
                    groupName=event.getGroup().getGroupName();
                } catch(NullPointerException e)  {
                    groupName="";
                }

            String[] row = {event.getId().toString(), event.getCreated_ts().toString(), event.getUpdated_ts().toString(), groupName, event.getServiceIdentifier(),event.getEventResponsible() , event.getEventRaw()};
            this.modelResolvedEvents.addRow(row);
        }
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

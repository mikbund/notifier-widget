package dk.notfound.notifier.widget;

import dk.notfound.notifier.controller.ServiceEntityHandler;
import dk.notfound.notifier.model.Event;
import dk.notfound.notifier.config.ConfigLoader;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
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
    private JButton jButtonSaveServiceEntity;
    private JButton jButtonReloadServiceEntities;
    private JCheckBox checkBoxAcknowledgeEventsOnTimer;
    private JCheckBox checkBoxAcknowledgeOnReception;
    private JFormattedTextField textFieldAcknowledgeTimer;
    private JFormattedTextField textFieldAcknowledgeUntilTS;
    private JButton jButtonDeleteServiceEntity;
    private JButton jButtonAutoClose;
    private EventNotifierHandler eventNotifierHandler = new EventNotifierHandler(this);
    private ServiceEntityHandler serviceEntityHandler = new ServiceEntityHandler();
    private Dimension frameMinimumDimension = new Dimension();

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

        jButtonReloadServiceEntities.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                serviceEntityHandler.addMissingServiceEntities();
                serviceEntityHandler.fetchServiceEntities();
                displayServiceEntities();
            }
        });

        jButtonAutoClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoAcknowledgeEvent();
                acknowledgeSelectedEvent();
            }
        });

        jButtonSaveServiceEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveServiceEntity();
                serviceEntityHandler.fetchServiceEntities();
                displayServiceEntities();
            }
        });

        jButtonDeleteServiceEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteServiceEntity();
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabbedPane.getSelectedIndex()) {
                    case 0:
                        break;

                    case 1:
                        displayAcknowledgedEvents();
                        break;

                    case 2:
                        serviceEntityHandler.fetchServiceEntities();
                        displayServiceEntities();
                        break;

                }
            }
        });


        jTableServiceEntities.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                    updateServiceEntitiesTextFieldsFromTbl();
            }
        });

    }


    public void autoAcknowledgeEvent() {
        String serviceIdentifier;
        Integer selectedRow = jTableEventList.getSelectedRow();

        serviceIdentifier = jTableEventList.getModel().getValueAt(selectedRow,2).toString();
        if(serviceIdentifier!=null)
            serviceEntityHandler.autoAcknowledgeEvent(serviceIdentifier, configLoader.getDefaultAutoAcknowledgeTime());

    }


    public void deleteServiceEntity() {

        Integer selectedRow = jTableServiceEntities.getSelectedRow();
        String cellValue = jTableServiceEntities.getModel().getValueAt(selectedRow,0).toString();
        Long cellId = Long.valueOf(cellValue);

        serviceEntityHandler.deleteServiceEntity(cellId);
        displayServiceEntities();

    }

    public void saveServiceEntity() {

        Integer selectedRow = jTableServiceEntities.getSelectedRow();
        String cellValue = jTableServiceEntities.getModel().getValueAt(selectedRow,0).toString();
        Long cellId = Long.valueOf(cellValue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime;
        Timestamp ts;

        ServiceEntity serviceEntity = serviceEntityHandler.getServiceEntityById(cellId);

        serviceEntity.setAutoAcknowledgeEventOnReception(checkBoxAcknowledgeOnReception.isSelected());
        serviceEntity.setAutoAcknowledgeEventOnTimer(checkBoxAcknowledgeEventsOnTimer.isSelected());
        serviceEntity.setEventAcknowledgeTimer(Long.valueOf(textFieldAcknowledgeTimer.getText().toString().trim()));

        localDateTime = LocalDateTime.parse(textFieldAcknowledgeUntilTS.getText(),formatter);
        ts = Timestamp.valueOf(localDateTime);

        serviceEntity.setAutoAcknowledgeEventOnReceptionUntilTs(ts);

        serviceEntityHandler.saveServiceEntity(serviceEntity);


    }

    public void updateServiceEntitiesTextFieldsFromTbl() {

        try {
            Long cellValue;
            ServiceEntity serviceEntity;

            cellValue = Long.valueOf(jTableServiceEntities.getModel().getValueAt(jTableServiceEntities.getSelectedRow(), 0).toString());

            serviceEntity = serviceEntityHandler.getServiceEntityById(cellValue);
            textFieldAcknowledgeTimer.setText(serviceEntity.getEventAcknowledgeTimer().toString());
            textFieldAcknowledgeUntilTS.setText(serviceEntity.getAutoAcknowledgeEventOnReceptionUntilTs().toLocalDateTime().format(serviceEntityHandler.dateTimeFormatter()));
            checkBoxAcknowledgeEventsOnTimer.setSelected(serviceEntity.getAutoAcknowledgeEventOnTimer());
            checkBoxAcknowledgeOnReception.setSelected(serviceEntity.getAutoAcknowledgeEventOnReception());
        } catch(Exception e) {

            textFieldAcknowledgeTimer.setText("");
            textFieldAcknowledgeUntilTS.setText("");
            checkBoxAcknowledgeEventsOnTimer.setSelected(false);
            checkBoxAcknowledgeOnReception.setSelected(false);
        }


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

        String[] ColumnServiceEntityTbl = {"Id",  "ServiceIdentifier", "autoAcknowledgeEventOnReception", "autoAcknowledgeEventOnReceptionUntilTs", "autoAcknowledgeEventOnTimer","eventAcknowledgeTimer"};
        jTableServiceEntities.setModel(modelServiceEntities);

        for(String s: ColumnServiceEntityTbl) {
            modelServiceEntities.addColumn(s);
        }


        frameMinimumDimension.setSize(800,400);

        frame = new JFrame("Widget");
        frame.setContentPane(alertPanel);
        //frame.setBounds(300,300,300,300);
        frame.setTitle("Event notifier");
        frame.setMinimumSize(frameMinimumDimension);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public synchronized void addEventToEventTable(Event event) {

        String[] row = {event.getId().toString(), event.getCreated_ts().toString(), event.getServiceIdentifier(), event.getEventRaw()};
        this.model.addRow(row);

    }


    public void displayServiceEntities() {

        Collection<ServiceEntity> serviceEntities = serviceEntityHandler.getServiceEntityCollection();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime current_date_time = LocalDateTime.now();
        Timestamp timestamp_object = Timestamp.valueOf(current_date_time);


        modelServiceEntities.setRowCount(0);


        for(ServiceEntity se: serviceEntities) {

            if(se.getAutoAcknowledgeEventOnReceptionUntilTs()==null)
                se.setAutoAcknowledgeEventOnReceptionUntilTs(timestamp_object);


                    String[] row = {
                                        se.getId().toString(),
                                        se.getServiceIdentifier(),
                                        se.getAutoAcknowledgeEventOnReception().toString(),
                                        se.getAutoAcknowledgeEventOnReceptionUntilTs().toLocalDateTime().format(serviceEntityHandler.dateTimeFormatter()).toString(),
                                        se.getAutoAcknowledgeEventOnTimer().toString(),
                                        se.getEventAcknowledgeTimer().toString(),
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
        // disables editing of table
        jTableResolvedEvents.setEnabled(false);
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

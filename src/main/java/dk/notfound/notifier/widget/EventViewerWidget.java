package dk.notfound.notifier.widget;

import javax.swing.*;

public class EventViewerWidget {

      JPanel panel;
    private JTable eventList;
    private JButton acknowledgeEvent;
    private JButton muteEvents;

    public void runWidget() {

        JFrame frame = new JFrame("Widget");
        frame.setContentPane(new EventViewerWidget().panel);
        frame.setVisible(true);
        frame.setBounds(300,300,300,300);
        frame.setTitle("Event notifier");
        frame.setSize(600,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

package dk.notfound.notifier.controller;

public class NotifierUpdater extends Thread {


    public NotifierUpdater() {


    }

    public void run() {


        try {
            sleep(30*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }





}

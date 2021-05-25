package dk.notfound.notifier.config;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.HashMap;
public class ConfigLoader
{


    private String user="Foo";
    private String listUnhandledEvents = "http://localhost:8080/event/unhandled/";
    private String listAllEvents = "http://localhost:8080/event/";
    private String acknowledgeEvent = "http://localhost:8080/event/acknowledge/";
    private Integer pollFrequency = 30;

    private Properties properties = new Properties();
    private String filePath = System.getProperty("user.dir") + "/"+ "config.properties";


    public ConfigLoader() {
        this.filePath=filePath;

        try {

            properties.load(getResource());

        } catch(Exception e) {
            System.out.println("Unable to find config: " + e);
        }

    }


    public HashMap<String,String> getConfigCollection() {

        Enumeration em = properties.keys();
        HashMap<String,String> propertyMap = new HashMap();

        while(em.hasMoreElements()) {
            String key = (String) em.nextElement();
            String property = properties.getProperty(key);
            propertyMap.put(key,property);
        }
        return propertyMap;
    }



    public  void printConfiguration() {
        Enumeration em = properties.keys();
        System.out.println("**** Configuration ****");
        System.out.println("File config:" + filePath);
        while(em.hasMoreElements()) {

            String key = (String) em.nextElement();
            String property = properties.getProperty(key);
            System.out.println(key + "=" + property);
        }
    }


    public String getProperty(String property) {
        return properties.getProperty(property);
    }

    public String getUser() {
        return user;
    }

    public String getListUnhandledEvents() {
        return listUnhandledEvents;
    }

    public String getListAllEvents() {
        return listAllEvents;
    }

    public String getAcknowledgeEvent() {
        return acknowledgeEvent;
    }

    public Integer getPollFrequency() {

        pollFrequency = Integer.decode(properties.getProperty("pollFrequency"));
        return this.pollFrequency;
    }

    public void ResourceLoader(String filePath)
    {
        this.filePath = filePath;

        if(filePath.startsWith("/"))
        {
            throw new IllegalArgumentException("Relative paths may not have a leading slash!");
        }
    }

    public InputStream getResource() throws NoSuchFileException

    {

/*
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);
*/

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(filePath));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return fileInputStream;
    }




}

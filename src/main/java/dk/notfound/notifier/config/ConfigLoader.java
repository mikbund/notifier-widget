package dk.notfound.notifier.config;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.HashMap;
public class ConfigLoader
{


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


    public String getWiki() { return properties.getProperty("wiki"); }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }

    public String getUser() {
        return properties.getProperty("user");
    }

    public String getListUnhandledEvents() {

        return properties.getProperty("listUnhandledEvents");
    }

    public long getDefaultAutoAcknowledgeTime() {
        return Integer.decode(properties.getProperty("defaultAutoAcknowledgeTime"));
    }

    public String getEventResponsible() { return properties.getProperty("eventResponsible"); }

    public String getListAllEvents() {
        return properties.getProperty("listAllEvents");
    }

    public String getListAcknowledgedEvents() {
        return properties.getProperty("listAcknowledgedEvents");
    }

    public String getAcknowledgeEvent() {
        return properties.getProperty("acknowledgeEvent");
    }

    public Integer getPollFrequency() {
        return Integer.decode(properties.getProperty("pollFrequency"));
    }

    public String getListServiceEntities() {
        return properties.getProperty("listServiceEntities");
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

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(filePath));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return fileInputStream;
    }


}

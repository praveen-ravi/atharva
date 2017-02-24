package com.atharva;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 16733 on 26/01/17.
 */
public class SystemPropertiesConfig {
    private Properties props=new Properties();
    public String getProperty(String key){
        return (props.getProperty(key));
    }
    public SystemPropertiesConfig() throws IOException {

        File propFile = new File("src/main/resources/system.properties");

        InputStream inputStream = new FileInputStream(propFile);
        if(inputStream!=null){
            props.load(inputStream);
            //System.setProperties(props);

        }else{
            throw new IOException("Failed to read Object properties file");
        }

    }
}

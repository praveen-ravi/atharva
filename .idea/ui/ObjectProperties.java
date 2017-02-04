package com.atharva.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by 16733 on 26/01/17.
 */
public class ObjectProperties {
    Logger logger= LogManager.getLogger(getClass());
    private Properties props=new Properties();

    public ObjectProperties(String propFile) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile );
        if(inputStream!=null){
            props.load(inputStream);
        }else{
            throw new IOException("Failed to read Object properties file");
        }
    }

    public String getProperty(String key){
        return(props.getProperty(key));
    }
}

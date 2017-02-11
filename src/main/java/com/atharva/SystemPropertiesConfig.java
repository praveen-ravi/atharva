package com.atharva;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 16733 on 26/01/17.
 */
public class SystemPropertiesConfig {
    public static void loadConfig() throws IOException {
        Properties props=new Properties();
        InputStream inputStream =SystemPropertiesConfig.class.getResourceAsStream("system.properties" );
        if(inputStream!=null){
            props.load(inputStream);
            System.setProperties(props);
        }else{
            throw new IOException("Failed to read Object properties file");
        }

    }
}

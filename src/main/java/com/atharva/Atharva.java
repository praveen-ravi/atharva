package com.atharva;
import com.atharva.resources.Trading;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by 16733 on 26/01/17.
 */
public class Atharva extends Application<AtharvaConfiguration> {
    static Logger log= LogManager.getLogger(Atharva.class);
    static final Level REPORT=Level.forName("REPORT",50);

    public static void main( String[] args) throws Exception {
        SystemPropertiesConfig sysprop=new SystemPropertiesConfig();
        System.setProperty("sharekhansite",sysprop.getProperty("sharekhansite"));
        System.setProperty("webdriver.chrome.driver",sysprop.getProperty("webdriver.chrome.driver"));
        System.setProperty("webdriver.gecko.driver",sysprop.getProperty("webdriver.gecko.driver"));
        new Atharva().run(args);

    }

    @Override
    public String getName() {
        return "Atharva";
    }

    @Override
    public void initialize(Bootstrap<AtharvaConfiguration> bootstrap) {

    }

    @Override
    public void run(AtharvaConfiguration configuration,
                    Environment environment) {

        environment.jersey().register(new Trading(environment.getValidator(),configuration.getTradeSettings()));

    }


}

package com.atharva;

import com.atharva.trade.TradeSettings;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

/**
 * Created by 16733 on 12/02/17.
 */
public class AtharvaConfiguration extends Configuration {
    @NotNull
    @JsonProperty("tradeSettings")
    TradeSettings tradeSettings=new TradeSettings();





    public TradeSettings getTradeSettings() {
        return tradeSettings;
    }


}

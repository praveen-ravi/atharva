package com.atharva.resources;

import com.atharva.trade.Order;
import com.atharva.trade.TradeHandler;
import com.atharva.trade.TradeSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 16733 on 12/02/17.
 */
@Path("/trade")
@Produces(MediaType.APPLICATION_JSON)
public class Trading {
    Logger logger= LogManager.getLogger(Trading.class);

    private final Validator validator;
    private Map<String,TradeHandler> tradeHandlersInstances;
    private TradeSettings tradeSettings;

    public Trading(Validator validator, TradeSettings tradeSettings) {

        this.validator = validator;
        this.tradeSettings=tradeSettings;
        tradeHandlersInstances=new HashMap<>();
    }

    @POST
    @Path("/startTrade")
    public Response startTrade(Order order){
        Set<ConstraintViolation<Order>> voilations=validator.validate(order,Order.class);
        if(voilations.size()>0){
            ArrayList<String> errorResponseMsg=new ArrayList<>();
            for(ConstraintViolation<Order> voilation:voilations){
                errorResponseMsg.add(voilation.getPropertyPath().toString()+":"+voilation.getMessage());
            }
            logger.info("Bad order request recieved with voilations:"+errorResponseMsg);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseMsg).build();
        }else{

            TradeHandler tradeHandler=new TradeHandler();
            tradeHandler.setOrder(order);
            tradeHandler.setTradeSettings(tradeSettings.clone());
            tradeHandler.start();
            tradeHandlersInstances.put(tradeHandler.getUniqueID(),tradeHandler);
            logger.info("Starting new trade");
            return Response.status(Response.Status.OK).entity(tradeHandler.toString()).build();

        }
    }

    @GET
    @Path("/hello")
    public Response hello(){
        return Response.status(Response.Status.OK).entity(tradeSettings.toString()).build();
    }
}

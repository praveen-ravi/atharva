package com.atharva.resources;

import com.atharva.exceptions.OrderPlacementExecption;
import com.atharva.trade.Order;
import com.atharva.trade.OrderInterface;
import com.atharva.trade.TradeHandler;
import com.atharva.trade.TradeSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response startTrade(Order order){
        logger.info("Entered");
        Set<ConstraintViolation<Order>> voilations=null;
        try {
            voilations = validator.validate(order, OrderInterface.class);
        }catch (Exception e){
            e.printStackTrace();
        }
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
            tradeHandler.setTradeSettings(tradeSettings);
            tradeHandler.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
            tradeHandlersInstances.put(tradeHandler.getUniqueID(),tradeHandler);
            logger.info("Starting new trade");
            return Response.status(Response.Status.OK).entity(tradeHandler.toString()).build();

        }
    }

    @GET
    @Path("/endtrade/{uniqueId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endTrade(@PathParam("uniqueId") String uniqueId){
        TradeHandler tradeHandler = tradeHandlersInstances.get(uniqueId);
        try {
            if(tradeHandler!=null) {
                tradeHandler.endTrade();
            }else{
                return Response.status(Response.Status.NOT_FOUND).entity("{\"Status\":\"Failed\"}").build();
            }
        } catch (OrderPlacementExecption orderPlacementExecption) {
            return Response.status(Response.Status.OK).entity("{\"Status\":\"Failed\"}").build();
        }
        return Response.status(Response.Status.OK).entity("{\"Status\":\"Success\"}").build();
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello(){
        return Response.status(Response.Status.OK).entity(tradeSettings.toString()).build();
    }
}

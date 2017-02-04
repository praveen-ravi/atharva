package com.atharva;
import com.atharva.trade.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by 16733 on 26/01/17.
 */
public class Atharva {
    static Logger log= LogManager.getLogger(Atharva.class);
    static final Level REPORT=Level.forName("REPORT",50);
    public static ArrayList<Double> sample1=new ArrayList<Double>();
    public static void main(String arg[]){
        try {
            AssetClass asset1= new AssetClass();
            asset1.setExchange("NSE");
            SimpleDateFormat format=new SimpleDateFormat("HH:mm");
            Date marketCloseTime=format.parse("15:30");
            asset1.setMarketCloseTime(marketCloseTime);
            Order order1 = new Order();
            order1.setTradeType(TradeType.BUY);
            order1.setScrip("BOSCHLTD");
            order1.setCapital(BigDecimal.valueOf(10000));
            order1.setAssetClass(asset1);


            TradeSettings settings1=new TradeSettings(76.04);
            order1.setOrderQty((long) ((order1.getCapital().longValue()*0.05)/settings1.getStoploss()));

            TradeHandler handler=new TradeHandler();
            log.log(REPORT,"OngoingTrade\tScrip\tCurrentPrice\tCummulativePriceDiff\tActiveTradePriceDiff\tActiveTradeProfit\tTrendReversalTrailer\tStopLossTrailer\tTradeActive\tTotalPriceDiff\tTotalProfitorLoss");

            Thread trade1=new TradeHandler();
            ((TradeHandler)trade1).setOrder(order1);
            ((TradeHandler)trade1).setTradeSettings(settings1);


            AssetClass asset2= new AssetClass();
            asset2.setExchange("NSE");
            format=new SimpleDateFormat("HH:mm");
            marketCloseTime=format.parse("15:30");
            asset2.setMarketCloseTime(marketCloseTime);
            Order order2 = new Order();
            order2.setTradeType(TradeType.SELL);
            order2.setScrip("RELIANCE");
            order2.setCapital(BigDecimal.valueOf(10000));
            order2.setAssetClass(asset2);


            TradeSettings settings2=new TradeSettings(0.64);
            order2.setOrderQty((long) ((order2.getCapital().longValue()*0.05)/settings2.getStoploss()));



            Thread trade2=new TradeHandler();
            ((TradeHandler)trade2).setOrder(order2);
            ((TradeHandler)trade2).setTradeSettings(settings2);


            AssetClass asset3= new AssetClass();
            asset3.setExchange("NSE");
            format=new SimpleDateFormat("HH:mm");
            marketCloseTime=format.parse("15:30");
            asset3.setMarketCloseTime(marketCloseTime);
            Order order3 = new Order();
            order3.setTradeType(TradeType.SELL);
            order3.setScrip("IDEA");
            order3.setCapital(BigDecimal.valueOf(10000));
            order3.setAssetClass(asset3);


            TradeSettings settings3=new TradeSettings(0.35);
            order3.setOrderQty((long) ((order3.getCapital().longValue()*0.05)/settings3.getStoploss()));



            Thread trade3=new TradeHandler();
            ((TradeHandler)trade3).setOrder(order3);
            ((TradeHandler)trade3).setTradeSettings(settings3);


            AssetClass asset4= new AssetClass();
            asset4.setExchange("NSE");
            format=new SimpleDateFormat("HH:mm");
            marketCloseTime=format.parse("15:30");
            asset4.setMarketCloseTime(marketCloseTime);
            Order order4 = new Order();
            order4.setTradeType(TradeType.BUY);
            order4.setScrip("NIFTY");
            order4.setCapital(BigDecimal.valueOf(10000));
            order4.setAssetClass(asset4);


            TradeSettings settings4=new TradeSettings(7.21);
            order4.setOrderQty((long) ((order4.getCapital().longValue()*0.05)/settings4.getStoploss()));



            Thread trade4=new TradeHandler();
            ((TradeHandler)trade4).setOrder(order4);
            ((TradeHandler)trade4).setTradeSettings(settings4);

            getSample();

            trade1.start();
            trade2.start();
            trade3.start();
            trade4.start();
            //handler.startTrade(order1,settings);


        } catch (Exception e) {
            log.error("Failed to load system config due to "+e);
        }
    }

    static void getSample() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Reliance.csv"))));
        String line;
        do{
            line=reader.readLine();
            if(line!=null){
                sample1.add(Double.parseDouble(line));
            }else{
                break;
            }

        }while(true);
    }


}

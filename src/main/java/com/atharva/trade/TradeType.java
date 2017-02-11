package com.atharva.trade;

/**
 * Created by 16733 on 26/01/17.
 */
public enum TradeType {
    BUY(1,"Buy"),
    SELL(-1,"Sell"),
    SHORTSELL(-1,"ShortSell"),
    BUYBIGTRADE(1,"BuyBigTrade"),
    SELLBIGTRADE(-1,"SellBigTrade");

    private int tradeDirection;
    private String uiText;
    TradeType(int tradeDirection,String uiText){
        this.tradeDirection=tradeDirection;
        this.uiText=uiText;
    }

    public int getTradeDirection(){
        return(this.tradeDirection);
    }

    public String getUiText(){ return (this.uiText);}

    public TradeType getOppositeDirection(){
        switch (this){
            case BUY:
                return(SELL);
            case SELL:
                return (BUY);
            case SHORTSELL:
                return (BUY);
            case BUYBIGTRADE:
                return (SELLBIGTRADE);
            case SELLBIGTRADE:
                return (BUYBIGTRADE);

                default:return(null);
        }
    }

}

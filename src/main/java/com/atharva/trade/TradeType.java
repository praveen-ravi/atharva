package com.atharva.trade;

/**
 * Created by 16733 on 26/01/17.
 */
public enum TradeType {
    BUY(1),
    SELL(-1),
    SHORTSELL(-1),
    BUYBIGTRADE(1),
    SELLBIGTRADE(-1);

    private int tradeDirection;
    TradeType(int tradeDirection){
        this.tradeDirection=tradeDirection;
    }

    public int getTradeDirection(){
        return(this.tradeDirection);
    }

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

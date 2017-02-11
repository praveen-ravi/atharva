package com.atharva.trade;

/**
 * Created by 16733 on 07/02/17.
 */
public class User {
    private String name;
    private String accountNo;
    private String dpAccountNo;
    private String loginId;
    private String membershipPassword;
    private String tradingPassword;

    public String getDpAccountNo() {
        return dpAccountNo;
    }

    public void setDpAccountNo(String dpAccountNo) {
        this.dpAccountNo = dpAccountNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMembershipPassword() {
        return membershipPassword;
    }

    public void setMembershipPassword(String membershipPassword) {
        this.membershipPassword = membershipPassword;
    }

    public String getTradingPassword() {
        return tradingPassword;
    }

    public void setTradingPassword(String tradingPassword) {
        this.tradingPassword = tradingPassword;
    }
}

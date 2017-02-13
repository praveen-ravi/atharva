package com.atharva.trade;

import javax.validation.constraints.NotNull;

/**
 * Created by 16733 on 07/02/17.
 */
public class User {
    @NotNull
    private String iduser;
    @NotNull
    private String name;
    @NotNull
    private String skAccountNo;
    @NotNull
    private String skDpAccountNo;
    @NotNull
    private String skLoginId;
    @NotNull
    private String ckMembershipPassword;
    @NotNull
    private String skTradingPassword;

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getSkDpAccountNo() {
        return skDpAccountNo;
    }

    public void setSkDpAccountNo(String skDpAccountNo) {
        this.skDpAccountNo = skDpAccountNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkAccountNo() {
        return skAccountNo;
    }

    public void setSkAccountNo(String skAccountNo) {
        this.skAccountNo = skAccountNo;
    }

    public String getSkLoginId() {
        return skLoginId;
    }

    public void setSkLoginId(String skLoginId) {
        this.skLoginId = skLoginId;
    }

    public String getCkMembershipPassword() {
        return ckMembershipPassword;
    }

    public void setCkMembershipPassword(String ckMembershipPassword) {
        this.ckMembershipPassword = ckMembershipPassword;
    }

    public String getSkTradingPassword() {
        return skTradingPassword;
    }

    public void setSkTradingPassword(String skTradingPassword) {
        this.skTradingPassword = skTradingPassword;
    }
}

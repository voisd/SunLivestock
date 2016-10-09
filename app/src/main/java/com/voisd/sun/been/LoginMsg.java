package com.voisd.sun.been;

import java.io.Serializable;

/**
 * Created by voisd on 16/5/22.
 */
public class LoginMsg implements Serializable {

    private String easeMobPasswd;
    private String easeMobAccount;
    private String token;


    public String getEaseMobPasswd() {
        return easeMobPasswd;
    }

    public void setEaseMobPasswd(String easeMobPasswd) {
        this.easeMobPasswd = easeMobPasswd;
    }

    public String getEaseMobAccount() {
        return easeMobAccount;
    }

    public void setEaseMobAccount(String easeMobAccount) {
        this.easeMobAccount = easeMobAccount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

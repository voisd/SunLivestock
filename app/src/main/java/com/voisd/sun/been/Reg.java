package com.voisd.sun.been;

import java.io.Serializable;

/**
 * Created by voisd on 2016/10/9.
 * 联系方式：531972376@qq.com
 */
public class Reg implements Serializable{

    /**
     * account : 13631429329
     * avatar : null
     * collections : null
     * comment_list : null
     * exp : 1
     * level : 1
     * name : 侯通海
     * password : 1
     * token : null
     * uid : 26
     */

    private String account;
    private String avatar;
    private String collections;
    private String comment_list;
    private String exp;
    private String level;
    private String name;
    private String password;
    private String token;
    private String uid;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCollections() {
        return collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public String getComment_list() {
        return comment_list;
    }

    public void setComment_list(String comment_list) {
        this.comment_list = comment_list;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

package com.voisd.sun.been;

import java.io.Serializable;

/**
 * Created by voisd on 2016/10/13.
 * 联系方式：531972376@qq.com
 */
public class PassWord implements Serializable{
    public String uid;
    public String name;
    public String avatar;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

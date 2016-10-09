package com.voisd.sun.been;

import java.io.Serializable;

/**
 * Created by voisd on 2016/10/9.
 * 联系方式：531972376@qq.com
 */
public class Category implements Serializable{
    String cid;
    String name;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

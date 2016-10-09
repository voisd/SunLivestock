package com.voisd.sun.been;

import java.io.Serializable;

/**
 * Created by voisd on 2016/9/22.
 */
public class News implements Serializable{

    /**
     * brief : 　　按照财政部和农业部在东北及内蒙地区建立玉米生产者补贴制度工作要求，丹东市从今年开始对玉米生产者实行补贴。补贴对象为2016-2018年全市合法耕地上的玉米实际生产者；补贴依据为生产者当年在合法耕地
     * category : {"cid":1,"name":"新闻"}
     * imgUrl : null
     * nId : 24
     * nbId : 23
     * source : null
     * time : 2016-09-07 14:26:50
     * title : 辽宁丹东市将全面落实玉米生产者补贴政策
     */

    private String brief;
    /**
     * cid : 1
     * name : 新闻
     */

    private CategoryBean category;
    private String imgUrl;
    private String nId;
    private String nbId;
    private String source;
    private String time;
    private String title;
    private String commentCount;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNId() {
        return nId;
    }

    public void setNId(String nId) {
        this.nId = nId;
    }

    public String getNbId() {
        return nbId;
    }

    public void setNbId(String nbId) {
        this.nbId = nbId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }



    public static class CategoryBean implements Serializable{
        private String cid;
        private String name;

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
}

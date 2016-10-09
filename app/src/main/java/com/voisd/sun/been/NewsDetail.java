package com.voisd.sun.been;

import java.io.Serializable;

/**
 * Created by voisd on 2016/9/26.
 */
public class NewsDetail implements Serializable {


    /**
     * cid : 1
     * name : 新闻
     */

    private CategoryBean category;
    /**
     * category : {"cid":1,"name":"新闻"}
     * collection : false
     * commentCount : 2
     * content : 　　近日，天津检验检疫局临港办事处工作人员在对一船进口大豆进行计重后，发现实际卸货重量比报检单据上少了400多吨。检验检疫人员遂对这批货物出具了重量证书，凭借这个重量证书，进口企业可以挽回损失约10万美元。
     * id : 168
     * pageViews : 0
     * source : null
     * time : 2016-09-07 17:27:12
     * title : 天津口岸计重检出进口大豆短重400吨
     * url : http://www.chinabreed.com/wap/bencandy.php?fid=60&id=715106
     */

    private boolean collection;
    private String commentCount;
    private String content;
    private String id;
    private String pageViews;
    private String source;
    private String time;
    private String title;
    private String url;

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageViews() {
        return pageViews;
    }

    public void setPageViews(String pageViews) {
        this.pageViews = pageViews;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class CategoryBean {
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

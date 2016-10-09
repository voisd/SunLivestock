package com.voisd.sun.been;

import java.io.Serializable;

/**
 * Created by voisd on 2016/9/28.
 * 联系方式：531972376@qq.com
 */
public class CommentList implements Serializable{


    /**
     * comment_id : 145
     * content : 好的好的那你
     * news : {"commentCount":1,"id":5502,"time":"2016-09-28 17:32:37"}
     * parent_id : 0
     * reportCount : null
     * report_time : 1475115410
     * root_parent_id : 0
     * user : {"avatar":"http://www.bk886.com:80/user_avatar_min/25/avatar_25_1474962386.jpg","exp":1,"level":1,"name":"阮星竹","uid":25}
     */

    private String comment_id;
    private String content;
    /**
     * commentCount : 1
     * id : 5502
     * time : 2016-09-28 17:32:37
     */

    private NewsBean news;
    private String parent_id;
    private String reportCount;
    private String report_time;
    private String root_parent_id;
    /**
     * avatar : http://www.bk886.com:80/user_avatar_min/25/avatar_25_1474962386.jpg
     * exp : 1
     * level : 1
     * name : 阮星竹
     * uid : 25
     */

    private UserBean user;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NewsBean getNews() {
        return news;
    }

    public void setNews(NewsBean news) {
        this.news = news;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getReportCount() {
        return reportCount;
    }

    public void setReportCount(String reportCount) {
        this.reportCount = reportCount;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getRoot_parent_id() {
        return root_parent_id;
    }

    public void setRoot_parent_id(String root_parent_id) {
        this.root_parent_id = root_parent_id;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class NewsBean implements Serializable{
        private String commentCount;
        private String id;
        private String time;

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class UserBean implements Serializable{
        private String avatar;
        private String exp;
        private String level;
        private String name;
        private String uid;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}


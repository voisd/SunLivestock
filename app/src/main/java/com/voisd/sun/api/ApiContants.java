package com.voisd.sun.api;

/**
 * Created by voisd on 2016/6/18.
 */
public class ApiContants {

    public static final class Urls {
        public final static String url = "http://www.bk886.com/";

        public final static String TEST_ACCOUNT = url + "tese";

        public final static String USERLOGIN_DO =  url + "userLogin.do";

        public final static String NEWS_DO = url + "news.do";

        public final static String NEWSDETAIL_DO =  url + "newsDetail.do";

        public final static String REPORTCOMMENT_DO =  url + "reportComment.do";

        public final static String COLLECTIONADD_DO = url + "collectionAdd.do";

        public final static String COLLECTIONDEL_DO = url + "collectionDel.do";

        public final static String COMMENTLIST_DO= url + "commentList.do";

        public final static String SUBCOMMENTLIST_DO= url + "subCommentList.do";

        public final static String COLLECTIONLIST_DO= url + "collectionList.do";

        public final static String USERUPDATE_DO= url + "userUpdate.do";

        public final static String UPLOADAVATAR_DO= url + "uploadAvatar.do";

        public final static String CHECKPHONE = url + "checkPhone.do";

        public final static String RANDOMNAME = url + "randomName.do";

        public final static String USERREGISTER = url + "userRegister.do";

        public final static String CATEGORY_DO = url + "category.do";

        public final static String UPDATEPWD_DO = url + "updatePwd.do"; //修改密码
    }

    public static final class EventTags {
        public final static int BEGIN_EVENT = 10;

        public final static int TEST_ACCOUNT = BEGIN_EVENT + 1;//测试

        public final static int USERLOGIN_DO = BEGIN_EVENT + 2;//登录

        public final static int NEWS_DO = BEGIN_EVENT + 3;//新闻首页

        public final static int NEWSDETAIL_DO = BEGIN_EVENT + 4;//新闻详情

        public final static int REPORTCOMMENT_DO = BEGIN_EVENT + 5;//帖子回复

        public final static int COLLECTIONADD_DO = BEGIN_EVENT + 6;//收藏

        public final static int COLLECTIONDEL_DO = BEGIN_EVENT + 7;//取消收藏

        public final static int COMMENTLIST_DO= BEGIN_EVENT + 8; //一级回复

        public final static int SUBCOMMENTLIST_DO= BEGIN_EVENT + 9; //二级级回复

        public final static int COLLECTIONLIST_DO=BEGIN_EVENT+10;//W我的收藏

        public final static int USERUPDATE_DO = BEGIN_EVENT+11;//我的资料修改

        public final static int UPLOADAVATAR_DO = BEGIN_EVENT+12;//头像上传

        public final static int CHECKPHONE = BEGIN_EVENT + 13; //验证号码合法

        public final static int RANDOMNAME = BEGIN_EVENT + 14; //获取随机名

        public final static int USERREGISTER = BEGIN_EVENT + 15; //注册

        public final static int CATEGORY_DO = BEGIN_EVENT + 16; //分类

        public final static int UPDATEPWD = BEGIN_EVENT + 17; //修改密码


    }

}

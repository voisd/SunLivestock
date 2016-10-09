package com.voisd.sun.utils.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.voisd.sun.common.Contants;
import com.voisd.sun.utils.AES;
import com.voisd.sun.utils.Md5;
import com.voisd.sun.utils.PreferenceUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;



/**
 * Created by voisd on 2016/10/8.
 * 联系方式：531972376@qq.com
 * MultiPart 图片上传
 */
public class HttpImageUtil {


    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*10000000; //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static final String SUCCESS="1";
    public static final String FAILURE="0";

    public static String[] uploadFile(final String url, final File file, final Context context) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        MultipartEntity reqEntity = new MultipartEntity();

        try {

            reqEntity.addPart("uid",  new StringBody(
                    AES.getSingleton().encrypt(PreferenceUtils.getPrefString(context, Contants.Preference.Uid,""))));
            String value =  url+"?uid="+PreferenceUtils.getPrefString(context, Contants.Preference.Uid,"")+
                    "&token="+PreferenceUtils.getPrefString(context, Contants.Preference.Token,"");
            System.out.println("__"+value);
            reqEntity.addPart("sign", new StringBody(AES.getSingleton().encrypt(Md5.str2Md5(value))));

            Bitmap photoBM = BitmapFactory.decodeFile(file.getPath());
            if (photoBM == null) {
                return null;
            }
            ByteArrayOutputStream photoBao = new ByteArrayOutputStream();
            boolean successCompress = photoBM.compress(Bitmap.CompressFormat.JPEG,
                    80, photoBao);
            if (!successCompress) {
                return null;
            }
            ByteArrayBody byteArrayBody = new ByteArrayBody(
                    photoBao.toByteArray(), "image/jpeg","android.jpg");
            photoBM.recycle();
            reqEntity.addPart("avatar", byteArrayBody);
            // InputStreamBody inbody = new InputStreamBody(new InputStream,
            // filename);
//            FileBody fileBody = new FileBody(file);
//            reqEntity.addPart("avatar ",fileBody);
//        try{
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
//            byte[] data = bos.toByteArray();
//            ByteArrayBody bab = new ByteArrayBody(data, "kfc.jpg");
//            reqEntity.addPart("image", bab);
//        }
//        catch(Exception e){
//            reqEntity.addPart("image", new StringBody("image error"));
//        }
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
//            response.setHeader("Content-Type","image/jpeg");
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            return new String[]{"0",s.toString()};
        }catch (Exception e){
            System.out.println("上传异常返回："+e.toString());
            return new String[]{"1",Contants.NetStatus.NETDISABLEORNETWORKDISABLE};
        }
    }
}


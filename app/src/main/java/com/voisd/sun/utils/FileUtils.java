package com.voisd.sun.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by voisd on 2016/9/26.
 * 联系方式：531972376@qq.com
 * 文件操作
 */
public class FileUtils {

    public static String packName = "/com.voisd.sun";
    public static String h5Work = "/h5";

    public static void initFileCachePath(Context context) {
        try{
            File cacheDir = null;
            cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + packName);

            if (!cacheDir.exists())
                cacheDir.mkdirs();

            File tempFile = new File(cacheDir + h5Work);
            if (!tempFile.exists()) {
                tempFile.mkdir();
            }

        }catch(Exception e){
        }
    }


    /**
     * 获取H5存储空间
     * */
    public static String getH5WorkFilePath(){
        File cacheDir = null;
        cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + packName);
        return  new File(cacheDir + h5Work).getPath();
    }


    public static void copyAssetsToSD(Context context,String assFileName)
    {
        if(new File(assFileName).exists())
            copyAssetsToSD(context,assFileName,getH5WorkFilePath());
        else{
            File cacheDir = null;
            cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + packName);

            if (!cacheDir.exists())
                cacheDir.mkdirs();

            File tempFile = new File(cacheDir + h5Work);
            if (!tempFile.exists()) {
                tempFile.mkdir();
            }
            copyAssetsToSD(context,assFileName,getH5WorkFilePath()+"/"+assFileName);
        }
    }


    public static void copyAssetsToSD(Context context,String assFileName,
                                 String strOutFileName)
    {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(strOutFileName);
            myInput = context.getAssets().open(assFileName);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        }catch (Exception e){
            System.out.println("复制异常"+e.toString());
        }
    }

    public static void deleteFolder(String path) {
        File f = new File(path);
        if (f.exists()) {
            // 在判断它是不是一个目录
            if (f.isDirectory()) {
                // System.out.println("该文件夹是一个目录");
                // 列出该文件夹下的所有内容
                String[] fileList = f.list();
                if(fileList==null){
                    return;
                }
                for (int i = 0; i < fileList.length; i++) {
                    // 对每个文件名进行判断
                    // 如果是文件夹 那么就循环deleteFolder
                    // 如果不是，直接删除文件
                    String name = path + File.separator + fileList[i];
                    File ff = new File(name);
                    if (ff.isDirectory()) {
                        deleteFolder(name);
                    } else {
                        ff.delete();
                    }
                }
                // 最后删除文件夹
                f.delete();

            } else {
                f.delete();
            }
        } else {
            System.out.println("不存在该文件夹");
        }
    }


    /**
     * 保存图片
     * */
    public static boolean saveFile(String fileName, ImageView imageView){
        boolean b = false;
        Bitmap bm = convertViewToBitmap(imageView);
        File cacheDir = null;
        cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + packName);

        if (!cacheDir.exists())
            cacheDir.mkdirs();

        File file = new File(cacheDir.getPath()+"/img");
        if (!file.exists())
            file.mkdirs();
        File f = new File(file.getPath()+"/"+fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            b = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static Bitmap convertViewToBitmap(View view)
    {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    /**
     * 获取拍照相片存储文件
     */
    public static File createFile(Context context){
        File file;
        file = new File(Environment.getExternalStorageDirectory().getPath() + packName);

        if (!file.exists())
            file.mkdirs();
        String timeStamp = String.valueOf(new Date().getTime());
        file = new File(file.getPath() +
                File.separator + timeStamp+".jpg");

        return file;
    }



}

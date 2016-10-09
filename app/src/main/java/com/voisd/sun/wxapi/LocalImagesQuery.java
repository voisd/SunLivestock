package com.voisd.sun.wxapi;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class LocalImagesQuery {

    private static final String[] PROJECTION_BUCKET = {MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATA};

    /**
     * 查询本地图片
     *
     * @param context
     * @return
     */
    public static List<ImagesEntry> query(Context context) {

        List<ImagesEntry> resultList = new ArrayList<ImagesEntry>();

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor mCursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                PROJECTION_BUCKET, null, null, orderBy + " DESC");

        if (mCursor == null)
            return resultList;

        try {
            while (mCursor.moveToNext()) {
                ImagesEntry entry = new ImagesEntry(mCursor.getInt(0),
                        mCursor.getString(1), mCursor.getString(2));

                if (!resultList.contains(entry)) {
                    resultList.add(entry);
                }
            }
        } catch (Exception ex) {

        } finally {
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }

        return resultList;
    }

    /**
     * 得到某个目录下的图片文件
     *
     * @param context
     * @param bucketId
     * @return
     */
    public static List<ImagesModel> getAlbumImages(Context context, String bucketUrl, int bucketId) {

        List<ImagesModel> resultList = new ArrayList<ImagesModel>();

        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

            String searchParams = "bucket_id = " + bucketId + "";

            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            Cursor mImageCursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    searchParams, null, orderBy + " DESC");

            if (mImageCursor.getCount() > 0) {

                for (int i = 0; i < mImageCursor.getCount(); i++) {
                    mImageCursor.moveToPosition(i);
                    int dataColumnIndex = mImageCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    String url = mImageCursor.getString(dataColumnIndex).toString();
                    ImagesModel galleryModel = new ImagesModel(url, false);

                    if (bucketUrl.substring(0, bucketUrl.lastIndexOf("/") + 1).equals(
                            url.substring(0, url.lastIndexOf("/") + 1))) {
                        galleryModel.status = false;
                        resultList.add(galleryModel);
                    }
                }
            }

            if (mImageCursor != null && !mImageCursor.isClosed()) {
                mImageCursor.close();
            }

        } catch (Exception e) {
        }

        return resultList;
    }
}

package com.voisd.sun.wxapi;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.voisd.sun.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PopupListAdapter extends ArrayAdapter<ImagesEntry> {

	private Context mContext;
	private ArrayList<ImagesEntry> mBucketEntryList;
	
	List<Integer> sizeList = new ArrayList<Integer>();
	
	private int  selectItem=-1; 

	public PopupListAdapter(Context context, int resource,
							ArrayList<ImagesEntry> categories, boolean isFromVideo) {
		super(context, resource, categories);
		mBucketEntryList = categories;
		mContext = context;
	}
	
	public int getCount() {
		return mBucketEntryList.size();
	}

	@Override
	public ImagesEntry getItem(int position) {
		return mBucketEntryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
   } 

	public void addLatestEntry(String url) {
		int count = mBucketEntryList.size();
		boolean success = false;
		for (int i = 0; i < count; i++) {
			if (mBucketEntryList.get(i).bucketName
					.equals(SelectImageContants.folderName)) {
				mBucketEntryList.get(i).bucketUrl = url;

				success = true;
				break;
			}
		}

		if (!success) {
			ImagesEntry latestBucketEntry = new ImagesEntry(0,
					SelectImageContants.folderName, url);
			mBucketEntryList.add(0, latestBucketEntry);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			LayoutInflater viewInflater;
			viewInflater = LayoutInflater.from(mContext);
			convertView = viewInflater.inflate(R.layout.selectimage_albums_item,
					parent, false);

			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.img);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.group_item_title);
			holder.imageSize = (TextView) convertView
					.findViewById(R.id.group_item_num);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImagesEntry bucketEntry = mBucketEntryList.get(position);

		Picasso.with(mContext).load(new File(bucketEntry.bucketUrl))
				.resize(150, 150).centerCrop()
				.placeholder(R.drawable.ic_launcher).into(holder.imageView);

		holder.nameTextView.setText(bucketEntry.bucketName);
		holder.imageSize.setText("(" + getImagesCount(bucketEntry.bucketId)
				+ ")");
//		holder.imageSize.setText("(" + getImageCount(bucketEntry.bucketUrl)
//				+ ")");
		// holder.imageSize.setText("(" + bucketEntry.getBucketSize() + ")");
		
//		 if (position == selectItem) {
//             convertView.setBackgroundResource(R.drawable.listview_bg_pressed);
//         }
//         else {
//             convertView.setBackgroundResource(R.drawable.listview_bg);
//         }
		
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView nameTextView;
		TextView imageSize;
	}

	public int getImagesCount(int bucketId) {
		
		Cursor mImageCursor = null;
		
		try {
			String searchParams = "bucket_id = " + bucketId + "";

			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			mImageCursor = mContext.getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					searchParams, null, null);

			return mImageCursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(mImageCursor != null)
			{
				mImageCursor.close();
			}
		}

		return 0;
	}
}

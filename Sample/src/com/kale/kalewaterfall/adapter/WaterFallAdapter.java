package com.kale.kalewaterfall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.kale.kalewaterfall.DataCallbackActivity;
import com.kale.kalewaterfall.R;
import com.kale.kalewaterfall.application.KaleApplication;
import com.kale.kalewaterfall.mode.Club;
import com.kale.kalewaterfall.mode.PhotosList;
import com.kale.kalewaterfall.util.AnalysisListener;
import com.kale.kalewaterfall.util.DNetworkImageView;
import com.kale.kalewaterfall.util.DataTask;
import com.kale.kalewaterfall.util.LruBitmapCache;

public class WaterFallAdapter extends BaseAdapter {

	private Context mContext;
	
	// 当前可显示的item数目
	private int mCurrentCount;
	// 下一页的起始数
	private int mNextStart;
	private List<PhotosList> mList;
	private PhotosList mPhotosList;
	
	// imageLoader对象，用来初始化NetworkImageView
	private static ImageLoader mImageLoader;
	

	public WaterFallAdapter(Context context, Club club) {
		mContext = context;
		// 初始化mImageLoader，并且传入了自定义的内存缓存
		mImageLoader = new ImageLoader(KaleApplication.requestQueue, new LruBitmapCache());
		mNextStart = club.data.next_start;
		mCurrentCount = club.data.object_list.size(); 
		mList = club.data.object_list;
	}

	@Override
	public int getCount() {
		return mCurrentCount; // 返回item的个数
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * 重要的方法。通过viewHolder复用view，并且设置好item的宽度
	 * 
	 * @param position
	 * 
	 * @param convertView
	 * 
	 * @param parent
	 * 
	 * @return
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// init convertView by layout
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.photo_item, null);
		}
		// item中显示图片的view
		final DNetworkImageView photoIv = ViewHolder.get(convertView, R.id.item_photo_imageView);
		// item中的图片下方的文字
		TextView msgTv = ViewHolder.get(convertView, R.id.item_title_textView);
		// item中星星的数目
		TextView starTv = ViewHolder.get(convertView, R.id.item_star_textView);
		
		mPhotosList = mList.get(position);
		
		float ratio = (float)mPhotosList.photo.height / mPhotosList.photo.width;
		photoIv.setHeightRatio(ratio);
		
		// 设置默认的图片
		photoIv.setDefaultImageResId(R.drawable.default_photo);
		// 设置图片加载失败后显示的图片
		photoIv.setErrorImageResId(R.drawable.error_photo);

		
		// 开始加载网络图片
		photoIv.setImageUrl(mPhotosList.photo.path, mImageLoader);
		msgTv.setText(mPhotosList.msg);
		starTv.setText(mPhotosList.favorite_count+"");
		return convertView;
	}
	
	
	/**
	 * @description 加载之前的数据
	 *
	 */
	public void loadOldData() {
		DataTask task = new DataTask();
		task.getJsonDataByIndex(mNextStart);
		task.setOnAnalysisListener(new AnalysisListener() {
			
			@Override
			public void onResponse(Object object) {
				if (object != null) {
					Club club = (Club)object;
					mNextStart = club.data.next_start;
					mCurrentCount += club.data.object_list.size(); 
					mList.addAll(club.data.object_list);
					((DataCallbackActivity)mContext).onUpdateSuccess();
				}else {
					Toast.makeText(mContext, "服务器数据出错了", 0).show();
					((DataCallbackActivity)mContext).onUpdateError();
				}
			}
			
			@Override
			public void onError(String msg) {
				Toast.makeText(mContext, "请检查网络连接", 0).show();
				((DataCallbackActivity)mContext).onUpdateError();
			}
		});
	}
	
	/**
	 * @description 加载最新的数据
	 *
	 */
	public void loadNewData() {
		DataTask task = new DataTask();
		task.getJsonDataByIndex(0);
		task.setOnAnalysisListener(new AnalysisListener() {
			
			@Override
			public void onResponse(Object object) {
				if (object != null) {
					Club club = (Club)object;
					mNextStart = club.data.next_start;
					mCurrentCount = club.data.object_list.size(); 
					mList = club.data.object_list;
					((DataCallbackActivity)mContext).onUpdateSuccess();
				}else {
					Toast.makeText(mContext, "服务器数据出错了", 0).show();
					((DataCallbackActivity)mContext).onUpdateError();
				}
			}
			
			@Override
			public void onError(String msg) {
				Toast.makeText(mContext, "请检查网络连接", 0).show();
				((DataCallbackActivity)mContext).onUpdateError();
			}
		});
	}

	
}

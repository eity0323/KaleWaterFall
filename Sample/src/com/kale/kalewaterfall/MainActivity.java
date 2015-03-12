package com.kale.kalewaterfall;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.kale.kalewaterfall.adapter.WaterFallAdapter;
import com.kale.kalewaterfall.mode.Club;
import com.kale.kalewaterfall.util.AnalysisListener;
import com.kale.kalewaterfall.util.DataTask;

public class MainActivity extends Activity implements DataCallbackActivity, AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

	// 瀑布流视图
	private StaggeredGridView mGridView;
	// 下拉刷新控件
	private SwipeRefreshLayout mSwipeLayout;
	// 底部view
	private TextView mFooterTv;
	// 适配器
	private WaterFallAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		// 首次加载最新的数据，然后配置适配器
		DataTask task = new DataTask();
		task.getJsonDataByIndex(0);
		task.setOnAnalysisListener(new AnalysisListener() {

			@Override
			public void onResponse(Object object) {
				if (object != null) {
					mAdapter = new WaterFallAdapter(MainActivity.this, (Club) object);
					mGridView.setAdapter(mAdapter);
				}
			}

			@Override
			public void onError(String msg) {
				Toast.makeText(getApplicationContext(), "请检查网络连接", 0).show();
			}
		});

	}

	/**
	 * @description 初始化控件
	 *
	 */
	private void initView() {
		mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
		// 添加底部view
		View footer = getLayoutInflater().inflate(R.layout.list_item_footer, null);
		mFooterTv = (TextView) footer.findViewById(R.id.footer_textView);
		mFooterTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAdapter.loadOldData();
				mFooterTv.setText("正在加载数据…");
			}
		});
		
		mGridView.addFooterView(mFooterTv);
		// mGridView.setOnScrollListener(this);
		mGridView.setOnItemClickListener(this);

		// 添加顶部的下拉刷新
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		// 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);
		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				mAdapter.loadNewData();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(getApplicationContext(), "click", 0).show();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			// 滚动停止，到底部时加载更多数据
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
			} else if (view.getFirstVisiblePosition() == 0) {
				mAdapter.loadOldData();
				mFooterTv.setText("正在加载数据…");
			}
			break;
		}
	}

	/**
	 * @description 加载数据成功时触发的方法
	 *
	 */
	@Override
	public void onUpdateSuccess() {
		// 停止刷新
		mSwipeLayout.setRefreshing(false);
		mFooterTv.setText("加载更多数据");
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * @description 加载数据失败时触发的方法
	 *
	 */
	@Override
	public void onUpdateError() {
		Toast.makeText(getApplicationContext(), "获取失败T_T", 0).show();
		mFooterTv.setText("加载更多数据");
	}
}

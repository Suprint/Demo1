package com.mpay.plus.mplus;

import java.util.Vector;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.News;
import com.mpay.plus.database.User;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

/**
 * @author quyenlm.vn@gmail.com
 * */
public class FrmNews extends AMPlusCore {
	public static final String TAG = "FrmNews";
	private MPNewsAdapter mAdapter;
	private ViewPager mViewPager;
	private PageIndicator mIndicator;
	private int curIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_main_news);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		setControls();
	}

	/**
	 * Init controls
	 * */
	private void setControls() {
		try {
			curIndex = this.getIntent().getExtras().getInt("index");
		} catch (Exception e) {
			
		}
		try {
			// Set news Slide
			Vector<News> news = null;

			boolean flag = true;
			while (news == null && flag) {
				news = getDba().getNews(DBAdapter.DB_GROUP_TYPE_NEWS);

				if (news == null) {
					// init default menu
					if (User.sLang.equals(Config.LANG_EN)) {
						saveMenuNews(Config.DEFAULT_MENU_NEWS[1]);
					} else {
						saveMenuNews(Config.DEFAULT_MENU_NEWS[0]);
					}

					news = getDba().getNews(DBAdapter.DB_GROUP_TYPE_NEWS);
					flag = false;
				}
			}

			mAdapter = new MPNewsAdapter(getSupportFragmentManager(), news, 1);
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mAdapter);
			// handle scroll confict with scrollviewer
			mViewPager.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});

			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					mViewPager.getParent().requestDisallowInterceptTouchEvent(
							true);
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
			
			mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
			mIndicator.setViewPager(mViewPager);
			
			if(curIndex > 0 && curIndex < mAdapter.getCount())
				mViewPager.setCurrentItem(curIndex);
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;

		if (!isBusy) {
			// Xu ly menu
			switch (item.getItemId()) {
			case android.R.id.home:
				goBack();
				break;

			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else
			result = false;

		return result;
	};

	public void onPause() {
		System.gc();
		super.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void goBack() {
		super.goBack();
	}
}

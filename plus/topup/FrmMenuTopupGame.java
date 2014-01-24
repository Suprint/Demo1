package com.mpay.plus.topup;

import java.util.Vector;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.FunctionType;
import com.mpay.MPFragmentAdapter;
import com.mpay.agent.R;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Group;
import com.mpay.plus.database.User;
import com.mpay.plus.imart.ViewTitlePaper;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Hien thi dang sach cac san pham
 * 
 * @author quyenlm.vn@gmail.com
 * 
 * */

public class FrmMenuTopupGame extends AMPlusCore {
	public final static String TAG = "FrmMenuGame";

	private MPFragmentAdapter mAdapter;
	private ViewPager mPager;
	private ViewTitlePaper mIndicator;
	private Vector<Group> mMenu;
	private LinearLayout title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_fragment_view_pager);

		AMPlusCore.CurFuntion = FunctionType.TOPUP;

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		try {
			// GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK#GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK
			boolean flag = true;
			while (mMenu == null && flag) {
				mMenu = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_TOPUP);

				if (mMenu == null) {
					// init default menu
					if (User.sLang.equals(Config.LANG_EN)) {
						saveMenuTopup(Config.DEFAULT_MENU_TOPUP[1]);
						saveUserTable();
					} else {
						saveMenuTopup(Config.DEFAULT_MENU_TOPUP[0]);
						saveUserTable();
					}

					mMenu = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_TOPUP);
					flag = false;
				}
				if(mMenu != null) {
					if(mMenu.size() > 0)
						mMenu.remove(0);
					if(mMenu.size() > 0)
						mMenu.remove(0);
				}
			}

			mAdapter = new MPFragmentAdapter(getSupportFragmentManager(), mMenu);
			mPager = (ViewPager) findViewById(R.id.pager);
			mPager.setAdapter(mAdapter);

			mIndicator = (ViewTitlePaper) findViewById(R.id.indicator);
			mIndicator.setViewPager(mPager);
			
			
			// Ẩn Tab
			title = (LinearLayout) findViewById(R.id.title);
			title.setVisibility(LinearLayout.GONE);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "onCreate",  ex);
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
//				toggle();
				break;

//			case R.id.setting:
//
//				break;

			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else
			result = false;

		return result;
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putString("tab", mTabHost.getCurrentTabTag());
	}
}
package com.mpay.plus.bill;

import java.util.Vector;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.view.Menu;
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

/**
 * Hien thi dang sach cac san pham
 * 
 * @author quyenlm.vn@gmail.com
 * 
 * */

public class FrmBillPayment extends AMPlusCore {
	public final static String TAG = "FrmBillPayment";

	private MPFragmentAdapter mAdapter;
	private ViewPager mPager;
	private ViewTitlePaper mIndicator;
	private Vector<Group> mMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_fragment_view_pager);

		AMPlusCore.CurFuntion = FunctionType.BILL_PAYMENT;

		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		getSupportActionBar().setHomeButtonEnabled(true);

		setControls();
	}

	private void setControls() {
		try {
			// GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK#GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK

			boolean flag = true;
			while (mMenu == null && flag) {
				mMenu = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);

				if (mMenu == null) {
					// init default menu
					if (User.sLang.equals(Config.LANG_EN)) {
						saveMenuBillPayment(Config.DEFAULT_MENU_BILL[1]);
						saveUserTable();
					} else {
						saveMenuBillPayment(Config.DEFAULT_MENU_BILL[0]);
						saveUserTable();
					}

					mMenu = getDba().getGroups(
							DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);
					flag = false;
				}
			}

			mAdapter = new MPFragmentAdapter(getSupportFragmentManager(), mMenu);
			mPager = (ViewPager) findViewById(R.id.pager);
			mPager.setAdapter(mAdapter);

			mIndicator = (ViewTitlePaper) findViewById(R.id.indicator);
			mIndicator.setViewPager(mPager);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.next, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;

		if (!isBusy) {
			// Xu ly menu
			switch (item.getItemId()) {
			case android.R.id.home:
				goBack();
				/*toggle();*/
				break;
//			case R.id.next:
//				AMPlusCore.settingActivity.addSettingOrUpdateSetting(R.string.bill_payment_name, "icon_billpayment", getClass().getName());
//				break;

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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpay.plus.banks;

import java.util.Vector;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.MPListAdapter;
import com.mpay.MPListAdapter.MPListType;
import com.mpay.agent.R;
import com.mpay.plus.database.Item;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmSaoKe extends AMPlusCore {
	public static final String TAG = "FrmSaoKe";
	private MPListAdapter mAdapter;
	private ListView lv = null;
	
	private String arrTime[] = null;
	private String arrAmount[] = null;
	private String arrDescription[] = null;
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_saoke);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.icon_navigation_previous_item);
		
		setControls();
	}

	private void setControls(){
		try{			
			Bundle receiveBundle = this.getIntent().getExtras();
			arrTime = receiveBundle.getStringArray("value1");
		
			arrAmount = new String[arrTime.length];
			arrDescription = new String[arrTime.length];
			
			for (int i = 0; i < arrTime.length; ++i) {
				String astr[] = arrTime[i].split("\\|");
				arrTime[i] = String.valueOf(i + 1) + ". " + Util.getTimeClient4(astr[0]);
				arrAmount[i] =  astr[1];
				arrDescription[i] = astr[2];
			}
		
			lv = (ListView) findViewById(R.id.lv_content);
			mAdapter = new MPListAdapter(FrmSaoKe.this);
			mAdapter.setType(MPListType.TITLE_DESCRIPTION_AMOUNT);
			mAdapter.setData(getItems());
			lv.setAdapter(mAdapter);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
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
	
	public void goBack() {
		lv = null;
		arrTime = null;
		arrAmount = null;
		arrDescription = null;
		super.goBack();
	}

	private Vector<Item> getItems(){
		Vector<Item> items = new Vector<Item>();
		for (int i = 0; i < arrAmount.length; ++i) {
			Item item = new Item();
			item.setTitle(arrTime[i]);
			item.setDescription(arrDescription[i]);
			item.setImage(Util.formatNumbersAsTien(arrAmount[i]) + " " + getString(R.string.vnd));
			items.add(item);
		}
		return items;
	}
}
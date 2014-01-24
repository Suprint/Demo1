package com.mpay.plus.topup;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.FunctionType;
import com.mpay.MPListAdapter;
import com.mpay.MPListAdapter.MPListType;
import com.mpay.agent.R;
import com.mpay.plus.database.Item;
import com.mpay.plus.mplus.AMPlusCore;

/**
 * 
 * @author THANHNAM
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmMenuTopup extends AMPlusCore {
	public enum TopupType {
		MOBILE_PREPAID, MOBILE_POSTPAID, GAME_PREPAID,
	}

	private ListView lvMenu = null;
	private MPListAdapter mAdapter;
	private List<Item> items;
	public static final String KEY_ITEM = "ITEM";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_list);

		AMPlusCore.CurFuntion = FunctionType.TOPUP;

		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mAdapter = new MPListAdapter(FrmMenuTopup.this);
		mAdapter.setType(MPListType.ICON_TITLE_DESCRIPTION);

		items = new ArrayList<Item>();
		items.add(new Item(this.getString(R.string.topup_tra_truoc), getString(R.string.m_item_topup_tt_ghichu),
				"icon_topup_prepaid"));
		items.add(new Item(this.getString(R.string.topup_tra_sau), getString(R.string.m_item_topup_ts_ghichu),
				"icon_topup_postpaid"));
		items.add(new Item(this.getString(R.string.topup_game), getString(R.string.m_item_topup_game_ghichu),
				"icon_topup_game"));

		mAdapter.setData(items);

		lvMenu = (ListView) findViewById(R.id.lvcontent);
		lvMenu.setAdapter(mAdapter);
		lvMenu.setSelection(0);

		setEvent();
		this.setTitle(R.string.topup_name);
	}

	public void goBack() {
		lvMenu = null;

		super.goBack();
	}

	private void setEvent() {
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				setAct(position);
			}
		});
	}

	private void setAct(int iactId) {
		Intent intent;
//		Bundle sendBundle;
		switch (iactId) {

		case 0:
			intent = new Intent(FrmMenuTopup.this, FrmTopupPrepaid.class);
//			sendBundle = new Bundle();
			// sendBundle.putByte("type", (byte)0);
//			sendBundle.putSerializable("type", TopupType.MOBILE_PREPAID);
			items.get(0).setTitle(getString(R.string.topup_tra_truoc));
			items.get(0).setDescription(getString(R.string.topup_tra_truoc_des));
			intent.putExtra(KEY_ITEM, items.get(0));
//			intent.putExtras(sendBundle);
			startActivityForResult(intent, 0);
			break;

		case 1:
			intent = new Intent(FrmMenuTopup.this, FrmTopupPostpaid.class);

			items.get(1).setTitle(getString(R.string.topup_tra_sau_));
			items.get(1).setDescription(getString(R.string.topup_tra_truoc_des));
			intent.putExtra(KEY_ITEM, items.get(1));
			startActivityForResult(intent, 0);
			break;

		case 2:
			// intent = new Intent(FrmMenuTopup.this, FrmTopupPrepaid.class);
			// sendBundle = new Bundle();
			// sendBundle.putSerializable("type", TopupType.GAME_PREPAID);
			// intent.putExtras(sendBundle);
			// startActivityForResult(intent, 0);

			intent = new Intent(FrmMenuTopup.this, FrmMenuTopupGame.class);
//			intent.putExtra(KEY_ITEM, items.get(2));
			startActivityForResult(intent, 0);
			// sendBundle = new Bundle();
			// sendBundle.putSerializable("type", TopupType.GAME_PREPAID);
			// intent.putExtras(sendBundle);
			// startActivityForResult(intent, 0);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == SUCCESS) {
			// no
		} else {
			super.onActivityResult(requestCode, resultCode, data);
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
				/*toggle();*/
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

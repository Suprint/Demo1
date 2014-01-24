package com.mpay.plus.system;

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
import com.mpay.plus.imart.FrmPurcharsedProduct;
import com.mpay.plus.mplus.AMPlusCore;

/**
 * 
 * @author THANHNAM
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmMenuLog extends AMPlusCore {
	public enum LogType {
		TRANSACTION,
		BILL_PAYMENT,
	}
	
	public enum LogState {
		ALL,
		SUCCESS,
		ERROR,
		PENDING
	}
	
	private int selectedLog = 0;
	private ListView lvMenu = null;
	private MPListAdapter mAdapter;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_list);
		setTitle(this.getString(R.string.transaction_log));
		AMPlusCore.CurFuntion = FunctionType.HISTORY;

		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		mAdapter = new MPListAdapter(FrmMenuLog.this);
		mAdapter.setType(MPListType.TITLE_DESCRIPTION_NO_ICON);

		
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(this.getString(R.string.log_all), this.getString(R.string.log_all_des), "icon_transfer_mid"));
		items.add(new Item(this.getString(R.string.log_imart), this.getString(R.string.log_imart_des), "icon_transfer_account"));
		items.add(new Item(this.getString(R.string.log_hoadon),this.getString(R.string.log_hoadon_des), "icon_transfer_credit"));
		items.add(new Item(this.getString(R.string.tc_ket_qua_gd), this.getString(R.string.tc_ket_qua_gd_des), "icon_transfer_nationid"));
		mAdapter.setData(items);
		lvMenu = (ListView) findViewById(R.id.lvcontent);
		lvMenu.setAdapter(mAdapter);
		lvMenu.setSelection(0);

		setEvent();
	}

	public void goBack() {
		lvMenu = null;

		super.goBack();
	}

	private void setEvent() {
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				selectedLog = position;
				onCreateMyDialog(MPIN).show();
			}
		});
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

	public void cmdNextMPIN() {
		Intent intent = null;
		Bundle sendBundle = null;
		switch (selectedLog) {
		case 0: // Transaction
			intent = new Intent(FrmMenuLog.this, FrmTransactionLog_old.class);
			sendBundle = new Bundle();
			sendBundle.putSerializable("log_type", LogType.TRANSACTION);
			intent.putExtras(sendBundle);
			startActivity(intent);
			break;

		case 1: // Product
			intent = new Intent(this, FrmPurcharsedProduct.class);
			sendBundle = new Bundle();
			sendBundle.putByte("islog", (byte) 0);
			intent.putExtras(sendBundle);
			startActivityForResult(intent, 0);
			break;
		
		case 2: // Bill payment
			intent = new Intent(FrmMenuLog.this, FrmTransactionLog_old.class);
			sendBundle = new Bundle();
			sendBundle.putSerializable("log_type", LogType.BILL_PAYMENT);
			intent.putExtras(sendBundle);
			startActivity(intent);
			break;

			
		case 3: //Pending
			intent = new Intent(FrmMenuLog.this, FrmPenddingLog.class);
			startActivityForResult(intent, 0);
			break;
		}
	}
}
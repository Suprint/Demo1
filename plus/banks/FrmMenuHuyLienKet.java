package com.mpay.plus.banks;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.MPListAdapter;
import com.mpay.MPListAdapter.MPListType;
import com.mpay.agent.R;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.User;
import com.mpay.plus.mplus.AMPlusCore;

/**
 * 
 * @author HOANG HA
 * 
 */
public class FrmMenuHuyLienKet extends AMPlusCore {
	private MPListAdapter mAdapter;
	private ListView lvMenu = null;
	public static boolean iFlag = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_list);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		setTitle(R.string.bk_link_remove);
		
		mAdapter = new MPListAdapter(FrmMenuHuyLienKet.this);
		mAdapter.setType(MPListType.ICON_TITLE_DESCRIPTION);
		List<Item> items = new ArrayList<Item>();
		
		items.add(new Item(this.getString(R.string.bk_link_remove_emonkey), this.getString(R.string.bk_link_remove_emonkey_detail), "icon_plus_agent"));
		
		items.add(new Item(this.getString(R.string.bk_link_remove_atm), this.getString(R.string.bk_link_remove_atm_detail), "icon_naptien_thenganhang"));
		
		mAdapter.setData(items);
		
		lvMenu = (ListView) findViewById(R.id.lvcontent);
		lvMenu.setAdapter(mAdapter);
		lvMenu.setSelection(0);
		
		setEvent();
	}
	
	public void goBack (){
		lvMenu = null;
		
		super.goBack();
	}
	
	private void setEvent(){
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				setAct(position);
			}
		});
	}
	
	@Override
	public void cmdNextXacNhan() {
		if (AMPlusCore.ACTION == AMPlusCore.iDANG_KY_NAP_RUT_TIEN) {
			Intent intent = new Intent(FrmMenuHuyLienKet.this,
					FrmDangKyNapRutTien.class);
			startActivity(intent);
		}
		super.cmdNextXacNhan();
	}
	
	private void setAct(int iactId) {
		Intent intent = null;
		switch (iactId) {
		case 0: // Hủy liên kết thẻ eMonkey
			if(!"".equals(User.myAccountLink)){
				intent = new Intent(FrmMenuHuyLienKet.this, FrmLinkCardEMonkey.class);
				startActivity(intent);
			}else {
				sThongBao = getString(R.string.msg_notfound) + " " + getString(R.string.bk_link_card).toLowerCase();
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
			
		case 1: // Hủy liên kết thẻ ATM
			if(User.arrMyAcountATMLink.size() > 0){
				intent = new Intent(FrmMenuHuyLienKet.this, FrmLinkCardATM.class);
				startActivity(intent);
			}else {
				sThongBao = getString(R.string.msg_notfound) + " " + getString(R.string.bk_link_card_atm).toLowerCase();
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;
		
		if(!isBusy){
			// Xu ly menu
			switch (item.getItemId()) {
			case android.R.id.home:
				goBack();
				break;
			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else result = false;
		
		return result;
	}    
}

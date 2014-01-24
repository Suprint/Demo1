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
import com.mpay.FunctionType;
import com.mpay.MPListAdapter;
import com.mpay.MPListAdapter.MPListType;
import com.mpay.agent.R;
import com.mpay.mplus.dialog.DialogPending;
import com.mpay.plus.database.Item;
import com.mpay.plus.mplus.AMPlusCore;

/**
 * 
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmMenuTransfer extends AMPlusCore {
	public enum TransferType{
		MID,
		ACCOUNT,
		DEBIT_CARD,
		NATION_ID,
		INTER_BANK
	}
	
	private DialogPending dialog = null;
	private MPListAdapter mAdapter;
	private ListView lvMenu = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_list);
		
		AMPlusCore.CurFuntion = FunctionType.FUND_TRANSFER;

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		
		mAdapter = new MPListAdapter(FrmMenuTransfer.this);
		mAdapter.setType(MPListType.ICON_TITLE_DESCRIPTION);
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(this.getString(R.string.bk_ck), this.getString(R.string.bk_ck_detail), "icon_transfer"));
//		items.add(new Item(this.getString(R.string.bk_link_card), this.getString(R.string.bk_link_card_detail), "icon_transfer_mid"));
//		items.add(new Item(this.getString(R.string.bk_link_card_atm),this.getString(R.string.bk_link_card_atm_detail), "icon_transfer_card"));
//		items.add(new Item(this.getString(R.string.bk_link_address), this.getString(R.string.bk_link_address_detail), "icon_transfer_interbank"));
		
		mAdapter.setData(items);
		
		lvMenu = (ListView) findViewById(R.id.lvcontent);
		lvMenu.setAdapter(mAdapter);
		lvMenu.setSelection(0);
		
		if(dialog == null){
			dialog = new DialogPending(FrmMenuTransfer.this);
			dialog.setTitles(getString(R.string.dang_xu_ly_title_));
			dialog.setContents(getString(R.string.dang_xu_ly));
		}
		
		setEvent();
		setTitle(R.string.transfer_name);
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
	
	private void setAct(int iactId) {

		if(dialog == null){
			dialog = new DialogPending(FrmMenuTransfer.this);
			dialog.setTitles(getString(R.string.dang_xu_ly_title_));
			dialog.setContents(getString(R.string.dang_xu_ly));
		}
		
		dialog.show();
		Intent intent;
//		Bundle sendBundle = new Bundle();
		switch (iactId) {

		case 0: // Transfer
			intent = new Intent(FrmMenuTransfer.this, FrmTransfer.class);
//			sendBundle.putSerializable("type", TransferType.MID);
//			intent.putExtras(sendBundle);
			startActivity(intent);
			break;
			
//		case 1: // Link Card
//			intent = new Intent(FrmMenuTransfer.this, FrmLinkCard.class);
////			sendBundle.putSerializable("type", TransferType.ACCOUNT);
////			intent.putExtras(sendBundle);
//			startActivity(intent);
//			break;
//			
//		case 2: // Link Card ATM
//			intent = new Intent(FrmMenuTransfer.this, FrmLinkCardATM.class);
////			sendBundle.putSerializable("type", TransferType.DEBIT_CARD);
////			intent.putExtras(sendBundle);
//			startActivity(intent);
//			break;
//			
//		case 3: // Address M-Link
//			intent = new Intent(FrmMenuTransfer.this, FrmAddressMLink.class);
////			sendBundle.putSerializable("type", TransferType.INTER_BANK);
////			intent.putExtras(sendBundle);
//			startActivity(intent);
//			break;
		}
	}
	
	@Override
	public void onStop() {
		if(dialog != null)
			dialog.dismiss();
		super.onStop();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == SUCCESS){
			// no
		}else {
			super.onActivityResult(requestCode, resultCode, data);
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
//				
//			case R.id.setting:
//				
//				break;
				
			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else result = false;
		
		return result;
	};
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString("tab", mTabHost.getCurrentTabTag());
    }
}

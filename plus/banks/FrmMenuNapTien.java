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
import com.mpay.plus.database.Item;
import com.mpay.plus.database.User;
import com.mpay.plus.mplus.AMPlusCore;

/**
 * 
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmMenuNapTien extends AMPlusCore {
	public enum TransferType{
		MID,
		ACCOUNT,
		DEBIT_CARD,
		NATION_ID,
		INTER_BANK
	}
	
	private MPListAdapter mAdapter;
	private ListView lvMenu = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_list);
		
		AMPlusCore.CurFuntion = FunctionType.FUND_TRANSFER;

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		
		mAdapter = new MPListAdapter(FrmMenuNapTien.this);
		mAdapter.setType(MPListType.ICON_TITLE_DESCRIPTION);
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(this.getString(R.string.bk_naptien_mplus), this.getString(R.string.bk_naptien_mplus_detail), "icon_plus_agent"));
		items.add(new Item(this.getString(R.string.bk_naptien_Card), this.getString(R.string.bk_naptien_Card_detail), "icon_naptien_thenganhang"));
		items.add(new Item(this.getString(R.string.bk_naptien_Card_cao),this.getString(R.string.bk_naptien_Card_cao_detail), "icon_naptien_thecao"));
		items.add(new Item(this.getString(R.string.bk_naptien_thongbao), this.getString(R.string.bk_naptien_thongbao_detail), "icon_message_agent"));
		
		mAdapter.setData(items);
		
		lvMenu = (ListView) findViewById(R.id.lvcontent);
		lvMenu.setAdapter(mAdapter);
		lvMenu.setSelection(0);
		
		setEvent();
		setTitle(R.string.nap_tien_agent);
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
			Intent intent = new Intent(FrmMenuNapTien.this,
					FrmDangKyNapRutTien.class);
			startActivity(intent);
		}
		super.cmdNextXacNhan();
	}
	
	private void setAct(int iactId) {
		Intent intent;
//		Bundle sendBundle = new Bundle();
		switch (iactId) {
		

		case 0: // Nap tiền từ M-Plus
			if (User.isRegisted && !User.isRegNapTien) {
				AMPlusCore.ACTION = AMPlusCore.iDANG_KY_NAP_RUT_TIEN;
				sThongBao = getString(R.string.msg_register_bank_account_require);
				
				setMessageConfirm(getString(R.string.xac_nhan), "", "", "", sThongBao,
						getString(R.string.cmd_ok), getString(R.string.cmd_back), true, false);
				onCreateMyDialog(XAC_NHAN).show();
			} else {
				intent = new Intent(FrmMenuNapTien.this, FrmNapRutTien.class);
	//			sendBundle.putSerializable("type", TransferType.MID);
	//			intent.putExtras(sendBundle);
				startActivity(intent);
			}
			break;
			
		case 1: // Nạp tiền từ thẻ ngân hàng
			intent = new Intent(FrmMenuNapTien.this, FrmNapTheNganHang.class);
//			sendBundle.putSerializable("type", TransferType.ACCOUNT);
//			intent.putExtras(sendBundle);
			startActivity(intent);
			break;
			
		case 2: // Nạp tiền từ thẻ vào
			intent = new Intent(FrmMenuNapTien.this, FrmNapTheCao.class);
//			sendBundle.putSerializable("type", TransferType.DEBIT_CARD);
//			intent.putExtras(sendBundle);
			startActivity(intent);
			break;
			
		case 3: // Thông báo nạp tiền từ một kênh khác
			intent = new Intent(FrmMenuNapTien.this, FrmThongBaoNapTien.class);
//			sendBundle.putSerializable("type", TransferType.INTER_BANK);
//			intent.putExtras(sendBundle);
			startActivity(intent);
			break;
		}
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

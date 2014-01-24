package com.mpay.plus.mplus;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.mpay.MPListAdapter;
import com.mpay.MPListAdapter.MPListType;
import com.mpay.agent.R;
import com.mpay.plus.banks.FrmAddressMLink;
import com.mpay.plus.banks.FrmDangKyNapRutTien;
import com.mpay.plus.banks.FrmLinkCardATM;
import com.mpay.plus.banks.FrmLinkCardEMonkey;
import com.mpay.plus.banks.FrmMenuHuyLienKet;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.User;
import com.mpay.plus.imart.FrmPurcharsedProduct;
import com.mpay.plus.system.FrmRegistrationInfo;
import com.mpay.plus.system.PreferencesActivity;
import com.mpay.plus.util.Util;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Sliding menu main
 * 
 * @author quyenlm.vn@gmail.com
 * 
 * */
public class MenuMain extends SherlockListFragment {
	private static SlidingFragmentActivity mContext;
	private static TextView infoTaikhoan = null;
	private static String dangky;
	private static List<Item> items = null;
	static MPListAdapter mAdpapter = null;

	public static MenuMain newInstance(SlidingFragmentActivity context) {
		MenuMain menu = new MenuMain();
		menu.setParent(context);
		return menu;
	}

	public void setParent(SlidingFragmentActivity context) {
		mContext = context;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		items = new Vector<Item>();
		
		// Title Tien ich
		items.add(new Item(getString(R.string.util_name), "", "no_icon"));

		items.add(new Item(getString(R.string.info_hint), "", "icon_transfer"));
		
		items.add(new Item(getString(R.string.bk_link_card), "", "icon_lienketthe_emonkey"));
		
		items.add(new Item(getString(R.string.bk_link_card_atm), "", "icon_lienketthe_atm"));
		
//		items.add(new Item(getString(R.string.bk_link_remove), "", "icon_transfer"));

		items.add(new Item(getString(R.string.dang_ky_nap_rut_tien), "", "icon_transfer"));
		
		items.add(new Item(getString(R.string.btn_vaokhothe), "", "icon_transfer"));

		items.add(new Item(getString(R.string.bk_link_address) , "", "icon_deliver_agent"));
				
		// Title Thiet Lap
		items.add(new Item(getString(R.string.setting_name_thietlap), "", "no_icon"));
				
		items.add(new Item(getString(R.string.setting_name), "", "icon_action_settings"));

		
		
		
//		// Title Tien ich
//		items.add(new Item(getString(R.string.util_name), "NULL", "no_icon"));
//
//		for (int i = AMPlusCore.settingActivity.getSetting().length-1; i >= 0; i--) {
//			if(!"".equals(AMPlusCore.settingActivity.getSetting()[i])){
//				String dataSetting[] = AMPlusCore.settingActivity.getSetting()[i].split("\\|", -1);
//				items.add(new Item(getString(Integer.parseInt(dataSetting[0])), dataSetting[2], dataSetting[1]));
//			}
//		}
//		
//		// Title Thiet Lap
//		items.add(new Item(getString(R.string.setting_name_thietlap), "NULL", "no_icon"));
//				
//		items.add(new Item(getString(R.string.setting_name), "com.mpay.plus.system.PreferencesActivity", "icon_action_settings"));
		

		
		
		
		
		
//		// Tiele Tien ich
//		items.add(new Item(getString(R.string.util_name), "", "no_icon"));
//		
//		items.add(new Item(getString(R.string.imart_name), "", "icon_purcharse"));
//		items.add(new Item(getString(R.string.topup_name), "", "icon_topup"));
//		items.add(new Item(getString(R.string.bill_payment_name_main), "", "icon_billpayment"));
//		items.add(new Item(getString(R.string.history_name), "", "icon_history"));
//		
////		// them so du
////		items.add(new Item(getString(R.string.bk_sodu), "", "icon_balance_inquiry"));
//		
//		items.add(new Item(getString(R.string.transfer_name), "", "icon_transfer"));
//
////		// them sao ke
////		items.add(new Item(getString(R.string.bk_saoke), "", "icon_ministatement"));
//		
//		items.add(new Item(getString(R.string.nap_tien_agent), "",	"icon_deliver_agent"));
//		
//		// Title Thiet Lap
//		items.add(new Item(getString(R.string.setting_name_thietlap), "", "no_icon"));
//		
//		items.add(new Item(getString(R.string.setting_name), "", "icon_action_settings"));
		
		
		mAdpapter = new MPListAdapter(getActivity());
		mAdpapter.setType(MPListType.ICON_TITLE_SMALL);
		mAdpapter.setData(items);
		setListAdapter(mAdpapter);
		dangky = getString(R.string.title_confirm_dangky);

		infoTaikhoan = (TextView) getActivity().findViewById(R.id.infoTaikhoan);
		
		showInfoAcount();
	}
	
	public static void showInfoAcount() {
		infoTaikhoan.setText("");
		if(infoTaikhoan != null && User.isRegisted){
			infoTaikhoan.setText(Util.formatNumbersAsMid(User.MID));
		}else {
			infoTaikhoan.setText(dangky);
		}
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.old_menu_main, null);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
//		try {
//			Intent intent = new Intent();
//			String strClass = items.get(position).getDescription();
//			Class<?> className = Class.forName(strClass);
//			intent.setClass(getActivity(), className);
//
//			if(strClass.equals("com.mpay.plus.system.FrmRegistrationInfo"))
//				intent.putExtra("isFlag", true);
//			
//			startActivity(intent);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
		
		
		
		
		

		Intent intent = new Intent();
		switch (position) {
		case 1: // Thông tin cá nhân
			intent.setClass(getActivity(), FrmRegistrationInfo.class);
			intent.putExtra("isFlag", true);
			startActivity(intent);
			break;
		case 2: // Liên kết thẻ eMonkey
			intent.setClass(getActivity(), FrmLinkCardEMonkey.class);
			startActivity(intent);
			break;
		case 3: // Liên kết thẻ ATM
			FrmMenuHuyLienKet.iFlag = false;
			intent.setClass(getActivity(), FrmLinkCardATM.class);
			startActivity(intent);
			break;
			
//		case 4: // Hủy liên kết thẻ
//			if("".equals(User.myAccountLink) && User.arrMyAcountATMLink.size() <= 0)
//				Toast.makeText(mContext, getString(R.string.msg_notfound) + " " + getString(R.string.link_name).toLowerCase(), Toast.LENGTH_LONG).show();
//			else {
//				FrmMenuHuyLienKet.iFlag = true;
//				intent.setClass(getActivity(), FrmMenuHuyLienKet.class);
//				startActivity(intent);
//			}
//			break;
		case 4: // Đăng ký nạp rút tiền
			intent.setClass(getActivity(), FrmDangKyNapRutTien.class);
			startActivity(intent);
			break;
		case 5: // Vào kho thẻ
			intent.setClass(getActivity(), FrmPurcharsedProduct.class);
			startActivity(intent);
			break;
			
		case 6: // Địa điểm M-Link
			intent.setClass(getActivity(), FrmAddressMLink.class);
			startActivity(intent);
			break;

		case 8: // Setting 
			intent.setClass(getActivity(), PreferencesActivity.class);
			startActivity(intent);
			break;

		}

		if (mContext != null)
			if(position == 0 || position == 8){
			}else{
				if(position ==4)
					if("".equals(User.myAccountLink) && User.arrMyAcountATMLink.size() <= 0){
					}else mContext.toggle();
				else 
					mContext.toggle();
			}
		
		
		
		
		
		
		
		
		
//		Intent intent = new Intent();
//		switch (position) {
//		case 1: // imart
//			if (AMPlusCore.CurFuntion != FunctionType.SALE) {
//				intent.setClass(getActivity(), FrmSale.class);
//				startActivity(intent);
//			}
//			break;
//
//		case 2: // Topup
//			if (AMPlusCore.CurFuntion != FunctionType.TOPUP) {
//				intent.setClass(getActivity(), FrmMenuTopup.class);
//				startActivity(intent);
//			}
//			break;
//
//		case 3: // BillPayment
//			if (AMPlusCore.CurFuntion != FunctionType.BILL_PAYMENT) {
//				intent.setClass(getActivity(), FrmBillPayment.class);
//				startActivity(intent);
//			}
//			break;
//
//		case 4: // History
//			if (AMPlusCore.CurFuntion != FunctionType.HISTORY) {
//				intent.setClass(getActivity(), FrmMenuLog.class);
//				startActivity(intent);
//			}
//			break;
//
//		case 5: // Transfer
//			intent.setClass(getActivity(), FrmTransfer.class);
//			startActivity(intent);
//			break;
//
//		case 6: // Nap tien
//				intent = new Intent(getActivity(), FrmMenuNapTien.class);
//				startActivityForResult(intent, 0);
//			break;
//
//		case 8: // Setting
//			intent.setClass(getActivity(), PreferencesActivity.class);
//			startActivity(intent);
//			break;
//		}
//
//		if (mContext != null)
//			if(position == 0 || position == 7 || position == 8){
//			}else{
//				mContext.toggle();
//			}
	}
	
//	@Override
//	public void onStop() {
//		Log.i("Log : ", "onStop");
//		super.onStop();
//	}
//	@Override
//	public void onStart() {
//		Log.i("Log : ", "onStart");
//		super.onStart();
//	}
//	@Override
//	public void onResume() {
//		Log.i("Log : ", "onResume");
//		super.onResume();
//	}

	// the meat of switching the above fragment
//	private void switchFragment(SherlockFragment fragment) {
//		if (getActivity() == null)
//			return;
//
//		if (getActivity() instanceof FragmentChangeActivity) {
//			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
//			fca.switchContent(fragment);
//		} else if (getActivity() instanceof ResponsiveUIActivity) {
//			ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
//			ra.switchContent(fragment);
//		}
//	}
//	 
//	private onClick onclick;
//	
//	@Override
//	public void onAttach(Activity activity) {
//		if(onclick == null)
//		onclick = (onClick) activity;
//		super.onAttach(activity);
//	}
//	
//	interface onClick{
//		void setOnclick(int event);
//	}
}
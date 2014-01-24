//package com.mpay.plus.mplus;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.text.method.PasswordTransformationMethod;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.actionbarsherlock.app.SherlockActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;
//import com.mpay.MPListAdapter;
//import com.mpay.MPListAdapter.MPListType;
//import com.mpay.agent.R;
//import com.mpay.business.IProcess;
//import com.mpay.business.ThreadThucThi;
//import com.mpay.plus.config.Config;
//import com.mpay.plus.database.ConfirmItem;
//import com.mpay.plus.database.User;
//import com.mpay.plus.lib.MPlusLib;
//import com.mpay.plus.util.Util;
//
///**
// * Hien thi confirm
// * 
// * @author quyenlm.vn@gmail.com
// * 
// * */
//public final class FrmConfirm extends SherlockActivity {
//	public static final String TAG = "FrmConfirm";
//    private static final String KEY_CONTENT = "FrmConfirm:Content";
//    
//    public static IProcess mIProcess;
//	public static AMPlusCore mParent;
//	
////    private TextView tvTitle = null;
//    private ListView lvItem = null;    
//    private MPListAdapter mAdapter;
//    
//    private ConfirmItem mItem;
//    
//    public static FrmConfirm newInstance(ConfirmItem item) {
//        FrmConfirm fragment = new FrmConfirm();
//        fragment.setConfirmInfo(item);
//        return fragment;
//    }
//    
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		setContentView(R.layout.confirm);
//		
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
//		
//		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
//			//mItem = (ConfirmItem) savedInstanceState.getSerializable(KEY_CONTENT);
//		}
//		
//		try{
//			mItem = (ConfirmItem) this.getIntent().getSerializableExtra("confirm_info");
//			
////			tvTitle = (TextView) findViewById(R.id.tv_title);
//			mAdapter = new MPListAdapter(FrmConfirm.this);
//			mAdapter.setType(MPListType.TITLE_DESCRIPTION);
//			
//			if(mItem != null){
////				tvTitle.setText(mItem.getTitle());
//				mAdapter.setData(mItem.getItems());
//			}
//			
//			lvItem = (ListView) findViewById(R.id.lv_content);
//			lvItem.setAdapter(mAdapter);
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "onCreate", ex);
//		}
//	}
//	
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //outState.putSerializable(KEY_CONTENT, mItem);
//    }
//
//    @Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Khoi tao menu
//		getSupportMenuInflater().inflate(R.menu.ok, menu);
//		return true;
//    }
//    
//    @Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		boolean result = true;
//		
//		Intent returnIntent = new Intent();
//		
//		// Xu ly menu
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			setResult(RESULT_CANCELED, returnIntent);
//			finish();
//			break;
//			
//		case R.id.ok:
//			cmdNextXacNhan();
//			break;
//			
//		default:
//			result = super.onMenuItemSelected(featureId, item);
//			break;
//		}
//			
//		return result;
//	};
//		
//	public static void setIProcess(IProcess iProcess)
//	{
//		//Thiet lap iProcess se thuc thi
//		mIProcess = iProcess;
//	}
//	
//	public static void setParent(AMPlusCore parent)
//	{
//		//Activity parent
//		mParent = parent;
//	}
//	
//    private void setConfirmInfo(ConfirmItem item){
//    	this.mItem = item;
//    	if(mItem != null && mAdapter != null)
//			mAdapter.setData(mItem.getItems());
//    }
//
//    public void cmdNextXacNhan() {
//		onCreateMPinDialog().show();
//	}
//    
//    /**
//	 * THONG_BAO Tạo dialog thông báo XAC_NHAN Tạo dialog xác nhận giao dịch
//	 * MPIN Tạo dialog nhập MPIN
//	 */
//	protected Dialog onCreateMPinDialog() {
//		LayoutInflater factory = LayoutInflater.from(FrmConfirm.this);
//		View view;
//		
//		view = factory.inflate(R.layout.mpin, null);
//		final EditText etMPIN = (EditText) view.findViewById(R.id.etmpin);
//		etMPIN.setInputType(InputType.TYPE_CLASS_PHONE);
//		etMPIN.setTransformationMethod(PasswordTransformationMethod
//				.getInstance());
//		etMPIN.requestFocus();
//		etMPIN.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				Util.ChuanHoaMPIN(etMPIN, this);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start,
//					int count, int after) {
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start,
//					int before, int count) {
//			}
//
//		});
//
//		return new AlertDialog.Builder(FrmConfirm.this)
//				.setTitle(getString(R.string.mpin))
//				.setView(view)
//				.setCancelable(true)
//				.setIcon(R.drawable.icon_key)
//				.setPositiveButton(getString(R.string.cmd_ok),
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								dialog.dismiss();
//								
//								if (User.isRegisted) {
//									if (!etMPIN.getText().toString().trim().equals("")) {
//										if (checkMPin(etMPIN.getText().toString())) {
//											etMPIN.setText("");
//											cmdNextMPIN();
//										} else {
//											etMPIN.setText("");
//										}
//									} else {
//										onCreateMPinDialog().show();
//										Toast.makeText(FrmConfirm.this,
//												getString(R.string.phai_nhap) + " " + getString(R.string.mpin),
//												Toast.LENGTH_LONG).show();
//									}
//								} else {
////									MPlusLib.getResult("");
////									onCreateDialog(AMPlusCore.DANG_KY, R.drawable.icon_confirm).show();
//								}
//							}
//						})
//				.setNegativeButton(getString(R.string.cmd_back),
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								etMPIN.setText("");
//								dialog.dismiss();
//								cmdBackMPIN();
//							}
//						}).create();
//	}
//	
//	public boolean checkMPin(String vmpin) {
//		try {
//			if (MPlusLib.loadKey(vmpin) != null) {
//				User.iSoLanSaiMPIN = 0;
//				AMPlusCore.saveUserTable();
//				return true;
//			} else {
//				User.iSoLanSaiMPIN++;
//
//				if (User.iSoLanSaiMPIN >= Config.iMPIN_MAX_WRONG) {
//					User.sprivateEncKey = "";
//				}
//				AMPlusCore.saveUserTable();
//				showDiaLogWrongMPin();
//				return false;
//			}
//		} catch (Exception e) {
//			return false;
//		}
//	}
//	
//	public void showDiaLogWrongMPin() {
//		AlertDialog.Builder alert = new AlertDialog.Builder(this);
//		alert.setTitle(getString(R.string.thong_bao));
//		alert.setIcon(R.drawable.icon_notice);
//		alert.setMessage(getString(R.string.alert_message_nhap_sai_pin)
//				+ User.iSoLanSaiMPIN
//				+ "/"
//				+ Config.iMPIN_MAX_WRONG
//				+ ")"
//				+ (User.iSoLanSaiMPIN < Config.iMPIN_MAX_WRONG ? getString(R.string.alert_message_nhap_sai_pin2)
//						: getString(R.string.alert_message_nhap_sai_pin_yeu_cau)));
//		alert.setPositiveButton(getString(R.string.cmd_ok),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						if (User.iSoLanSaiMPIN >= Config.iMPIN_MAX_WRONG) {
//							setResult(AMPlusCore.EXITCODE, null);
//							finish();
//						} else onCreateMPinDialog();
//					}
//
//				});
//		alert.show();
//	}
//	
//    public void cmdNextMPIN() {
//    	AMPlusCore.threadThucThi = new ThreadThucThi(mParent);    	
//		AMPlusCore.threadThucThi.setIProcess(mIProcess, AMPlusCore.ACTION);
//		AMPlusCore.threadThucThi.Start();
//		
//		finish();
//	}
//
//	public void cmdBackMPIN() {
//	}
//}
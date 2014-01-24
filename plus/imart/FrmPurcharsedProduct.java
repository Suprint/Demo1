package com.mpay.plus.imart;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.MPSupported;
import com.mpay.ProductAdapter;
import com.mpay.agent.R;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.Product;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * @author quyenlm.vn@gmail.com
 * */
public class FrmPurcharsedProduct extends AMPlusCore {
//	private TextView tvInfo;
	private RelativeLayout loutInfo;
	private ImageView imgNotice;
	private TextView tvNotice;
	private ListView lvCard;
	private ProductAdapter mAdapter;
//	private byte isLogMaThe;
	private Vector<Product> mItems;
	private GridView grid_menu_co;
	private CheckBox checkMo = null;
	private CheckBox checkDong = null;
	private CheckBox checkAll = null;
	private Vector<Product> mItems_tmp;
	private boolean isCheck = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_imart_purcharsed_product);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		setControls();
	}

	private void setControls() {
		try {
			mItems_tmp = new Vector<Product>();
//			tvInfo = (TextView) findViewById(R.id.tv_info);
			loutInfo = (RelativeLayout) findViewById(R.id.lout_info);
			tvNotice = (TextView) findViewById(R.id.tv_notice);
			imgNotice = (ImageView) findViewById(R.id.img_notice);
			lvCard = (ListView) findViewById(R.id.lv_content);
			checkMo = (CheckBox) findViewById(R.id.checkMo);
			checkDong = (CheckBox) findViewById(R.id.checkDong);
			checkAll = (CheckBox) findViewById(R.id.checkAll);

			// Set Item Menu Log Imart
			List<Item> items = new ArrayList<Item>();
			items.add(new Item(getString(R.string.cmd_delete), "",
					"icon_content_discard"));
			items.add(new Item(getString(R.string.cmd_send), "",
					"icon_content_email"));
			items.add(new Item(getString(R.string.cmd_use), "",
					"icon_content_use"));

			grid_menu_co = (GridView) findViewById(R.id.grid_menu_co);
			grid_menu_co.setAdapter(new MPSupported(this, items));

			items = null;
//			isLogMaThe = 0;
//			try {
//				Bundle receiveBundle = this.getIntent().getExtras();
//				isLogMaThe = receiveBundle.getByte("islog");
//
//				if (isLogMaThe == 1) {
//
//					// msg = receiveBundle.getString("msg");
//					String data = receiveBundle.getString("data");
//					String pdcd = receiveBundle.getString("pdcd");
//					String price = receiveBundle.getString("price");
//					parseProductFromIMart(pdcd, price, data);
//
//					if (getProducts().size() == 1) {
//
//						final DialogConfirmiMart dialog = new DialogConfirmiMart(
//								this, getProducts());
//						dialog.setTitles(getString(R.string.title_ketquagiaodich));
//						dialog.setMyMessage(getString(R.string.confirm_info_muathe));
//						dialog.setMyMessage1(getString(R.string.confirm_guimathe));
//						dialog.setBtn_dongy(getString(R.string.btn_co_guingay));
//						dialog.setBtn_khongdongy(getString(R.string.btn_khong_desau));
//						dialog.setOnClickDialogConfirm(new DialogConfirmiMart.onClickDialogConfirm() {
//
//							@Override
//							public void onclickKhongDongY() {
//							}
//
//							@Override
//							public void onclickDongY(Product product) {
//								if (product != null) {
//									cmdNextXacNhanSendImart(product);
//								}
//								dialog.dismiss();
//							}
//						});
//						dialog.show();
//					}
//
//				}
				mItems = getDba().getProducts();
//			} catch (Exception ex) {
//				MPlusLib.debug(TAG, "setControls", ex);
//			}
			if (getProducts() != null && getProducts().size() > 0) {
				for (int i = 0; i < mItems.size(); i++) {
					mItems_tmp.add(mItems.get(i));
				}

//				tvInfo.setText(getSizeCard());
				tvNotice.setVisibility(View.GONE);
				imgNotice.setVisibility(View.GONE);

				mAdapter = new ProductAdapter(FrmPurcharsedProduct.this,
						getProducts(), 0, false);
				lvCard.setAdapter(mAdapter);
			} else {
				loutInfo.setVisibility(View.GONE);
			}

			setTitle(getString(R.string.imart_lichsu)+ (getSizeCard().equals("0") || getSizeCard().equals("")  ? "" : ": "+getSizeCard()));
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}

		isEvent();
		
	}

	private String getSizeCard() {
		String mes = "0";
		try {
			if (mItems_tmp != null && mItems_tmp.size() > 0) {
				mes = Util.insertString(
						getString(R.string.purchased_product_found),
						new String[] { String.valueOf(mItems_tmp.size()) });
			}
		} catch (Exception e) {
		}
		return mes;
	}

	private void isEvent() {
		try {
			grid_menu_co.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});
	
			grid_menu_co
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							isForcus(position);
						}
					});
			checkMo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (checkDong.isChecked()) {
						if (isChecked) {
							initAddData(1);
						} else {
							initRemoveData(1);
						}
						refreshData();
					} else {
						checkMo.setChecked(true);
					}
				}
			});
			checkDong
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (checkMo.isChecked()) {
								if (isChecked) {
									initAddData(0);
								} else {
									initRemoveData(0);
								}
								refreshData();
							} else {
								checkDong.setChecked(true);
							}
						}
					});
	
			checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						checkAll.setText(getString(R.string.check_bochon));
						for (int i = 0; i < mItems_tmp.size(); i++) {
							mItems_tmp.get(i).setmIsSelected(true);
						}
						refreshData();
					} else {
						checkAll.setText(getString(R.string.check_chontatca));
						if (isCheck) {
							for (int i = 0; i < mItems_tmp.size(); i++) {
								mItems_tmp.get(i).setmIsSelected(false);
							}
						}
						refreshData();
					}
					isCheck = true;
				}
	
			});
			if(mAdapter != null)
			mAdapter.setClickCheck(new ProductAdapter.clickCheck() {
	
				@Override
				public void clickClick() {
					if (mAdapter.getProducts().size() == mAdapter
							.getSelectProducts().size()) {
						checkAll.setChecked(true);
					} else {
						isCheck = false;
						checkAll.setChecked(false);
					}
				}
			});
		} catch (Exception e) {
		}
	}

//	@Override
//	public void cmdNextXacNhanSendImart(Product product) {
//		sendSMS(product);
//		super.cmdNextXacNhanSendImart(product);
//	}

	private void refreshData() {
//		tvInfo.setText(getSizeCard());
		mAdapter.initData(mItems_tmp);
		mAdapter.notifyDataSetChanged();
		setTitle(getString(R.string.imart_lichsu)+ (getSizeCard().equals("0") || getSizeCard().equals("")  ? "" : ": "+getSizeCard()));
	}

	private Vector<Product> initRemoveData(int flag) {
		int sizeItem = mItems.size();
		for (int i = 0; i < sizeItem; i++) {
			if (mItems.get(i).getIsUsed() == flag) {
				mItems_tmp.remove(mItems.get(i));
			}
		}
		return mItems_tmp;
	}

	private Vector<Product> initAddData(int flag) {
		initRemoveData(flag);
		for (int i = 0; i < mItems.size(); i++) {
			if (mItems.get(i).getIsUsed() == flag) {
				mItems.get(i).setmIsSelected(false);
				mItems_tmp.add(mItems.get(i));
			}
		}
		return mItems_tmp;
	}

	private void isForcus(int position) {
		switch (position) {
		case 0:
			if (getProducts() != null && mAdapter.getSelectProducts() != null
					&& mAdapter.getSelectProducts().size() > 0) {
				// thong bao xoa the trong kho
				int count = 0;
				for (int i = 0; i < mAdapter.getSelectProducts().size(); i++) {
					if (mAdapter.getSelectProducts().get(i).getIsUsed() == 0) {
						count++;
					}
				}
				if (count > 0) {
					// thong bao xoa the chua su dung
					sThongBao = Util.insertString(
							getString(R.string.xoa_the_error),
							new String[] { String.valueOf(mAdapter
									.getSelectProducts().size()) });
				} else {
					// thong bao xoa the
					sThongBao = Util.insertString(getString(R.string.xoa_the),
							new String[] { String.valueOf(mAdapter
									.getSelectProducts().size()) });
				}
				setMessageConfirm(getString(R.string.title_xacnhangiaodich), "", "",
						"", sThongBao, getString(R.string.btn_dongy), getString(R.string.btn_huy), true, false);
				
				onCreateMyDialog(XAC_NHAN).show();
			} else if (getProducts() != null && getProducts().size() > 0
					&& mAdapter.getSelectProducts().size() == 0) {
				// thong bao khong co the nao duoc chon
				sThongBao = getString(R.string.confirm_detlete_empty);
				onCreateMyDialog(THONG_BAO).show();
			} else if (getProducts() == null || getProducts().size() == 0) {
				// thong bao khong co the nao trong kho
				sThongBao = getString(R.string.purchased_product_notfound);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		case 1:
			if (getProducts() != null && mAdapter.getSelectProducts() != null
					&& mAdapter.getSelectProducts().size() == 1) {
				// gui SMS
				sendSMS(mAdapter.getSelectProducts().get(0));
				processLog();
			} else if (getProducts() != null
					&& mAdapter.getSelectProducts() != null
					&& mAdapter.getSelectProducts().size() > 1) {
				// thong bao khi chon nhieu the
				sThongBao = getString(R.string.confirm_sendcard_multi);
				onCreateMyDialog(THONG_BAO).show();
			} else if (getProducts() != null && getProducts().size() > 0
					&& mAdapter.getSelectProducts() != null
					&& mAdapter.getSelectProducts().size() == 0) {
				// thong bao khi khong chon the nao
				sThongBao = getString(R.string.confirm_sendcard_empty);
				onCreateMyDialog(THONG_BAO).show();
			} else if (getProducts() == null || getProducts().size() == 0) {
				// thong bao khong co the nao trong kho
				sThongBao = getString(R.string.purchased_product_notfound);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		case 2:
			if (getProducts() != null && mAdapter.getSelectProducts() != null
					&& mAdapter.getSelectProducts().size() == 1) {
				// nap the
				napThe(mAdapter.getSelectProducts().get(0));
				processLog();
			} else if (getProducts() != null
					&& mAdapter.getSelectProducts() != null
					&& mAdapter.getSelectProducts().size() > 1) {
				// thong bao khi chon nhieu the
				sThongBao = getString(R.string.confirm_usercard_multi);
				onCreateMyDialog(THONG_BAO).show();
			} else if (getProducts() != null && getProducts().size() > 0
					&& mAdapter.getSelectProducts() != null
					&& mAdapter.getSelectProducts().size() == 0) {
				sThongBao = getString(R.string.confirm_usercard_empty);
				onCreateMyDialog(THONG_BAO).show();
			} else if (getProducts() == null || getProducts().size() == 0) {
				// thong bao khong co the nao trong kho
				sThongBao = getString(R.string.purchased_product_notfound);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;

		default:
			break;
		}
	}

	private Vector<Product> getProducts() {
		return mItems;
	}

//	private void parseProductFromIMart(String pdcd, String price, String data) {
//		try {
//			String sSeri = "";
//			String sAccNo = "";
//			String sPin = "";
//			String sexDate = "";
//			String sSupport = "";
//
//			String[] stemp = data.split("\\|");
//			mItems = new Vector<Product>(stemp.length - 1);
//
//			for (int i = 1; i < stemp.length; i++) {
//				if (!stemp[i].trim().equals("")) {
//					String[] sarrTemp = stemp[i].split("!", -1);
//					sSupport = sarrTemp[sarrTemp.length - 1];
//					for (int j = 0; j < sarrTemp.length - 1; ++j) {
//						if (!sarrTemp[j].trim().equals("")) {
//							if (sarrTemp[j].trim().startsWith("1"))
//								sSeri = sarrTemp[j].trim().substring(1);
//							else if (sarrTemp[j].trim().startsWith("2"))
//								sAccNo = sarrTemp[j].trim().substring(1);
//							else if (sarrTemp[j].trim().startsWith("3"))
//								sPin = sarrTemp[j].trim().substring(1);
//							else if (sarrTemp[j].trim().startsWith("4"))
//								sexDate = sarrTemp[j].trim().substring(1);
//						}
//					}
//
//					Product product = new Product();
//					product.setProductCode(pdcd);
//					product.setPrice(price);
//					product.setAccNo(sAccNo);
//					product.setPin(sPin);
//					product.setSeri(sSeri);
//					product.setExDate(sexDate);
//					product.setHotLine(sSupport);
//					product.setIsUsed(0);
//					product.setId(getDba().insertOrUpdateProduct(product));
//					getProducts().add(product);
//
//					sSeri = "";
//					sAccNo = "";
//					sPin = "";
//					sexDate = "";
//					sSupport = "";
//				}
//			}
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "parseProductFromIMart", ex);
//		}
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Khoi tao menu
		getSupportMenuInflater().inflate(R.menu.product, menu);
		return true;
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

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (isFinished) {
	// Product product = null;
	// for (int i = 0; i < getProducts().size(); i++) {
	// if(getProducts().get(i).getId() ==
	// mItems_tmp.get(mAdapter.getSelectedRadioChoiceIndex()).getId()){
	// product = getProducts().get(i);
	// product.setIsUsed(1);
	// }
	// }
	// if (product != null && getDba().insertOrUpdateProduct(product) != -1) {
	// refreshData();
	// }
	// } else
	// super.onActivityResult(requestCode, resultCode, data);
	// }

	private void processLog() {
		Product product = null;
		for (int i = 0; i < getProducts().size(); i++) {
			if (getProducts().get(i).getId() == mItems_tmp.get(
					mAdapter.getSelectedRadioChoiceIndex()).getId()) {
				product = getProducts().get(i);
				product.setIsUsed(1);
			}
		}
		if (product != null && getDba().insertOrUpdateProduct(product) != -1) {
			refreshData();
		}
	}

	public void goBack() {

//		tvInfo = null;
		loutInfo = null;
		imgNotice = null;
		tvNotice = null;
		lvCard = null;
		mAdapter = null;
//		isLogMaThe = 0;
		mItems = null;
		grid_menu_co = null;
		checkMo = null;
		checkDong = null;
		checkAll = null;
		mItems_tmp = null;

//		if (isLogMaThe == 1) {
//			setResult(AMPlusCore.SUCCESS);
//		}

		super.goBack();
	}

//	private void napThe(Product product) {
//		String sPinCode = product.getPin();
//		if (!sPinCode.equals(""))
//			try {
//				Intent callIntent = new Intent(Intent.ACTION_VIEW);
//				callIntent.setData(Uri.parse("tel:"
//						+ Uri.encode("*100*" + sPinCode + "#")));
//				startActivity(callIntent);
//				processLog();
//				isFinished = false;
//			} catch (Exception e) {
//			}
//	}

//	private void sendSMS(Product produc) {
//		try {
//			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//			sendIntent.putExtra("sms_body", getProductInfoSms(produc));
//			sendIntent.setType("vnd.android-dir/mms-sms");
//			startActivity(sendIntent);
//			processLog();
//			isFinished = false;
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "sendSMS", ex);
//		}
//	}

//	private String getProductInfoSms(Product product) {
//		String msg = "";
//
//		StringBuffer sBuf = new StringBuffer();
//		sBuf.append(product.getProductCode());
//		sBuf.append(" ");
//		sBuf.append(Util.formatNumbersAsTien(product.getPrice()));
//		sBuf.append(" VND:");
//		if (!product.getAccNo().equals("")) {
//			sBuf.append(getString(R.string.acc_none));
//			sBuf.append(" ");
//			sBuf.append(product.getAccNo());
//			sBuf.append(", ");
//		}
//
//		if (!product.getPin().equals("")) {
//			sBuf.append(getString(R.string.card_pin_none));
//			sBuf.append(" ");
//			sBuf.append(product.getPin());
//			sBuf.append(", ");
//		}
//
//		if (!product.getExDate().equals("")) {
//			sBuf.append(getString(R.string.card_exdate_none));
//			sBuf.append(" ");
//			sBuf.append(product.getExDate());
//			sBuf.append(", ");
//		}
//
//		if (!product.getSeri().equals("")) {
//			sBuf.append(getString(R.string.card_seri));
//			sBuf.append(" ");
//			sBuf.append(product.getSeri());
//			sBuf.append(", ");
//		}
//
//		if (!product.getHotLine().equals("")) {
//			sBuf.append(getString(R.string.card_hotline_none));
//			sBuf.append(" ");
//			sBuf.append(product.getHotLine());
//			sBuf.append(", ");
//		}
//
//		msg = sBuf.toString().trim();
//		if (msg.charAt(msg.length() - 1) == ',')
//			msg = msg.substring(0, msg.length() - 1);
//
//		return msg;
//	}

	@Override
	public void cmdNextXacNhan() {
		deleteLog();
		super.cmdNextXacNhan();
	}

	private void deleteLog() {
		try {

			int sizePro = mAdapter.getSelectProducts().size();
			for (int i = 0; i < sizePro; i++) {
				getDba().deleteProduct(
						String.valueOf(mAdapter.getSelectProducts().get(i)
								.getId()));
				// getProducts().remove(mAdapter.getSelectProducts().get(i));
				// mItems_tmp.remove(mAdapter.getSelectProducts().get(i));
			}

			mItems_tmp.clear();
			mItems.clear();
			mItems = getDba().getProducts();
			if (getProducts() != null && getProducts().size() > 0) {
				for (int i = 0; i < mItems.size(); i++) {
					mItems_tmp.add(mItems.get(i));
				}
			}

			if (getProducts() == null || getProducts().size() <= 0) {
				loutInfo.setVisibility(View.GONE);
				tvNotice.setVisibility(View.VISIBLE);
				imgNotice.setVisibility(View.VISIBLE);
			}

		} catch (Exception e) {
		} finally {
			refreshData();
		}
	}

	@Override
	public void onStop() {
		goBack();
		super.onStop();
	}
}
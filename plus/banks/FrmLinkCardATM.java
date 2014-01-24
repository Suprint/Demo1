package com.mpay.plus.banks;

import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.AnyObject;
import com.mpay.plus.database.ConfirmItem;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.User;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

public class FrmLinkCardATM extends AMPlusCore implements IProcess {

	private AutoCompleteTextView et_to_account = null;
	private AutoCompleteTextView et_to_hoten = null;
	private AutoCompleteTextView et_to_date = null;
	private Button btn_tieptuc = null;
	private ListView list_supported_co = null;
	private CardAdapter adapterCard = null;
	private TextView hotline = null;
	private LinearLayout layout_supported = null;
	private Button btn_huylienket = null;
	private boolean iFlag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_bk_link_atm);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		setTitle(getString(R.string.bk_link_card_atm));
		setControls();
	}

	private void setControls() {
		btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
		et_to_account = (AutoCompleteTextView) findViewById(R.id.et_to_account);
		et_to_hoten = (AutoCompleteTextView) findViewById(R.id.et_to_hoten);
		et_to_date = (AutoCompleteTextView) findViewById(R.id.et_to_date);
		btn_huylienket = (Button) findViewById(R.id.btn_huylienket);
		layout_supported = (LinearLayout) findViewById(R.id.layout_supported);
		layout_supported = (LinearLayout) findViewById(R.id.layout_supported);
		adapterCard = new CardAdapter(FrmLinkCardATM.this, getObjLink(), 0);
		list_supported_co = (ListView) findViewById(R.id.list_supported_co);
		hotline = (TextView) findViewById(R.id.hotline);
		setData();
		setEvent();
	}

	private void setData() {
		
		if (User.arrMyAcountATMLink.size() > 0) {
			if ("".equals(User.arrMyAcountATMLink.get(0))) {
				return;
			}

			
			layout_supported.setVisibility(TextView.VISIBLE);
			adapterCard.reData(getObjLink());
			adapterCard.setSelectindex(0);
			list_supported_co.setVisibility(ListView.VISIBLE);
			list_supported_co.setAdapter(adapterCard);
			adapterCard.notifyDataSetChanged();

			hotline.setVisibility(TextView.GONE);
		}else {
			hotline.setVisibility(TextView.VISIBLE);

			layout_supported.setVisibility(TextView.GONE);
			list_supported_co.setVisibility(ListView.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
	}

	private Vector<AnyObject> getObjLink() {
		Vector<AnyObject> link = new Vector<AnyObject>();
		for (int i = 0; i < User.arrMyAcountATMLink.size(); i++) {
			String[] sItem = User.arrMyAcountATMLink.get(i).split("-", -1);
			AnyObject item = new AnyObject();
			item.setsID(sItem[0]); // Id Bank
			item.setsType(sItem[1]); // Tên Bank
			item.setsContent(sItem[2]); // Số tài khoản
			item.setsDescription(sItem[3]); // Tên tài khoản
			item.setsIsActive(sItem[4]); // Ngày hết hạn
			link.add(item);
		}

		return link;
	}

	private void setEvent() {
		et_to_account.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				beforesSelection = before;
				selection = start;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaMID(et_to_account, this, selection,
						beforesSelection);
			}
		});

		et_to_date.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				beforesSelection = before;
				selection = start;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				Util.ChuanHoaDate(et_to_date, this);
			}
		});
		btn_tieptuc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iFlag = false;
				goNext();
			}
		});
		btn_huylienket.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iFlag = true;
				goNext();
			}
		});
	}

	private String getToAccount() {
		return Util.keepABCAndNumber(et_to_account.getText().toString());
	}

	private String getFullName() {
		return Util.keepABCAndNumber(et_to_hoten.getText().toString());
	}

	private String getDate() {
		return Util.keepNumbersOnly(et_to_date.getText().toString());
	}

	private String getCardID() {
		return getObjLink().get(adapterCard.getSelectindex()).getsID();
	}

	private boolean checkData() {
		sThongBao = "";
		if ("".equals(getToAccount())) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.card_number_atm);
			et_to_account.requestFocus();
			return false;
		}
		if (getToAccount().length() < Config.iACC_MIN_LENGTH
				|| getToAccount().length() > Config.iACC_MAX_LENGTH) {
			sThongBao = Util.insertString(
					getString(R.string.card_number_atm_wrong),
					new String[] { String.valueOf(Config.iACC_MIN_LENGTH),
							String.valueOf(Config.iACC_MAX_LENGTH), "<br/>" });
			et_to_account.requestFocus();
			return false;
		}
		if ("".equals(getFullName())) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.hoten);
			et_to_hoten.requestFocus();
			return false;
		}
		if ("".equals(getDate())) {
			sThongBao = getString(R.string.phai_nhap) + " "
					+ getString(R.string.ngay_hethan);
			et_to_date.requestFocus();
			return false;
		}
		if (getDate().length() < Config.iNGAYSINH_MIN_LENGTH) {
			sThongBao = Util.insertString(
					getString(R.string.ngay_hethan_wrong),
					new String[] { "<br/>" });
			et_to_date.requestFocus();
			return false;
		}

		return true;
	}

	@Override
	public void goBack() {
		et_to_account = null;
		et_to_hoten = null;
		et_to_date = null;
		btn_tieptuc = null;
		list_supported_co = null;
		hotline = null;
		layout_supported = null;
		btn_huylienket = null;
		super.goBack();
	}

	@Override
	public void goNext() {
		if (iFlag) {
			AMPlusCore.ACTION = L_LINK_ATM_REMOVE;
			showConfirm();
		} else {
			if (checkData()) {
				AMPlusCore.ACTION = L_LINK_ATM;
				showConfirm();
			} else {
				onCreateMyDialog(THONG_BAO).show();
			}
		}
		super.goNext();
	}

	public void showConfirm() {
		ConfirmItem confirm = new ConfirmItem();
		confirm.setItems(getConfirmContent());
		if (iFlag) {
			setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.bk_link_remove_atm_message), "",
					"", "", getString(R.string.btn_huy) + " " + getString(R.string.btn_lienket).toLowerCase(), 
					getString(R.string.btn_khong_desau), true, true);
		} else {
			setMessageConfirm(getString(R.string.title_xacnhangiaodich), getString(R.string.bk_link_card_atm), "", 
					"", "", getString(R.string.btn_lienket), getString(R.string.btn_khong_desau), true, true);
		}
		dialogConfrim(confirm, FrmLinkCardATM.this, FrmLinkCardATM.this);
	}

	private Vector<Item> getConfirmContent() {
		Vector<Item> items = new Vector<Item>();

		if (iFlag)
			items.add(new Item(getString(R.string.tainganhang) + ": ",
					getObjLink().get(adapterCard.getSelectindex()).getsType(),
					""));

		items.add(new Item(getString(R.string.acc) + ": ", Util
				.formatNumbersAsMid(iFlag ? getObjLink().get(
						adapterCard.getSelectindex()).getsContent()
						: getToAccount()), ""));

		items.add(new Item(getString(R.string.fullName) + ": ",
				iFlag ? getObjLink().get(
						adapterCard.getSelectindex()).getsDescription()
						: getFullName(), ""));

		items.add(new Item(
				getString(R.string.ngay_hethan) + ": ",
				Util.formatDate(iFlag ? getObjLink().get(
						adapterCard.getSelectindex()).getsIsActive() : getDate()),
				""));

		return items;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void processDataSend(byte iTag) {
		switch (iTag) {
		case L_LINK_ATM:
			 ServiceCore.TaskLinkCardATM(getToAccount(), getFullName(), getDate());
			break;
		case L_LINK_ATM_REMOVE:
			 ServiceCore.TaskLinkCardATMRemove(getCardID());
			break;

		default:
			break;
		}
	}

	@Override
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {

		switch (iTag) {
		case L_LINK_ATM:
//			dataReceived = "val:1|Ngân hàng Agribank";
			if (dataReceived.startsWith("val:")) {
				String sCard = Util.sCatVal(dataReceived).replace("|", "-");
				User.arrMyAcountATMLink.add(sCard + "-"
						+ Util.formatAccount(getToAccount()) + "-"
						+ getFullName() + "-" + getDate());

				setData();
				saveUserTable();
				sThongBao = Util
						.insertString(
								getString(R.string.confirm_cardlink_success),
								new String[] { getToAccount(),
										sCard.split("-", -1)[1] });
				onCreateMyDialog(THONG_BAO).show();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;
		case L_LINK_ATM_REMOVE:
			if (dataReceived.startsWith("val:")) {
				User.arrMyAcountATMLink.remove(adapterCard.getSelectindex());

				setData();
				saveUserTable();
				sThongBao = "Thành công";
				onCreateMyDialog(THONG_BAO).show();
			} else {
				sThongBao = Util.sCatVal(dataReceived);
				onCreateMyDialog(THONG_BAO).show();
			}
			break;

		default:
			break;
		}
	}
	
	private class CardAdapter extends BaseAdapter {
		private Context mContext;
		private Vector<AnyObject> data;
		private int selectindex = 0;

		public CardAdapter(Context context, Vector<AnyObject> data,
				int indexCurren) {
			super();
			mContext = context;
			this.data = data;
			this.selectindex = indexCurren;
		}

		@Override
		public int getCount() {
			return data.size();
		}
		
		public void reData(Vector<AnyObject> data) {
			this.data = data;
		}

		public int getSelectindex() {
			return selectindex;
		}

		public void setSelectindex(int selectindex) {
			this.selectindex = selectindex;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.old_list_item_radio, null);
			}

			RadioButton radio = (RadioButton) view.findViewById(R.id.rdo);
			TextView tvTitle = (TextView) view.findViewById(R.id.tvrdo_title);
			TextView tvrdo_detail = (TextView) view
					.findViewById(R.id.tvrdo_detail);

			tvTitle.setText(data.get(position).getsContent());
			tvrdo_detail.setText(data.get(position).getsType());

			if (position == getSelectindex())
				radio.setChecked(true);
			else
				radio.setChecked(false);

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setSelectindex(position);
					CardAdapter.this.notifyDataSetChanged();
				}
			});
			return view;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}
}

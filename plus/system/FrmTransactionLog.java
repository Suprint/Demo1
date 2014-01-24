package com.mpay.plus.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.LogBillItem;
import com.mpay.plus.database.LogTransactionItem;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.system.FrmMenuLog.LogState;
import com.mpay.plus.system.FrmMenuLog.LogType;
import com.mpay.plus.util.Util;
import com.mpay.ui.base.BaseListAdapter;
import com.mpay.ui.base.BaseListFragment;
import com.mpay.ui.base.BaseListLoader;
import com.mpay.ui.base.BaseTabsAdapter;

/**
 * Hien thi log giao dich
 * 
 * @author quyenlm.vn@gmail.com
 * */
public class FrmTransactionLog extends AMPlusCore {
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private LogTabsAdapter mTabsAdapter;
	private LogType iType = LogType.TRANSACTION;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs_pager);


		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(true);

		try {
			iType = (LogType) getIntent().getExtras().get("log_type");
		} catch (Exception e) {
		}

		try {
			if (iType == LogType.BILL_PAYMENT)
				this.setTitle(getString(R.string.bill_payment_log));

			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();

			mViewPager = (ViewPager) findViewById(R.id.pager);

			mTabsAdapter = new LogTabsAdapter(this, mTabHost, mViewPager);

			if (iType == LogType.TRANSACTION) {
				Bundle args = new Bundle();
				args.putSerializable("log_type", iType);
				args.putSerializable("log_state", LogState.ALL);
				mTabsAdapter.addTab(
						mTabHost.newTabSpec("All").setIndicator(
								getString(R.string.result_all)),
						LogListFragment.class, args);
			}

			Bundle argsSuccess = new Bundle();
			argsSuccess = new Bundle();
			argsSuccess.putSerializable("log_type", iType);
			argsSuccess.putSerializable("log_state", LogState.SUCCESS);
			mTabsAdapter.addTab(
					mTabHost.newTabSpec("Error").setIndicator(
							getString(R.string.result_success)),
					LogListFragment.class, argsSuccess);

			if (iType == LogType.TRANSACTION) {
				Bundle argsErr = new Bundle();
				argsErr = new Bundle();
				argsErr.putSerializable("log_type", iType);
				argsErr.putSerializable("log_state", LogState.ERROR);
				mTabsAdapter.addTab(
						mTabHost.newTabSpec("Error").setIndicator(
								getString(R.string.result_error)),
						LogListFragment.class, argsErr);

				Bundle argsPend = new Bundle();
				argsPend = new Bundle();
				argsPend.putSerializable("log_type", iType);
				argsPend.putSerializable("log_state", LogState.PENDING);
				mTabsAdapter.addTab(mTabHost.newTabSpec("Pending")
						.setIndicator(getString(R.string.result_pending)),
						LogListFragment.class, argsPend);
			}

			if (savedInstanceState != null) {
				mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "onCreate", ex);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
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

	private static class LogTabsAdapter extends BaseTabsAdapter {

		public LogTabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity, tabHost, pager);
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = getTabs().get(position);
			return LogListFragment.newInstance(info.getBundle());
		}
	}

	private static class LogListAdapter extends BaseListAdapter {
		public LogListAdapter(Context context) {
			super(context);
		}

		/**
		 * Populate new items in the list.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;

			if (convertView == null) {
				view = super.mInflater.inflate(R.layout.old_list_item_log, parent,
						false);
			} else {
				view = convertView;
			}

			Item item = getItem(position);
			((ImageView) view.findViewById(R.id.image)).setImageDrawable(Util
					.getImage(getContext(), item.getImage()));
			((TextView) view.findViewById(R.id.title)).setText(item.getTitle());
			((TextView) view.findViewById(R.id.description)).setText(item
					.getDescription());

			return view;
		}
	}

	private static class LogListLoader extends BaseListLoader {

		public LogListLoader(Context context, Bundle bundleInfo) {
			super(context, bundleInfo);
		}

		@Override
		public List<Item> loadInBackground() {
			// Create corresponding array of entries and load their labels.
			List<Item> entries = null;

			LogType logType = LogType.TRANSACTION;
			LogState logState = LogState.ALL;
			try {
				logType = (LogType) super.getBundle().getSerializable(
						"log_type");
				logState = (LogState) super.getBundle().getSerializable(
						"log_state");
			} catch (Exception e) {
			}

			if (logType == LogType.TRANSACTION) {
				entries = getTransactionLog(logState);
			} else if (logType == LogType.BILL_PAYMENT) {
				entries = getBillPaymentLog(logState);
			}

			if (entries == null)
				entries = new ArrayList<Item>();

			// Done!
			return entries;
		}

		/**
		 * type = 1: get tranName; type = 2 : get dstName
		 * */
		private String getTransactionName(String command, int type) {
			String tranName = command;

			if (command.equals("0" + Config.M_IMART_THANH_TOAN)) {
				// mua hang imart
				tranName = getString(R.string.imart_name);
			} else if (command.equals("1" + Config.M_IMART_THANH_TOAN)) {
				// topup mobile tra truoc
				tranName = getString(R.string.topup_tra_truoc);
			} else if (command.equals("2" + Config.M_IMART_THANH_TOAN)) {
				// topup mobile tra sau
				tranName = getString(R.string.topup_tra_sau);
			} else if (command.equals("3" + Config.M_IMART_THANH_TOAN)) {
				// topup game
				tranName = getString(R.string.topup_game);
			} else if (command.equals(Config.M_IMART_THANH_TOAN)) {
				// topup game
				tranName = getString(R.string.imart_name) + "/"
						+ getString(R.string.topup_name);
			} else if (command.equals(Config.M_HOA_DON_THANH_TOAN)) {
				tranName = getString(R.string.bill_payment_name);
			} else if (command.equals(Config.A_CK_AGENT)) {
				tranName = getString(R.string.transfer_name);
			} else if (command.equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
				tranName = getString(R.string.nap_tien_agent);
			} else if (command.equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
				tranName = getString(R.string.rut_tien_agent);
			}

			return tranName;
		}

		/**
		 * type = 1: get tranName; type = 2 : get dstName
		 * */
		private String getTransactionDetail(LogTransactionItem log) {
			String command = log.getTranCommand();

			StringBuffer sBuf = new StringBuffer();

			if (command.equals("0" + Config.M_IMART_THANH_TOAN)) {
				// mua hang imart
				sBuf.append(getString(R.string.san_pham));
				sBuf.append(": ");
				sBuf.append(log.getDestinationNo());
				sBuf.append("\n");
			} else if (command.equals("1" + Config.M_IMART_THANH_TOAN)) {
				// topup mobile tra truoc
				sBuf.append(getString(R.string.msg_to));
				sBuf.append(": ");
				sBuf.append(log.getDestinationNo());
				sBuf.append("\n");
			} else if (command.equals("2" + Config.M_IMART_THANH_TOAN)) {
				// topup mobile tra sau
				sBuf.append(getString(R.string.msg_to));
				sBuf.append(": ");
				sBuf.append(log.getDestinationNo());
				sBuf.append("\n");
			} else if (command.equals("3" + Config.M_IMART_THANH_TOAN)) {
				// topup game
				sBuf.append(getString(R.string.msg_to));
				sBuf.append(": ");
				sBuf.append(log.getDestinationNo());
				sBuf.append("\n");
			} else if (command.equals(Config.M_HOA_DON_THANH_TOAN)) {
				// Bill payment
				String[] pcd = log.getDestinationNo().split("\\.");
				String supplierName = "";
				String billCode = log.getDestinationNo();
				if (pcd.length > 1) {
					billCode = pcd[1];
					Item supplier = getDba().getItem(pcd[0],
							DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);
					if (supplier != null) {
						supplierName = supplier.getTitle();
					} else
						supplierName = pcd[0];
				}
				// supplier name
				sBuf.append(getString(R.string.biller));
				sBuf.append(": ");
				sBuf.append(supplierName);
				sBuf.append("\n");
				// payment code
				sBuf.append(getString(R.string.so_hoa_don));
				sBuf.append(": ");
				sBuf.append(billCode);
				sBuf.append("\n");
			} else if (command.equals(Config.A_CK_AGENT)) {
				sBuf.append(getString(R.string.msg_to));
				sBuf.append(": ");
				sBuf.append(log.getDestinationNo());
				sBuf.append("\n");
			} else if (command.equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
				sBuf.append(getString(R.string.msg_from));
				sBuf.append(": ");
				sBuf.append(log.getDestinationNo());
				sBuf.append("\n");
			} else if (command.equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
				sBuf.append(getString(R.string.msg_to));
				sBuf.append(": ");
				sBuf.append(log.getDestinationNo());
				sBuf.append("\n");
			}

			return sBuf.toString();
		}

		private Vector<Item> getTransactionLog(LogState state) {
			// LOG_TABLE ( LOG_ID, LOG_TIME, LOG_SeqNo,LOG_TranCode,DstNo);
			Vector<LogTransactionItem> logItems = null;
			logItems = getDba().getLogItem(state);
			LogTransactionItem log = null;
			Vector<Item> showItems = new Vector<Item>();

			if (logItems != null && logItems.size() > 0) {

				StringBuffer sBuf;
				String dstLabel = "";
				String icon = "icon_notice";
				String errMsg = "";
				for (int i = 0; i < logItems.size(); ++i) {
					try {
						log = logItems.elementAt(i);
						sBuf = new StringBuffer();

						sBuf.append(getTransactionName(log.getTranCommand(), 1));
						sBuf.append("\n");

						sBuf.append(getString(R.string.seqno));
						sBuf.append(": ");
						sBuf.append(log.getSeqNo());
						sBuf.append("\n");

						if (!log.getDestinationNo().equals("")) {
							// Destination
							dstLabel = getTransactionDetail(log);
							if (!dstLabel.equals("")) {
								sBuf.append(dstLabel);
							}
						}

						if (!log.getAmount().equals("")) {
							// Amount
							sBuf.append(getString(R.string.amount).replace(
									Config.FIELD_REQUIRED, ""));
							sBuf.append(": ");
							sBuf.append(Util.formatNumbersAsTien(log
									.getAmount()));
							sBuf.append("\n");
						}

						if (!log.getDescription().equals("")) {
							// Description
							sBuf.append(getString(R.string.noi_dung));
							sBuf.append(": ");
							sBuf.append(log.getDescription());
							sBuf.append("\n");
						}
						if(!log.getTranCommand().equals(Config.M_HOA_DON_THANH_TOAN)){
							sBuf.append(getString(R.string.ket_qua));
							sBuf.append(": ");
							if (log.getResult().startsWith("val")) {
								icon = Config.ICON_SUCCESS;
								sBuf.append(getString(R.string.ket_qua_val));
							} else if (log.getResult().startsWith("err")) {
								icon = Config.ICON_ERROR;
								errMsg = Util.sCatVal(log.getResult());

								sBuf.append(getString(R.string.ket_qua_err));
								if (!errMsg.equals("")) {
									sBuf.append(" (");
									sBuf.append(errMsg);
									sBuf.append(")");
								}
							} else {
								icon = Config.ICON_NOTICE;
								sBuf.append(getString(R.string.ket_qua_no));
							}
						}

						Item item = new Item();
						item.setTitle(log.getLogTime());
						item.setDescription(sBuf.toString());
						item.setImage(icon);
						showItems.add(item);
					} catch (Exception ex) {
						MPlusLib.debug(TAG, "getTransactionLog", ex);
					}
				}
			}

			return showItems;
		}

		/**
		 * Get bill payment log items
		 * */
		private Vector<Item> getBillPaymentLog(LogState state) {
			Vector<Item> showItems = new Vector<Item>();
			Vector<LogBillItem> vecLogProduct = null;
			vecLogProduct = getDba().getLogHoaDon();

			if (vecLogProduct != null && vecLogProduct.size() > 0) {
				for (int i = 0; i < vecLogProduct.size(); ++i) {
					try {
						String sNhaCC = "";
						int sTypeHoaDon = 0;
						// String arrHoaDon[];

						LogBillItem log = (LogBillItem) vecLogProduct
								.elementAt(i);
						if (log != null) {
							sNhaCC = "";
							sTypeHoaDon = 0;
							// arrHoaDon = log.getBillCode().split("\\.");
							sNhaCC = log.getSupplierId();

							Item supplier = getDba().getItem(
									log.getSupplierId(),
									DBAdapter.DB_GROUP_TYPE_BILLPAYMENT);
							if (supplier != null) {
								sNhaCC = supplier.getTitle();
								sTypeHoaDon = supplier.getSupplyType();
							}

							StringBuffer sBuf = new StringBuffer();
							sBuf.append("   ");
							sBuf.append(getString(R.string.biller));
							sBuf.append(": ");
							sBuf.append(sNhaCC);

							if (sTypeHoaDon == DBAdapter.DB_SUPPLIER_TYPE_CUSTOM_CODE) {
								sBuf.append("\n   ");
								sBuf.append(getString(R.string.ma_khach_hang));
								sBuf.append(": ");
								sBuf.append(log.getBillCode());
							} else if (sTypeHoaDon == DBAdapter.DB_SUPPLIER_TYPE_BILL_CODE) {
								sBuf.append("\n   ");
								sBuf.append(getString(R.string.so_hoa_don));
								sBuf.append(": ");
								sBuf.append(log.getBillCode());
							} else {
								sBuf.append("\n   ");
								sBuf.append(getString(R.string.ma_dat_cho));
								sBuf.append(": ");
								sBuf.append(log.getBillCode());
							}

							sBuf.append("\n   ");
							sBuf.append(getString(R.string.seqno));
							sBuf.append(": ");
							sBuf.append(log.getSeqNo());

							sBuf.append("\n   ");
							sBuf.append(getString(R.string.amount));
							sBuf.append(": ");
							sBuf.append(Util.formatNumbersAsTien(log
									.getAmount()));

							sBuf.append("\n   ");
							sBuf.append(getString(R.string.noi_dung));
							sBuf.append(": ");
							sBuf.append(log.getDescription());

							String sKetQua = "";
							String icon = "icon_notice";
							if (log.getResult().length() < 3) {
								icon = Config.ICON_NOTICE;
								sKetQua = getString(R.string.ket_qua_no);
							} else if (log.getResult().startsWith("err:")) {
								icon = Config.ICON_ERROR;
								sKetQua = getString(R.string.ket_qua_err)
										+ " (" + Util.sCatVal(log.getResult())
										+ ")";
							} else if (log.getResult().startsWith("val")) {
								icon = Config.ICON_SUCCESS;
								sKetQua = getString(R.string.ket_qua_val);
							} else {
								icon = Config.ICON_NOTICE;
								sKetQua = log.getResult();
							}

							sBuf.append("\n   ");
							sBuf.append(getString(R.string.ket_qua));
							sBuf.append(": ");
							sBuf.append(sKetQua);

							if ((state == LogState.ALL)
									|| (state == LogState.SUCCESS && icon
											.equals(Config.ICON_SUCCESS))
									|| (state == LogState.ERROR && icon
											.equals(Config.ICON_ERROR))
									|| (state == LogState.PENDING && icon
											.equals(Config.ICON_NOTICE))) {
								Item item = new Item();
								item.setTitle(log.getLogTime());
								item.setDescription(sBuf.toString());
								item.setImage(icon);
								showItems.add(item);
							}
						}
					} catch (Exception ex) {
						MPlusLib.debug(TAG, "getBillPaymentLog", ex);
					}
				}
			}
			return showItems;
		}

		private String getString(int resId) {
			return getContext().getString(resId);
		}
	}

	public static class LogListFragment extends BaseListFragment {
		public final String TAG = "LogListFragment";

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}

		@Override
		protected void setControls() {
			// Give some text to display if there is no data. In a real
			// application this would come from a resource.
			setEmptyText(getString(R.string.msg_notfound));

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			// Create an empty adapter we will use to display the loaded data.
			setAdapder(new LogListAdapter(getActivity()));
			setListAdapter(getAdapder());

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, getBundle(), this);
		};

		@Override
		public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
			return new LogListLoader(getActivity(), args);
		}

		public static LogListFragment newInstance(Bundle args) {
			LogListFragment instance = new LogListFragment();
			instance.setBundle(args);
			return instance;
		}
	}
}
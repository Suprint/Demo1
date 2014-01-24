package com.mpay.plus.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.mplus.dialog.DialogConfirmMessage;
import com.mpay.mplus.dialog.DialogFrmConfirm;
import com.mpay.plus.baseconnect.MPConnection;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.LogBillItem;
import com.mpay.plus.database.LogTransactionItem;
import com.mpay.plus.database.User;
import com.mpay.plus.imart.FrmPurcharsedProduct;
import com.mpay.plus.imart.ViewTitlePaper;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.system.FrmMenuLog.LogState;
import com.mpay.plus.system.FrmMenuLog.LogType;
import com.mpay.plus.util.Util;
import com.mpay.ui.base.BaseListAdapter;
import com.mpay.ui.base.BaseListFragment;
import com.mpay.ui.base.BaseListLoader;

/**
 * Hien thi log giao dich
 * 
 * @author quyenlm.vn@gmail.com
 * */
public class FrmTransactionLog_old extends AMPlusCore {
//	private TabHost mTabHost;
	
//	private TitlePageIndicator indicator;
//	private ViewPager mViewPager;
//	private LogTabsAdapter mTabsAdapter;
	private LogType iType = LogType.TRANSACTION;
	public static byte iTag = iCHUYEN_KHOAN;

    private ViewPager mPager;    
    private ViewTitlePaper mIndicator;
    private LinearLayout titleTab;
    private boolean isBill = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_fragment_view_pager);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);

		try {
			iType = (LogType) getIntent().getExtras().get("log_type");
		} catch (Exception e) {
		}

		try {
			if (iType == LogType.BILL_PAYMENT){
				this.setTitle(getString(R.string.bill_payment_log));
				isBill = true;
			}else {
				isBill = false;
			}
			 
				titleTab = (LinearLayout) findViewById(R.id.title);
				mPager = (ViewPager)findViewById(R.id.pager);
			    mPager.setAdapter(new PaperAdapter(getSupportFragmentManager()));
			        
		        mIndicator = (ViewTitlePaper)findViewById(R.id.indicator);
		        mIndicator.setViewPager(mPager);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "onCreate", ex);
		}
	}
	
	class PaperAdapter extends FragmentPagerAdapter{

//		private String title[] = {"Tất cả", "Thành công", "Lỗi", "Chưa rõ"};
		
		private String title[] = getResources().getStringArray(R.array.list_title_tab);
		
		public PaperAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Bundle bundle = new Bundle();
			if(isBill){
				bundle.putSerializable("log_type", iType);
				bundle.putSerializable("log_state", LogState.SUCCESS);
			}else {
				if(arg0 == 0){
					bundle.putSerializable("log_type", iType);
					bundle.putSerializable("log_state", LogState.ALL);
				}if( arg0 == 1){
					bundle.putSerializable("log_type", iType);
					bundle.putSerializable("log_state", LogState.SUCCESS);
				}if(arg0 == 2){
					bundle.putSerializable("log_type", iType);
					bundle.putSerializable("log_state", LogState.ERROR);
				}if(arg0 == 3){
					bundle.putSerializable("log_type", iType);
					bundle.putSerializable("log_state", LogState.PENDING);
				}
			}
			
			return LogListFragment.newInstance(bundle);
		}

		@Override
		public int getCount() {
			if(isBill){
				titleTab.setVisibility(ViewTitlePaper.GONE);
				return 1;
			}else {
				titleTab.setVisibility(ViewTitlePaper.VISIBLE);
				return title.length;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if(isBill){
				return title[1];
			}else {
				return title[position];
			}
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
				break;

			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else
			result = false;

		return result;
	};
	
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
	
		private Vector<Item> getTransactionLog(LogState state) {
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

						if(!log.getTranCommand().equals("")){
							sBuf.append("<b>"+getTransactionName(log.getTranCommand(), 1));
							sBuf.append("</b><br/>");
						}
						
						if (!log.getDestinationNo().equals("")) {
							// Destination
							dstLabel = getTransactionDetail(log);
							if (!dstLabel.equals("")) {
								sBuf.append(dstLabel);
							}
						}
						
						if(!log.getAmount().equals("")){
							if(log.getmAmount_cost().equals("")){
								sBuf.append(getString(R.string.title_sotien_));
							}else {
								sBuf.append(getString(R.string.amount));
							}
							sBuf.append(": |<b>");
							sBuf.append(Util.formatNumbersAsTien(log.getAmount()));
							sBuf.append("|</b>");
						}
						if (log.getTranCommand().equals("0" + Config.M_IMART_THANH_TOAN)) {
							// mua hang imart show so luong the
							if(!log.getAddParam().equals("")){
								sBuf.append("<br/>");
								sBuf.append(getString(R.string.so_luong));
								sBuf.append(": |<b>");
								sBuf.append(log.getAddParam() + " " +  getString(R.string.cmd_card));
								sBuf.append("|</b>");
							}
						}

						if(!log.getmAmount_cost().equals("")){
							sBuf.append("<br/>");
							sBuf.append(getString(R.string.title_sotien_));
							sBuf.append(": |<b>");
							sBuf.append(Util.formatNumbersAsTien(log.getmAmount_cost()));
							sBuf.append("|</b>");
						}

						if(!log.getDescription().equals("")){
							sBuf.append("<br/>");
							sBuf.append(getString(R.string.thongtin));
							sBuf.append(": |<b>");
							sBuf.append(log.getDescription());
							sBuf.append("|</b>");
						}
						
						if(!log.getSeqNo().equals("")){
							sBuf.append("<br/>");
							sBuf.append(getString(R.string.seqno));
							sBuf.append(": |<b>");
							sBuf.append(log.getSeqNo());
							sBuf.append("|</b>");
						}
						sBuf.append("<br/>");
						sBuf.append(getString(R.string.ket_qua));
						sBuf.append(": |<b>");
						if (log.getResult().startsWith("val")) {
							icon = Config.ICON_SUCCESS;
							sBuf.append(getString(R.string.ket_qua_val));
						} else if (log.getResult().startsWith("err")) {
							icon = Config.ICON_ERROR;
							errMsg = Util.sCatVal(log.getResult());
	
							if (!errMsg.equals("")) {
								sBuf.append(errMsg);
							}
						} else {
							icon = Config.ICON_NOTICE;
							sBuf.append(getString(R.string.ket_qua_no));
						}

						Item item = new Item();
						item.setTitle( Util.getTimeClient4(log.getLogTime()));
						item.setDescription(sBuf.toString());
						item.setImage(icon);
						item.setmResult(log.getResult()+","+log.getSeqNo());
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
							sBuf.append(": <b>");
							sBuf.append(sNhaCC+"</b>");

							if (sTypeHoaDon == DBAdapter.DB_SUPPLIER_TYPE_CUSTOM_CODE) {
								sBuf.append("<br/>");
								sBuf.append(getString(R.string.ma_khach_hang));
								sBuf.append(": <b>");
								sBuf.append(log.getBillCode()+"</b>");
							} else if (sTypeHoaDon == DBAdapter.DB_SUPPLIER_TYPE_BILL_CODE) {
								sBuf.append("<br/>");
								sBuf.append(getString(R.string.so_hoa_don));
								sBuf.append(": <b>");
								sBuf.append(log.getBillCode()+"</b>");
							} else {
								sBuf.append("<br/>");
								sBuf.append(getString(R.string.ma_dat_cho));
								sBuf.append(": <b>");
								sBuf.append(log.getBillCode()+"</b>");
							}

							sBuf.append("<br/>");
							sBuf.append(getString(R.string.title_menhgia_));
							sBuf.append(": <b>");
							sBuf.append(Util.formatNumbersAsTien(log
									.getAmount())+"</b>");

							sBuf.append("<br/>");
							sBuf.append(getString(R.string.noi_dung));
							sBuf.append(": <b>");
							sBuf.append(log.getDescription()+"</b>");

							sBuf.append("<br/>");
							sBuf.append(getString(R.string.seqno));
							sBuf.append(": <b>");
							sBuf.append(log.getSeqNo()+"</b>");
							
							String icon = "icon_notice";
							if (log.getResult().length() < 3) {
								icon = Config.ICON_NOTICE;
							} else if (log.getResult().startsWith("err:")) {
								icon = Config.ICON_ERROR;
							} else if (log.getResult().startsWith("val")) {
								icon = Config.ICON_SUCCESS;
							} else {
								icon = Config.ICON_NOTICE;
							}
							if ((state == LogState.ALL)
									|| (state == LogState.SUCCESS && icon
											.equals(Config.ICON_SUCCESS))
									|| (state == LogState.ERROR && icon
											.equals(Config.ICON_ERROR))
									|| (state == LogState.PENDING && icon
											.equals(Config.ICON_NOTICE))) {
								Item item = new Item();
								item.setTitle( Util.getTimeClient4(log.getLogTime()));
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
	public static class LogListAdapter extends BaseListAdapter implements IProcess{
		private AMPlusCore context;
		private String sSequen = "";
		private String sCommand = "";
		private boolean isFinished = false;
		private LogTransactionItem logItemTran = null;
		public LogListAdapter(Context context) {
			super(context);
			this.context = (AMPlusCore) context;
		}

		/**
		 * Populate new items in the list.
		 */
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;

			if (convertView == null) {
				view = super.mInflater.inflate(R.layout.old_list_item_log, parent,
						false);
			} else {
				view = convertView;
			}

			final Item item = getItem(position);
			((ImageView) view.findViewById(R.id.image)).setImageDrawable(Util
					.getImage(getContext(), item.getImage()));
			((TextView) view.findViewById(R.id.title)).setText(Html.fromHtml(item.getTitle()));
			((TextView) view.findViewById(R.id.description)).setText(Html.fromHtml(conVertDes(item.getDescription())));
			
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Item item = getItem(position);
					
					if(!item.getmResult().equals("")){
						String arr[] = item.getmResult().split(",", -1);
						sSequen = arr[1];
						if(arr[0].equals("")){
							logItemTran = getDba().getLogPending(sSequen);
							if(logItemTran != null){
								sCommand = logItemTran.getTranCommand();
								sThongBao = conVertDes(item.getDescription()+"<br/>");
								getTag(sCommand.split("\\|", -1)[0], item.getDescription());
								onCreateMyDiaLog_(XAC_NHAN).show();
							}
						}
					}
				}
			});

			return view;
		}
		
		private String conVertDes(String des) {
			return des.indexOf("|") >= 0 ? des.replace("|", "") : des;
		}
		
		private String catHTML(String data) {
			String result = data;
			if(result.indexOf("</b>") >= 0){
				result = result.replace("</b>", "");
			}if(result.indexOf("<br/>") >= 0){
				result = result.replace("<br/>", "");
			}if(result.indexOf("<b>") >= 0){
				result = result.replace("<b>", "");
			}
			return result;
		}
		
		private void getTag(String tag, String des) {
			String []arrDes = catHTML(des).split("\\|", -1);
//			for (int i = 0; i < arrDes.length; i++) {
//				Log.i("Log : ", "arr["+i+"] : "+arrDes[i]);
//			}
			if (tag.equals("0" + Config.M_IMART_THANH_TOAN)) {
				iTag = iMUA_HANG;
				logItemTran.setAmount(arrDes[3]);
				logItemTran.setAddParam(arrDes[5].replace(context.getString(R.string.cmd_card), ""));
				logItemTran.setDescription(arrDes[9]);
			} else if (tag.equals("1" + Config.M_IMART_THANH_TOAN)) {
				iTag = iTOPUP_TRA_TRUOC;
				logItemTran.setAddParam(arrDes[3]);
				logItemTran.setAmount(arrDes[5]);
			} else if (tag.equals("2" + Config.M_IMART_THANH_TOAN)) {
				iTag = iTOPUP_TRA_SAU;
				logItemTran.setAddParam(arrDes[3]);
				logItemTran.setAmount(arrDes[5]);
			} else if (tag.equals("3" + Config.M_IMART_THANH_TOAN)) {
				iTag = iTOPUP_GAME;
				logItemTran.setAddParam(arrDes[3]);
				logItemTran.setAmount(arrDes[5]);
			} else if (tag.equals(Config.M_HOA_DON_THANH_TOAN)) {
				iTag = iTHANH_TOAN_HOA_DON;
				logItemTran.setDestinationNo(arrDes[3]);
				logItemTran.setDescription(arrDes[1]);
				logItemTran.setAmount(arrDes[7]);
			} else if (tag.equals(Config.A_CK_AGENT)) {
				iTag = iCHUYEN_KHOAN;
			} else if (tag.equals(Config.A_NAP_TIEN_TU_TK_BANK)) {
				iTag = iNAP_TIEN_AGENT;
			} else if (tag.equals(Config.A_RUT_TIEN_VE_TK_BANK)) {
				iTag = iRUT_TIEN_AGENT;
			}
		}
		
		private void cmdNextXacNhan() {
			if(isFinished){
				context.startActivity(new Intent(context, FrmPurcharsedProduct.class));
				context.finish();
			}else {
				threadThucThi = new ThreadThucThi(context);
				threadThucThi.setIProcess(LogListAdapter.this, iTag);
				threadThucThi.Start();
			}
		}

		@Override
		public void processDataSend(byte iTag) {
			switch (iTag) {
				case iRUT_TIEN_AGENT:
				case iNAP_TIEN_AGENT:
				case iTHANH_TOAN_HOA_DON:
				case iTOPUP_TRA_TRUOC:
				case iTOPUP_TRA_SAU:
				case iTOPUP_GAME:
				case iCHUYEN_KHOAN:
				case iMUA_HANG:
					ServiceCore.TaskCheckPending(sSequen, sCommand);
					break;
	
				default:
					break;
			}
		}

		@Override
		public void processDataReceived(String dataReceived, byte iTag,
				byte iTagErr) {
			sThongBao = "";
			String sKetQua = "val";
			
			// gia lap du lieu
//			dataReceived = "val:66322455|3BVNP-9999-1077266!417/02/2015!1BVN1077266!(18001090)";
			
			if (dataReceived.startsWith("val")) {
				sKetQua = "val";
				User.isActived = true;
			} else if (dataReceived.startsWith("pen")
					|| (iTagErr >= MPConnection.ERROR_NOT_DECODE_DATA && iTagErr <= MPConnection.ERROR_RECEIVING_INTERUPT))
				sKetQua = "";
			else
				sKetQua = dataReceived;
			if (!sKetQua.equals("")) {
				deleteLogPending(sSequen);
				saveLogTransaction("", sSequen, "", "", "", sKetQua, "", "", "");
				isFinished = true;
			}
			saveUserTable();

			String sTime = "";
			try {
				sTime = Util.getTimeClient3(logItemTran.getLogTime());
			} catch (Exception ex) {
			}

			switch (iTag) {
			case iCHUYEN_KHOAN:
				Util.insertString(context.getString(R.string.pendding_chuyen_khoan),
						new String[] { Util.formatNumbersAsTien(logItemTran.getAmount()),
					logItemTran.getAddParam(), " "+sTime });
				break;

			case iTHANH_TOAN_HOA_DON:
				// Update Log Bill Payment Result
				LogBillItem logBill = new LogBillItem();
				logBill.setLogTime("");
				logBill.setSeqNo(sSequen);
				logBill.setAmount("");
				logBill.setDescription("");
				logBill.setResult(sKetQua.equals("err") ? dataReceived : sKetQua);
				
				String[] bill = TextUtils.split(logItemTran.getDestinationNo(), "\\.");
				if(bill.length > 1){
					logBill.setSupplierId(bill[0]);
					logBill.setBillCode(bill[1]);
				} else logBill.setBillCode(logItemTran.getDestinationNo());
				
				getDba().insertOrUpdateBillLog(logBill);

				sThongBao = Util.insertString(
						context.getString(R.string.pendding_tt_hoa_don),
						new String[] { logItemTran.getDestinationNo(), logItemTran.getDescription(),
							logItemTran.getAmount(), " "+sTime });
				break;

			case iMUA_HANG:
				sThongBao = Util.insertString(
						context.getString(R.string.pendding_imart),
						new String[] { logItemTran.getAddParam(), logItemTran.getDescription(),
								logItemTran.getAmount(), " "+sTime });
				break;

			case iTOPUP_TRA_TRUOC:
				sThongBao = Util.insertString(context.getString(R.string.pendding_topup),
						new String[] { logItemTran.getAmount(),
					logItemTran.getAddParam(), " "+sTime });
				break;

			case iTOPUP_GAME:

				sThongBao = Util.insertString(
						context.getString(R.string.pendding_topup_game), new String[] {
								logItemTran.getAmount(), logItemTran.getAddParam(),
								" "+sTime });
				break;

			case iTOPUP_TRA_SAU:
				sThongBao = Util.insertString(
						context.getString(R.string.pendding_topup_ts),
						new String[] { logItemTran.getAmount(),
							logItemTran.getAddParam(), " "+sTime });
				break;

			case iNAP_TIEN_AGENT:
				Util.insertString(
						context.getString(R.string.pendding_nap_tien_agent),
						new String[] { logItemTran.getDescription(), " "+sTime });
				break;

			case iRUT_TIEN_AGENT:
				Util.insertString(
						context.getString(R.string.pendding_rut_tien_agent),
						new String[] { logItemTran.getAmount(), " "+sTime });
				break;

			}

			if (dataReceived.startsWith("val:")) {
				
				dataReceived = Util.sCatVal(dataReceived);
				// String arrTemp[];
				switch (iTag) {
				case iRUT_TIEN_AGENT:
				case iNAP_TIEN_AGENT:
				case iTHANH_TOAN_HOA_DON:
				case iTOPUP_TRA_TRUOC:
				case iTOPUP_TRA_SAU:
				case iTOPUP_GAME:
				case iCHUYEN_KHOAN:
					sThongBao += "<br/><br/>" + context.getString(R.string.pend_ketqua_thanhcong);
					onCreateMyDiaLog_(THONG_BAO).show();
					break;
				case iMUA_HANG:
//					sThongBao = Util.insertString(context.getString(R.string.thongbao_nhieuthe), new String[]{logItemTran.getAddParam(), logItemTran.getDescription(), Util.formatNumbersAsTien( logItemTran.getAmount())+" "+ context.getString(R.string.vnd), sTime});
					sThongBao += "<br/><br/>" + context.getString(R.string.pend_ketqua_thanhcong);
					String data = Util.sCatVal(dataReceived);
					isFinished = true;
					AMPlusCore.parseProductFromIMart(logItemTran.getDestinationNo(), logItemTran.getAmount(), data);
					onCreateMyDiaLog_(XAC_NHAN_PENDING).show();
					break;
				}
			} else if (dataReceived.startsWith("pen:")) {
				// show Confirm Pedding
				sThongBao += "<br/><br/>" + context.getString(R.string.pend_ketqua_pend);
				onCreateMyDiaLog_(THONG_BAO).show();
			} else {
				// err
				sThongBao += "<br/><br/>" + context.getString(R.string.pend_ketqua_err) + " "
						+ Util.sCatVal(dataReceived);
				onCreateMyDiaLog_(THONG_BAO).show();
			}
		}

		private Dialog onCreateMyDiaLog_(int tag) {
			switch (tag) {
			case THONG_BAO:
				
				DialogConfirmMessage dialogConfirmMessage = new DialogConfirmMessage(context);
				dialogConfirmMessage.setMyMessage(sThongBao);
				dialogConfirmMessage.setDongy(context.getString(R.string.cmd_ok));
				dialogConfirmMessage.setOnClickDialogConfirm(new DialogConfirmMessage.onClickDialogConfirm() {
					
					@Override
					public void onclickKhongDongY() {
					}
					
					@Override
					public void onclickDongY() {
						sThongBao = "";
						cmdNextThongBao();
					}
				});
				
				return dialogConfirmMessage;
			case XAC_NHAN:

				DialogFrmConfirm dialog = new DialogFrmConfirm(context);
				dialog.setIProcess(LogListAdapter.this);
				dialog.setParent(context);
				dialog.setTitles(context.getString(R.string.title_xacnhangiaodich));
				dialog.setMyMessage(context.getString(R.string.title_kiemtraketqua));
				dialog.setMyMessage1(sThongBao);
				dialog.setBtn_dongy(context.getString(R.string.btn_dongy));
				dialog.setBtn_khongdongy(context.getString(R.string.btn_huy));
				dialog.setInitMpin(false);
				dialog.setClickDialogConfirm_Dongy_(new DialogFrmConfirm.onClickDialogConfirm_Dongy_() {
					
					@Override
					public void onClickDonY_(String data) {
						cmdNextXacNhan();
					}
				});
				dialog.setClickDialogConfirm_Khongdongy(new DialogFrmConfirm.onClickDialogConfirm_Khongdongy() {
					
					@Override
					public void onClickKhongDongY() {
						
					}
				});

				return dialog;
			case XAC_NHAN_PENDING:

				DialogFrmConfirm dialog1 = new DialogFrmConfirm(context);
				
				dialog1.setIProcess(LogListAdapter.this);
				dialog1.setTitles(context.getString(R.string.title_ketquagiaodich));
				dialog1.setOther(sThongBao);
				dialog1.setBtn_dongy(context.getString(R.string.btn_vaokhothe));
				dialog1.setBtn_khongdongy(context.getString(R.string.btn_ketthuc));
				dialog1.setMPinEnable(false);
				dialog1.setClickDialogConfirm_Dongy(new DialogFrmConfirm.onClickDialogConfirm_Dongy() {
					
					@Override
					public void onClickDonY() {
						cmdNextXacNhan();
					}
				});
				dialog1.setClickDialogConfirm_Khongdongy(new DialogFrmConfirm.onClickDialogConfirm_Khongdongy() {
					
					@Override
					public void onClickKhongDongY() {
						
					}
				});

				return dialog1;
			default:
				break;
			}
			return null;

		}
		
		private void cmdNextThongBao() {
			context.startActivity(new Intent(context, FrmTransactionLog_old.class));
			context.finish();
		}
		
		@Override
		public void processErrConnect(String sErr, byte iTagErr) {
			
		}
	}
}
package com.mpay.plus.database;

import java.util.UUID;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mpay.plus.config.Config;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.system.FrmMenuLog.LogState;
import com.mpay.plus.util.Util;

/**
 * 
 * @author THANHNAM
 * @author quyenlm.vn@gmail.com
 */

public class DBAdapter {
	private final String TAG = "DBAdapter";
	// DATABASE_VERSION=1, tuong ung voi cac phien ban M-Plus < 3.8
	// DATABASE_VERSION=2, tuong ung voi cac phien ban M-Plus = 3.8
	// DATABASE_VERSION=3, tuong ung voi cac phien ban M-Plus >= 3.8
	// DATABASE_VERSION=4, tuong ung voi cac phien ban M-Plus >= 4.0

	private static int DATABASE_VERSION = 4;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqlDb;
	private Context mContext;
	public static final String DATABASE_NAME = "DATABASEUSER";

	// Oldversion
	private static final String USER_TABLE = "USER_TABLE";
	private static final String THAM_SO_TABLE = "THAM_SO";
	private static final String MLOG_TABLE = "MLOG";

	// Bang NguoiDung
	// USER_MID, USER_ACCLIST, USER_PRKEY, USER_THAMSO, USER_VERSION_LINK,
	// USER_NEWS, USER_MENUCARD, USER_MENUTOPUP, USER_MENUMORE
	private static final String TBL_NGUOIDUNG = "TBL_NGUOIDUNG";
	private static final String USER_MID = "USER_MID";
	private static final String USER_ACCLIST = "USER_ACCLIST";
	private static final String USER_PRKEY = "USER_PRKEY";
	private static final String USER_THAMSO = "USER_THAMSO";
	private static final String USER_VERSION_LINK = "USER_VERSION_LINK";
	private static final String USER_NEWS = "USER_NEWS";
	private static final String USER_MENUCARD = "USER_MENUCARD";
	private static final String USER_MENUTOPUP = "USER_MENUTOPUP";
	private static final String USER_MENUMORE = "USER_MENUMORE";

	// Bang MLog_habit
	private static final String TBL_MLOG_HABIT = "TBL_MLOG_HABIT";
	private static final String MLOG_HABIT_ID = "MLOG_HABIT_ID";
	private static final String MLOG_HABIT_HABIT = "MLOG_HABIT_HABIT";

	// Dinh nghia cac bang
	private static final String TABLE_GROUP = "mp_group";
	private static final String TABLE_ITEM = "mp_item";
	private static final String TABLE_NEWS = "mp_news";
	private static final String TABLE_PRODUCT = "mp_product";
	private static final String TABLE_TRAN_LOG1 = "mp_tran_log";
	private static final String TABLE_BILL_LOG = "mp_bill_log";
	private static final String TABLE_PENDING_LOG = "mp_pending_log";
	private static final String TABLE_QUESTION_KENH = "mp_question_kenh_log";
	
	// Bang Question - Kenh
	private static final String QK_ID = "_id";
	private static final String QK_sID = "qk_sid";
	private static final String QK_sCONTENT = "qk_content";
	private static final String QK_sDESCRIPTION = "qk_description";
	private static final String QK_sLINK = "qk_link";
	private static final String QK_sTYPE = "qk_type";
	private static final String QK_isACTIVE = "qk_isactive";
	

	// Bang Item
	private static final String ITEM_ID = "_id";
	private static final String ITEM_ITEM_ID = "item_id";
	private static final String ITEM_TITLE = "title";
	private static final String ITEM_DESC = "description";
	private static final String ITEM_IMG = "image";
	private static final String ITEM_LINK = "link";
	private static final String ITEM_HELP = "help_content";
	private static final String ITEM_ADDPARAM = "add_param";
	private static final String ITEM_SAVE_TYPE = "save_type";
	private static final String ITEM_SUPPLY_TYPE = "supply_type";
	private static final String ITEM_INDEX = "_index";
	private static final String ITEM_ISACTIVED = "is_actived";
	private static final String ITEM_GROUP_ID = "group_id";
	private static final String ITEM_GROUP_TYPE = "group_type";

	// Bang group
	private static final String GROUP_ID = "_id";
	private static final String GROUP_GROUP_ID = "group_id";
	private static final String GROUP_TITLE = "title";
	private static final String GROUP_DESC = "description";
	private static final String GROUP_IMG = "image";
	private static final String GROUP_LINK = "link";
	private static final String GROUP_ADDPARAM = "add_param";
	private static final String GROUP_INDEX = "_index";
	private static final String GROUP_ISACTIVED = "is_actived";
	private static final String GROUP_GROUP_TYPE = "group_type";

	// Bang news
	private static final String NEWS_ID = "_id";
	private static final String NEWS_NEWS_ID = "news_id";
	private static final String NEWS_TITLE = "title";
	private static final String NEWS_DESC = "description";
	private static final String NEWS_IMG = "image";
	private static final String NEWS_LINK = "link";
	private static final String NEWS_ADDPARAM = "add_param";
	private static final String NEWS_INDEX = "_index";
	private static final String NEWS_ISACTIVED = "is_actived";
	private static final String NEWS_GROUP_ID = "group_id";
	private static final String NEWS_GROUP_TYPE = "group_type";

	// DB GROUP
	public static final int DB_GROUP_TYPE_BANK = 6; // Nhom ngan hang
	public static final int DB_GROUP_TYPE_BILLPAYMENT = 7; // Nhom thanh toan
	public static final int DB_GROUP_TYPE_PRODUCT = 8; // Nhom san pham
	public static final int DB_GROUP_TYPE_TOPUP = 9; // Nhom topup

	public static final int DB_GROUP_TYPE_NEWS = 20; // Nhom Tin tuc
	public static final int DB_GROUP_TYPE_ADVERDS = 21; // Nhom Quang cao
	public static final int DB_GROUP_TYPE_QUESTION = 22; // Nhom Question
	public static final int DB_GROUP_TYPE_KENH = 23; // Nhom kenh

	public static final int DB_GROUP_TYPE_ALL = 30; // Nhom giao dich loi
	public static final int DB_GROUP_TYPE_OK = 31; // Nhom giao dich loi
	public static final int DB_GROUP_TYPE_ERROR = 32; // Nhom giao dich loi
	public static final int DB_GROUP_TYPE_PENDING = 33; // Nhom giao dich loi

	// Bảng Log Transaction
	private static final String LOG_ID = "_id";
	private static final String LOG_TIME = "time";
	private static final String LOG_SEQNO = "seqno";
	private static final String LOG_TRANCODE = "tranCode";
	private static final String LOG_DSTNO = "dstno";
	private static final String LOG_DESC = "description";
	private static final String LOG_RESULT = "result";
	private static final String LOG_AMOUNT = "amount";
	private static final String LOG_ADDPARAM = "add_param";
	private static final String LOG_AMOUNT_COST = "amount_cost";

	// Bang Log Product
	private static final String PRODUCT_ID = "_id";
	private static final String PRODUCT_TIME = "time";
	private static final String PRODUCT_PRODUCT = "product";
	private static final String PRODUCT_PRICE = "price";
	private static final String PRODUCT_SERI = "seri";
	private static final String PRODUCT_ACCNO = "accno";
	private static final String PRODUCT_PINCODE = "pincode";
	private static final String PRODUCT_EXDATE = "exdate";
	private static final String PRODUCT_HOTLINE = "hotline";
	private static final String PRODUCT_USED = "is_used";
	private static final String PRODUCT_ADDPARAM = "add_param";

	// Bang Log Bill Payment
	private static final String BILL_ID = "_id";
	private static final String BILL_TIME = "time";
	private static final String BILL_SEQNO = "seqno";
	private static final String BILL_AMOUNT = "amount";
	private static final String BILL_BILL_CODE = "bill_code";
	private static final String BILL_DESC = "description";
	private static final String BILL_RESULT = "result";
	private static final String BILL_ADDPARAM = "add_param";
	private static final String BILL_SUPPLIER_ID = "supplier_id";

	// Bang Log Pending
	private static final String LOG_PENDING_ID = "_id";
	private static final String LOG_PENDING_TIME = "time";
	private static final String LOG_PENDING_SEQNO = "seqno";
	private static final String LOG_PENDING_COMMAND = "command";
	private static final String LOG_PENDING_ADDPARAM = "add_param";

	// DB SUPPLIER TYPE
	public static final int DB_SUPPLIER_TYPE_CUSTOM_CODE = 0; // Ma khach hang
	public static final int DB_SUPPLIER_TYPE_BILL_CODE = 1; // Ma bill
	public static final int DB_SUPPLIER_TYPE_BOOKING_CODE = 2; // Ma dat cho

	// DB RECORD STATE
	public static final int DB_IS_ACTIVED = 1; // Duoc kich hoat
	public static final int DB_IS_NOT_ACTIVED = 0; // Khong duoc kich hoat

	private static class DatabaseHelper extends SQLiteOpenHelper {
		// public static final String TAG = "DatabaseHelper";

		public DatabaseHelper(Context context, String name,
				SQLiteDatabase.CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(" CREATE TABLE " + TBL_NGUOIDUNG + " (" + USER_MID
					+ " TEXT, " + USER_ACCLIST + " TEXT, " + USER_PRKEY
					+ " TEXT, " + USER_THAMSO + " TEXT," + USER_VERSION_LINK
					+ " TEXT, " + USER_NEWS + " TEXT, " + USER_MENUCARD
					+ " TEXT, " + USER_MENUTOPUP + " TEXT, " + USER_MENUMORE
					+ " TEXT" + ")");

			db.execSQL(" CREATE TABLE " + TBL_MLOG_HABIT + " ( "
					+ MLOG_HABIT_ID + " TEXT, " + MLOG_HABIT_HABIT + " TEXT);");

			// Thuc hien lenh tao bang Log Transaction
			// db.execSQL(" CREATE TABLE " + TABLE_TRAN_LOG + " ( " + LOG_ID +
			// " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOG_TIME + " TEXT, " +
			// LOG_SEQNO + " TEXT, " + LOG_TRANCODE + " TEXT, " + LOG_DSTNO +
			// " TEXT);");
			StringBuilder cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_TRAN_LOG1 + " ( ");
			cmd.append(LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(LOG_TIME + " TEXT, ");
			cmd.append(LOG_SEQNO + " TEXT, ");
			cmd.append(LOG_TRANCODE + " TEXT, ");
			cmd.append(LOG_DSTNO + " TEXT, ");
			cmd.append(LOG_DESC + " TEXT, ");
			cmd.append(LOG_RESULT + " TEXT, ");
			cmd.append(LOG_AMOUNT + " TEXT, ");
			cmd.append(LOG_ADDPARAM + " TEXT, ");
			cmd.append(LOG_AMOUNT_COST + " TEXT );");
			db.execSQL(cmd.toString());

			// Thuc hien lenh tao bang Log Product
			// db.execSQL(" CREATE TABLE LOGBUYCARD ( ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT, PRODUCT TEXT, MONEY TEXT, SERI TEXT, ACCNO TEXT, PINCODE TEXT, EXDATE TEXT, HOTLINE TEXT, USED INTEGER, PARAM TEXT );");
			cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_PRODUCT + " ( ");
			cmd.append(PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(PRODUCT_TIME + " TEXT, ");
			cmd.append(PRODUCT_PRODUCT + " TEXT, ");
			cmd.append(PRODUCT_PRICE + " TEXT, ");
			cmd.append(PRODUCT_SERI + " TEXT, ");
			cmd.append(PRODUCT_ACCNO + " TEXT, ");
			cmd.append(PRODUCT_PINCODE + " TEXT, ");
			cmd.append(PRODUCT_EXDATE + " TEXT, ");
			cmd.append(PRODUCT_HOTLINE + " TEXT, ");
			cmd.append(PRODUCT_USED + " INTEGER, ");
			cmd.append(PRODUCT_ADDPARAM + " TEXT );");
			db.execSQL(cmd.toString());

			// Thuc hien lenh tao bang Log Bill Payment
			// db.execSQL(" CREATE TABLE LOG_HOADON ( ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT, SeqNo TEXT, SoTien TEXT, MaHoaDon TEXT, NoiDung Text, KetQua TEXT);");
			cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_BILL_LOG + " ( ");
			cmd.append(BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(BILL_TIME + " TEXT, ");
			cmd.append(BILL_SEQNO + " TEXT, ");
			cmd.append(BILL_AMOUNT + " TEXT, ");
			cmd.append(BILL_BILL_CODE + " TEXT, ");
			cmd.append(BILL_DESC + " TEXT, ");
			cmd.append(BILL_RESULT + " TEXT, ");
			cmd.append(BILL_SUPPLIER_ID + " TEXT, ");
			cmd.append(BILL_ADDPARAM + " TEXT );");
			db.execSQL(cmd.toString());

			// Thuc hien lenh tao bang Log pending
			// db.execSQL(" CREATE TABLE LOG_PENDING ( ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT, SeqNo TEXT, LENH TEXT);");
			cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_PENDING_LOG + " ( ");
			cmd.append(LOG_PENDING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(LOG_PENDING_TIME + " TEXT, ");
			cmd.append(LOG_PENDING_SEQNO + " TEXT, ");
			cmd.append(LOG_PENDING_COMMAND + " TEXT, ");
			cmd.append(LOG_PENDING_ADDPARAM + " TEXT );");
			db.execSQL(cmd.toString());

			onCreateV4(db);
		}

		/**
		 * Tao bang version >= 4.0
		 * */
		public void onCreateV4(SQLiteDatabase db) {

			// Thuc hien lenh tao bang Group
			StringBuilder cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_GROUP + " ( ");
			cmd.append(GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(GROUP_GROUP_ID + " TEXT NOT NULL, ");
			cmd.append(GROUP_TITLE + " TEXT NOT NULL, ");
			cmd.append(GROUP_DESC + " TEXT, ");
			cmd.append(GROUP_IMG + " TEXT, ");
			cmd.append(GROUP_LINK + " TEXT, ");
			cmd.append(GROUP_ADDPARAM + " TEXT, ");
			cmd.append(GROUP_INDEX + " INTEGER, ");
			cmd.append(GROUP_GROUP_TYPE + " INTEGER, ");
			cmd.append(GROUP_ISACTIVED + " INTEGER );");
			db.execSQL(cmd.toString());

			// Thuc hien lenh tao bang Item
			cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_ITEM + " ( ");
			cmd.append(ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(ITEM_ITEM_ID + " TEXT NOT NULL, ");
			cmd.append(ITEM_TITLE + " TEXT NOT NULL, ");
			cmd.append(ITEM_DESC + " TEXT, ");
			cmd.append(ITEM_IMG + " TEXT, ");
			cmd.append(ITEM_LINK + " TEXT, ");
			cmd.append(ITEM_ADDPARAM + " TEXT, ");
			cmd.append(ITEM_INDEX + " INTEGER, ");

			cmd.append(ITEM_HELP + " TEXT, ");
			cmd.append(ITEM_SAVE_TYPE + " INTEGER, ");
			cmd.append(ITEM_SUPPLY_TYPE + " INTEGER, ");
			cmd.append(ITEM_GROUP_ID + " INTEGER, ");
			cmd.append(ITEM_GROUP_TYPE + " INTEGER, ");
			cmd.append(ITEM_ISACTIVED + " INTEGER );");
			db.execSQL(cmd.toString());

			// Thuc hien lenh tao bang News
			cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_NEWS + " ( ");
			cmd.append(NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(NEWS_NEWS_ID + " TEXT NOT NULL, ");
			cmd.append(NEWS_TITLE + " TEXT NOT NULL, ");
			cmd.append(NEWS_DESC + " TEXT, ");
			cmd.append(NEWS_IMG + " TEXT, ");
			cmd.append(NEWS_LINK + " TEXT, ");
			cmd.append(NEWS_ADDPARAM + " TEXT, ");
			cmd.append(NEWS_INDEX + " INTEGER, ");

			cmd.append(NEWS_GROUP_ID + " INTEGER, ");
			cmd.append(NEWS_GROUP_TYPE + " INTEGER, ");
			cmd.append(NEWS_ISACTIVED + " INTEGER );");
			db.execSQL(cmd.toString());
			
			// Thuc hien lenh tao bang Question - Kenh
			cmd = new StringBuilder();
			cmd.append("CREATE TABLE " + TABLE_QUESTION_KENH + " ( ");
			cmd.append(QK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
			cmd.append(QK_sID + " TEXT NOT NULL, ");
			cmd.append(QK_sCONTENT + " TEXT NOT NULL, ");
			cmd.append(QK_sDESCRIPTION + " TEXT, ");
			cmd.append(QK_sLINK + " TEXT, ");
			cmd.append(QK_sTYPE + " TEXT, ");
			cmd.append(QK_isACTIVE + " INTEGER );");
			db.execSQL(cmd.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// if (oldVersion < 3) {
			// db.execSQL(" CREATE TABLE " + TBL_NGUOIDUNG + " ( " + USER_MID
			// + " text, " + USER_ACCLIST + " TEXT, " + USER_PRKEY
			// + " TEXT, " + USER_THAMSO + " TEXT,"
			// + USER_VERSION_LINK + " TEXT, " + USER_NEWS + " TEXT, "
			// + USER_MENUCARD + " TEXT, " + USER_MENUTOPUP
			// + " TEXT, " + USER_MENUMORE + " TEXT" + " )");
			//
			// db.execSQL(" CREATE TABLE " + TBL_MLOG_HABIT + " ( "
			// + MLOG_HABIT_ID + " TEXT, " + MLOG_HABIT_HABIT
			// + " TEXT );");
			//
			// db.execSQL(" CREATE TABLE LOGBUYCARD ( ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT,PRODUCT TEXT, MONEY TEXT, SERI TEXT, ACCNO TEXT, PINCODE TEXT, EXDATE TEXT );");
			// db.execSQL(" CREATE TABLE LOG_HOADON ( ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT , SeqNo TEXT, SoTien TEXT, MaHoaDon TEXT, NoiDung Text, KetQua TEXT );");
			// db.execSQL(" CREATE TABLE LOG_PENDING ( ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT , SeqNo TEXT, LENH TEXT );");
			// }

			if (oldVersion < 4)
				onCreateV4(db);
		}
	}

	public DBAdapter(Context ctx) {
		this.mContext = ctx;
	}

	public DBAdapter open() {
		if (dbHelper == null)
			dbHelper = new DatabaseHelper(mContext, DATABASE_NAME, null,
					DATABASE_VERSION);
		if (sqlDb == null)
			sqlDb = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (sqlDb != null) {
			try {
				sqlDb.close();
				sqlDb = null;
			} catch (Exception e) {
			}
		}

		if (dbHelper != null) {
			try {
				dbHelper.close();
				dbHelper = null;
			} catch (Exception e) {
			}
		}
	}

	public void loadData() {
		boolean isOnOneMobile = true;

		try {
			open();
			Cursor cur = sqlDb
					.rawQuery(
							"SELECT USER_MID, USER_ACCLIST, USER_PRKEY, USER_THAMSO, USER_VERSION_LINK, USER_NEWS FROM "
									+ TBL_NGUOIDUNG, null);

			if (cur != null) {
				int iCount = cur.getCount();
				if (iCount >= 1) {
					cur.moveToFirst();
					// USER_MID, USER_ACCLIST, USER_PRKEY, USER_THAMSO,
					// USER_VERSION_LINK, USER_NEWS, USER_MENUCARD,
					// USER_MENUTOPUP,
					// USER_MENUMORE
					User.MID = cur.getString(0);
					
					String account[] = cur.getString(1).split(",", -1);
					
					// Account eMonkey Link
					if(account.length > 0){
							User.myAccountLink = account[0];
					}

					// Account MID List
					if(account.length > 1){
							User.arrMyAcountList = account[1].split("\\|", -1);
					}

					// Account ATM link list
					if(account.length > 2){
						if(!"".equals(account[2])){
						String arrMyAcountATMLink[] = account[2].split("\\|", -1);
							for (int i = 0; i < arrMyAcountATMLink.length; i++) {
								if(!"".equals(arrMyAcountATMLink[i])){
									User.arrMyAcountATMLink.add(arrMyAcountATMLink[i]);
								}
							}
						}
					}
					
					
					User.sprivateEncKey = cur.getString(2);
					String[] arrThamSo = cur.getString(3).split("\\|", -1);
					
					try {
						User.sIMEI = Util.getIMEI(mContext);
					} catch (Exception ex) {
					}
					if (User.sIMEI == null || User.sIMEI.equals("")
							|| User.sIMEI.equalsIgnoreCase("NULL"))
						User.sIMEI = arrThamSo[0];
					
					else if (!User.sIMEI.equals(arrThamSo[0])) {
						try {
							UUID.fromString(arrThamSo[0]);
							User.sIMEI = arrThamSo[0];
						} catch (Exception ex) {
							isOnOneMobile = false;
						}
					}
					User.setSeqNos(arrThamSo[1]);
					User.iTimeDiff = Integer
							.parseInt(arrThamSo[2].equals("") ? "0"
									: arrThamSo[2]);
					User.iSoLanSaiMPIN = Integer.parseInt("0" + arrThamSo[3]);
					if (User.iSoLanSaiMPIN >= 3)
						User.iSoLanSaiMPIN = 0;
					User.isRegisted = "1".equals(arrThamSo[4]) ? true : false;
					User.sLang = arrThamSo[5];
					User.sKetNoi = arrThamSo[6];
					User.iURL_INDEX = Integer.parseInt("0" + arrThamSo[7]);
					User.sID_NEWS = arrThamSo[8];
					User.sID_MENU_CARD = arrThamSo[9];
					User.sID_MENU_TOPUP = arrThamSo[10];

					if (arrThamSo.length > 11) {
						User.isCHUA_DAT_MAT_KHAU = arrThamSo[11].equals("0") ? true
								: false;
					}

					if (arrThamSo.length > 19) {
						// agent v40
						User.sID_MENU_MAIN = arrThamSo[12];
						User.sID_MENU_BILL = arrThamSo[13];

						User.sID_MENU_QUESTION = arrThamSo[14];
						User.sID_MENU_KENH = arrThamSo[15];
						
						User.sSMS_SERVERFONE = arrThamSo[16];
						User.sCustAnser = arrThamSo[17];
						User.sCustName = arrThamSo[18];
						User.sCustCMND = arrThamSo[19];
						User.sCustBirthday = arrThamSo[20];
						User.sCustQesID = arrThamSo[21];

						User.isActived = arrThamSo[22].equals("1") ? true
								: false;
						User.isRegNapTien = arrThamSo[23].equals("1") ? true
								: false;
						User.BANK_MID = arrThamSo[24];
					}

					User.sVersionAndLink = cur.getString(4);
					User.sADVERD = cur.getString(5);
					cur.close();
				} else {
					cur.close();
					isOnOneMobile = dongBoDuLieu();
				}
			}
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "loadData",  ex);
		}

		if (!isOnOneMobile) {
			
			try {
				User.sprivateEncKey = "";
				saveUserTable();
			} catch (Exception ex) {
				MPlusLib.debug(TAG, "loadData",  ex);
			}
		}
		User.isFisrt = true;
	}

	private boolean dongBoDuLieu() {
		boolean isOnOneMobile = true;
		try {
			// User;
			Cursor cur = sqlDb.rawQuery("SELECT * FROM " + USER_TABLE, null);

			if (cur.getCount() >= 1) {
				cur.moveToFirst();
				User.MID = cur.getString(1);
//				User.arrMyAcountList = cur.getString(2).split("!");

				String account[] = cur.getString(2).split(",", -1);
				
				// Account eMonkey Link
				if(account.length > 0){
					User.myAccountLink = account[0];
				}

				// Account MID List
				if(account.length > 1){
					if(!"".equals(account[1]))
						User.arrMyAcountList = account[1].split("\\|", -1);
				}
				
				// Account ATM link list
				if(account.length > 2){
					if(!"".equals(account[2])){
						String arrMyAcountATMLink[] = account[2].split("\\|", -1);
						for (int i = 0; i < arrMyAcountATMLink.length; i++) {
							if(!"".equals(arrMyAcountATMLink[i])){
								User.arrMyAcountATMLink.add(arrMyAcountATMLink[i]);
							}
						}
					}
				}
				
				// User.sIMEI = cur.getString(3);
				try {
					User.sIMEI = Util.getIMEI(mContext);
				} catch (Exception ex) {
					MPlusLib.debug(TAG, "dongBoDuLieu",  ex);
				}

				if (User.sIMEI == null || User.sIMEI.equals("")
						|| User.sIMEI.equalsIgnoreCase("NULL"))
					User.sIMEI = cur.getString(3).trim();
				User.sprivateEncKey = cur.getString(4).trim();
				User.setSeqNos(cur.getString(5).trim());
				cur.close();
				// Tham So
				Cursor curThamSo = sqlDb.rawQuery("SELECT * FROM "
						+ THAM_SO_TABLE, null);
				curThamSo.moveToFirst();
				User.sLang = curThamSo.getString(1);
				String str = curThamSo.getString(2);
				if (str.split("\\|").length > 1) {
					User.iTimeDiff = Long.parseLong(str.split("\\|")[1]);
				}
				String sketnoi = curThamSo.getString(3);
				if (sketnoi.split("\\|").length > 1) {
					User.sKetNoi = sketnoi.split("\\|")[0];
				} else
					User.sKetNoi = sketnoi;
				User.iSoLanSaiMPIN = curThamSo.getInt(4);
				User.iSoLanSaiMPIN = User.iSoLanSaiMPIN >= 3 ? 0
						: User.iSoLanSaiMPIN;
				curThamSo.close();
				User.iURL_INDEX = 0;
				User.isRegisted = true;
				User.sID_NEWS = "";
				User.sID_MENU_CARD = "";
				User.sID_MENU_TOPUP = "";
				User.sID_MENU_QUESTION = "";
				User.sID_MENU_KENH = "";
				User.sVersionAndLink = "";
				User.sADVERD = "";
				// SAVE
				saveUserTable();
				// MLOG
				Cursor curMLog = sqlDb.rawQuery("SELECT * FROM " + MLOG_TABLE,
						null);
				String sMID = "";
				String sACC = "";
				String sCARD = "";
				if (curMLog.getCount() > 0) {
					curMLog.moveToLast();
					sMID = curMLog.getString(1);
					sACC = curMLog.getString(2);
					sCARD = curMLog.getString(3);
					while (curMLog.moveToPrevious()) {
						sMID += "|" + curMLog.getString(1);
						sACC += "|" + curMLog.getString(2);
						sCARD += "|" + curMLog.getString(3);
					}
				}
				curMLog.close();
				// Save
				saveCaches(sMID, "", "", "", "", "", "", "");
				saveCaches("", sACC, "", "", "", "", "", "");
				saveCaches("", "", sCARD, "", "", "", "", "");
			} else
				cur.close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "dongBoDuLieu",  ex);
		}

		return isOnOneMobile;
	}

	public void saveUserTable() {
		try {
			open();

			ContentValues cv = new ContentValues();
			String sMaskAcc = "";
			String sStep = "|";

			// add Account eMonkey Link
			sMaskAcc += User.myAccountLink;
			sMaskAcc += ",";
			
			// add Account MID list
			for (int i = 0; i < User.arrMyAcountList.length; i++){
				if(!"".equals(User.arrMyAcountList[i]))
					sMaskAcc += User.arrMyAcountList[i] + sStep;
			}
			sMaskAcc += ",";
			
			// add Account ATM Link

			for (int i = 0; i < User.arrMyAcountATMLink.size(); i++) {
				if(!"".equals(User.arrMyAcountATMLink.get(i)))
					sMaskAcc += User.arrMyAcountATMLink.get(i) + sStep;
			}
			
			// sIMEI|sSeqNo|iTimeDiff|iSoLanSaiMPIN|isDaDangKy|sLang|sKetNoi|iURL_INDEX|sID_NEWS|sID_MENU_CARD|sID_MENU_TOPUP|isCHUA_DAT_MAT_KHAU
			StringBuffer sParam = new StringBuffer();
			sParam.append(User.sIMEI);
			sParam.append(sStep);
			sParam.append(User.getSeqNo());
			sParam.append(sStep);
			sParam.append(String.valueOf(User.iTimeDiff));
			sParam.append(sStep);
			sParam.append(String.valueOf(User.iSoLanSaiMPIN));
			sParam.append(sStep);
			sParam.append(User.isRegisted ? "1" : "0");
			sParam.append(sStep);
			sParam.append(User.sLang);
			sParam.append(sStep);
			sParam.append(User.sKetNoi);
			sParam.append(sStep);
			sParam.append(String.valueOf(User.iURL_INDEX));
			sParam.append(sStep);
			sParam.append(User.sID_NEWS);
			sParam.append(sStep);
			sParam.append(User.sID_MENU_CARD);
			sParam.append(sStep);
			sParam.append(User.sID_MENU_TOPUP);
			sParam.append(sStep);
			sParam.append(User.isCHUA_DAT_MAT_KHAU ? "0" : "1");

			// v40
			sParam.append(sStep);
			sParam.append(User.sID_MENU_MAIN);
			sParam.append(sStep);
			sParam.append(User.sID_MENU_BILL);
			sParam.append(sStep);
			sParam.append(User.sID_MENU_QUESTION);
			sParam.append(sStep);
			sParam.append(User.sID_MENU_KENH);

			// Agent
			sParam.append(sStep);
			sParam.append(User.sSMS_SERVERFONE);
			sParam.append(sStep);
			sParam.append(User.sCustAnser);
			sParam.append(sStep);
			sParam.append(User.sCustName);
			sParam.append(sStep);
			sParam.append(User.sCustCMND);
			sParam.append(sStep);
			sParam.append(User.sCustBirthday);
			sParam.append(sStep);
			sParam.append(User.sCustQesID);
			sParam.append(sStep);
			sParam.append(User.isActived ? "1" : "0");
			sParam.append(sStep);
			sParam.append(User.isRegNapTien ? "1" : "0");
			sParam.append(sStep);
			sParam.append(User.BANK_MID);
			
			cv.put(USER_MID, User.MID);
			cv.put(USER_ACCLIST, sMaskAcc);
			cv.put(USER_PRKEY, User.sprivateEncKey);
			cv.put(USER_THAMSO, sParam.toString());

			Cursor cur = sqlDb.rawQuery(
					"SELECT USER_MID FROM " + TBL_NGUOIDUNG, null);
			if (cur.getCount() >= 1) {
				sqlDb.update(TBL_NGUOIDUNG, cv, null, null);
			} else {
				cv.put(USER_VERSION_LINK, "");
				cv.put(USER_NEWS, "");
				cv.put(USER_MENUCARD, "");
				cv.put(USER_MENUTOPUP, "");
				cv.put(USER_MENUMORE, "");
				sqlDb.insert(TBL_NGUOIDUNG, null, cv);
			}
			cur.close();
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveUserTable",  ex);
		}
	}

	public void saveMenuId(String sVersionLink, String sAds, String sMenuCard,
			String sMenuTopup, String sMenuMore) {
		open();
		try {
			ContentValues cv = new ContentValues();
			if (!"".equals(sVersionLink))
				cv.put(USER_VERSION_LINK, sVersionLink);
			if (!"".equals(sAds))
				cv.put(USER_NEWS, sAds);
			if (!"".equals(sMenuCard))
				cv.put(USER_MENUCARD, sMenuCard);
			if (!"".equals(sMenuTopup))
				cv.put(USER_MENUTOPUP, sMenuTopup);
			if (!"".equals(sMenuMore))
				cv.put(USER_MENUMORE, sMenuMore);
			Cursor cur = sqlDb.rawQuery(
					"SELECT USER_MID FROM " + TBL_NGUOIDUNG, null);
			if (cur.getCount() >= 1) {
				sqlDb.update(TBL_NGUOIDUNG, cv, null, null);
			} else {
				sqlDb.insert(TBL_NGUOIDUNG, null, cv);
			}
			cur.close();
		} catch (Exception ex) {
		}
		close();
	}

	public long saveLogTransaction(LogTransactionItem log) {
		long output = -1;
		try {
			open();

			ContentValues cv = new ContentValues();
			cv.put(LOG_TIME, log.getLogTime());
			cv.put(LOG_SEQNO, log.getSeqNo());
			cv.put(LOG_TRANCODE, log.getTranCommand());
			cv.put(LOG_DSTNO, log.getDestinationNo());
			cv.put(LOG_DESC, log.getDescription());
			cv.put(LOG_AMOUNT, log.getAmount());
			cv.put(LOG_RESULT, log.getResult());
			cv.put(LOG_ADDPARAM, log.getAddParam());
			cv.put(LOG_AMOUNT_COST, log.getmAmount_cost());

			if (getLogNumRow() >= Config.iMAX_LOG)
				deleteLog();
			output = sqlDb.insert(TABLE_TRAN_LOG1, null, cv);

			close();
		} catch (Exception ex) {
		}
		return output;
	}

	public long insertOrUpdateLogTransaction(LogTransactionItem log) {
		long output = -1;
		try {
			open();

			if (getLogNumRow() >= Config.iMAX_LOG)
				deleteLog();

//			if (isPending) {
//				// Ghi ket qua kiem tra giao dich
//				output = saveLogTransaction(log);
//			} else {
				Cursor cur = sqlDb.rawQuery(
						"SELECT * FROM " + TABLE_TRAN_LOG1 + " WHERE "
								+ LOG_SEQNO + "=?",
						new String[] { log.getSeqNo() });
				int count = 0;
				if (cur != null) {
					count = cur.getCount();
					cur.close();
				}

				if (count > 0) {
					// Cap nhat ket qua giao dich
					ContentValues cv = new ContentValues();
					cv.put(LOG_RESULT, log.getResult());
					output = sqlDb.update(TABLE_TRAN_LOG1, cv,
							LOG_SEQNO + "=?", new String[] { log.getSeqNo() });
				} else {
					// Them giao dich
					output = saveLogTransaction(log);
				}
//			}
		} catch (Exception ex) {
		} finally {
			close();
		}
		return output;
	}
	
	private int getLogNumRow() {
		Cursor cur = sqlDb.rawQuery("SELECT " + LOG_ID + " FROM "
				+ TABLE_TRAN_LOG1, null);
		int x = cur.getCount();
		cur.close();
		return x;
	}

	private void deleteLog() {
		Cursor cur = sqlDb.rawQuery("SELECT " + LOG_ID + " FROM "
				+ TABLE_TRAN_LOG1, null);
		cur.moveToFirst();
		sqlDb.delete(TABLE_TRAN_LOG1, LOG_ID + "=?",
				new String[] { String.valueOf(cur.getInt(0)) });
		cur.close();
	}

	/**
	 * flag = 0 : all log; flag = 1 : error log; flag = 2 : pending log
	 * */
	public Vector<LogTransactionItem> getLogItem(LogState flag) {
		Vector<LogTransactionItem> logItems = null;

		try {
			open();
			StringBuffer sBuf = new StringBuffer();
			sBuf.append("SELECT * FROM ");
			sBuf.append(TABLE_TRAN_LOG1);

			if (flag == LogState.SUCCESS) {
				sBuf.append(" WHERE ");
				sBuf.append(LOG_RESULT);
				sBuf.append(" LIKE 'val%'");
			} else if (flag == LogState.ERROR) {
				sBuf.append(" WHERE ");
				sBuf.append(LOG_RESULT);
				sBuf.append(" LIKE 'err%'");
			} else if (flag == LogState.PENDING) {
				sBuf.append(" WHERE ");
				sBuf.append(LOG_RESULT);
				sBuf.append(" NOT LIKE 'err%' AND ");
				sBuf.append(LOG_RESULT);
				sBuf.append(" NOT LIKE 'val%'");
			}

			sBuf.append(" ORDER BY ");
			sBuf.append(LOG_ID);
			sBuf.append(" DESC");

			Cursor cur = sqlDb.rawQuery(sBuf.toString(), null);

			if (cur.getCount() > 0) {
				LogTransactionItem item = null;
				logItems = new Vector<LogTransactionItem>();

				int clId = cur.getColumnIndex(LOG_ID);
				int clTime = cur.getColumnIndex(LOG_TIME);
				int clSeqno = cur.getColumnIndex(LOG_SEQNO);
				int clTrancode = cur.getColumnIndex(LOG_TRANCODE);
				int clDstno = cur.getColumnIndex(LOG_DSTNO);
				int clDesc = cur.getColumnIndex(LOG_DESC);
				int clAmount = cur.getColumnIndex(LOG_AMOUNT);
				int clResult = cur.getColumnIndex(LOG_RESULT);
				int clParam = cur.getColumnIndex(LOG_ADDPARAM);
				int clAmount_cost = cur.getColumnIndex(LOG_AMOUNT_COST);

				while (cur.moveToNext()) {
					item = new LogTransactionItem();
					item.setId(cur.getInt(clId));
					item.setLogTime(cur.getString(clTime));
					item.setSeqNo(cur.getString(clSeqno));
					item.setTranCommand(cur.getString(clTrancode));
					item.setDestinationNo(cur.getString(clDstno));
					item.setDescription(cur.getString(clDesc));
					item.setAmount(cur.getString(clAmount));
					item.setResult(cur.getString(clResult));
					item.setAddParam(cur.getString(clParam));
					item.setmAmount_cost(cur.getString(clAmount_cost));
					logItems.add(item);

//					Log.i("Log : ", "-----------------------------");
//					Log.i("Log : ", "LOG_ID : "+cur.getInt(clId));
//					Log.i("Log : ", "LOG_TIME : "+cur.getString(clTime));
//					Log.i("Log : ", "LOG_SEQNO : "+cur.getString(clSeqno));
//					Log.i("Log : ", "LOG_TRANCODE : "+cur.getString(clTrancode));
//					Log.i("Log : ", "LOG_DSTNO : "+cur.getString(clDstno));
//					Log.i("Log : ", "LOG_DESC : "+cur.getString(clDesc));
//					Log.i("Log : ", "LOG_AMOUNT : "+cur.getString(clAmount));
//					Log.i("Log : ", "LOG_RESULT : "+cur.getString(clResult));
//					Log.i("Log : ", "LOG_ADDPARAM : "+cur.getString(clParam));
//					Log.i("Log : ", "LOG_AMOUNT_COST : "+cur.getString(clAmount_cost));
				}
			}

			cur.close();
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getLogItem",  ex);
		}

		return logItems;
	}

	// XU LY LOG BILL PAYMENT
	public long insertOrUpdateBillLog(LogBillItem logItem) {
		long output = -1;
		try {
			open();
			// if (logItem.getResult().length() > 40)
			// logItem.setResult(ketQua.substring(0, 40));
			// if (sNoiDung.length() > 40)
			// sNoiDung = sNoiDung.substring(0, 40);

			// xoa khi log day
			Cursor cur = sqlDb.rawQuery("SELECT " + BILL_ID + " FROM "
					+ TABLE_BILL_LOG, null);
			if (cur.getCount() >= Config.iMAX_LOG) {
				cur.moveToFirst();
				sqlDb.delete("LOG_HOADON", BILL_ID + "=?",
						new String[] { String.valueOf(cur.getInt(0)) });
			}
			cur.close();

			// /
			cur = sqlDb.rawQuery("SELECT " + BILL_ID + " FROM "
					+ TABLE_BILL_LOG + " WHERE " + BILL_SEQNO + "=? AND "
					+ BILL_BILL_CODE + "=?", new String[] { logItem.getSeqNo(),
					logItem.getBillCode() });
			if (cur.getCount() > 0) {
				cur.moveToLast();
				ContentValues cv = new ContentValues();
				cv.put(BILL_RESULT, logItem.getResult());
				String sID = String.valueOf(cur.getInt(0));
				cur.close();
				output = sqlDb.update(TABLE_BILL_LOG, cv, BILL_ID + "=?",
						new String[] { sID });
			} else {
				cur.close();
				ContentValues cv = new ContentValues();
				cv.put(BILL_TIME, logItem.getLogTime());
				cv.put(BILL_SEQNO, logItem.getSeqNo());
				cv.put(BILL_AMOUNT, logItem.getAmount());
				cv.put(BILL_BILL_CODE, logItem.getBillCode());
				cv.put(BILL_DESC, logItem.getDescription());
				cv.put(BILL_RESULT, logItem.getResult());
				cv.put(BILL_SUPPLIER_ID, logItem.getSupplierId());
				cv.put(BILL_ADDPARAM, "");
				output = sqlDb.insert(TABLE_BILL_LOG, null, cv);
			}
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateBillLog",  ex);
		}

		return output;
	}

	public Vector<LogBillItem> getLogHoaDon() {
		Vector<LogBillItem> vec = null;
		Cursor cur = null;
		try {
			open();
			cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_BILL_LOG
					+ " ORDER BY " + BILL_ID + " DESC", null);
			if (cur.getCount() > 0) {
				vec = new Vector<LogBillItem>();

				int clId = cur.getColumnIndex(BILL_ID);
				int clBillCode = cur.getColumnIndex(BILL_BILL_CODE);
				int clAmount = cur.getColumnIndex(BILL_AMOUNT);
				int clDesc = cur.getColumnIndex(BILL_DESC);
				int clSeqno = cur.getColumnIndex(BILL_SEQNO);
				int clResult = cur.getColumnIndex(BILL_RESULT);
				int clSupplierId = cur.getColumnIndex(BILL_SUPPLIER_ID);
				int clTime = cur.getColumnIndex(BILL_TIME);
				int clParam = cur.getColumnIndex(BILL_ADDPARAM);

				while (cur.moveToNext()) {
					LogBillItem item = new LogBillItem();
					item.setId(cur.getInt(clId));
					item.setBillCode(cur.getString(clBillCode));
					item.setAmount(cur.getString(clAmount));
					item.setDescription(cur.getString(clDesc));
					item.setSeqNo(cur.getString(clSeqno));
					item.setResult(cur.getString(clResult));
					item.setSupplierId(cur.getString(clSupplierId));
					item.setLogTime(cur.getString(clTime));
					item.setAddParam(cur.getString(clParam));
					vec.addElement(item);
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getLogHoaDon", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}
		return vec;
	}

	// XU LY ALL LOG
	public void saveCaches(String sMID, String sACC, String sCard,
			String sTelTopup, String sNhaCungCap, String sHoaDon,
			String sTelTopupTraSau, String sGameAcc) {
		ContentValues cv = new ContentValues();
		String sID = "";
		String sContent = "";
		if (!"".equals(sMID)) {
			sID = "MID";
			sContent = sMID;
		}
		if (!"".equals(sACC)) {
			sID = "ACC";
			sContent = sACC;
		}
		if (!"".equals(sCard)) {
			sID = "CARD";
			sContent = sCard;
		}
		if (!"".equals(sTelTopup)) {
			sID = "TEL";
			sContent = sTelTopup;
		}
		if (!"".equals(sNhaCungCap)) {
			sID = "NHACC";
			sContent = sNhaCungCap;
		}
		if (!"".equals(sHoaDon)) {
			sID = "MAHOADON";
			sContent = sHoaDon;
		}

		if (!"".equals(sTelTopupTraSau)) {
			sID = "TEL_TS";
			sContent = sTelTopupTraSau;
		}

		if (!"".equals(sGameAcc)) {
			sID = "GAME_TT";
			sContent = sGameAcc;
		}
		
		Cursor cur = null;
		open();
		try {
			cur = sqlDb.rawQuery("SELECT * FROM " + TBL_MLOG_HABIT
					+ " WHERE " + MLOG_HABIT_ID + "=?", new String[] { sID });
			if (cur.getCount() > 0) {
				cur.moveToFirst();
				String sTemp = cur.getString(1);
				String arrTemp[] = sTemp.split("\\|");
				cur.close();
				cur = null;
				int i = 0;
				for (i = 0; i < arrTemp.length; ++i) {
					if ((sID.equals("TEL") || sID.equals("TEL_TS")
							|| sID.equals("MAHOADON") || sID.equals("GAME_TT"))
							&& (arrTemp[i].equals(sContent)))
						break;
					else if ((sID.equals("MID") || sID.equals("ACC") || sID
							.equals("CARD"))
							&& (arrTemp[i].split(":").length > 1)
							&& arrTemp[i].split(":")[1].equals(sContent
									.split(":")[1])) {
						break;
					} else if (sID.equals("NHACC")
							&& arrTemp[i].split(":").length > 1
							&& arrTemp[i].split(":")[1].equals(sContent
									.split(":")[1]))
						break;
				}
				if (i >= arrTemp.length) {

					if (arrTemp.length < Config.ihabitLog) {
						sTemp = sContent + "|" + sTemp;
					} else {
						sTemp = sContent;
						for (int j = 0; j < arrTemp.length - 1; ++j) {
							sTemp += "|" + arrTemp[j];
						}
					}
				} else {
					for (int j = i; j > 0; --j) {
						arrTemp[j] = arrTemp[j - 1];
					}
					arrTemp[0] = sContent;
					sTemp = sContent;
					for (int j = 1; j < arrTemp.length; ++j) {
						sTemp += "|" + arrTemp[j];
					}
				}
				cv.put(MLOG_HABIT_ID, sID);
				cv.put(MLOG_HABIT_HABIT, sTemp);
				sqlDb.update(TBL_MLOG_HABIT, cv, MLOG_HABIT_ID + "=?",
						new String[] { sID });
			} else {
				cur.close();
				cur = null;
				cv.put(MLOG_HABIT_ID, sID);
				cv.put(MLOG_HABIT_HABIT, sContent);
				sqlDb.insert(TBL_MLOG_HABIT, null, cv);
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "saveCaches",  ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}
	}

	public String getCaches(int iType) {
		String sID = "";
		String sContent = "";
		open();
		try {

			switch (iType) {
			case 1:
				sID = "MID";
				break;

			case 2:
				sID = "ACC";
				break;

			case 3:
				sID = "CARD";
				break;

			case 4:
				sID = "TEL";
				break;

			case 5:
				sID = "NHACC";
				break;

			case 6:
				sID = "MAHOADON";
				break;

			case 7:
				sID = "TEL_TS";
				break;

			case 8:
				sID = "GAME_TT";
				break;
			}

			Cursor cur = sqlDb.rawQuery("SELECT * FROM " + TBL_MLOG_HABIT
					+ " WHERE " + MLOG_HABIT_ID + "=?", new String[] { sID });

			if (cur.getCount() > 0) {
				cur.moveToFirst();
				sContent = cur.getString(1);
			}

			MPlusLib.showDebug("Habit: " + sContent);
			cur.close();
		} catch (Exception ex) {
		}
		close();
		return sContent;
	}

	// XU LY PRODUCT, CARD
	public long insertOrUpdateProduct(Product product) {
		long output = -1;
		Cursor cur = null;
		try {
			open();

			ContentValues cv = new ContentValues();
			cv.put(PRODUCT_TIME, Util.getTimeClient());
			cv.put(PRODUCT_PRODUCT, product.getProductCode());
			cv.put(PRODUCT_PRICE, product.getPrice());
			cv.put(PRODUCT_SERI, product.getSeri());
			cv.put(PRODUCT_ACCNO, product.getAccNo());
			cv.put(PRODUCT_PINCODE, product.getPin());
			cv.put(PRODUCT_EXDATE, product.getExDate());
			cv.put(PRODUCT_HOTLINE, product.getHotLine());
			cv.put(PRODUCT_USED, product.getIsUsed());
			cv.put(PRODUCT_ADDPARAM, "");
			
			if (product.getId() != -1) {
				cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_PRODUCT
						+ " WHERE " + PRODUCT_ID + "=?",
						new String[] { String.valueOf(product.getId()) });
			}

			if (cur != null && cur.getCount() > 0) {
				output = sqlDb.update(TABLE_PRODUCT, cv, PRODUCT_ID + "=?",
						new String[] { String.valueOf(product.getId()) });
			} else {
				output = sqlDb.insert(TABLE_PRODUCT, null, cv);
			}		
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateProduct", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}

		return output;
	}

	/**
	 * Lay danh sach the da mua
	 * */
	public Vector<Product> getProducts() {
		Vector<Product> vec = null;
		Cursor cur = null;
		try {
			open();

			cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_PRODUCT
					+ " ORDER BY " + PRODUCT_ID + " DESC", null);
			if (cur.getCount() > 0) {
				vec = new Vector<Product>();
				int clId = cur.getColumnIndex(PRODUCT_ID);
				int clTime = cur.getColumnIndex(PRODUCT_TIME);
				int clProduct = cur.getColumnIndex(PRODUCT_PRODUCT);
				int clPrice = cur.getColumnIndex(PRODUCT_PRICE);
				int clSeri = cur.getColumnIndex(PRODUCT_SERI);
				int clAccNo = cur.getColumnIndex(PRODUCT_ACCNO);
				int clPin = cur.getColumnIndex(PRODUCT_PINCODE);
				int clExDate = cur.getColumnIndex(PRODUCT_EXDATE);
				int clHotline = cur.getColumnIndex(PRODUCT_HOTLINE);
				int clUsed = cur.getColumnIndex(PRODUCT_USED);
				int clParam = cur.getColumnIndex(PRODUCT_ADDPARAM);

				while (cur.moveToNext()) {
					Product pro = new Product();
					pro.setId(cur.getInt(clId));
					pro.setTime(cur.getString(clTime));
					pro.setProductCode(cur.getString(clProduct));
					pro.setPrice(cur.getString(clPrice));
					pro.setSeri(cur.getString(clSeri));
					pro.setAccNo(cur.getString(clAccNo));
					pro.setPin(cur.getString(clPin));
					pro.setExDate(cur.getString(clExDate));
					pro.setHotLine(cur.getString(clHotline));
					pro.setIsUsed(cur.getInt(clUsed));
					pro.setAddParam(cur.getString(clParam));
					vec.add(pro);
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getProducts", ex);
		}  finally {
			if (cur != null)
				cur.close();
			close();
		}
		return vec;
	}

	/**
	 * Lay danh sach the da mua
	 * */
	public int countProducts() {
		int output = 0;
		try {
			open();

			Cursor cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_PRODUCT
					+ " ORDER BY " + PRODUCT_ID + " DESC", null);
			output = cur.getCount();
			cur.close();
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "countProducts", ex);
		}

		return output;
	}

	public void deleteProduct(String sID) {
		try {
			open();
			sqlDb.delete(TABLE_PRODUCT, PRODUCT_ID + "=?", new String[] { sID });
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "deleteProduct", ex);
		}
	}

	public void insertLogPending(long time, String seqNo, String sLenh) {
		try {
			open();
			ContentValues cv = new ContentValues();
			cv.put(LOG_PENDING_TIME, String.valueOf(time));
			cv.put(LOG_PENDING_SEQNO, seqNo);
			cv.put(LOG_PENDING_COMMAND, sLenh);
			cv.put(LOG_PENDING_ADDPARAM, "");
			Cursor cur = sqlDb.rawQuery("SELECT " + LOG_PENDING_ID + " FROM "
					+ TABLE_PENDING_LOG + " WHERE " + LOG_PENDING_SEQNO + "=?",
					new String[] { seqNo });
			int icount = cur.getCount();
			cur.close();
			if (icount < 1) {
				sqlDb.insert(TABLE_PENDING_LOG, null, cv);
			}
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertLogPending", ex);
		}

	}

	public LogTransactionItem getLogPending(String sSequen) {
		Cursor cur = null;
		LogTransactionItem item = null;
		try {
			open();
			cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_PENDING_LOG + " WHERE " + LOG_PENDING_SEQNO 
					+ " = \"" +  sSequen
					+ "\" ORDER BY " + LOG_PENDING_ID + " DESC", null);

			if (cur.getCount() > 0) {
				int clId = cur.getColumnIndex(LOG_PENDING_ID);
				int clTime = cur.getColumnIndex(LOG_PENDING_TIME);
				int clSeqno = cur.getColumnIndex(LOG_PENDING_SEQNO);
				int clCmd = cur.getColumnIndex(LOG_PENDING_COMMAND);
				int clParam = cur.getColumnIndex(LOG_PENDING_ADDPARAM);

				long iTime = 0;

				while (cur.moveToNext()) {
					try {
						iTime = Long.parseLong(cur.getString(clTime));

						item = new LogTransactionItem();
						item.setId(cur.getInt(clId));
						if (Util.isIn24h(iTime)) {
							// in time to check
								item.setLogTime(cur.getString(clTime));
								item.setSeqNo(cur.getString(clSeqno));
								item.setTranCommand(cur.getString(clCmd));
								item.setAddParam(cur.getString(clParam));
						}
					} catch (Exception ex) {
						MPlusLib.debug(TAG, "getLogPendingTopup", ex);
					}
				}
			}else {
				return null;
			}
			cur.close();
			cur = null;
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getLogPendingTopup", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}
		if(item == null){
			item = new LogTransactionItem();
		}

		return item;
	}
	
	public Vector<LogTransactionItem> getLogPendingCard() {
		Vector<LogTransactionItem> items = null;
		Cursor cur = null;
		try {
			open();

			cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_PENDING_LOG
					+ " ORDER BY " + LOG_PENDING_ID + " DESC", null);
			items = new Vector<LogTransactionItem>();
			Vector<LogTransactionItem> ovItems = new Vector<LogTransactionItem>();

			if (cur.getCount() > 0) {
				int clId = cur.getColumnIndex(LOG_PENDING_ID);
				int clTime = cur.getColumnIndex(LOG_PENDING_TIME);
				int clSeqno = cur.getColumnIndex(LOG_PENDING_SEQNO);
				int clCmd = cur.getColumnIndex(LOG_PENDING_COMMAND);
				int clParam = cur.getColumnIndex(LOG_PENDING_ADDPARAM);

				LogTransactionItem item = null;
				long iTime = 0;

				while (cur.moveToNext()) {
					try {
						iTime = Long.parseLong(cur.getString(clTime));

						item = new LogTransactionItem();
						item.setId(cur.getInt(clId));
						if (Util.isIn24h(iTime)) {
							// in time to check
							String str = cur.getString(clCmd);
							if(str.startsWith("0k")){
								item.setLogTime(cur.getString(clTime));
								item.setSeqNo(cur.getString(clSeqno));
								item.setTranCommand(cur.getString(clCmd));
								item.setAddParam(cur.getString(clParam));
								items.add(item);
							}
						} else {
							// over time to check
							ovItems.add(item);
						}
					} catch (Exception ex) {
						MPlusLib.debug(TAG, "getLogPendingTopup", ex);
					}
				}
			}
			cur.close();
			cur = null;
			if (ovItems != null && ovItems.size() > 0) {
				// delete over time item
				for (LogTransactionItem item : ovItems) {
					try {
						sqlDb.delete(TABLE_PENDING_LOG, LOG_PENDING_ID + "=?",
								new String[] { String.valueOf(item.getId()) });
					} catch (Exception ex) {
						MPlusLib.debug(TAG, "getLogPendingTopup", ex);
					}
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getLogPendingTopup", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}

		return items;
	}
	
	public Vector<LogTransactionItem> getLogPending() {
		Vector<LogTransactionItem> items = null;
		Cursor cur = null;
		try {
			open();
			cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_PENDING_LOG
					+ " ORDER BY " + LOG_PENDING_ID + " DESC", null);
			items = new Vector<LogTransactionItem>();
			Vector<LogTransactionItem> ovItems = new Vector<LogTransactionItem>();

			if (cur.getCount() > 0) {
				int clId = cur.getColumnIndex(LOG_PENDING_ID);
				int clTime = cur.getColumnIndex(LOG_PENDING_TIME);
				int clSeqno = cur.getColumnIndex(LOG_PENDING_SEQNO);
				int clCmd = cur.getColumnIndex(LOG_PENDING_COMMAND);
				int clParam = cur.getColumnIndex(LOG_PENDING_ADDPARAM);

				LogTransactionItem item = null;
				long iTime = 0;

				while (cur.moveToNext()) {
					try {
						iTime = Long.parseLong(cur.getString(clTime));

						item = new LogTransactionItem();
						item.setId(cur.getInt(clId));
						if (Util.isIn24h(iTime)) {
							// in time to check
							item.setLogTime(cur.getString(clTime));
							item.setSeqNo(cur.getString(clSeqno));
							item.setTranCommand(cur.getString(clCmd));
							item.setAddParam(cur.getString(clParam));
							items.add(item);
						} else {
							// over time to check
							ovItems.add(item);
						}
					} catch (Exception ex) {
						MPlusLib.debug(TAG, "getLogPending", ex);
					}
				}
			}
			cur.close();
			cur = null;
			if (ovItems != null && ovItems.size() > 0) {
				// delete over time item
				for (LogTransactionItem item : ovItems) {
					try {
						sqlDb.delete(TABLE_PENDING_LOG, LOG_PENDING_ID + "=?",
								new String[] { String.valueOf(item.getId()) });
					} catch (Exception ex) {
						MPlusLib.debug(TAG, "getLogPending", ex);
					}
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getLogPending", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}

		return items;
	}

	public long deleteLogPending(String sSeqNo) {
		long output = -1;
		try {
			open();
			
			output = sqlDb.delete(TABLE_PENDING_LOG, LOG_PENDING_SEQNO + "=?",
					new String[] { sSeqNo });
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "deleteLogPending", ex);
		}
		return output;
	}

	public void delDatabase() {
		sqlDb.execSQL("DROP DATABASE " + DATABASE_NAME);
	}

	// XU LY GROUP

	/**
	 * Them hoac update group vao db
	 * */
	public long insertOrUpdateGroup(Group group) {
		long output = -1;
		try {
			open();
			output = _insertOrUpdateGroup(group);
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateGroup", ex);
		}

		return output;
	}

	/**
	 * Them hoac update group vao db
	 * */
	private long _insertOrUpdateGroup(Group group) {
		Cursor cur = null;
		long output = -1;

		ContentValues cValues = new ContentValues();
		cValues.put(GROUP_GROUP_ID, group.getGroupId());
		cValues.put(GROUP_TITLE, group.getTitle());
		cValues.put(GROUP_DESC, group.getDescription());
		cValues.put(GROUP_IMG, group.getImage());
		cValues.put(GROUP_LINK, group.getLink());
		cValues.put(GROUP_ADDPARAM, group.getAddParam());
		cValues.put(GROUP_INDEX, group.getIndex());
		cValues.put(GROUP_GROUP_TYPE, group.getGroupType());
		cValues.put(GROUP_ISACTIVED, group.getIsActived());

		cur = sqlDb.rawQuery(
				"SELECT * FROM " + TABLE_GROUP + " WHERE " + GROUP_GROUP_ID
						+ "=? AND " + GROUP_GROUP_TYPE + "=?",
				new String[] { group.getGroupId(),
						String.valueOf(group.getGroupType()) });

		if (cur != null && cur.getCount() > 0) {
			output = sqlDb.update(
					TABLE_GROUP,
					cValues,
					GROUP_GROUP_ID + "=? AND " + GROUP_GROUP_TYPE + "=?",
					new String[] { group.getGroupId(),
							String.valueOf(group.getGroupType()) });
		} else {
			output = sqlDb.insert(TABLE_GROUP, null, cValues);
		}
		if (cur != null)
			cur.close();
		return output;
	}

	/**
	 * 
	 * Them hoac update group vao db, co cap nhat index cua group
	 * 
	 * */
	public long insertOrUpdateGroupIndex(Group group) {
		Cursor cur = null;
		long output = -1;
		try {
			open();

			cur = sqlDb.rawQuery(
					"SELECT * FROM " + TABLE_GROUP + " WHERE " + GROUP_GROUP_ID
							+ "!=? AND " + GROUP_GROUP_TYPE + "=?",
					new String[] { group.getGroupId(),
							String.valueOf(group.getGroupType()) });

			if (cur != null && cur.getCount() > 0) {
				ContentValues cValues = new ContentValues();
				while (cur.moveToNext()) {
					// update index++ cac group cung loai
					cValues.remove(GROUP_INDEX);
					cValues.put(GROUP_INDEX,
							cur.getInt(cur.getColumnIndex(GROUP_INDEX)) + 1);

					sqlDb.update(TABLE_GROUP, cValues, GROUP_ID + "=?",
							new String[] { String.valueOf(cur.getLong(cur
									.getColumnIndex(GROUP_ID))) });
				}
			}
			if (cur != null) {
				cur.close();
				cur = null;
			}
			output = _insertOrUpdateGroup(group);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateGroupIndex", ex);
		} finally {
			close();
		}

		return output;
	}

	/**
	 * Xoa cac nhom chua duoc kich hoat
	 * */
	public long deleteNotActivedGroups(int groupType) {
		long output = 0;
		try {
			open();
			output = sqlDb.delete(
					TABLE_GROUP,
					GROUP_GROUP_TYPE + "=? AND " + GROUP_ISACTIVED + "!=?",
					new String[] { String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "deleteNotActivedGroups", ex);
		}
		return output;
	}

	/**
	 * Kich hoat, huy kich hoat
	 * */
	public long activeGroups(int groupType, int activeState) {
		long output = 0;
		try {
			open();

			ContentValues cValues = new ContentValues();
			cValues.put(GROUP_ISACTIVED, activeState);
			output = sqlDb.update(TABLE_GROUP, cValues,
					GROUP_GROUP_TYPE + "=?",
					new String[] { String.valueOf(groupType) });

			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "activeGroups", ex);
		}
		return output;
	}

	/**
	 * Lay danh sach cac group dang duoc actived thuoc groupType
	 * */
	public Vector<Group> getGroups(int groupType) {
		Vector<Group> vec = null;
		Cursor cur = null;

		try {
			open();
			cur = sqlDb.rawQuery(
					"SELECT * FROM " + TABLE_GROUP + " WHERE "
							+ GROUP_GROUP_TYPE + "=? AND " + GROUP_ISACTIVED
							+ "=? ORDER BY " + GROUP_INDEX + " ASC",
					new String[] { String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });
			if (cur != null && cur.getCount() > 0) {
				vec = new Vector<Group>(cur.getCount());

				while (cur.moveToNext()) {
					Group item = new Group();
					item.setId(cur.getLong(cur.getColumnIndex(GROUP_ID)));
					item.setGroupId(cur.getString(cur
							.getColumnIndex(GROUP_GROUP_ID)));
					item.setTitle(cur.getString(cur.getColumnIndex(GROUP_TITLE)));
					item.setDescription(cur.getString(cur
							.getColumnIndex(GROUP_DESC)));
					item.setImage(cur.getString(cur.getColumnIndex(GROUP_IMG)));
					item.setLink(cur.getString(cur.getColumnIndex(GROUP_LINK)));
					item.setAddParam(cur.getString(cur
							.getColumnIndex(GROUP_ADDPARAM)));
					item.setIndex(cur.getInt(cur.getColumnIndex(GROUP_INDEX)));
					item.setGroupType(cur.getInt(cur
							.getColumnIndex(GROUP_GROUP_TYPE)));
					item.setIsActived(cur.getInt(cur
							.getColumnIndex(GROUP_ISACTIVED)));
					vec.addElement(item);
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getGroups", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}
		return vec;
	}

	// XU LY ITEM

	/**
	 * Them hoac item vao db
	 * */
	public long insertOrUpdateItem(Item item) {
		long output = -1;
		try {
			open();
			output = _insertOrUpdateItem(item);
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateItem", ex);
		}

		return output;
	}

	/**
	 * Them hoac item vao db
	 * */
	public long _insertOrUpdateItem(Item item) {
		Cursor cur = null;
		long output = -1;

		ContentValues cValues = new ContentValues();
		cValues.put(ITEM_ITEM_ID, item.getItemId());
		cValues.put(ITEM_TITLE, item.getTitle());
		cValues.put(ITEM_DESC, item.getDescription());
		cValues.put(ITEM_IMG, item.getImage());
		cValues.put(ITEM_LINK, item.getLink());
		cValues.put(ITEM_ADDPARAM, item.getAddParam());
		cValues.put(ITEM_INDEX, item.getIndex());
		cValues.put(ITEM_GROUP_TYPE, item.getGroupType());
		cValues.put(ITEM_ISACTIVED, item.getIsActived());

		cValues.put(ITEM_GROUP_ID, item.getGroupId());
		cValues.put(ITEM_SAVE_TYPE, item.getSaveType());
		cValues.put(ITEM_SUPPLY_TYPE, item.getSupplyType());
		cValues.put(ITEM_HELP, item.getHelpContent());

		cur = sqlDb.rawQuery(
				"SELECT * FROM " + TABLE_ITEM + " WHERE " + ITEM_ITEM_ID
						+ "=? AND " + GROUP_GROUP_TYPE + "=?",
				new String[] { item.getItemId(),
						String.valueOf(item.getGroupType()) });

		if (cur != null && cur.getCount() > 0) {
			output = sqlDb.update(TABLE_ITEM, cValues, ITEM_ITEM_ID + "=? AND "
					+ GROUP_GROUP_TYPE + "=?", new String[] { item.getItemId(),
					String.valueOf(item.getGroupType()) });
		} else {
			output = sqlDb.insert(TABLE_ITEM, null, cValues);
		}

		if (cur != null)
			cur.close();

		return output;
	}

	/**
	 * 
	 * Them hoac update item vao db, co cap nhat index cua item
	 * 
	 * */
	public long insertOrUpdateItemIndex(Item item) {
		Cursor cur = null;
		long output = -1;
		try {
			open();

			cur = sqlDb.rawQuery(
					"SELECT * FROM " + TABLE_ITEM + " WHERE " + ITEM_ITEM_ID
							+ "!=? AND " + GROUP_GROUP_TYPE + "=?",
					new String[] { item.getItemId(),
							String.valueOf(item.getGroupType()) });

			if (cur != null && cur.getCount() > 0) {
				ContentValues cValues = new ContentValues();
				while (cur.moveToNext()) {
					// update index++ cac group cung loai
					cValues.remove(ITEM_INDEX);
					cValues.put(ITEM_INDEX,
							cur.getInt(cur.getColumnIndex(ITEM_INDEX)) + 1);

					sqlDb.update(TABLE_ITEM, cValues, ITEM_ID + "=?",
							new String[] { String.valueOf(cur.getLong(cur
									.getColumnIndex(ITEM_ID))) });
				}
			}

			output = _insertOrUpdateItem(item);
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateItemIndex", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}

		return output;
	}

	/**
	 * Xoa cac nhom chua duoc kich hoat
	 * */
	public long deleteNotActivedItems(int groupType) {
		long output = 0;
		try {
			open();
			output = sqlDb.delete(
					TABLE_ITEM,
					GROUP_GROUP_TYPE + "=? AND " + ITEM_ISACTIVED + "!=?",
					new String[] { String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "deleteNotActivedItems", ex);
		}
		return output;
	}

	/**
	 * Kich hoat, huy kich hoat
	 * */
	public long activeItems(int groupType, int activeState) {
		long output = 0;
		try {
			open();

			ContentValues cValues = new ContentValues();
			cValues.put(ITEM_ISACTIVED, activeState);
			output = sqlDb.update(TABLE_ITEM, cValues, GROUP_GROUP_TYPE + "=?",
					new String[] { String.valueOf(groupType) });

			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "activeItems", ex);
		}
		return output;
	}

	/**
	 * Lay Item
	 * */
	public Item getItem(String itemId, int groupType) {
		Item item = null;
		Cursor cur = null;
		try {
			open();
			cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_ITEM + " WHERE "
					+ ITEM_ITEM_ID + "=? AND " + GROUP_GROUP_TYPE + "=?",
					new String[] { itemId, String.valueOf(groupType) });

			if (cur.getCount() > 0) {
				int clId = cur.getColumnIndex(ITEM_ID);
				int clItemId = cur.getColumnIndex(ITEM_ITEM_ID);
				int clTitle = cur.getColumnIndex(ITEM_TITLE);
				int clDesc = cur.getColumnIndex(ITEM_DESC);
				int clImage = cur.getColumnIndex(ITEM_IMG);
				int clLink = cur.getColumnIndex(ITEM_LINK);
				int clParam = cur.getColumnIndex(ITEM_ADDPARAM);
				int clIndex = cur.getColumnIndex(ITEM_INDEX);
				int clGroupType = cur.getColumnIndex(ITEM_GROUP_TYPE);
				int clIsActived = cur.getColumnIndex(ITEM_ISACTIVED);
				int clGroupId = cur.getColumnIndex(ITEM_GROUP_ID);
				int clSaveType = cur.getColumnIndex(ITEM_SAVE_TYPE);
				int clSupplyType = cur.getColumnIndex(ITEM_SUPPLY_TYPE);
				int clHelp = cur.getColumnIndex(ITEM_HELP);

				while (cur.moveToNext()) {
					item = new Item();
					item.setId(cur.getInt(clId));
					item.setItemId(cur.getString(clItemId));
					item.setTitle(cur.getString(clTitle));
					item.setDescription(cur.getString(clDesc));
					item.setImage(cur.getString(clImage));
					item.setLink(cur.getString(clLink));
					item.setAddParam(cur.getString(clParam));
					item.setIndex(cur.getInt(clIndex));
					item.setGroupType(cur.getInt(clGroupType));
					item.setIsActived(cur.getInt(clIsActived));

					item.setGroupId(cur.getString(clGroupId));
					item.setSaveType(cur.getInt(clSaveType));
					item.setSupplyType(cur.getInt(clSupplyType));
					item.setHelpContent(cur.getString(clHelp));
					break;
				}
			}			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getItem", ex);
		} finally {
			if(cur != null)
				cur.close();
			close();
		}
		return item;
	}

	/**
	 * Lay danh sach cac group dang duoc actived thuoc groupType
	 * */
	public Vector<Item> getItems(String groupId, int groupType) {
		Vector<Item> vec = null;
		Cursor cur = null;

		try {
			open();
			cur = sqlDb.rawQuery(
					"SELECT * FROM " + TABLE_ITEM + " WHERE " + ITEM_GROUP_ID
							+ "=? AND " + GROUP_GROUP_TYPE + "=? AND "
							+ GROUP_ISACTIVED + "=? ORDER BY " + ITEM_INDEX + " ASC",
					new String[] { groupId, String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });

			if (cur != null && cur.getCount() > 0) {
				vec = new Vector<Item>(cur.getCount());

				while (cur.moveToNext()) {
					Item item = new Item();
					item.setId(cur.getLong(cur.getColumnIndex(ITEM_ID)));
					item.setItemId(cur.getString(cur
							.getColumnIndex(ITEM_ITEM_ID)));
					item.setTitle(cur.getString(cur.getColumnIndex(ITEM_TITLE)));
					item.setDescription(cur.getString(cur
							.getColumnIndex(ITEM_DESC)));
					item.setImage(cur.getString(cur.getColumnIndex(ITEM_IMG)));
					item.setLink(cur.getString(cur.getColumnIndex(ITEM_LINK)));
					item.setAddParam(cur.getString(cur
							.getColumnIndex(ITEM_ADDPARAM)));
					item.setIndex(cur.getInt(cur.getColumnIndex(ITEM_INDEX)));
					item.setGroupType(cur.getInt(cur
							.getColumnIndex(ITEM_GROUP_TYPE)));
					item.setIsActived(cur.getInt(cur
							.getColumnIndex(ITEM_ISACTIVED)));

					item.setGroupId(cur.getString(cur
							.getColumnIndex(ITEM_GROUP_ID)));
					item.setSaveType(cur.getInt(cur
							.getColumnIndex(ITEM_SAVE_TYPE)));
					item.setSupplyType(cur.getInt(cur
							.getColumnIndex(ITEM_SUPPLY_TYPE)));
					item.setHelpContent(cur.getString(cur
							.getColumnIndex(ITEM_HELP)));

					vec.addElement(item);
				}
			}

		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getItems", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}
		return vec;
	}

	// XU LY NEWS

	/**
	 * Them hoac news vao db
	 * */
	public long insertOrUpdateNews(News item) {
		long output = -1;
		try {
			open();
			output = _insertOrUpdateNews(item);
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateNews", ex);
		}

		return output;
	}
	
	/**
	 * Them hoac Question, Kenh vao db
	 * */
	public long insertOrUpdateKQuestion(AnyObject item) {
		long output = -1;
		try {
			open();
			output = _insertOrUpdateKQuestion(item);
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateKQuestion", ex);
		}

		return output;
	}
	
	/**
	 * Them hoac Question, Kenh vao db
	 * */
	private long _insertOrUpdateKQuestion(AnyObject item) {
		Cursor cur = null;
		long output = -1;

		ContentValues cValues = new ContentValues();
		cValues.put(QK_sID, item.getsID());
		cValues.put(QK_sCONTENT, item.getsContent());
		cValues.put(QK_sDESCRIPTION, item.getsDescription());
		cValues.put(QK_sLINK, item.getsLink());
		cValues.put(QK_sTYPE, item.getsType());
		cValues.put(QK_isACTIVE, item.getsIsActive());

		cur = sqlDb.rawQuery(
				"SELECT * FROM " + TABLE_QUESTION_KENH + " WHERE " + QK_sTYPE
						+ "=? AND " + QK_ID + "=?",
				new String[] { item.getsType(), String.valueOf(item.getsID()) });

		if (cur != null && cur.getCount() > 0) {
			output = sqlDb.update(TABLE_QUESTION_KENH, cValues, QK_sTYPE + "=? AND "
					+ QK_ID + "=?", new String[] { item.getsType(),
					String.valueOf(item.getsID()) });
		} else {
			output = sqlDb.insert(TABLE_QUESTION_KENH, null, cValues);
		}
		
		if (cur != null)
			cur.close();

		return output;
	}
	

	/**
	 * Them hoac news vao db
	 * */
	private long _insertOrUpdateNews(News item) {
		Cursor cur = null;
		long output = -1;

		ContentValues cValues = new ContentValues();
		cValues.put(NEWS_NEWS_ID, item.getNewsId());
		cValues.put(NEWS_TITLE, item.getTitle());
		cValues.put(NEWS_DESC, item.getDescription());
		cValues.put(NEWS_IMG, item.getImage());
		cValues.put(NEWS_LINK, item.getLink());
		cValues.put(NEWS_ADDPARAM, item.getAddParam());
		cValues.put(NEWS_INDEX, item.getIndex());
		cValues.put(NEWS_GROUP_TYPE, item.getGroupType());
		cValues.put(NEWS_ISACTIVED, item.getIsActived());
		cValues.put(NEWS_GROUP_ID, item.getGroupId());

		cur = sqlDb.rawQuery(
				"SELECT * FROM " + TABLE_NEWS + " WHERE " + NEWS_NEWS_ID
						+ "=? AND " + GROUP_GROUP_TYPE + "=?",
				new String[] { item.getNewsId(),
						String.valueOf(item.getGroupType()) });

		if (cur != null && cur.getCount() > 0) {
			output = sqlDb.update(TABLE_NEWS, cValues, NEWS_NEWS_ID + "=? AND "
					+ GROUP_GROUP_TYPE + "=?", new String[] { item.getNewsId(),
					String.valueOf(item.getGroupType()) });
		} else {
			output = sqlDb.insert(TABLE_NEWS, null, cValues);
		}
		
		if (cur != null)
			cur.close();

		return output;
	}

	/**
	 * 
	 * Them hoac update news vao db, co cap nhat index cua news
	 * 
	 * */
	public long insertOrUpdateNewsIndex(News item) {
		long output = -1;
		try {
			open();
			output = insertOrUpdateNews(item);
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "insertOrUpdateNewsIndex", ex);
		}

		return output;
	}

	/**
	 * Xoa cac news chua duoc kich hoat
	 * */
	public long deleteNotActivedNews(int groupType) {
		long output = 0;
		try {
			open();
			output = sqlDb.delete(
					TABLE_NEWS,
					GROUP_GROUP_TYPE + "=? AND " + NEWS_ISACTIVED + "!=?",
					new String[] { String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "deleteNotActivedNews", ex);
		}
		return output;
	}

	/**
	 * Kich hoat, huy kich hoat
	 * */
	public long activeNews(int groupType, int activeState) {
		long output = 0;
		try {
			open();

			ContentValues cValues = new ContentValues();
			cValues.put(NEWS_ISACTIVED, activeState);
			output = sqlDb.update(TABLE_NEWS, cValues, GROUP_GROUP_TYPE + "=?",
					new String[] { String.valueOf(groupType) });

			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "activeNews", ex);
		}
		return output;
	}
	
	
	/**
	 * Kich hoat, huy kich hoat Question
	 * */
	public long activeQuestion_Kenh(int type, int activeState) {
		long output = 0;
		try {
			open();

			ContentValues cValues = new ContentValues();
			cValues.put(QK_isACTIVE, activeState);
			output = sqlDb.update(TABLE_QUESTION_KENH, cValues, QK_sTYPE + "=?",
					new String[] { String.valueOf(type) });

			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "activeQuestion_Kenh", ex);
		}
		return output;
	}
	
	/**
	 * Xoa cac Question, Kenh chua duoc kich hoat
	 * */
	public long deleteNotActivedKQuestion(int groupType) {
		long output = 0;
		try {
			open();
			output = sqlDb.delete(
					TABLE_QUESTION_KENH,
					QK_sTYPE + "=? AND " + QK_isACTIVE + "!=?",
					new String[] { String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });
			close();
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "deleteNotActivedKQuestion", ex);
		}
		return output;
	}
	
	/**
	 * Lay danh sach cac news dang duoc actived thuoc groupType
	 * */
	public Vector<AnyObject> getKQuestion(int stype) {
		Vector<AnyObject> vec = null;
		Cursor cur = null;

		try {
			open();
			cur = sqlDb.rawQuery(
					"SELECT * FROM " + TABLE_QUESTION_KENH + " WHERE " + QK_sTYPE
							+ "=? AND "	+ QK_isACTIVE + "=? ORDER BY " + QK_ID + " ASC",
					new String[] { String.valueOf(stype), String.valueOf(DB_IS_ACTIVED) });
			
			
			if (cur != null && cur.getCount() > 0) {
				vec = new Vector<AnyObject>(cur.getCount());

				while (cur.moveToNext()) {
					
					AnyObject kQuestion = new AnyObject();
					String sID = cur.getString(cur.getColumnIndex(QK_ID));
					kQuestion.setsID(sID.length() == 1 ? "0"+sID : sID);
					kQuestion.setsContent(cur.getString(cur.getColumnIndex(QK_sCONTENT)));
					kQuestion.setsDescription(cur.getString(cur.getColumnIndex(QK_sDESCRIPTION)));
					kQuestion.setsLink(cur.getString(cur.getColumnIndex(QK_sLINK)));
					kQuestion.setsType(cur.getString(cur.getColumnIndex(QK_sTYPE)));
					kQuestion.setsIsActive(cur.getString(cur.getColumnIndex(QK_isACTIVE)));	
					vec.addElement(kQuestion);
				}
			}
			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getKQuestion", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}
		return vec;
	}
	

	/**
	 * Lay danh sach cac news dang duoc actived thuoc groupType
	 * */
	public Vector<News> getNews(String groupId, int groupType) {
		Vector<News> vec = null;
		Cursor cur = null;

		try {
			open();
			cur = sqlDb.rawQuery(
					"SELECT * FROM " + TABLE_NEWS + " WHERE " + NEWS_GROUP_ID
							+ "=? AND " + NEWS_GROUP_TYPE + "=? AND "
							+ NEWS_ISACTIVED + "=? ORDER BY " + NEWS_INDEX + " ASC",
					new String[] { groupId, String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });

			if (cur != null && cur.getCount() > 0) {
				vec = new Vector<News>(cur.getCount());

				while (cur.moveToNext()) {
					News item = new News();
					item.setId(cur.getLong(cur.getColumnIndex(NEWS_ID)));
					item.setNewsId(cur.getString(cur
							.getColumnIndex(NEWS_NEWS_ID)));
					item.setTitle(cur.getString(cur.getColumnIndex(NEWS_TITLE)));
					item.setDescription(cur.getString(cur
							.getColumnIndex(NEWS_DESC)));
					item.setImage(cur.getString(cur.getColumnIndex(NEWS_IMG)));
					item.setLink(cur.getString(cur.getColumnIndex(NEWS_LINK)));
					item.setAddParam(cur.getString(cur
							.getColumnIndex(NEWS_ADDPARAM)));
					item.setIndex(cur.getInt(cur.getColumnIndex(NEWS_INDEX)));
					item.setGroupType(cur.getInt(cur
							.getColumnIndex(NEWS_GROUP_TYPE)));
					item.setIsActived(cur.getInt(cur
							.getColumnIndex(NEWS_ISACTIVED)));

					item.setGroupId(cur.getString(cur
							.getColumnIndex(NEWS_GROUP_ID)));

					vec.addElement(item);
				}
			}
			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getNews", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}
		return vec;
	}

	/**
	 * Lay danh sach cac news dang duoc actived thuoc groupType
	 * */
	public Vector<News> getNews(int groupType) {
		Vector<News> vec = null;
		Cursor cur = null;

		try {
			open();
			cur = sqlDb.rawQuery(
					"SELECT * FROM " + TABLE_NEWS + " WHERE " + NEWS_GROUP_TYPE
							+ "=? AND " + NEWS_ISACTIVED + "=? ORDER BY " + NEWS_INDEX + " ASC",
					new String[] { String.valueOf(groupType),
							String.valueOf(DB_IS_ACTIVED) });

			if (cur != null && cur.getCount() > 0) {
				vec = new Vector<News>(cur.getCount());

				while (cur.moveToNext()) {
					News item = new News();
					item.setId(cur.getLong(cur.getColumnIndex(NEWS_ID)));
					item.setNewsId(cur.getString(cur
							.getColumnIndex(NEWS_NEWS_ID)));
					item.setTitle(cur.getString(cur.getColumnIndex(NEWS_TITLE)));
					item.setDescription(cur.getString(cur
							.getColumnIndex(NEWS_DESC)));
					item.setImage(cur.getString(cur.getColumnIndex(NEWS_IMG)));
					item.setLink(cur.getString(cur.getColumnIndex(NEWS_LINK)));
					item.setAddParam(cur.getString(cur
							.getColumnIndex(NEWS_ADDPARAM)));
					item.setIndex(cur.getInt(cur.getColumnIndex(NEWS_INDEX)));
					item.setGroupType(cur.getInt(cur
							.getColumnIndex(NEWS_GROUP_TYPE)));
					item.setIsActived(cur.getInt(cur
							.getColumnIndex(NEWS_ISACTIVED)));

					item.setGroupId(cur.getString(cur
							.getColumnIndex(NEWS_GROUP_ID)));

					vec.addElement(item);
				}
			}
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getNews", ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}

		return vec;
	}

	/**
	 * Tim news khuyen mai cua itemId
	 * */
	public News getSpecialNews(String itemId) {
		News item = null;
		Cursor cur = null;

		try {
			open();
			cur = sqlDb.rawQuery("SELECT * FROM " + TABLE_NEWS + " WHERE "
					+ NEWS_ISACTIVED + "=?" + " AND " + NEWS_NEWS_ID
					+ " LIKE '%" + itemId + "%'",
					new String[] { String.valueOf(DB_IS_ACTIVED) });

			if (cur != null && cur.getCount() > 0) {
				String tempId = "";
				String[] arrId = null;
				while (cur.moveToNext() && item == null) {
					tempId = cur.getString(cur.getColumnIndex(NEWS_NEWS_ID));
					arrId = tempId.split(",");

					if (arrId != null) {
						for (int i = 0; i < arrId.length; i++) {
							if (arrId[i].equalsIgnoreCase(itemId)) {
								item = new News();
								item.setId(cur.getLong(cur
										.getColumnIndex(NEWS_ID)));
								item.setNewsId(cur.getString(cur
										.getColumnIndex(NEWS_NEWS_ID)));
								item.setTitle(cur.getString(cur
										.getColumnIndex(NEWS_TITLE)));
								item.setDescription(cur.getString(cur
										.getColumnIndex(NEWS_DESC)));
								item.setImage(cur.getString(cur
										.getColumnIndex(NEWS_IMG)));
								item.setLink(cur.getString(cur
										.getColumnIndex(NEWS_LINK)));
								item.setAddParam(cur.getString(cur
										.getColumnIndex(NEWS_ADDPARAM)));
								item.setIndex(cur.getInt(cur
										.getColumnIndex(NEWS_INDEX)));
								item.setGroupType(cur.getInt(cur
										.getColumnIndex(NEWS_GROUP_TYPE)));
								item.setIsActived(cur.getInt(cur
										.getColumnIndex(NEWS_ISACTIVED)));
								item.setGroupId(cur.getString(cur
										.getColumnIndex(NEWS_GROUP_ID)));
								break;
							}
						}
					}
				}
			}			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "getSpecialNews",  ex);
		} finally {
			if (cur != null)
				cur.close();
			close();
		}

		return item;
	}
}
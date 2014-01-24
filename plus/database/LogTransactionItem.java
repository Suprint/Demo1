package com.mpay.plus.database;

/**
 * @author quyenlm.vn@gmail.com
 * 
 *         Mo ta mot log giao dich
 * */
public class LogTransactionItem extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long mId;
	private String mLogTime;
	private String mSeqNo;
	private String mTranCode;
	private String mAmount;
	private String mDstNo;
	private String mResult;
	private String mAddParam;
	private String mAmount_cost;

	public LogTransactionItem() {
		super("", "", "");

		mId = -1;
		mLogTime = "";
		mSeqNo = "";
		mTranCode = "";
		mAmount = "";
		mResult = "";
		mDstNo = "";
		mAddParam = "";
	}

	public LogTransactionItem(String title, String description, String img) {
		super(title, description, img);

		mId = -1;
		mLogTime = "";
		mSeqNo = "";
		mTranCode = "";
		mAmount = "";
		mResult = "";
		mDstNo = "";
		mAddParam = "";
	}

	public String getDestinationNo() {
		return mDstNo;
	}

	public void setDestinationNo(String destination) {
		this.mDstNo = destination;
	}

	public String getAmount() {
		return mAmount;
	}

	public void setAmount(String amount) {
		this.mAmount = amount;
	}

	public String getTranCommand() {
		return mTranCode;
	}

	public void setTranCommand(String tranCode) {
		this.mTranCode = tranCode;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public String getLogTime() {
		return mLogTime;
	}

	public void setLogTime(String logTime) {
		this.mLogTime = logTime;
	}

	public String getSeqNo() {
		return mSeqNo;
	}

	public void setSeqNo(String seqNo) {
		this.mSeqNo = seqNo;
	}

	public String getResult() {
		return mResult;
	}

	public void setResult(String result) {
		this.mResult = result;
	}

	public String getAddParam() {
		return mAddParam;
	}

	public void setAddParam(String addParam) {
		this.mAddParam = addParam;
	}

	public void setmAmount_cost(String mAmount_cost) {
		this.mAmount_cost = mAmount_cost;
	}

	public String getmAmount_cost() {
		return mAmount_cost;
	}

	@Override
	public String toString() {
		return mTitle + ": " + mDescription;
	}
}
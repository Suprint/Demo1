package com.mpay.plus.database;


/**
 * @author quyenlm.vn@gmail.com
 * 
 * Mo ta log giao dich thanh toan hoa don/Bill Payment
 * 
 * */
public class LogBillItem extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long mId;
	private String mLogTime;
    private String mSeqNo;
    private String mAmount;
    private String mBillCode;
    private String mResult;
    private String mAddParam;
    private String mSupplierId;
        
    public LogBillItem() {
    	super("", "", "");
    	
    	mId = -1;
    	mLogTime = "";
        mSeqNo = "";
        mAmount = "";
        mBillCode = "";
        mResult = "";
        mAddParam = "";
        mSupplierId = "";
    }

    public LogBillItem(String title, String description, String img) {
    	super(title, description, img);
    	
    	mId = -1;
    	mLogTime = "";
        mSeqNo = "";
        mAmount = "";
        mBillCode = "";
        mResult = "";
        mAddParam = "";
        mSupplierId = "";
    }

    
    public String getBillCode() {
        return mBillCode;
    }

    public void setBillCode(String billCode) {
    	this.mBillCode = billCode;
    }
    
    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
    	this.mAmount = amount;
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
    
    public String getSupplierId() {
        return mSupplierId;
    }

    public void setSupplierId(String supplierId) {
    	this.mSupplierId = supplierId;
    }
    
    @Override
    public String toString() {
        return mTitle + ": " + mDescription;
    }
}
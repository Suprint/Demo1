package com.mpay.plus.database;


/**
 * @author quyenlm.vn@gmail.com
 * 
 * Mo ta mot loai san pham (Viettel Card,...) hoac mot nha cung cap (EVN), hoac mot chuc nang (Chuyen khoan, sao ke),...
 * */
public class Product extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long mId;
	private String mTime;
    private String mProductCode;
    private String mPrice;
    private String mAddParam;
    private String mSeri;
    private String mAccNo;
    private String mPin;
    private String mExDate;
    private String mHotline;
    private int mIsUsed;
    private boolean mIsSelected;
    
    public Product() {
    	super("", "", "");
    	
    	mId = -1;
    	mTime = "";
        mProductCode = "";
        mPrice = "";
        mAddParam = "";
        mSeri = "";
        mAccNo = "";
        mPin = "";        
        mExDate = "";
        mHotline = "";
        
        mIsUsed = 0;
    }

    public Product(String title, String description, String img) {
    	super(title, description, img);
    	
    	mId = -1;
    	mTime = "";
        mProductCode = "";
        mPrice = "";
        mAddParam = "";
        mSeri = "";
        mAccNo = "";
        mPin = "";
        mExDate = "";
        mHotline = "";
        
        mIsUsed = 0;
    }

    
    public String getAddParam() {
        return mAddParam;
    }

    public void setAddParam(String addParam) {
    	this.mAddParam = addParam;
    }
    
    public String getHotLine() {
        return mHotline;
    }
    
    public void setHotLine(String hotline) {
    	this.mHotline = hotline;
    }
    
    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
    	this.mPrice = price;
    }
    
    public long getId() {
        return mId;
    }
    
    public void setId(long id) {
    	this.mId = id;
    }
        
    public String getPin() {
        return mPin;
    }

    public void setPin(String pin) {
        this.mPin = pin;
    }
    
    public int getIsUsed() {
        return mIsUsed;
    }

    public void setIsUsed(int isUsed) {
    	this.mIsUsed = isUsed;
    }
    
    public String getExDate() {
        return mExDate;
    }
    
    public void setExDate(String exDate) {
    	this.mExDate = exDate;
    }
        
    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }
    
    public String getProductCode() {
        return mProductCode;
    }

    public void setProductCode(String product) {
    	this.mProductCode = product;
    }
    
    public String getSeri() {
        return mSeri;
    }
    
    public void setSeri(String seri) {
    	this.mSeri = seri;    			
    }
        
    public String getAccNo() {
        return mAccNo;
    }

    public void setAccNo(String accNo) {
        this.mAccNo = accNo;
    }
    
    public boolean getmIsSelected() {
        return mIsSelected;
    }
    
    public void setmIsSelected(boolean mIsSelected) {
		this.mIsSelected = mIsSelected;
	}
    
//    public void setAccNo(boolean isSelected) {
//        this.mIsSelected = isSelected;
//    }
    
    @Override
    public String toString() {
        return mTitle + ": " + mDescription;
    }
}
package com.mpay.plus.database;

/**
 * @author quyenlm.vn@gmail.com
 * 
 *         Mo ta mot loai san pham (Viettel Card,...) hoac mot nha cung cap
 *         (EVN), hoac mot chuc nang (Chuyen khoan, sao ke),...
 * */
public class Item extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long mId;
	private String mItemId;
	private String mLink;
	private String mHelpContent;
	private String mAddParam;
	private int mSaveType;
	private int mSupplyType;
	private int mIndex;
	private int mIsActived;
	private boolean mIsSpecial;
	private String mGroupId;
	private int mGroupType;
	private String mResult;

	public Item() {
		super("", "", "");

		mId = -1;
		mItemId = "";
		mLink = "";
		mHelpContent = "";
		mAddParam = "";
		mSaveType = -1;
		mSupplyType = -1;
		mIndex = -1;
		mIsActived = -1;
		mIsSpecial = false;
		mGroupId = "";
		mGroupType = -1;
		mResult = "";
	}

	public Item(String title, String description, String img) {
		super(title, description, img);

		mId = -1;
		mItemId = "";
		mLink = "";
		mHelpContent = "";
		mAddParam = "";
		mSaveType = -1;
		mSupplyType = -1;
		mIndex = -1;
		mIsActived = -1;
		mIsSpecial = false;
		mGroupId = "";
		mGroupType = -1;
	}

	public String getAddParam() {
		return mAddParam;
	}

	public void setAddParam(String addParam) {
		this.mAddParam = addParam;
	}

	public String getGroupId() {
		return mGroupId;
	}

	public void setGroupId(String groupId) {
		this.mGroupId = groupId;
	}

	public int getGroupType() {
		return mGroupType;
	}

	public void setGroupType(int groupType) {
		this.mGroupType = groupType;
	}

	public String getHelpContent() {
		return mHelpContent;
	}

	public void setHelpContent(String helpContent) {
		this.mHelpContent = helpContent;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public int getIndex() {
		return mIndex;
	}

	public void setIndex(int index) {
		this.mIndex = index;
	}

	public int getIsActived() {
		return mIsActived;
	}

	public void setIsActived(int isActived) {
		this.mIsActived = isActived;
	}

	public boolean getIsSpecial() {
		return mIsSpecial;
	}

	public void setIsSpecial(boolean isSpecial) {
		this.mIsSpecial = isSpecial;
	}

	public String getItemId() {
		return mItemId;
	}

	public void setItemId(String itemId) {
		this.mItemId = itemId;
	}

	public String getLink() {
		return mLink;
	}

	public void setLink(String link) {
		this.mLink = link;
	}

	public int getSaveType() {
		return mSaveType;
	}

	public void setSaveType(int saveType) {
		this.mSaveType = saveType;
	}

	public int getSupplyType() {
		return mSupplyType;
	}

	public void setSupplyType(int supplyType) {
		this.mSupplyType = supplyType;
	}

	public String getmResult() {
		return mResult;
	}

	public void setmResult(String mResult) {
		this.mResult = mResult;
	}

	@Override
	public String toString() {
		return mTitle + ": " + mDescription;
	}
}
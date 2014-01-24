package com.mpay.plus.database;


/**
 * @author quyenlm.vn@gmail.com
 * 
 * Mo ta mot nhom san pham (Mobile Card, Game Card,...) hoac mot nhom nha cung cap (Electricity Supplier), hoac mot nhom chuc nang (Bank, MPlus),...
 * */
public class Group extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long mId;
    private String mGroupId;
    private String mLink;
    private String mAddParam;
    private int mIndex;
    private int mGroupType;
    private int mIsActived;
    
    public Group() {
    	super("", "", "");
    	mId = -1;
        mLink = "";
        mAddParam = "";
        mIndex = -1;
        mIsActived = -1;
        mGroupId = "";
        mGroupType = -1;
    }

    public Group(String title, String description, String img) {
    	super(title, description, img);
    	
    	mId = -1;
    	mLink = "";
        mAddParam = "";
        mIndex = -1;
        mIsActived = -1;
        mGroupId = "";
        mGroupType = -1;
    }
      
    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
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
              
    @Override
    public String toString() {
        return mTitle + ": " + mDescription;
    }
}
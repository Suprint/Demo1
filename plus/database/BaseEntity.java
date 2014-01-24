package com.mpay.plus.database;

import java.io.Serializable;

/**
 * @author quyenlm.vn@gmail.com
 * 
 * Lop co so cho cac entity
 * Thuc thi giao dien Serializable, co kha nang tuan tu hoa
 * */
public class BaseEntity implements Serializable {
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	
	protected String mTitle;
	protected String mDescription;
    protected String mImage;
    
    public BaseEntity() {
    	mTitle = "";
    	mDescription = "";
    	mImage = "";
    }
    
    public BaseEntity(String title, String description, String img) {
    	mTitle = title;
    	mDescription = description;
    	mImage = img;
    }
    
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
    	this.mTitle = title;
    }
    
    public String getDescription() {
        return mDescription;
    }
    
    public void setDescription(String description) {
    	this.mDescription = description;
    }
        
    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }
}
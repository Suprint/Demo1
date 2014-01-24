package com.mpay.plus.database;

import java.util.Vector;


/**
 * @author quyenlm.vn@gmail.com
 * 
 * Mo ta thong tin xac nhan mot giao dich
 * */
public class ConfirmItem extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<Item> mItems = null;
	
    public ConfirmItem() {
    	super("", "", "");
    }

    public ConfirmItem(String title, String description, String img) {
    	super(title, description, img);
    }
    
    public Vector<Item> getItems() {
        return mItems;
    }

    public void setItems(Vector<Item> items) {
    	this.mItems = items;
    }
        
    @Override
    public String toString() {
        return mTitle + ": " + mDescription;
    }
}
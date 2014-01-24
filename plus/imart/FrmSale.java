package com.mpay.plus.imart;

import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.FunctionType;
import com.mpay.MPFragmentAdapter;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.plus.config.Config;
import com.mpay.plus.database.DBAdapter;
import com.mpay.plus.database.Group;
import com.mpay.plus.database.Item;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;
//import com.viewpagerindicator.TitlePageIndicator;

/**
 * Hien thi dang sach cac san pham
 * 
 * @author quyenlm.vn@gmail.com
 * 
 * */

public class FrmSale extends AMPlusCore implements IProcess {
	public final static String TAG = "FrmSale";
	
    private MPFragmentAdapter mAdapter;
    private ViewPager mPager;    
	private ViewTitlePaper mIndicator;
    private Vector<Group> mMenu;
    public static Item mItem;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_fragment_view_pager);               
        
        AMPlusCore.CurFuntion = FunctionType.SALE;
        
        getSupportActionBar().setIcon(R.drawable.icon_navigation_previous_item);
		getSupportActionBar().setHomeButtonEnabled(true);
		
        try{
	        //GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK#GRCD|GRNM|GRDE|GRLK!PDCD|PDNM|PDDE|PDLK!PDCD|PDNM|PDDE|PDLK
	        boolean flag = true;
        	while(mMenu == null && flag) {
        		mMenu = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_PRODUCT);
    			
    	        if(mMenu == null){
    	        	//init default menu
    	        	if(User.sLang.equals(Config.LANG_EN)){
    	        		saveMenuProduct(Config.DEFAULT_MENU_CARD[1]);
    	        		saveUserTable();
    	        	} else {
    	        		saveMenuProduct(Config.DEFAULT_MENU_CARD[0]);
    	        		saveUserTable();
    	        	}
    	        	
    	        	mMenu = getDba().getGroups(DBAdapter.DB_GROUP_TYPE_PRODUCT);
    	        	flag = false;
    	        }
        	}
				        
	        mAdapter = new MPFragmentAdapter(getSupportFragmentManager(), mMenu);
	        mAdapter.setProcess(FrmSale.this);
	        mPager = (ViewPager)findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);
	        
	        mIndicator = (ViewTitlePaper)findViewById(R.id.indicator);
	        mIndicator.setViewPager(mPager);
        } catch (Exception ex) {
        	MPlusLib.debug(TAG, "setControls", ex);
		}
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Khoi tao menu
		getSupportMenuInflater().inflate(R.menu.sale, menu);
		return true;
	}
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;
		
		if(!isBusy){
			// Xu ly menu
			switch (item.getItemId()) {
			case android.R.id.home:
				goBack();
				/*toggle();*/
				break;
				
			case R.id.purchased_product:
				ACTION = iGET_LOG;
				onCreateMyDialog(MPIN).show();
				break;
				
			default:
				result = super.onMenuItemSelected(featureId, item);
				break;
			}
		} else result = false;
		
		return result;
	};
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
           
    @Override public void cmdNextMPIN() {
    	if(ACTION == iGET_LOG) {
    		Intent intent = new Intent(this, FrmPurcharsedProduct.class);
			Bundle sendBundle = new Bundle();
			sendBundle.putByte("islog", (byte) 0);
			intent.putExtras(sendBundle);
			startActivityForResult(intent, 0);
    	}
    };
    
    //#mark Connect
   	public void processDataSend(byte iTag){
  		switch (iTag) {
		case iGET_DETAIL:
			if(mItem != null)
				ServiceCore.TaskSearchProduct("+" + mItem.getItemId());
			break;
		}
  	}
  	
  	public void processDataReceived(String sDataReceived,byte iTag, byte iTagErr){
  		switch (iTag) {
		case iGET_DETAIL:
			sThongBao = "";
			if (sDataReceived.startsWith("val:")) {
	  			sDataReceived = Util.sCatVal(sDataReceived);
	  			Bundle sendBundle = new Bundle();
	  	      	Intent intent = new Intent(FrmSale.this, FrmProductDetail.class);
    			//pdcd|pdnm|pdgrcd|mxvl|price|promo
	  			sendBundle.putString("value", sDataReceived.split("#")[0]);
	  	      	intent.putExtras(sendBundle);
	  	      	intent.putExtra("item", mItem);
	  	      	startActivity(intent);
	  		}
	  		else{
	  			sThongBao = Util.sCatVal(sDataReceived);
	  			onCreateMyDialog(THONG_BAO).show();
	  		}
			break;
		}
  	}
}
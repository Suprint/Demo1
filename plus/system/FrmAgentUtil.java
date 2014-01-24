package com.mpay.plus.system;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.plus.database.User;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.Util;

/**
 * 
 *
 * @author quyenlm.vn@gmail.com
 * 
 */
public class FrmAgentUtil extends AMPlusCore implements IProcess {
	private ListView lvMenu = null;
	private int iType = 0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_list);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.icon_navigation_previous_item);
		
		setControls();
	}
	
	private void setControls(){
		try {			
			lvMenu = (ListView) findViewById(R.id.lvcontent);
			
			if(iType == 0){
				//Goi hotline
				this.setTitle(getString(R.string.hotline_name));
				
				String[] arr = new String[User.arrHotline.length];
				for(int i = 0; i < User.arrHotline.length; i++){
					arr[i] = getString(R.string.hotline_name) + ": " + Util.formatNumbersAsMid(User.arrHotline[i]);
				}
				
				lvMenu.setAdapter(new ArrayAdapter<String>(this, R.layout.old_tvitem, arr));
				lvMenu.setSelection(0);
				
				lvMenu.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						if(position < User.arrHotline.length){
							try{
							Intent callIntent = new Intent(Intent.ACTION_VIEW);
							callIntent.setData(Uri.parse("tel:" + Uri.encode(User.arrHotline[position])));
							startActivity(callIntent);
							} catch (Exception ex) {
								MPlusLib.debug(TAG, "setControls", ex);
							}
						}
					}
				});
			} else {
				// Gui, nhan y kien dong gop
				this.setTitle(getString(R.string.suggestions));
				
				String[] arr = new String[]{getString(R.string.suggestions_send), getString(R.string.suggestions_receive)};
				lvMenu.setAdapter(new ArrayAdapter<String>(this, R.layout.old_tvitem, arr));
				lvMenu.setSelection(0);
				
				lvMenu.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						switch (position) {
						case 0:
							Intent callIntent = new Intent(FrmAgentUtil.this, FrmSuggestions.class);
							startActivity(callIntent);
							break;

						case 1:
							break;
						}
					}
				});
			}
			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
	}
	
	public void goBack (){
		lvMenu = null;
		
		super.goBack();
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
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString("tab", mTabHost.getCurrentTabTag());
    }
    
 // #mark Connect
 	public void processDataSend(byte iTag) {
 		ServiceCore.TaskSendOrReceiveSuggestion("");
 	}
 	
// 	/**
//	 * Lay thong tin
//	 * */
//	private void parseConversation(String data){
//		int len = data.length();
//		if(len > 0 && data.charAt(len - 1) == '#'){
//			data = data.substring(0, len - 1);
//			len = data.length();
//		}
//							
//		if(len > 0){
//			String allData[] = MpLib.split(data, "#");
//			if(allData != null && allData.length > 0){					
//				int i;
//				String[] conv; //conversation
//									
//				len = allData.length;
//				cmt = new String[len]; // gop y
//				sdtm = new String[len]; // thoi gian gui gop y
//				answ = new String[len]; // cau tra loi
//				astm = new String[len]; // thoi gian tra loi
//				
//				for(i = 0; i < len; i++){
//					if(allData[i].length() > 0){
//						conv = MpLib.split(allData[i], "|");
//						if(conv != null && conv.length > 3){
//							cmt[i] = conv[0];
//							sdtm[i] = conv[1];
//							if(conv[2] == null || conv[2].length()==0){
//								answ[i] = Locale.get("idea.pending");
//								astm[i] = "";	
//							} else {
//								answ[i] = conv[2];
//								astm[i] = conv[3];	
//							}							
//						}
//					}
//				}
//			}
//		}
//	}
	
 	public void processDataReceived(String dataReceive, byte iTag, byte iTagErr) {
//		Trả về:
//		val:AES(comm1|sdtm1|answ1|astm1#comm2|sdtm2|answ2|astm2)#time
//		err:AES(Thông báo lỗi)#time
//		Nếu answ=null thì astm=sdtm và answ=M-Pay đang xem xét ý kiến của Quý khách. Xin trân trọng cảm ơn!
//		dataReceive = "val:Them xo so vao di|12/12/2012|uk thi them|23/12/2012#Them tien vao di|12/12/2012|uk thi them tien|23/12/2012#Them toan vao di|12/12/2012||#";
//		dataReceive = "val:" +  MpLib.sCatVal(dataReceive);
 		String sData = "";
		if (dataReceive.startsWith("val:")) {		
			sData = Util.sCatVal(dataReceive);
			if(sData.equals("null")){
				sThongBao = getString(R.string.suggestions_notfound);
	 			onCreateMyDialog(THONG_BAO).show();
			} else {
//				parseConversation(sData);							
//				if(cmt != null && cmt.length > 0){
//					MPayment.switchDisplay(null, getMenuYKien());
//				} else {
//					clearSubItem();
//					MPayment.ROOT.getMenuTienIch().getMenuYKienDongGop().clearSubItem();
//					System.gc();
//					MPayment.switchDisplay(MpLib.showAlert(Locale.get("messages.notfoundconver"), false),
//							MPayment.ROOT.getMenuTienIch().getMenuYKienDongGop());
//				}
			}
									
		} else {
 			sThongBao = Util.sCatVal(dataReceive);
 			onCreateMyDialog(THONG_BAO).show();
 		}
 	}
}

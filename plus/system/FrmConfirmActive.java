package com.mpay.plus.system;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mpay.agent.R;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.mplus.FrmMain;

public class FrmConfirmActive extends SherlockActivity {
	public static final String TAG = "FrmConfirmActive";
//	private Drawable toRecycle;
	private Button btn_tieptuc = null;
	private ImageView imageback = null;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	    Window window = getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setTheme(R.style.Theme_MPay_Camera);
		setContentView(R.layout.old_dialog_confirm_active);

//		try {
//			getSupportActionBar().setHomeButtonEnabled(true);
//			getSupportActionBar().setIcon(
//					R.drawable.icon_navigation_previous_item, false);
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "onCreate", ex);
//		}

		setControls();
	}

	private void setControls() {
		try {
			setTitle(R.string.title_dangky);
//			ImageView imageView = (ImageView) findViewById(R.id.id_splash);
			btn_tieptuc = (Button) findViewById(R.id.btn_tieptuc);
//			toRecycle = imageView.getDrawable();
			imageback = (ImageView) findViewById(R.id.imageback);
			imageback.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle sendBundle = new Bundle();
					Intent intent = null;
					clearItem();
					finish();
					sendBundle.putBoolean("value", false);
					intent = new Intent(FrmConfirmActive.this, FrmMain.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtras(sendBundle);
					startActivity(intent);
					System.gc();
				}
			});
			
			btn_tieptuc.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Bundle sendBundle = new Bundle();
					Intent intent = null;
					clearItem();
					finish();

					sendBundle.putBoolean("value", false);
					intent = new Intent(FrmConfirmActive.this, FrmRegistration.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtras(sendBundle);
					startActivity(intent);
					System.gc();
				}
			});
			
		} catch (Exception ex) {
			MPlusLib.debug(TAG, "setControls", ex);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Khoi tao menu
//		getSupportMenuInflater().inflate(R.menu.next, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;

		Bundle sendBundle = new Bundle();
		Intent intent = null;
		// Xu ly menu
		switch (item.getItemId()) {
		case android.R.id.home:
			clearItem();
			finish();
			sendBundle.putBoolean("value", false);
			intent = new Intent(FrmConfirmActive.this, FrmMain.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtras(sendBundle);
			startActivity(intent);
			System.gc();
			break;

//		case R.id.next:
//			clearItem();
//			finish();
//
//			sendBundle.putBoolean("value", false);
//			intent = new Intent(FrmConfirmActive.this, FrmRegistration.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.putExtras(sendBundle);
//			startActivity(intent);
//			System.gc();
//			break;

		default:
			result = super.onMenuItemSelected(featureId, item);
			break;
		}

		return result;
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			clearItem();
			finish();
			Bundle sendBundle = new Bundle();
			sendBundle.putBoolean("value", false);
			Intent intent = new Intent(FrmConfirmActive.this, FrmMain.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtras(sendBundle);
			startActivity(intent);
			System.gc();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void clearItem() {
	}

	public void onDestroy() {
		clearItem();
//		try {
//			if (toRecycle != null) {
//				((BitmapDrawable) toRecycle).getBitmap().recycle();
//			}
//			toRecycle = null;
//		} catch (Exception ex) {
//			MPlusLib.debug(TAG, "onDestroy", ex);
//		}
		System.gc();
		Runtime.getRuntime().gc();
		super.onDestroy();
	}
}
package com.mpay.plus.banks;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mpay.agent.R;
import com.mpay.business.IProcess;
import com.mpay.business.ServiceCore;
import com.mpay.business.ThreadThucThi;
import com.mpay.plus.database.AddressLink;
import com.mpay.plus.mplus.AMPlusCore;
import com.mpay.plus.util.GMapV2Direction;

public class FrmAddressMLink extends AMPlusCore implements IProcess{

	private LatLng myLocation = null;
	private GoogleMap map = null;
	private GMapV2Direction md;
	private List<AddressLink> listAddress = null;
	private Polyline poly = null;
	
	private final int maxLevel  = 100000000;
	private int betweenKm = maxLevel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setTheme(R.style.Theme_MPay_Camera);
		setContentView(R.layout.old_bk_mlink);
		
		setUpMapIfNeeded();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_option_map, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.op2km:
			betweenKm = 3;
			break;
		case R.id.op5km:
			betweenKm = 6;
			break;
		case R.id.op10km:
			betweenKm = 11;
			break;
		case R.id.op20km:
			betweenKm = 21;
			break;
		case R.id.op50km:
			betweenKm = 51;
			break;
		case R.id.op100km:
			betweenKm = 101;
			break;
		case R.id.opAllkm:
			betweenKm = maxLevel;
			break;
		default:
			break;
		}

		if(map != null)
			map.clear();
			setData();
		return super.onOptionsItemSelected(item);
	}

	private void setUpMapIfNeeded() {
		if (map == null) {
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (map != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		md = new GMapV2Direction();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = service.getBestProvider(criteria, false);
		Location location = service.getLastKnownLocation(provider);
		myLocation = new LatLng(location.getLatitude(), location.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));

		Marker mar = map.addMarker(new MarkerOptions()
		.position(myLocation)
		.title("My Location")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
		.anchor(0.5f, 1.0f));
		mar.showInfoWindow();
		setEvent();
	}
	
	private void setData() {
		Marker mar = map.addMarker(new MarkerOptions()
		.position(myLocation)
		.title("My Location")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
		.anchor(0.5f, 1.0f));
		mar.showInfoWindow();
		for (int i = 0; i < listAddress.size(); i++) {
			LatLng anyLocation = new LatLng(Double.parseDouble(listAddress.get(i).getLatitude()), 
					Double.parseDouble(listAddress.get(i).getLongitude()));

			double km = 0;
				Document doc = md.getDocument(myLocation, anyLocation);
				try {
					 km = Double.parseDouble(md.getDistance(doc).replace("km", "").trim());
				} catch (Exception e) {
				}
			if(km < betweenKm){
				map.addMarker(new MarkerOptions()
						.position(anyLocation)
						.title(listAddress.get(i).getAddress().toString())
						.snippet(km == 0 ? "" : "Chiều dài: "+km+ " km")
//						.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, listAddress.get(i), getDistance(myLocation, anyLocation))))
						.icon(BitmapDescriptorFactory.defaultMarker())
						.anchor(0.5f, 1.0f));
			}
		}
	}
	
	private void setEvent() {
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(poly != null)
					poly.remove();
				
				Document doc = md.getDocument(myLocation, marker.getPosition());
				
				ArrayList<LatLng> directionPoint = md.getDirection(doc);
				PolylineOptions rectLine = new PolylineOptions().width(3).color(
						Color.RED);
				for (int i = 0; i < directionPoint.size(); i++) {
					rectLine.add(directionPoint.get(i));
				}
				poly = map.addPolyline(rectLine);
				return false;
			}
		});
		
		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				Toast.makeText(FrmAddressMLink.this, "To do something", Toast.LENGTH_LONG).show();
			}
		});
		goNext();
	}
	
	@Override
	public void goNext() {
		super.goNext();
		threadThucThi = new ThreadThucThi(this);
		threadThucThi.setIProcess(this, Byte.MIN_VALUE);
		threadThucThi.Start();
	}

	@Override
	public void processDataSend(byte iTag) {
		ServiceCore.TaskLinkAddress(String.valueOf(myLocation.latitude+","+myLocation.longitude), "", "");
	}

	@Override
	public void processDataReceived(String dataReceived, byte iTag, byte iTagErr) {
		
//		dataReceived = "val:shfjsjfjfj";
		
		Log.i("Log : ", "dataReceived : "+dataReceived);
		
		if(dataReceived.startsWith("val:")){
			
			if (listAddress == null){
				Log.i("Log : ", "null");
				listAddress = new ArrayList<AddressLink>();
				listAddress.add(new AddressLink("21.0272662", "105.8554513",
						"4 Lê Thạch, Tràng Tiền, Hoàn Kiếm, Hà Nội, Việt Nam", "Địa điểm quý khách có thể thanh toán bằng thẻ eMonkey" ));
				listAddress.add(new AddressLink("21.0358515", "105.8260943",
						"167 Đội cấn, Ba Đình, Hà Nội, Việt Nam", "Địa điểm quý khách có thể thanh toán bằng thẻ eMonkey" ));
				listAddress.add(new AddressLink("21.0276005", "105.790872",
						"2 Duy Tân, Dịch vọng, Cầu Giấy, Hà Nội, Việt Nam", "Địa điểm quý khách có thể thanh toán bằng thẻ eMonkey" ));
				listAddress.add(new AddressLink("21.0040872", "105.8554513",
						"98 Võ Thị Sáu, Thanh Nhàn, Hai Bà Trưng, Hà Nội, Việt Nam", "Địa điểm quý khách có thể thanh toán bằng thẻ eMonkey" ));
				listAddress.add(new AddressLink("21.0520539", "105.7468535",
						"Đình Quán, Phú Diễn, Từ Liêm, Hà Nội, Việt Nam", "Địa điểm quý khách có thể thanh toán bằng thẻ eMonkey" ));
				listAddress.add(new AddressLink("21.013553", "105.7768981",
						"Mễ Trì Hạ, Từ Liêm, Hà Nội, Việt Nam", "Địa điểm quý khách có thể thanh toán bằng thẻ eMonkey" ));
				listAddress.add(new AddressLink("19.81523", "105.7839466",
						"08 Giáp Bắc, Trường Thi, Thanh Hóa, Việt Nam", "Địa điểm quý khách có thể thanh toán bằng thẻ eMonkey" ));

				setData();
			}
		}
	}
//	 Convert a view to bitmap
//		public Bitmap createDrawableFromView(Context context, AddressLink address, double distance) {
//			
//			View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.old_window_info, null);
//			TextView title = (TextView) view.findViewById(R.id.title);
//			title.setText(address.getAddress().toString());
//			TextView body = (TextView) view.findViewById(R.id.body);
//			body.setText(address.getDesscription().toString() + "\nKhoảng cách: "+distance + " Km");
//			
//			DisplayMetrics displayMetrics = new DisplayMetrics();
//			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//			view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
//			view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
//			view.buildDrawingCache();
//			Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//	 
//			Canvas canvas = new Canvas(bitmap);
//			view.draw(canvas);
//	 
//			return bitmap;
//		}
 
}

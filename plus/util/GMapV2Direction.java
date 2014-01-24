package com.mpay.plus.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GMapV2Direction {
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";

	public GMapV2Direction() {
	}

	public Document getDocument(LatLng start, LatLng end) {
		try {
			String url = "http://maps.googleapis.com/maps/api/directions/xml?"
					+ "origin=" + start.latitude + "," + start.longitude
					+ "&destination=" + end.latitude + "," + end.longitude
					+ "&sensor=false&units=metric&mode=driving";

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			
			
			HttpPost httpPost = new HttpPost(url);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			
			InputStream in = response.getEntity().getContent();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(in);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Lấy Chiều dài đường đi
	public String getDistance(Document doc) {
		 String tag[] = {"text"};
		 String result_in_kms = "";
		if (doc != null) {
           NodeList nl;
           ArrayList<String> args = new ArrayList<String>();
           for (String s : tag) {
               nl = doc.getElementsByTagName(s);
               if (nl.getLength() > 0) {
                   Node node = nl.item(nl.getLength() - 1);
                   args.add(node.getTextContent());
               } else {
                   args.add(" - ");
               }
           }
           result_in_kms = String.format("%s", args.get(0));
       }
		return result_in_kms;
	}
	
	// Lay khoang cach duong chim bay
	public double getDistance(LatLng LatLng1, LatLng LatLng2) {
		double distance = 0;
		Location locationA = new Location("Point A");
		locationA.setLatitude(LatLng1.latitude);
		locationA.setLongitude(LatLng1.longitude);
		Location locationB = new Location("Point B");
		locationB.setLatitude(LatLng2.latitude);
		locationB.setLongitude(LatLng2.longitude);
		distance = locationA.distanceTo(locationB);
		// return (int) Math.ceil(distance/1000);
		return distance / 1000;
	}
	
//	public String getDistance(double lat1, double lon1, double lat2, double lon2) {
//	    String result_in_kms = "";
//	    String url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
//	    String tag[] = {"text"};
//	    HttpResponse response = null;
//	    try {
//	        HttpClient httpClient = new DefaultHttpClient();
//	        HttpContext localContext = new BasicHttpContext();
//	        HttpPost httpPost = new HttpPost(url);
//	        response = httpClient.execute(httpPost, localContext);
//	        InputStream is = response.getEntity().getContent();
//	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//	        Document doc = builder.parse(is);
//	        if (doc != null) {
//	            NodeList nl;
//	            ArrayList args = new ArrayList();
//	            for (String s : tag) {
//	                nl = doc.getElementsByTagName(s);
//	                if (nl.getLength() > 0) {
//	                    Node node = nl.item(nl.getLength() - 1);
//	                    args.add(node.getTextContent());
//	                } else {
//	                    args.add(" - ");
//	                }
//	            }
//	            result_in_kms = String.format("%s", args.get(0));
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return result_in_kms;
//	}

	public String getDurationText(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("duration");
		Node node1 = nl1.item(0);
		NodeList nl2 = node1.getChildNodes();
		Node node2 = nl2.item(getNodeIndex(nl2, "text"));
		Log.i("DurationText", node2.getTextContent());
		return node2.getTextContent();
	}

	public int getDurationValue(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("duration");
		Node node1 = nl1.item(0);
		NodeList nl2 = node1.getChildNodes();
		Node node2 = nl2.item(getNodeIndex(nl2, "value"));
		Log.i("DurationValue", node2.getTextContent());
		return Integer.parseInt(node2.getTextContent());
	}

	public String getDistanceText(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("distance");
		Node node1 = nl1.item(0);
		NodeList nl2 = node1.getChildNodes();
		Node node2 = nl2.item(getNodeIndex(nl2, "text"));
		Log.i("DistanceText", node2.getTextContent());
		return node2.getTextContent();
	}

	public int getDistanceValue(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("distance");
		Node node1 = nl1.item(0);
		NodeList nl2 = node1.getChildNodes();
		Node node2 = nl2.item(getNodeIndex(nl2, "value"));
		Log.i("DistanceValue", node2.getTextContent());
		return Integer.parseInt(node2.getTextContent());
	}

	public String getStartAddress(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("start_address");
		Node node1 = nl1.item(0);
		Log.i("StartAddress", node1.getTextContent());
		return node1.getTextContent();
	}

	public String getEndAddress(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("end_address");
		Node node1 = nl1.item(0);
		Log.i("StartAddress", node1.getTextContent());
		return node1.getTextContent();
	}

	public String getCopyRights(Document doc) {
		NodeList nl1 = doc.getElementsByTagName("copyrights");
		Node node1 = nl1.item(0);
		Log.i("CopyRights", node1.getTextContent());
		return node1.getTextContent();
	}

	public ArrayList<LatLng> getDirection(Document doc) {
		NodeList nl1, nl2, nl3;
		ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
		try {
			nl1 = doc.getElementsByTagName("step");
			if (nl1.getLength() > 0) {
				for (int i = 0; i < nl1.getLength(); i++) {
					Node node1 = nl1.item(i);
					nl2 = node1.getChildNodes();
	
					Node locationNode = nl2
							.item(getNodeIndex(nl2, "start_location"));
					nl3 = locationNode.getChildNodes();
					Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
					double lat = Double.parseDouble(latNode.getTextContent());
					Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
					double lng = Double.parseDouble(lngNode.getTextContent());
					listGeopoints.add(new LatLng(lat, lng));
	
					locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
					nl3 = locationNode.getChildNodes();
					latNode = nl3.item(getNodeIndex(nl3, "points"));
					ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
					for (int j = 0; j < arr.size(); j++) {
						listGeopoints.add(new LatLng(arr.get(j).latitude, arr
								.get(j).longitude));
					}
	
					locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
					nl3 = locationNode.getChildNodes();
					latNode = nl3.item(getNodeIndex(nl3, "lat"));
					lat = Double.parseDouble(latNode.getTextContent());
					lngNode = nl3.item(getNodeIndex(nl3, "lng"));
					lng = Double.parseDouble(lngNode.getTextContent());
					listGeopoints.add(new LatLng(lat, lng));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listGeopoints;
	}

	private int getNodeIndex(NodeList nl, String nodename) {
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}

	private ArrayList<LatLng> decodePoly(String encoded) {
		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(position);
		}
		return poly;
	}

	// ===============================
	// lay khoang cach
	public double CalculationByDistance(LatLng StartP, LatLng EndP) {
		int Radius = 6371;// radius of earth in Km
		double lat1 = StartP.latitude / 1E6;
		double lat2 = EndP.latitude / 1E6;
		double lon1 = StartP.longitude / 1E6;
		double lon2 = EndP.longitude / 1E6;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double valueResult = Radius * c;
		double km = valueResult / 1;
		DecimalFormat newFormat = new DecimalFormat("####");
		int kmInDec = Integer.valueOf(newFormat.format(km));
		double meter = valueResult % 1000;
		int meterInDec = Integer.valueOf(newFormat.format(meter));
		Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
				+ " Meter   " + meterInDec);
		
		return Radius * c;
	}
}

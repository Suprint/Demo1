package com.mpay.plus.lib;

import java.math.BigInteger;

import com.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;


public class myClass{
	 String sSeperate = "`";
	 String mModulus =  null;
	 String mExponent =  null;
	 String mPriExponent =  null;
     String mP =  null;
     String mQ =  null;
     String mDP =  null;
     String mDQ =  null;
     String QInv =  null;
     boolean isPrivate = false;
	
	public myClass(RSAKeyParameters mk){
		mModulus  = mk.getModulus().toString(16);
		mExponent = mk.getExponent().toString(16);
		isPrivate = false;
	}
	public myClass(RSAPrivateCrtKeyParameters mk){
		mModulus  = mk.getModulus().toString(16);
		mExponent = mk.getPublicExponent().toString(16);
		mPriExponent = mk.getExponent().toString(16);
	    mP = mk.getP().toString(16);
	    mQ = mk.getQ().toString(16);
	    mDP = mk.getDP().toString(16);
	    mDQ = mk.getDQ().toString(16);
	    QInv = mk.getQInv().toString(16);
	    isPrivate = true;
	}
	public myClass(String sChuoiKey){
		// Chuoi Ngan cach 
		String[] tmp = sChuoiKey.split(sSeperate);
		if (tmp.length > 2 && tmp.length < 9){
			// Private key
			mModulus  = tmp[0];
			mExponent = tmp[1];
			mPriExponent = tmp[2];
		    mP = tmp[3];
		    mQ = tmp[4];
		    mDP = tmp[5];
		    mDQ = tmp[6];
		    QInv = tmp[7];
		    isPrivate = true;
		}else{
			// Public Key
			mModulus  = tmp[0];
			mExponent = tmp[1];
			isPrivate = false;
		}
	}
	public Object getKey(){
		Object tmp = null;
		if (!isPrivate){
			// Key Public
			BigInteger mod = new BigInteger(mModulus,16);
			BigInteger exp = new BigInteger(mExponent,16);
			tmp = new RSAKeyParameters(false, mod, exp);
		}else{
			// Key Private
			BigInteger mod = new BigInteger(mModulus,16);
			BigInteger exp1 = new BigInteger(mExponent,16);
			BigInteger exp2 = new BigInteger(mPriExponent,16);
			BigInteger P = new BigInteger(mP,16);
			BigInteger Q = new BigInteger(mQ,16);
			BigInteger DP = new BigInteger(mDP,16);
			BigInteger DQ = new BigInteger(mDQ,16);
			BigInteger INV = new BigInteger(QInv,16);
			tmp = new RSAPrivateCrtKeyParameters(mod, exp1, exp2, P, Q, DP, DQ, INV);
		}
		return tmp;
	}
	public String getAllKey(){
		String tmp = "";
		if (!isPrivate){
			// Key Public
			tmp = mModulus+ sSeperate + mExponent;
		}else{
			// Key Private
			tmp = mModulus + sSeperate 
				+ mExponent + sSeperate 
				+ mPriExponent + sSeperate 
				+ mP + sSeperate 
				+ mQ +  sSeperate 
				+ mDP + sSeperate 
				+ mDQ + sSeperate 
				+ QInv; 
		}
		return tmp;
	}
	public boolean IsPrivate(){
		return isPrivate;
	}	
}

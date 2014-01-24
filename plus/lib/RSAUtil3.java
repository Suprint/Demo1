package com.mpay.plus.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import com.org.bouncycastle.crypto.AsymmetricBlockCipher;
import com.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.org.bouncycastle.crypto.BufferedBlockCipher;
import com.org.bouncycastle.crypto.digests.MD5Digest;
import com.org.bouncycastle.crypto.digests.SHA256Digest;
import com.org.bouncycastle.crypto.encodings.PKCS1Encoding;
import com.org.bouncycastle.crypto.engines.AESFastEngine;
import com.org.bouncycastle.crypto.engines.RSAEngine;
import com.org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import com.org.bouncycastle.crypto.modes.CBCBlockCipher;
import com.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.org.bouncycastle.crypto.params.KeyParameter;
import com.org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import com.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import com.org.bouncycastle.encoders.Base64;
import com.org.bouncycastle.util.encoders.Hex;

public class RSAUtil3 {
	private SHA256Digest digest = null;
	private MD5Digest digestMD5 = null;

	private String bytesToHex(byte[] b) {
		int size = b.length;
		StringBuffer h = new StringBuffer(size);
		for (int i = 0; i < size; i++) {
			int u = b[i] & 255; // unsigned conversion
			if (u < 16) {
				h.append("0" + Integer.toHexString(u));
			} else {
				h.append(Integer.toHexString(u));
			}
		}
		return h.toString();
	}

//	private byte[] hexToBytes(String str) {
//		if (str == null)
//			return new byte[0];
//
//		int len = str.length(); // probab ly should check length
//		char hex[] = str.toCharArray();
//		byte[] buf = new byte[len / 2];
//
//		for (int pos = 0; pos < len / 2; pos++)
//			buf[pos] = (byte) (((toDataNibble(hex[2 * pos]) << 4) & 0xF0) | (toDataNibble(hex[2 * pos + 1]) & 0x0F));
//
//		return buf;
//	}
//
//	private byte toDataNibble(char c) {
//		if (('0' <= c) && (c <= '9'))
//			return (byte) ((byte) c - (byte) '0');
//		else if (('a' <= c) && (c <= 'f'))
//			return (byte) ((byte) c - (byte) 'a' + 10);
//		else if (('A' <= c) && (c <= 'F'))
//			return (byte) ((byte) c - (byte) 'A' + 10);
//		else
//			return -1;
//	}

	// ================================PUBLIC===============================================
	// ma hoa va giai ma
	public String encryptAES(String plainText, String password)
			throws Exception {
		byte[] tmp = plainText.getBytes();
		return transform(true, tmp, password);
	}

	public String decryptAES(String cipherText, String password)
			throws Exception {
		byte[] raw = Base64Coder.decode(cipherText);
		return transform(false, raw, password);
	}

	// RSA
	public String encodeRSA(String data, Object rsaKey) throws Exception {
		RSAKeyParameters pub = (RSAKeyParameters) rsaKey;
		return this.encode(data, pub);
	}

	public String decodeRSA(String data, Object rsaKey) throws Exception {
		RSAKeyParameters pub = (RSAKeyParameters) rsaKey;
		return this.decode(data, pub);
	}

	public String encodeRSA(String data, String rsaKey) throws Exception {
		myClass mc = new myClass(rsaKey);
		return this.encodeRSA(data, mc.getKey());
	}

	public String decodeRSA(String data, String rsaKey) throws Exception {
		myClass mc = new myClass(rsaKey);
		return this.decodeRSA(data, mc.getKey());
	}

	public String hashSHA(String data) {
		// Thuc hien Bam 1 chieu
		String tmp = "";
		byte[] resBuf = new byte[getSHA256().getDigestSize()];
		byte[] m = toByteArray(data);
		getSHA256().update(m, 0, m.length);
		getSHA256().doFinal(resBuf, 0);
		tmp = new String(Hex.encode(resBuf));
		return tmp;
	}

	public String hashMD5Base64(String data){
		// Thuc hien Bam 1 chieu
		String tmp = "";
        byte[] resBuf = new byte[getMD5().getDigestSize()];
        byte[] m = toByteArray(data);
        getMD5().update(m, 0, m.length);
        getMD5().doFinal(resBuf, 0);
        tmp = new String(Base64.encode(resBuf));
		return tmp;
	}
	
	public String hashMD5(String data) {
		// Thuc hien Bam 1 chieu
		String tmp = "";
		byte[] resBuf = new byte[getMD5().getDigestSize()];
		byte[] m = toByteArray(data);
		getMD5().update(m, 0, m.length);
		getMD5().doFinal(resBuf, 0);
		tmp = new String(Hex.encode(resBuf));
		return tmp;
	}

	/*
	 * Tra ve cac xau dung lam key 0: Public Key 1: Private Key
	 */
	public String[] genKeys(int BitSize) throws Exception {
		SecureRandom rnd = new SecureRandom();
		int BZ = 512; // default

		BigInteger exp;
		if (BitSize != 0)
			BZ = BitSize;

		exp = new BigInteger(BZ, rnd);

		exp = exp.or(BigInteger.valueOf(0x1));
		RSAKeyPairGenerator pGen = new RSAKeyPairGenerator();
		RSAKeyGenerationParameters genParam = new RSAKeyGenerationParameters(
				exp, new SecureRandom(), BZ, 25);
		pGen.init(genParam);
		AsymmetricCipherKeyPair pair = pGen.generateKeyPair();
		RSAKeyParameters pubParameters = (RSAKeyParameters) pair.getPublic();
		RSAPrivateCrtKeyParameters privParameters = (RSAPrivateCrtKeyParameters) pair
				.getPrivate();

		String[] myKey = new String[2];

		myClass mc = new myClass(pubParameters);
		myKey[0] = mc.getAllKey();

		mc = new myClass(privParameters);
		myKey[1] = mc.getAllKey();

		return myKey;
	}

	public String[] genKeys() throws Exception {
		return genKeys(512);
	}

	// =============================================================================================
	private SHA256Digest getSHA256() {
		if (digest == null) {

			digest = new SHA256Digest();
		}
		return digest;
	}

	private MD5Digest getMD5() {
		if (digestMD5 == null) {

			digestMD5 = new MD5Digest();
		}
		return digestMD5;
	}

	// =============================================================================================
	private byte[] toByteArray(String input) {
		byte[] bytes = new byte[input.length()];

		for (int i = 0; i != bytes.length; i++) {
			bytes[i] = (byte) input.charAt(i);
		}

		return bytes;
	}

	private String encode(String data, RSAKeyParameters key) throws Exception {
		String hexinput = bytesToHex(data.getBytes());
		byte[] raw = Hex.decode(hexinput);
		return process(raw, true, key);
	}

	private String decode(String data, RSAKeyParameters key) throws Exception {
		// byte[] raw = Hex.decode(data);
		byte[] raw = Base64Coder.decode(data);
		String s = this.process(raw, false, key);
		return new String(Hex.decode(s));
		// return new String();
	}

	private String process(byte[] data, boolean encode, RSAKeyParameters key)
			throws Exception {
		// /
		AsymmetricBlockCipher eng = new RSAEngine();
		eng = new PKCS1Encoding(eng);
		eng.init(encode, key);// ma hoa
		//
		int block = 0;
		// if(encode)
		block = eng.getInputBlockSize();
		// else
		// block = eng.getOutputBlockSize();
		// System.out.println(eng.getInputBlockSize() + "//" +
		// eng.getOutputBlockSize());
		int max = data.length;
		int index = 0;
		String result = "";
		byte[] buff = null;
		byte[] buffkq = new byte[] {};
		byte[] tmp = null;
		if (block > max)
			block = max;
		while (index < max) {
			if (index + block > max)
				block = max - index;
			// System.out.println("block=" + block);
			buff = eng.processBlock(data, index, block);
			// System.out.println("buff size=" + buff.length);
			tmp = new byte[buffkq.length + buff.length];

			System.arraycopy(buffkq, 0, tmp, 0, buffkq.length);
			System.arraycopy(buff, 0, tmp, buffkq.length, buff.length);
			buffkq = new byte[tmp.length];
			System.arraycopy(tmp, 0, buffkq, 0, tmp.length);

			index += block;
		}
		if (!encode) {
			result = new String(Hex.encode(buffkq));
		} else
			result = Base64Coder.encodeString(buffkq);
		return result;
	}

	private String transform(boolean encrypt, byte[] inputBytes, String password)
			throws Exception {
		String result = "";
		byte[] key = new byte[getMD5().getDigestSize()];
		byte[] m = toByteArray(password);
		getMD5().update(m, 0, m.length);
		getMD5().doFinal(key, 0);
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
				new CBCBlockCipher(new AESFastEngine()));
		cipher.init(encrypt, new KeyParameter(key));

		ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		int inputLen;
		int outputLen;

		byte[] inputBuffer = new byte[1024];
		byte[] outputBuffer = new byte[cipher.getOutputSize(inputBuffer.length)];

		while ((inputLen = input.read(inputBuffer)) > -1) {
			outputLen = cipher.processBytes(inputBuffer, 0, inputLen,
					outputBuffer, 0);
			if (outputLen > 0) {
				output.write(outputBuffer, 0, outputLen);
			}
		}

		outputLen = cipher.doFinal(outputBuffer, 0);
		if (outputLen > 0) {
			output.write(outputBuffer, 0, outputLen);
		}
		if (!encrypt) {
			result = new String(output.toByteArray());
		} else
			result = Base64Coder.encodeString(output.toByteArray());
		return result;
	}
}

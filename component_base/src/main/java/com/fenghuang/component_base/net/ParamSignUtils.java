package com.fenghuang.component_base.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 请求参数签名工具类
 *
 */
public class ParamSignUtils {
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args)
	{
		//961AF8DF5831249776735F061E2C6CA4
		HashMap<String, String> signMap = new HashMap<String, String>();
		/*signMap.put("timestamp","1513910076700");
		signMap.put("installPath","/data/app/com.wmzn.navigaion.activity-2/base.apk");
		signMap.put("deviceMark","732269661e230beb");
		signMap.put("appVersion","1.0.0");
		signMap.put("isRoot","1");
		signMap.put("mac","64:cc:2e:a0:af:7b");
		signMap.put("src","1080*1920");
		signMap.put("androidId","732269661e230beb");
		signMap.put("platform","Xiaomi");
		signMap.put("osVersion","21");
		signMap.put("model","RedmiNote3");
		signMap.put("vender","Xiaomi");
		signMap.put("gp","1");*/
		
		signMap.put("deviceMark","732269661e230beb");
		signMap.put("versionMark","3.0.7");
		signMap.put("classifyName","音频");
		signMap.put("type","1");
		signMap.put("timestamp","1515029203");
		String secret="uploadClickDataInterface";
		HashMap SignHashMap=ParamSignUtils.sign(signMap, secret);
		System.out.println("Sign:"+SignHashMap.get("appSign"));
		
		//signMap.put("secret","test");
		//HashMap SignHashMasp=ParamSignUtils.sign(signMap, null);
		//System.out.println("SignHashMap2:"+SignHashMasp);
		/*List<String> ignoreParamNames=new ArrayList<String>();
		ignoreParamNames.add("a");
		HashMap SignHashMap2=ParamSignUtils.sign(signMap,ignoreParamNames, secret);
		System.out.println("SignHashMap2:"+SignHashMap2);*/
	}

	public static HashMap<String, String> sign(Map<String, String> paramValues,
			String secret) {
		return sign(paramValues, null, secret);
	}

	/**
	 * @param paramValues
	 * @param ignoreParamNames
	 * @param secret
	 * @return
	 */
	public static HashMap<String, String> sign(Map<String, String> paramValues,
			List<String> ignoreParamNames, String secret) {
		try {
			HashMap<String, String> signMap = new HashMap<String, String>();
			StringBuilder sb = new StringBuilder();
			List<String> paramNames = new ArrayList<String>(paramValues.size());
			paramNames.addAll(paramValues.keySet());
			if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
				for (String ignoreParamName : ignoreParamNames) {
					paramNames.remove(ignoreParamName);
				}
			}
			Collections.sort(paramNames);
			sb.append(secret);
			for (String paramName : paramNames) {
				sb.append(paramName).append(paramValues.get(paramName));
			}
			sb.append(secret);
			byte[] md5Digest = getMD5Digest(sb.toString());
			String sign = byte2hex(md5Digest);
			signMap.put("appParam", sb.toString());
			signMap.put("appSign", sign);
			return signMap;
		} catch (IOException e) {
			throw new RuntimeException("加密签名计算错误", e);
		}
		
	}

	public static String utf8Encoding(String value, String sourceCharsetName) {
		try {
			return new String(value.getBytes(sourceCharsetName), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@SuppressWarnings("unused")
	private static byte[] getSHA1Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			bytes = md.digest(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		}
		return bytes;
	}

	private static byte[] getMD5Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		}
		return bytes;
	}


	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}


	public static String encodeUri(String uri) {
		String newUri = "";
		StringTokenizer st = new StringTokenizer(uri, "/ ", true);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (tok.equals("/"))
				newUri += "/";
			else if (tok.equals(" "))
				newUri += "%20";
			else {
				try {
					newUri += URLEncoder.encode(tok, "UTF-8");
				} catch (UnsupportedEncodingException ignored) {
				}
			}

		}
		return newUri;
	}


	public static String decodeUri(String uri) {
		if (uri == null) {
			return null;
		}

		String newUri = "";
		StringTokenizer st = new StringTokenizer(uri, "/ ", true);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (tok.equals("/"))
				newUri += "/";
			else if (tok.equals("%20"))
				newUri += " ";
			else {
				try {
					newUri += URLDecoder.decode(tok, "UTF-8");
				} catch (UnsupportedEncodingException ignored) {
				}
			}

		}
		return newUri;
	}

}
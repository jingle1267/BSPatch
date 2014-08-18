package com.example.bspatch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {
	public static String calculateHash(MessageDigest algorithm, String fileName)
			throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DigestInputStream dis = new DigestInputStream(bis, algorithm);
			// read the file and update the hash calculation
			while (dis.read() != -1)
				;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
				}
			}
		}
		// get the hash value as byte array
		byte[] hash = algorithm.digest();
		return byte2hex(hash);
	}

	public static void main(String[] args) {
		System.out.println("main()");
		String fileName = "C:\\22.log";
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			System.out.println(calculateHash(sha1, fileName));
			System.out.println(calculateHash(md5, fileName));
			System.out.println("sha1:" + getFileSha1(fileName));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 适用于上G大的文件
	 */
	public static String getFileSha1(String path){
		File file = new File(path);
		FileInputStream in = null;
		MessageDigest messagedigest;
		try {
			in = new FileInputStream(file);
			messagedigest = MessageDigest.getInstance("SHA-1");
			byte[] buffer = new byte[1024 * 1024 * 10];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				// 该对象通过使用 update（）方法处理数据
				messagedigest.update(buffer, 0, len);
			}
			// 对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest
			// 对象被重新设置成其初始状态。
			return byte2hex(messagedigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs.append("0").append(stmp);
			else {
				hs = hs.append(stmp);
			}
		}
		return String.valueOf(hs);
	}
}

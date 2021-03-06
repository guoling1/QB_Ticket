package com.jkm.util.fusion;



import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * 对字符串进行加解密和加解压
 * @author wujh
 *
 */
@SuppressWarnings("restriction")
public class GZipUtil {
	private static Logger log = Logger.getLogger(GZipUtil.class);
	
	/**
	 * 将字符串压缩后Base64
	 * @param primStr 待加压加密函数
	 * @return
	 */
	public static String gzipString(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}
		ByteArrayOutputStream out = null;  
		GZIPOutputStream gout = null;  
		try{  
			out = new ByteArrayOutputStream();  
			gout = new GZIPOutputStream(out);
			gout.write(primStr.getBytes(Constants.transfer_charset));
			gout.flush();
		} catch (IOException e) {
			log.error("对字符串进行加压加密操作失败：", e);
			return null;
		} finally {
			if (gout != null) {
				try {
					gout.close();
				} catch (IOException e) {
					log.error("对字符串进行加压加密操作，关闭gzip操作流失败：", e);
				}
			}
		}
		return new BASE64Encoder().encode(out.toByteArray());
	}
	
	/**
	 * 将压缩并Base64后的字符串进行解密解压
	 * @param compressedStr 待解密解压字符串
	 * @return
	 */
	public static final String ungzipString(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream gin = null;
		String decompressed = null;
		try {
			byte[] compressed = new BASE64Decoder().decodeBuffer(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			gin = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while((offset = gin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString(Constants.transfer_charset);
		} catch (IOException e) {
			log.error("对字符串进行解密解压操作失败：", e);
			decompressed = null;
		} finally {
			if (gin != null) {
				try {
					gin.close();
				} catch (IOException e) {
					log.error("对字符串进行解密解压操作，关闭压缩流失败：", e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("对字符串进行解密解压操作，关闭输入流失败：", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("对字符串进行解密解压操作，关闭输出流失败：", e);
				}
			}
		}
		return decompressed;
	}
	
	public static void main(String[] args) {
		System.out.println(ungzipString(gzipString("1234567")));
	}
}

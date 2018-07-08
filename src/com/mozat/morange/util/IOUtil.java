/**
 * 
 */
package com.mozat.morange.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zergbird
 * 
 */
public class IOUtil {

	public static Charset utf8 = Charset.forName("UTF-8");

	static Timer timer = new Timer();
	static Logger logger = LoggerFactory.getLogger(IOUtil.class);

	public interface IUrlRequester {
		void onByte(byte[] bytes);
	}

	public static void writeCompressedBytes(DataOutputStream bo, int gzipFlag,
			byte[] data) throws IOException {
		bo.writeByte(gzipFlag); // write gzip flag as 1

		if (gzipFlag == 0) {
			// IOUtil.writeBytes(bo, data);
			bo.write(data);
		} else {
			// IOUtil.writeBytes(bo, IOUtil.zipBytesToBytes(data));
			bo.write(IOUtil.zipBytesToBytes(data));
		}

	}

	public static void writeCompressedString(DataOutputStream bo, int gzipFlag,
			String eventsStr) throws IOException {
		bo.writeByte(gzipFlag); // write gzip flag as 1

		if (gzipFlag == 0) {
			IOUtil.writeString(bo, eventsStr);
		} else {
			IOUtil.writeBytes(bo, IOUtil.zipStringToBytes(eventsStr));
		}

	}

	public static byte[] zipBytesToBytes(byte[] data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedOutputStream bufos = new BufferedOutputStream(
				new GZIPOutputStream(bos));
		bufos.write(data);

		bufos.flush();
		bos.flush();

		bufos.close();
		byte[] retval = bos.toByteArray();
		bos.close();
		return retval;
	}

	public static byte[] zipStringToBytes(String input) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedOutputStream bufos = new BufferedOutputStream(
				new GZIPOutputStream(bos));
		bufos.write(input.getBytes(utf8));

		bufos.flush();
		bos.flush();

		bufos.close();
		byte[] retval = bos.toByteArray();
		bos.close();
		return retval;
	}

	public static String unzipStringFromBytes(byte[] bytes) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		BufferedInputStream bufis = new BufferedInputStream(
				new GZIPInputStream(bis));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = bufis.read(buf)) > 0) {
			bos.write(buf, 0, len);
		}
		String retval = bos.toString();
		bis.close();
		bufis.close();
		bos.close();
		return retval;
	}

	static public void writeVarchar(DataOutputStream bout, String str)
			throws IOException {
		byte[] bytes = str.getBytes(utf8);
		bout.writeByte(bytes.length);
		bout.write(bytes);
	}

	static public String readVarchar(DataInputStream bin) throws IOException {
		int len = bin.readByte();
		if (len <= 0) {
			return "";
		}
		byte[] bytes = new byte[len];
		bin.readFully(bytes, 0, len);
		String ret = new String(bytes, utf8);
		return ret;

	}

	static public void writeShortString(DataOutputStream bout, String str)
			throws IOException {
		byte[] bytes = str.getBytes(utf8);
		bout.writeShort(bytes.length);
		bout.write(bytes);
	}

	static public String readShortString(DataInputStream bin)
			throws IOException {
		int len = bin.readShort();
		if (len <= 0) {
			return "";
		}
		byte[] bytes = new byte[len];
		bin.readFully(bytes, 0, len);
		String ret = new String(bytes, utf8);
		return ret;
	}

	static public void writeString(DataOutputStream bout, String str)
			throws IOException {
		byte[] bytes = str.getBytes(utf8);
		bout.writeInt(bytes.length);
		bout.write(bytes);
	}

	static public String readString(DataInputStream bin) throws IOException {
		int len = bin.readInt();
		if (len <= 0) {
			return "";
		}
		byte[] bytes = new byte[len];
		bin.readFully(bytes, 0, len);
		String ret = new String(bytes, utf8);
		return ret;
	}

	static public void writeShortBytes(DataOutputStream bout, byte[] bytes)
			throws IOException {
		bout.writeShort(bytes.length);
		bout.write(bytes);
	}

	static public byte[] readShortBytes(DataInputStream bin) throws IOException {
		int len = bin.readShort();
		if (len <= 0) {
			return null;
		}
		byte[] bytes = new byte[len];
		bin.readFully(bytes, 0, len);
		return bytes;
	}

	static public void writeBytes(DataOutputStream bout, byte[] bytes)
			throws IOException {
		bout.writeInt(bytes.length);
		bout.write(bytes);
	}

	static public byte[] readBytes(DataInputStream bin) throws IOException {
		int len = bin.readInt();
		if (len <= 0) {
			return null;
		}
		byte[] bytes = new byte[len];
		bin.readFully(bytes, 0, len);
		return bytes;
	}

	public static byte[] readTinyBytes(DataInputStream bin) throws IOException {
		int len = bin.readByte();
		if (len <= 0) {
			return null;
		}
		byte[] bytes = new byte[len];
		bin.readFully(bytes, 0, len);
		return bytes;
	}

	static public void writeTinyBytes(DataOutputStream bout, byte[] bytes)
			throws IOException {
		bout.writeByte(bytes.length);
		bout.write(bytes);
	}

	static public void writeRawBytes(DataOutputStream bout, byte[] bytes)
			throws IOException {
		bout.write(bytes);
	}

	static public byte[] readRawBytes(DataInputStream bin, int len)
			throws IOException {
		if (len <= 0) {
			return null;
		}
		byte[] bytes = new byte[len];
		bin.readFully(bytes, 0, len);
		return bytes;
	}

	public static void writeFileBytes(File file, byte[] bytes) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (Throwable t) {
			System.err.println("error in writing file: "
					+ file.getAbsolutePath());
		}
	}

	public static byte[] getFileBytes(File file) {
		try {
			FileInputStream is = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			is.read(bytes);
			is.close();
			return bytes;
		} catch (Throwable t) {
			System.err.println("error in reading file: "
					+ file.getAbsolutePath());
			return null;
		}
	}

	public static JSONObject parseJson(File file) {
		JSONObject json = null;
		if (file != null && file.exists()) {
			try {
				String value = new String(getFileBytes(file), utf8);
				if (value != null && value.length() > 0) {
					json = new JSONObject(value);
				}
			} catch (JSONException e) {
				System.err.println("json parser: " + e.toString());
				e.printStackTrace();
			}
		}
		return json;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public static byte[] getBytes(Object obj) throws java.io.IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}

	public static void dumpBytes(byte[] eventData, String file)
			throws IOException {
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(new File(file)));
			os.write(eventData);
		} finally {
			os.close();
		}
	}
}

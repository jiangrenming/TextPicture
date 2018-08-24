package main.demo.mobads.baidu.com.androidutils;

import java.nio.charset.Charset;

/**
 * 字节数组转换工具类
 */
public class BytesUtils {

	public static final String GBK = "GBK";
	public static final String UTF8 = "utf-8";
	public static final char[] ascii = "0123456789ABCDEF".toCharArray();
	private static char[] HEX_VOCABLE = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 将short整型数值转换为字节数组
	 * 
	 * @param data short整型数值
	 * @return 字节数组
	 */
	public static byte[] getBytes(short data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) ((data & 0xff00) >> 8);
		bytes[1] = (byte) (data & 0xff);
		return bytes;
	}

	/**
	 * 将字符转换为字节数组
	 * 
	 * @param data 字符
	 * @return 字节数组
	 */
	public static byte[] getBytes(char data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (data >> 8);
		bytes[1] = (byte) (data);
		return bytes;
	}

	/**
	 * 将布尔值转换为字节数组
	 * 
	 * @param data 布尔值
	 * @return 字节数组
	 */
	public static byte[] getBytes(boolean data) {
		byte[] bytes = new byte[1];
		bytes[0] = (byte) (data ? 1 : 0);
		return bytes;
	}

	/**
	 * 将整型数值转换为字节数组
	 * 
	 * @param data 整型数
	 * @return 字节数组
	 */
	public static byte[] getBytes(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((data & 0xff000000) >> 24);
		bytes[1] = (byte) ((data & 0xff0000) >> 16);
		bytes[2] = (byte) ((data & 0xff00) >> 8);
		bytes[3] = (byte) (data & 0xff);
		return bytes;
	}

	/**
	 * 将long整型数值转换为字节数组
	 * 
	 * @param data long整型数值
	 * @return 字节数组
	 */
	public static byte[] getBytes(long data) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) ((data >> 56) & 0xff);
		bytes[1] = (byte) ((data >> 48) & 0xff);
		bytes[2] = (byte) ((data >> 40) & 0xff);
		bytes[3] = (byte) ((data >> 32) & 0xff);
		bytes[4] = (byte) ((data >> 24) & 0xff);
		bytes[5] = (byte) ((data >> 16) & 0xff);
		bytes[6] = (byte) ((data >> 8) & 0xff);
		bytes[7] = (byte) (data & 0xff);
		return bytes;
	}

	/**
	 * 将float型数值转换为字节数组
	 * 
	 * @param data float型数值
	 * @return 字节数组
	 */
	public static byte[] getBytes(float data) {
		int intBits = Float.floatToIntBits(data);
		return getBytes(intBits);
	}

	/**
	 * 将double型数值转换为字节数组
	 * 
	 * @param data double型数值
	 * @return 字节数组
	 */
	public static byte[] getBytes(double data) {
		long intBits = Double.doubleToLongBits(data);
		return getBytes(intBits);
	}

	/**
	 * 将字符串按照charsetName编码格式的字节数组
	 * 
	 * @param data
	 *            字符串
	 * @param charsetName
	 *            编码格式
	 * @return 字节数组
	 */
	public static byte[] getBytes(String data, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		return data.getBytes(charset);
	}

	/**
	 * 将字符串按照GBK编码格式的字节数组
	 * @param data 字符串
	 * @return GBK编码格式的字节数组
	 */
	public static byte[] getBytes(String data) {
		return getBytes(data, GBK);
	}

	/**
	 * 将字节数组第0字节转换为布尔值
	 * 
	 * @param bytes 字节数组
	 * @return true 字节数组第0字节非0;
	 *  	   false 字节数组第0字节为0
	 */
	public static boolean getBoolean(byte[] bytes) {
		return bytes[0] == 1;
	}

	/**
	 * 将字节数组的第index字节转换为布尔值
	 * 
	 * @param bytes 字节数组
	 * @param index 第index字节
	 * @return true字节数组第index字节非0;false字节数组第index字节为0
	 */
	public static boolean getBoolean(byte[] bytes, int index) {
		return bytes[index] == 1;
	}

	/**
	 * 将字节数组前2字节转换为short整型数值
	 * 
	 * @param bytes 字节数组
	 * @return hort整型数值
	 */
	public static short getShort(byte[] bytes) {
		return (short) ((0xff00 & (bytes[0] << 8)) | (0xff & bytes[1]));
	}

	/**
	 * 将字节数组从startIndex开始的2个字节转换为short整型数值
	 * 
	 * @param bytes 字节数组
	 * @param startIndex 索引startIndex
	 * @return short整型数值
	 */
	public static short getShort(byte[] bytes, int startIndex) {
		return (short) ((0xff00 & (bytes[startIndex] << 8)) | (0xff & bytes[startIndex + 1]));
	}

	/**
	 * 将字节数组前2字节转换为字符
	 * 
	 * @param bytes 字节数组
	 * @return 前2字节转换后字符
	 */
	public static char getChar(byte[] bytes) {
		return (char) ((0xff00 & (bytes[0] << 8)) | (0xff & bytes[1]));
	}

	/**
	 * 将字节数组从startIndex开始的2个字节转换为字符
	 * 
	 * @param bytes 字节数组
	 * @param startIndex 字节数组开始位置startIndex
	 * @return 转换后字符
	 */
	public static char getChar(byte[] bytes, int startIndex) {
		return (char) ((0xff00 & (bytes[startIndex] << 8)) | (0xff & bytes[startIndex + 1]));
	}

	/**
	 * 将字节数组前4字节转换为整型数值
	 * 
	 * @param bytes 字节数组
	 * @return 整型数值
	 */
	public static int getInt(byte[] bytes) {
		return (0xff000000 & (bytes[0] << 24) | (0xff0000 & (bytes[1] << 16))
				| (0xff00 & (bytes[2] << 8)) | (0xff & bytes[3]));
	}

	/**
	 * 将字节数组从startIndex开始的4个字节转换为整型数值
	 * 
	 * @param bytes 字节数组
	 * @param startIndex 字节数组开始位置startIndex
	 * @return 整型数值
	 */
	public static int getInt(byte[] bytes, int startIndex) {
		return (0xff000000 & (bytes[startIndex] << 24)
				| (0xff0000 & (bytes[startIndex + 1] << 16))
				| (0xff00 & (bytes[startIndex + 2] << 8)) | (0xff & bytes[startIndex + 3]));
	}

	/**
	 * 将字节数组前8字节转换为long整型数值
	 * 
	 * @param bytes 字节数组
	 * @return long整型数值
	 */
	public static long getLong(byte[] bytes) {
		return (0xff00000000000000L & ((long) bytes[0] << 56)
				| (0xff000000000000L & ((long) bytes[1] << 48))
				| (0xff0000000000L & ((long) bytes[2] << 40))
				| (0xff00000000L & ((long) bytes[3] << 32))
				| (0xff000000L & ((long) bytes[4] << 24))
				| (0xff0000L & ((long) bytes[5] << 16))
				| (0xff00L & ((long) bytes[6] << 8)) | (0xffL & (long) bytes[7]));
	}

	/**
	 * 将字节数组从startIndex开始的8个字节转换为long整型数值
	 * 
	 * @param bytes 字节数组
	 * @param startIndex 字节数组从startIndex开始
	 * @return long整型数值
	 */
	public static long getLong(byte[] bytes, int startIndex) {
		return (0xff00000000000000L & ((long) bytes[startIndex] << 56)
				| (0xff000000000000L & ((long) bytes[startIndex + 1] << 48))
				| (0xff0000000000L & ((long) bytes[startIndex + 2] << 40))
				| (0xff00000000L & ((long) bytes[startIndex + 3] << 32))
				| (0xff000000L & ((long) bytes[startIndex + 4] << 24))
				| (0xff0000L & ((long) bytes[startIndex + 5] << 16))
				| (0xff00L & ((long) bytes[startIndex + 6] << 8)) | (0xffL & (long) bytes[startIndex + 7]));
	}

	/**
	 * 将字节数组前4字节转换为float型数值
	 * 
	 * @param bytes 字节数组
	 * @return float型数值
	 */
	public static float getFloat(byte[] bytes) {
		return Float.intBitsToFloat(getInt(bytes));
	}

	/**
	 * 将字节数组从startIndex开始的4个字节转换为float型数值
	 * 
	 * @param bytes 字节数组
	 * @param startIndex 字节数组从startIndex开始
	 * @return float型数值
	 */
	public static float getFloat(byte[] bytes, int startIndex) {
		byte[] result = new byte[4];
		System.arraycopy(bytes, startIndex, result, 0, 4);
		return Float.intBitsToFloat(getInt(result));
	}

	/**
	 * 将字节数组前8字节转换为double型数值
	 * 
	 * @param bytes 字节数组
	 * @return double型数值
	 */
	public static double getDouble(byte[] bytes) {
		long l = getLong(bytes);
		return Double.longBitsToDouble(l);
	}

	/**
	 * 将字节数组从startIndex开始的8个字节转换为double型数值
	 * 
	 * @param bytes 字节数组 
	 * @param startIndex 字节数组从startIndex开始
	 * @return double型数值
	 */
	public static double getDouble(byte[] bytes, int startIndex) {
		byte[] result = new byte[8];
		System.arraycopy(bytes, startIndex, result, 0, 8);
		long l = getLong(result);
		return Double.longBitsToDouble(l);
	}

	/**
	 * 将charsetName编码格式的字节数组转换为字符串
	 * 
	 * @param bytes 字节数组
	 * @param charsetName 编码格式
	 * @return 字符串
	 */
	public static String getString(byte[] bytes, String charsetName) {
		return new String(bytes, Charset.forName(charsetName));
	}

	/**
	 * 将GBK编码格式的字节数组转换为字符串
	 * 
	 * @param bytes 字节数组
	 * @return 字符串
	 */
	public static String getString(byte[] bytes) {
		return getString(bytes, GBK);
	}

	/**
	 * 将数据长度为奇数或偶数的16进制字符串转换为bcd字节数组
	 * @param hex 需要填充的数据
	 * @param isLeft 待补充的字符是是否是左填充
	 * @return 转换后的数组
	 */
	public static byte[] str2bcd(String hex, boolean isLeft) {
		if (hex == null || "".equals(hex)) {
			return null;
		}
		
		if (hex.length() % 2 != 0) {
			if(StringUtils.isEmpty("0")) {
				hex += "0";
			} else {
				if(isLeft) {
					hex = "0" + hex;
				} else {
					hex += "0";
				}
			}
		}
		
		int len = hex.length() / 2;

		byte[] result = new byte[len];
		char[] chArr = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(chArr[pos]) << 4 | toByte(chArr[pos + 1]));
		}
		return result;
	}
	
	
	/**
	 * 将16进制字符串转换为字节数组
	 * 
	 * @param hex 字符串
	 * @return 字节数组
	 */
	public static byte[] hexStringToBytes(String hex) {
		if (hex == null || "".equals(hex)) {
			return null;
		}
		int len = hex.length() / 2;
		byte[] result = new byte[len];
		char[] chArr = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(chArr[pos]) << 4 | toByte(chArr[pos + 1]));
		}
		return result;
	}
	
	/**
	 * 将数据长度为奇数或偶数的16进制字符串转换为字节数组
	 * @param hex 需要填充的数据
	 * @param subStr 如果数据长度为奇数需要补充的字符
	 * @param isLeft 待补充的字符是是否是左填充
	 * @return 转换后的数组
	 */
	public static byte[] hexStringToBytes(String hex, String subStr, boolean isLeft) {
		if (hex == null || "".equals(hex)) {
			return null;
		}
		
		if (hex.length() % 2 != 0) {
			if(StringUtils.isEmpty(subStr)) {
				hex += "0";
			} else {
				if(isLeft) {
					hex = subStr + hex;
				} else {
					hex += subStr;
				}
			}
		}
		
		int len = hex.length() / 2;

		byte[] result = new byte[len];
		char[] chArr = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(chArr[pos]) << 4 | toByte(chArr[pos + 1]));
		}
		return result;
	}
	/**
	 * 将16进制字符串转换为字节数组
	 * 
	 * @param hex 字符串
	 * @return 字节数组
	 */
	public static byte[] hexToBytes(String hex) {
		if (hex.length() % 2 != 0)
			throw new IllegalArgumentException(
					"input string should be any multiple of 2!");
		hex.toUpperCase();

		byte[] byteBuffer = new byte[hex.length() / 2];

		byte padding = 0x00;
		boolean paddingTurning = false;
		for (int i = 0; i < hex.length(); i++) {
			if (paddingTurning) {
				char c = hex.charAt(i);
				int index = indexOf(hex, c);
				padding = (byte) ((padding << 4) | index);
				byteBuffer[i / 2] = padding;
				padding = 0x00;
				paddingTurning = false;
			} else {
				char c = hex.charAt(i);
				int index = indexOf(hex, c);
				padding = (byte) (padding | index);
				paddingTurning = true;
			}

		}
		return byteBuffer;
	}

	private static int indexOf(String input, char c) {
		
		for(int i = 0; i<HEX_VOCABLE.length;i++){
			if(c == HEX_VOCABLE[i]){
				return i;
			}
		}
		throw new IllegalArgumentException("err input:" + input);
	}

	/**
	 * 将BCD编码的字节数组转换为字符串
	 * 
	 * @param bcds 字节数组
	 * @return 字符串
	 */
	public static String bcdToString(byte[] bcds) {
		if (bcds == null || bcds.length == 0) {
			return null;
		}
		byte[] temp = new byte[2 * bcds.length];
		for (int i = 0; i < bcds.length; i++) {
			temp[i * 2] = (byte) ((bcds[i] >> 4) & 0x0f);
			temp[i * 2 + 1] = (byte) (bcds[i] & 0x0f);
		}
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < temp.length; i++) {
			res.append(ascii[temp[i]]);
		}
		return res.toString();
	}

	/**
	 * 字节转整形
	 * @param value 字节
	 * @return 整型
	 */
	public static int bcdToInt(byte value){
		return ((value>>4) * 10) + (value&0x0F);
	}
	
	/**
	 * 字节数组转16进制字符串
	 * @param bs 字节数组
	 * @return 16进制字符串
	 */
	public static String bytesToHex(byte[] bs) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bs) {
			int high = (b >> 4) & 0x0f;
			int low = b & 0x0f;
			sb.append(HEX_VOCABLE[high]);
			sb.append(HEX_VOCABLE[low]);
		}
		return sb.toString();
	}
	
	/**
	 * 字节数组取前len个字节转16进制字符串
	 * @param bs 字节数组
	 * @param len 取字节长度
	 * @return 16进制字符串
	 */
	public static String bytesToHex(byte[] bs, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<len; i++ ) {
			byte b = bs[i];
			int high = (b >> 4) & 0x0f;
			int low = b & 0x0f;
			sb.append(HEX_VOCABLE[high]);
			sb.append(HEX_VOCABLE[low]);
		}
		return sb.toString();
	}
	/**
	 * 字节数组偏移offset长度之后的取len个字节转16进制字符串
	 * @param bs 字节数组
	 * @param offset 偏移长度
	 * @param len 取字节长度
	 * @return 16进制字符串
	 */
	public static String bytesToHex(byte[] bs, int offset, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<len; i++ ) {
			byte b = bs[offset + i];
			int high = (b >> 4) & 0x0f;
			int low = b & 0x0f;
			sb.append(HEX_VOCABLE[high]);
			sb.append(HEX_VOCABLE[low]);
		}
		return sb.toString();
	}
	/**
	 * 字节数组转16进制字符串
	 * @param b 字节数组
	 * @return 16进制字符串
	 */
	public static String byteToHex(byte b) {
		StringBuilder sb = new StringBuilder();
			int high = (b >> 4) & 0x0f;
			int low = b & 0x0f;
			sb.append(HEX_VOCABLE[high]);
			sb.append(HEX_VOCABLE[low]);
		return sb.toString();
	}
	/**
	 * 将字节数组取反
	 * 
	 * @param src 字节数组
	 * @return 取反后字节数组
	 */
	public static String negate(byte[] src) {
		if (src == null || src.length == 0) {
			return null;
		}
		byte[] temp = new byte[2 * src.length];
		for (int i = 0; i < src.length; i++) {
			byte tmp = (byte) (0xFF ^ src[i]);
			temp[i * 2] = (byte) ((tmp >> 4) & 0x0f);
			temp[i * 2 + 1] = (byte) (tmp & 0x0f);
		}
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < temp.length; i++) {
			res.append(ascii[temp[i]]);
		}
		return res.toString();
	}

	/**
	 * 比较 字节数组 a, b是否相等
	 * @param a 字节数组a
	 * @param b 字节数组b
	 * @return false a,b 不相等;
	 * 			ture a,b 相等
	 */
	public static boolean compareBytes(byte[] a, byte[] b) {
		if (a == null || a.length == 0 || b == null || b.length == 0
				|| a.length != b.length) {
			return false;
		}
		if (a.length == b.length) {
			for (int i = 0; i < a.length; i++) {
				if (a[i] != b[i]) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	/**
	 * 只比对指定长度byte
	 * @param a 字节数组a
	 * @param b 字节数组b
	 * @param len 长度
	 * @return false a,b 指定长度不相等;
	 * 			ture a,b 指定长度相等
	 */
	public static boolean compareBytes(byte[] a, byte[] b, int len) {
		if (a == null || a.length == 0 || b == null || b.length == 0
				|| a.length < len || b.length < len) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将字节数组转换为二进制字符串
	 * 
	 * @param items 字节数组
	 * @return 二进制字符串
	 */
	public static String bytesToBinaryString(byte[] items) {
		if (items == null || items.length == 0) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (byte item : items) {
			buf.append(byteToBinaryString(item));
		}
		return buf.toString();
	}

	/**
	 * 将字节转换为二进制字符串
	 * 
	 * @param item 字节
	 * @return 二进制字符串
	 */
	public static String byteToBinaryString(byte item) {
		byte a = item;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			buf.insert(0, a % 2);
			a = (byte) (a >> 1);
		}
		return buf.toString();
	}

	/**
	 * 对数组a，b进行异或运算 
	 * @param a 数组a
	 * @param b 数组b
	 * @return NULL 数组a或者b一个null或者长度为0，或者a,b长度不一样;
	 * 数组a，b异或的字符数组
	 */
	public static byte[] xor(byte[] a, byte[] b) {
		if (a == null || a.length == 0 || b == null || b.length == 0
				|| a.length != b.length) {
			return null;
		}
		byte[] result = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = (byte) (a[i] ^ b[i]);
		}
		return result;
	}

	/**
	 * 对数组a，b进行异或运算 运算长度len
	 * @param a 数组a
	 * @param b 数组b
	 * @param len 运算长度
	 * @return NULL 数组a或者b一个null或者长度为0;
	 * 数组a，b异或的字符数组
	 */
	public static byte[] xor(byte[] a, byte[] b, int len) {
		if (a == null || a.length == 0 || b == null || b.length == 0) {
			return null;
		}
		if (a.length < len || b.length < len){
			return null;
		}
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = (byte) (a[i] ^ b[i]);
		}
		return result;
	}
	/**
	 * 将short整型数值转换为字节数组
	 * 
	 * @param num short整型数值
	 * @return 字节数组
	 */
	public static byte[] shortToBytes(int num) {
		byte[] temp = new byte[2];
		for (int i = 0; i < 2; i++) {
			temp[i] = (byte) ((num >>> (8 - i * 8)) & 0xFF);
		}
		return temp;
	}

	/**
	 * 将字节数组转为整型
	 * 
	 * @param arr 字节数组
	 * @return 整型
	 */
	public static int bytesToShort(byte[] arr) {
		int mask = 0xFF;
		int temp = 0;
		int result = 0;
		for (int i = 0; i < 2; i++) {
			result <<= 8;
			temp = arr[i] & mask;
			result |= temp;
		}
		return result;
	}

	/**
	 * 将整型数值转换为指定长度的字节数组
	 * 
	 * @param num 整型数值
	 * @return 字节数组
	 */
	public static byte[] intToBytes(int num) {
		byte[] temp = new byte[4];
		for (int i = 0; i < 4; i++) {
			temp[i] = (byte) ((num >>> (24 - i * 8)) & 0xFF);
		}
		return temp;
	}

	/**
	 * 将整型数值转换为指定长度的字节数组
	 * 
	 * @param src 整型数值
	 * @param len 长度
	 * @return 字节数组
	 */
	public static byte[] intToBytes(int src, int len) {
		if (len < 1 || len > 4) {
			return null;
		}
		byte[] temp = new byte[len];
		for (int i = 0; i < len; i++) {
			temp[len - 1 - i] = (byte) ((src >>> (8 * i)) & 0xFF);
		}
		return temp;
	}

	/**
	 * 将字节数组转换为整型数值
	 * 
	 * @param arr 字节数组
	 * @return 整型数值
	 */
	public static int bytesToInt(byte[] arr) {
		int mask = 0xFF;
		int temp = 0;
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result <<= 8;
			temp = arr[i] & mask;
			result |= temp;
		}
		return result;
	}

	/**
	 * 将long整型数值转换为字节数组
	 * @param num long整型数值
	 * @return 字节数组
	 */
	public static byte[] longToBytes(long num) {
		byte[] temp = new byte[8];
		for (int i = 0; i < 8; i++) {
			temp[i] = (byte) ((num >>> (56 - i * 8)) & 0xFF);
		}
		return temp;
	}

	/**
	 * 将字节数组转换为long整型数值
	 * 
	 * @param arr 字节数组
	 * @return long整型数值
	 */
	public static long bytesToLong(byte[] arr) {
		int mask = 0xFF;
		int temp = 0;
		long result = 0;
		int len = Math.min(8, arr.length);
		for (int i = 0; i < len; i++) {
			result <<= 8;
			temp = arr[i] & mask;
			result |= temp;
		}
		return result;
	}

	/**
	 * 将16进制字符转换为字节
	 * 
	 * @param c 16进制字符
	 * @return 字节
	 */
	public static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
	
	/**
	 * 功能描述：把两个字节的字节数组转化为整型数据，高位补零，例如：
	 * 有字节数组byte[] data = new byte[]{1,2};转换后int数据的字节分布如下：
	 * 00000000  00000000 00000001 00000010,函数返回258
	 * @param lenData 需要进行转换的字节数组
	 * @return  字节数组所表示整型值的大小
	 */
	public static int bytesToIntWhereByteLengthEquals2(byte lenData[]) {
		if(lenData.length != 2){
			return -1;
		}
		byte fill[] = new byte[]{0,0};
		byte real[] = new byte[4];
		System.arraycopy(fill, 0, real, 0, 2);
		System.arraycopy(lenData, 0, real, 2, 2);
		int len = byteToInt(real);
		return len;
		
	}
	
	/**
	 * 功能描述：将byte数组转化为int类型的数据
	 * @param byteVal 需要转化的字节数组
	 * @return 字节数组所表示的整型数据
	 */
	public static int byteToInt(byte[] byteVal) {
		int result = 0;
		for(int i = 0;i < byteVal.length;i++) {
			int tmpVal = (byteVal[i]<<(8*(3-i)));
			switch(i) {
				case 0:
					tmpVal = tmpVal & 0xFF000000;
					break;
				case 1:
					tmpVal = tmpVal & 0x00FF0000;
					break;
				case 2:
					tmpVal = tmpVal & 0x0000FF00;
					break;
				case 3:
					tmpVal = tmpVal & 0x000000FF;
					break;
			}
		
			result = result | tmpVal;
		}
		return result;
	}
	/**
	 * 对字符数组每个字节进行XOR运算得到字符
	 * @param bData 字符数组
	 * @return XOR后字符
	 */
    public static byte CheckXORSum(byte[] bData){
		byte sum = 0x00;
		for (int i = 0; i < bData.length; i++) {
			sum ^= bData[i];
		}
		return sum;
    }
    /**
     * 从offset开始 将后续长度为len的byte字节转为int
     * @param data 字符数组
     * @param offset 开始位置
     * @param len 长度
     * @return 转化后的字节
     */
    public static int bytesToInt(byte[] data, int offset, int len){
    	int mask = 0xFF;
		int temp = 0;
		int result = 0;
		len = Math.min(len, 4);
		for (int i = 0; i < len; i++) {
			result <<= 8;
			temp = data[offset + i] & mask;
			result |= temp;
		}
		return result;
    }
    
    /**
     * 去掉字符串首的c字符 
     * @param source 源字符串
     * @param c 指定字符
     * @return
     */
    public static String TrimCharLeft(String source, char c) {
		String beTrim = String.valueOf(c);
		source = source.trim();
		
		// 循环去掉字符串首的beTrim字符 
		String beginChar = source.substring(0, 1);
		while (beginChar.equalsIgnoreCase(beTrim)) {
			source = source.substring(1, source.length());
			beginChar = source.substring(0, 1);
		}
		return source;
	}
    

    /**
     * 去掉字符串尾的c字符 
     * @param source 源字符串
     * @param c 指定字符
     * @return
     */
    public static String TrimCharRight(String source, char c) {
		String beTrim = String.valueOf(c);
		source = source.trim();
		
		// 循环去掉字符串尾的beTrim字符  
		String endChar = source.substring(source.length() - 1, source.length());
		while (endChar.equalsIgnoreCase(beTrim)) {  
			source = source.substring(0, source.length() - 1);  
			endChar = source.substring(source.length() - 1, source.length());  
		}  
		return source;
	}
    

}

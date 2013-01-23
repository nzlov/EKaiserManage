package ekaiser.nzlov.util;

public class MathUtil {
	public static final String toHex(byte b) {
		  return ("" + "0123456789abcdef".charAt(0xf & b >> 4) + "0123456789abcdef".charAt(b & 0xf));
	}
	public static byte[] intToByteArray(final int integer) {
		int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
		byteArray[3 - n] = (byte) (integer>>> (n * 8));

		return (byteArray);
	}
	public static int byteArrayToInt(byte[] b, int offset) {
	       int value= 0;
	       for (int i = 0; i < 4; i++) {
	           int shift= (4 - 1 - i) * 8;
	           value +=(b[i + offset] & 0x000000FF) << shift;
	       }
	       return value;
	 }
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
		return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
		int pos = i * 2;
		d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	 /**
	  * 把字节数组转换成16进制字符串
	  *
	  * @param bArray
	  * @return
	  */
	 public static String bytesToHexString(byte[] bArray) {
	  StringBuffer sb = new StringBuffer(bArray.length);
	  String sTemp;
	  for (int i = 0; i < bArray.length; i++) {
	   sTemp = Integer.toHexString(0xFF & bArray[i]);
	   if (sTemp.length() < 2)
	    sb.append(0);
	   sb.append(sTemp.toUpperCase());
	  }
	  return sb.toString();
	 }
}

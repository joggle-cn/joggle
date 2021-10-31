package com.wuweibi.bullet.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;

public class Utils {

	public static SimpleDateFormat fomatDate = new SimpleDateFormat("mmddhhmmss");

	public Utils() {
	}


	public static String getString(byte bs[], int offset, int len) {
		int i = 0;
		int end = offset + len;
		for (i = offset; i < end; i++)
			if (bs[i] == 0)
				break;

		return new String(bs, offset, i - offset);
	}

	public static void md5(byte data[], int offset, int length, byte digest[],
			int dOffset) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data, offset, length);
			md5.digest(digest, dOffset, 16);
		} catch (Exception exception) {
		}
	}

	public static int ByteToInt(byte byte0) {
		return byte0;
	}

	public static byte IntToByte(int i) {
		return (byte) i;
	}

	public static int BytesToInt(byte abyte0[]) {
		return ((0xff & abyte0[0]) << 8) + abyte0[1];
	}

	public static byte[] IntToBytes(int i) {
		byte abyte0[] = new byte[2];
		abyte0[1] = (byte) (0xff & i);
		abyte0[0] = (byte) ((0xff00 & i) >> 8);
		return abyte0;
	}

	public static byte[] IntToBytes4(int i) {
		byte abyte0[] = new byte[4];
		abyte0[3] = (byte) (0xff & i);
		abyte0[2] = (byte) ((0xff00 & i) >> 8);
		abyte0[1] = (byte) ((0xff0000 & i) >> 16);
		abyte0[0] = (byte) ((0xff000000 & i) >> 24);
		return abyte0;
	}

	public static byte[] LongToBytes4(long l) {
		byte abyte0[] = new byte[4];
		abyte0[3] = (byte) (int) (255L & l);
		abyte0[2] = (byte) (int) ((65280L & l) >> 8);
		abyte0[1] = (byte) (int) ((0xff0000L & l) >> 16);
		abyte0[0] = (byte) (int) ((0xffffffffff000000L & l) >> 24);
		return abyte0;
	}

	public static void LongToBytes4(long l, byte abyte0[]) {
		abyte0[3] = (byte) (int) (255L & l);
		abyte0[2] = (byte) (int) ((65280L & l) >> 8);
		abyte0[1] = (byte) (int) ((0xff0000L & l) >> 16);
		abyte0[0] = (byte) (int) ((0xffffffffff000000L & l) >> 24);
	}


	/**
	 * Long 转byte
	 * @param l
	 * @return
	 */
	public static byte[] LongToBytes8(long l){
		byte[] bytes = new byte[8];
		LongToBytes8(l, bytes, 0);
		return bytes;
	}

	public static void LongToBytes8(long l, byte b[], int pos) {
		b[pos + 7] = (byte) (int) (255L & l);
		b[pos + 6] = (byte) (int) ((65280L & l) >> 8);
		b[pos + 5] = (byte) (int) ((0xff0000L & l) >> 16);
		b[pos + 4] = (byte) (int) ((0xff000000L & l) >> 24);
		b[pos + 3] = (byte) (int) ((0xff00000000L & l) >> 32);
		b[pos + 2] = (byte) (int) ((0xff0000000000L & l) >> 40);
		b[pos + 1] = (byte) (int) ((0xff000000000000L & l) >> 48);
		b[pos] = (byte) (int) ((0xff00000000000000L & l) >> 56);
	}

	public static void LongToBytes12(long l, byte b[], int pos) {
		b[pos] = (byte) (int) (255L & l);
		b[pos + 1] = (byte) (int) ((65280L & l) >> 8);
		b[pos + 2] = (byte) (int) ((0xff0000L & l) >> 16);
		b[pos + 3] = (byte) (int) ((0xff000000L & l) >> 24);
		b[pos + 4] = (byte) (int) ((0xff00000000L & l) >> 32);
		b[pos + 5] = (byte) (int) ((0xff0000000000L & l) >> 40);
		b[pos + 6] = (byte) (int) ((0xff000000000000L & l) >> 48);
		b[pos + 7] = (byte) (int) ((0xff00000000000000L & l) >> 56);
		b[pos + 8] = (byte) (int) (255L & l);
		b[pos + 9] = (byte) (int) ((65280L & l) >> 8);
		b[pos + 10] = (byte) (int) ((0xff0000L & l) >> 16);
		b[pos + 11] = (byte) (int) ((0xff000000L & l) >> 24);
	}



	public static long BytesToLong(byte[] b, int srcPos, int count,
			boolean inverse) {
		long l = 0L;
		int base = (srcPos + count) - 1;//最高位
		int i = 0;
		for (int j = 0; j < count+1; j++) {
			int ax = (int)((long) (i) * 8L);
			l |= (255L & (long) b[inverse ? base - j : i]) << ax;
			i++;
		}
		return l;
	}

	public static long Bytes4ToLong(byte abyte0[],int offset) {
		long a1 = (255L & (long) abyte0[0+offset]) << 24;
		long a2 = (255L & (long) abyte0[1+offset]) << 16;
		long a3 = (255L & (long) abyte0[2+offset]) << 8 ;
		long a4 =  255L & (long) abyte0[3+offset];
		long re = a1
				| a2
				| a3|a4;

		return  re;
	}
	 public static void main(String[] args) {

		 byte[] b = new byte[]{
				 0, 0, 0, 61,
				 0, 0, 0, 1,
				 -76, 109, -101,23,
				 60, 114, 30, -90,
				 0, 0, 0, 1
		 };
		 //[0, 0, 0, 61, 0, 0, 0, 1, -76, 109, -101, 23, 60, 112, -89, 0, 0, 0, 0, 1]
		 //[0, 0, 0, 61, 0, 0, 0, 1, -76, 109, -101, 23, 60, 114, 30, -90, 0, 0, 0, 1]
		 System.out.println(Utils.Bytes4ToLong(b,8));
		 System.out.println(Utils.Bytes4ToLong(b, 12));
		 System.out.println(Utils.Bytes4ToLong(b, 16));

		 byte[] as = Utils.LongToBytes4(3027082007L);
		 for(byte ad: as){
			 System.out.print(ad);
		 } System.out.println();
		 System.out.println(Utils.Bytes4ToLong(as, 0));


	}

	public static void IntToBytes(int i, byte abyte0[]) {
		abyte0[1] = (byte) (0xff & i);
		abyte0[0] = (byte) ((0xff00 & i) >> 8);
	}

	public static void IntToBytes4(int i, byte abyte0[]) {
		abyte0[3] = (byte) (0xff & i);
		abyte0[2] = (byte) ((0xff00 & i) >> 8);
		abyte0[1] = (byte) ((0xff0000 & i) >> 16);
		abyte0[0] = (byte) (int) ((0xffffffffff000000L & (long) i) >> 24);
	}

	public static int Bytes4ToInt(byte abyte0[]) {
		return (0xff & abyte0[0]) << 24 | (0xff & abyte0[1]) << 16
				| (0xff & abyte0[2]) << 8 | 0xff & abyte0[3];
	}

	public static int Bytes4ToInt(byte abyte0[], int offset) {
		return (0xff & abyte0[0 + offset]) << 24
				| (0xff & abyte0[1 + offset]) << 16
				| (0xff & abyte0[2 + offset]) << 8 | 0xff & abyte0[3 + offset];
	}

	public static long Bytes4ToLong(byte abyte0[]) {
		return (255L & (long) abyte0[0]) << 24
				| (255L & (long) abyte0[1]) << 16
				| (255L & (long) abyte0[2]) << 8 | 255L & (long) abyte0[3];
	}


    /**
     * byte 转long
     * @param abyte0
     * @return
     */
    public static long Bytes8ToLong(byte[] abyte0) {
        return Bytes8ToLong(abyte0, 0);
    }

    public static long Bytes8ToLong(byte[] abyte0, int offset) {

        return (255L & (long) abyte0[7 + offset]) << 56
                |  (255L & (long) abyte0[6+ offset]) << 48
                |  (255L & (long) abyte0[5+ offset]) << 40
                |  (255L & (long) abyte0[4+ offset]) << 32
                |  (255L & (long) abyte0[3+ offset]) << 24
                |  (255L & (long) abyte0[2+ offset]) << 16
                |  (255L & (long) abyte0[1+ offset]) << 8
                |   255L & (long) abyte0[0+ offset];
    }






}

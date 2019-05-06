package com.wuweibi.bullet.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * 工具类
 * @author marker
 * @version 1.0
 * @date 2012-10-14
 * */
public class Tools {

	//序列日期生成器格式化工具
	private static SimpleDateFormat sequenceDate = new SimpleDateFormat("mmddhhmmss");
	
	
	/**
	 * 获取序列格式化日期
	 * @return long 日期格式：mmddhhmmss
	 * */
	public static long getCurrentSequenceDate(){
		return Long.valueOf(Tools.sequenceDate.format(new Date()));
	}


	/**
	 * 获取IP
	 * @return
	 */
	public static String getIp() {
		InetAddress addr ;
		try {
            addr = getLocalHostLANAddress();
		} catch (UnknownHostException e) {
			return "";
		}
		return addr.getHostAddress();

//		System.out.println("Local HostAddress: "+);
//		String hostname = addr.getHostName();
//		System.out.println("Local host name: "+hostname);
	}

    // 正确的IP拿法，即优先拿site-local地址
    private static  InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }


}

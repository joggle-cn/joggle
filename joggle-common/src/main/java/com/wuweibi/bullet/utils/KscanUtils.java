package com.wuweibi.bullet.utils;

import com.wuweibi.bullet.protocol.consts.ProtocolType;
import com.wuweibi.bullet.protocol.domain.KscanResult;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class KscanUtils {

    static String[] TCP_ARRAY = new String[]{"ssh", "rdp", "redis", "mysql"};
    static String[] KEYWORD_ARRAY = new String[]{"http", "https","dns","smb"};
    static String P_HTTP = "http";
    static String P_HTTPS = "https";
    static String P_UDP = "udp";

    public static Integer toProtocol(String service) {
        // 如果service= https 则返回 PROTOCOL_HTTPS
        if (P_HTTP.equals(service)) {
            return ProtocolType.PROTOCOL_HTTP;
        }
        if (P_HTTPS.equals(service)) {
            return ProtocolType.PROTOCOL_HTTPS;
        }
        if (P_UDP.equals(service)) {
            return ProtocolType.PROTOCOL_UDP;
        }
        return ProtocolType.PROTOCOL_TCP;
    }


    /**
     * 转换为名称
     *
     * @param kscanResult
     * @return
     */
    public static String toName(KscanResult kscanResult) {
        String productName = kscanResult.getProductName();
        if (StringUtils.isNotBlank(productName)) {
            return productName;
        }

        if (ArrayUtils.contains(KEYWORD_ARRAY, kscanResult.getService())) {
            if(StringUtils.isNotBlank(kscanResult.getKeyword())){
                return kscanResult.getKeyword();
            }
        }


        if (StringUtils.isNotBlank(kscanResult.getFingerPrint())) {
            if (kscanResult.getFingerPrint().contains("宝塔")) {
                return "宝塔面板";
            }
            if (kscanResult.getFingerPrint().contains("nginx")) {
                return "nginx";
            }
            if (!kscanResult.getFingerPrint().contains(";")) { // 如果只有一项则返回fingerPrint
                return kscanResult.getFingerPrint();
            }
        }

        return "未知设备";
    }
}

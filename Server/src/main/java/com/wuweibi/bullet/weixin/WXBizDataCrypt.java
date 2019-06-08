package com.wuweibi.bullet.weixin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * 微信小程序
 * 数据解密算法
 *
 * @author zhonghongqiang
 *
 */

public class WXBizDataCrypt {

    private String appId;

    private String sessionKey;

    public WXBizDataCrypt(String appId, String sessionKey) {
        this.appId = appId;
        this.sessionKey = sessionKey;
    }

    /**
     * 1.对称解密使用的算法为 WXBizDataCrypt-128-CBC，数据采用PKCS#7填充。
     * 2.对称解密的目标密文为 Base64_Decode(encryptedData)。
     * 3.对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
     * 4.对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
     * @param
     * @throws Exception
     */
    public JSONObject decrypt(String encryptedData, String iv) throws Exception {
        initialize();

        String jsonStr = new String("");
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            /**
             * 小程序加密数据解密算法
             * https://developers.weixin.qq.com/miniprogram/dev/api/signature.html#wxchecksessionobject
             * 1.对称解密的目标密文为 Base64_Decode(encryptedData)。
             * 2.对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
             * 3.对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
             */
            byte[] encryptedByte = base64Decoder.decodeBuffer(encryptedData);
            byte[] sessionKeyByte = base64Decoder.decodeBuffer(this.sessionKey);
            byte[] ivByte = base64Decoder.decodeBuffer(iv);
            /**
             * 以下为AES-128-CBC解密算法
             */
            SecretKeySpec skeySpec = new SecretKeySpec(sessionKeyByte, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] original = cipher.doFinal(encryptedByte);
            jsonStr = new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Illegal Buffer");
        }
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (!jsonObject.getJSONObject("watermark").get("appid").toString().equals(this.appId)){
            throw new Exception("Illegal Buffer");
        }
        return jsonObject;
    }


    public static boolean initialized = false;



    /**BouncyCastle作为安全提供，防止我们加密解密时候因为jdk内置的不支持改模式运行报错。**/
    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
}
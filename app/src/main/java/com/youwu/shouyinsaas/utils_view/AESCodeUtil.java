package com.youwu.shouyinsaas.utils_view;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESCodeUtil {

    /**
     * 密钥，必须16位
     */
    private static final String IV_STRING = "0102030405060708";
    private static Base64 base64 = new Base64();

    private static String encryptAES(String content, String key)
            throws Exception {

        byte[] byteContent = content.getBytes("UTF-8");

        // 注意，为了能与 iOS 统一
        // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(byteContent);

        return new String(base64.encode(encryptedBytes));
    }

    private static String decryptAES(String content, String key)
            throws Exception {

        byte[] encryptedBytes = base64.decode(content);

        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");

        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] result = cipher.doFinal(encryptedBytes);

        return new String(result, "UTF-8");
    }

    /**
     * 用于加密
     * @param content
     * @return
     */
    public static String base64convert(String content){
        String str1 = content.replaceAll("/", "_");
        return str1.replaceAll("\\+", "-");
    }

    /**
     * 用于解密
     * @param content
     * @return
     */
    public static String base64convert2(String content){
        String str1 = content.replaceAll("-", "\\+");
        return str1.replaceAll("_", "/");
    }

    public static String encode(String content, String key){
        try {
            return base64convert(encryptAES(content,key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(String content, String key){
        try {
            return decryptAES(base64convert2(content), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(String code){
        return decode(code,IV_STRING);
    }

    public static String encode(String code){
        return encode(code,IV_STRING);
    }


    public static void main(String[] args) {
        String content = "123456789";
        String coded = encode(content);
        System.out.println("密文：" + coded);
        String origin = decode(coded);
        System.out.println("原文：" + origin);
    }

}
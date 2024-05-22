package com.luyouxiao.project.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;

import java.util.Map;

/**
 * 签名工具
 */
public class SignUtils {
    /**
     * 生成签名
     * @param hashMap 包含需要签名的参数的哈希映射
     * @param secretKey 密钥
     * @return 生成的签名字符串
     */
    public static String genSign(Map<String, String> hashMap, String secretKey) {
        // 使用SHA256算法的Digester
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        // 构建签名内容，将哈希映射转换为字符串并拼接密钥
        String content = hashMap.toString() + "." + secretKey;
        // 计算签名的摘要并返回摘要的十六进制表示形式
        return md5.digestHex(content);
    }

    public static String genSign(String body, String secretKey) {
        return DigestUtil.md5Hex(body + "." + secretKey);
    }
}
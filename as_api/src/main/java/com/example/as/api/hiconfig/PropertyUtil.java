package com.example.as.api.hiconfig;

import org.springframework.boot.system.ApplicationHome;

public class PropertyUtil {
    /**
     * Tomcat主要用来存动态数据，暂时没有CDN所以用它
     * CDN存储静态数据更快，nigix也有负载均衡用来存静态
     * /

    /**
     * 获取文件存储路径
     * 存储到本地tomcat时
     * @param append
     * @return
     */
    public static String getDocPath(String append) {
        return new ApplicationHome(HiConfigFileUtil.class).getSource().getParentFile()/*此目录是私有目录还要获取父级*/.getParentFile().getPath() + "/" + append;
    }

    /**
     * 获取CDN路径
     *
     * @param append
     * @return
     */
    public static String getCDNPrefix(String append) {
        return "http://127.0.0.1:8080/as/" + append;
    }
}

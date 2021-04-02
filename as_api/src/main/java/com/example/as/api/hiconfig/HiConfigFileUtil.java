package com.example.as.api.hiconfig;

import com.example.as.api.util.JsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HiConfigFileUtil {
    private static final String APPEND = "file/as/";
    private static String targetDir = PropertyUtil.getDocPath(APPEND);/*普通运行的路径*/
    private static final String CND_PREFIX = PropertyUtil.getCDNPrefix(APPEND);/*真正发布运行的cdn路径*/

    private static String genFileName(String namespace, String version) {
        return namespace + "_" + version;
    }

    public static void saveContent(HiConfigModel configModel) {
        String fileName = genFileName(configModel.namespace, configModel.version);
        configModel.originalUrl = saveContent(fileName, configModel.content);
        configModel.jsonUrl = saveContent(fileName + ".json", JsonUtil.toJsonString(configModel.contentMap));
    }

    /**
     * 模拟：将配置数据保存到CDN
     * 有CDN时要根据CDN的api将本地文件传送到CDN服务器
     * @param filename
     * @param content
     * @return
     */
    public static String saveContent(String filename, String content) {
        FileOutputStream fos = null;
        File tempFile = null;
        String cdnUrl = null;
        try {
            File targetFile = new File(targetDir, filename);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            /*临时文件，防止服务器在配置时宕机，产生错误的配置文件*/
            tempFile = File.createTempFile(filename, ".temp", targetFile.getParentFile());
            fos=new FileOutputStream(tempFile);
            fos.write(content.getBytes());
            fos.flush();

            tempFile.renameTo(targetFile);/*现临时再改名是配置文件的通用做法*/
            cdnUrl=CND_PREFIX+filename;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            HiConfigUtil.close(fos);
            HiConfigUtil.delete(tempFile);
        }
        return cdnUrl;
    }
}

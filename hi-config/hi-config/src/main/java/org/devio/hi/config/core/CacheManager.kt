package org.devio.hi.config.core

import android.content.Context
import java.io.*

class CacheManager(private val context: Context) {
    /**
     * 缓存配置
     */
    fun cache(config: Config?) {
        if (config?.content == null) return
        val targetDir = getTargetDir()
        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null
        var tempFile: File? = null
        try {
            tempFile = File.createTempFile(FILE_NAME, ".tep", targetDir)
            fos = FileOutputStream(tempFile)
            oos = ObjectOutputStream(BufferedOutputStream(fos))
            oos.writeObject(config)
            oos.flush()
            val targetFile = File(targetDir, FILE_NAME)
            tempFile.renameTo(targetFile)
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            HiConfigUtil.close(oos)
            HiConfigUtil.close(fos)
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete()
            }
        }
    }

    /**
     * 恢复配置
     */
    fun <T : Config?> restore(): T? {
        var fis: FileInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            var targetFile = File(getTargetDir(), FILE_NAME)
            var result: Config?
            if (!targetFile.exists()) {
                return null
            }
            fis = FileInputStream(targetFile)
            ois = ObjectInputStream(BufferedInputStream(fis))
            result = ois.readObject() as Config
            return result as T?
        } catch (e: Throwable) {
            e.printStackTrace()
        }finally {
            HiConfigUtil.close(fis)
            HiConfigUtil.close(ois)
        }
        return null
    }

    private fun getTargetDir(): File {
        return context.filesDir
    }

    companion object {
        const val FILE_NAME = "hiconfig"
    }
}
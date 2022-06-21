package com.hong.IO;


import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * 将 byte[]转变为 File对象
 */
public class MyFileUtil {

    public static File createTmpFile(InputStream inputStream, String name, String ext, File tmpDirFile) throws IOException {
        File resultFile = File.createTempFile(name, '.' + ext, tmpDirFile);
        resultFile.deleteOnExit();
        FileUtils.copyToFile(inputStream, resultFile);
        return resultFile;
    }

    public static File bytesToFile(byte[] bytes, String fileType) throws IOException {
        return createTmpFile(new ByteArrayInputStream(bytes),
                UUID.randomUUID().toString(),
                fileType,
                //todo 这里同样会 落盘 即 无法做到 零拷贝 的直接将流对象中的 内存数据 直接转化为 File对象数据类型 而是 必须 同通过磁盘的二次转化才能够 实现这样的操作

                Files.createTempDirectory("tempFile").toFile());
    }
    
	public static void main(String[] args) {
        File file = null;
        try {
            file = MyFileUtil.bytesToFile(new byte[1024], "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }
}

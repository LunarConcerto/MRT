package com.github.lunarconcerto.mrt.util;

import com.github.lunarconcerto.mrt.MRTStarter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FileUtil {

    private FileUtil() {

    }

    /**
     * 加载本包路径中的一个文件
     * @param path 文件的包路径
     * @return 对应文件的输入流
     */
    public static InputStream getResourceAsStream(String path) throws IOException {
        return Objects.requireNonNull(MRTStarter.class.getResource(path)).openStream();
    }

    public static String getUserPath(){
        return System.getProperty("user.dir");
    }

}

package com.github.lunarconcerto.mrt.util;

import com.github.lunarconcerto.mrt.MRTStarter;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FileUtil {

    public static final Image icon = new Image(FileUtil.getResourceAsStream("img/icon.cafe.png"));

    private FileUtil() {
    }

    /**
     * 加载本包路径中的一个文件
     *
     * @param path 文件的包路径
     * @return 对应文件的输入流
     */
    public static InputStream getResourceAsStream(String path) {
        try {
            return Objects.requireNonNull(MRTStarter.class.getResource(path)).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUserPath() {
        return System.getProperty("user.dir");
    }

    public static void writeStringToClipboard(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
    }

    public static String getStringFromClipboard() {
        return Clipboard.getSystemClipboard().getString();
    }

}

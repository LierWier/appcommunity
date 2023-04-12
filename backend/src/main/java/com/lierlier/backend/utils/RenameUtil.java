package com.lierlier.backend.utils;

import java.util.UUID;

public class RenameUtil {
    public static String renameFileByUUID(String name) {
        // 生成新的文件名
        return UUID.randomUUID() + name.substring(name.lastIndexOf("."));
    }
}

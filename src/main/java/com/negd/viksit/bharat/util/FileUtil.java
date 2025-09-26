package com.negd.viksit.bharat.util;

import java.util.UUID;

public class FileUtil {

    public static String detectMimeType(byte[] fileBytes) {
        if (fileBytes.length >= 4) {
            if (fileBytes[0] == 0x25 && fileBytes[1] == 0x50 && fileBytes[2] == 0x44 && fileBytes[3] == 0x46) {
                return "application/pdf";
            }
            if (fileBytes[0] == (byte) 0x89 && fileBytes[1] == 0x50 &&
                    fileBytes[2] == 0x4E && fileBytes[3] == 0x47) {
                return "image/png";
            }
            if (fileBytes[0] == (byte) 0xFF && fileBytes[1] == (byte) 0xD8) {
                return "image/jpeg";
            }
        }
        return "application/octet-stream";
    }

    public static String generateFileName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }
}


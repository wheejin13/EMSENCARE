package com.haemilsoft.encare.network;

import androidx.annotation.NonNull;

public class IntentFileType {

    public static final String FILE_TYPE_EXCEL = "excel";
    public static final String FILE_TYPE_DOC = "doc";
    public static final String FILE_TYPE_PPT = "ppt";
    public static final String FILE_TYPE_PDF = "pdf";
    public static final String FILE_TYPE_IMAGE = "image";
    public static final String FILE_TYPE_MP3 = "mp3";
    public static final String FILE_TYPE_MP4 = "mp4";
    public static final String FILE_TYPE_TXT = "txt";

    public static String get(@NonNull String fileType) throws Exception {
        switch(fileType) {
            case FILE_TYPE_EXCEL:   return "application/vnd.ms-excel";
            case FILE_TYPE_DOC:     return "application/msword";
            case FILE_TYPE_PPT:     return "application/vnd.ms-powerpoint";
            case FILE_TYPE_PDF:     return "application/pdf";
            case FILE_TYPE_IMAGE:   return "image/*";
            case FILE_TYPE_MP3:     return "audio/*";
            case FILE_TYPE_MP4:     return "video/*";
            case FILE_TYPE_TXT:     return "text/*";
        }

        throw new Exception("fileType not defined : " + fileType);
    }
}

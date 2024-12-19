package org.soulasphyxia.webcourseproject.user_actions;

public enum LogType {
    JSON(".json", "application/octet-stream"),
    XLSX(".xlsx", "application/vnd.ms-excel"),
    CSV(".csv", "text/csv"),
    XML(".xml", "application/octet-stream"),;
    public final String extension;
    public final String mediaType;
    LogType(String extenstion, String mediaType) {
        this.extension = extenstion;
        this.mediaType = mediaType;
    }
}

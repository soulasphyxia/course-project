package org.soulasphyxia.webcourseproject.user_actions;

public enum LogType {
    JSON(".json"), XLSX(".xlsx");
    public final String extension;
    LogType(String extenstion) {
        this.extension = extenstion;
    }
}

package com.amayadream.webchat.constant;

public enum ChatSubjectEnum {
    TEXT(0, "文本"),
    NOTICE(1,"通知"),
    IMAGE(2,"图片")
    ;

    private Integer value;
    private String memo;

    ChatSubjectEnum(Integer value,String memo) {
        this.value = value;
        this.memo = memo;
    }

    public Integer getValue() {
        return value;
    }

    public String getMemo() {
        return memo;
    }
}
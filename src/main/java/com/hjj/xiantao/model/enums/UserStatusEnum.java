package com.hjj.xiantao.model.enums;


import org.springframework.util.ObjectUtils;

public enum UserStatusEnum {

    NORMAL(0,"正常"),
    BANNED(1,"封号");

    private final String text;

    private final int value;

    UserStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }


    public static UserStatusEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserStatusEnum anEnum : UserStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}

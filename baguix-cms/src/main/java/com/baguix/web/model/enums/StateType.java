package com.baguix.web.model.enums;

/**
 * 数据库记录状态枚举(整形）
 * Created by Scott on 2016/2/24.
 */
public enum StateType {
    // 草稿
    DRAFT(0,"草稿"),
    // 显示
    SHOW(1,"显示"),
    // 隐藏
    HIDE(2,"隐藏"),
    // 暂删
    DELETE(3,"暂删"),
    // 删除
    REMOVE(4,"删除");

    // 定义私有变量
    private Integer dbValue;
    private String showText;

    // 构造函数
    StateType(Integer dbValue,String showText) {
        this.dbValue = dbValue;
        this.showText = showText;
    }

    // Setter && Getter
    public Integer getDbValue() {
        return dbValue;
    }

    public void setDbValue(Integer dbValue) {
        this.dbValue = dbValue;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }
}


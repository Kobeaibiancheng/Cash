package commen;

import lombok.Getter;

@Getter
public enum  OrderStatus {
    PLAYING(1,"待支付"),OK(2,"支付成功");
    private int flag;
    private String desc;//描述

    OrderStatus(int flag,String desc) {
        this.desc = desc;
        this.flag = flag;
    }

    public static OrderStatus valueOf(int flag) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.flag == flag) {
                return orderStatus;
            }
        }
        throw new RuntimeException("orderStatus flag" + flag + "not found!");
    }
}

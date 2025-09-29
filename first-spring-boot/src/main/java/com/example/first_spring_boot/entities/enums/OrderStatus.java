package com.example.first_spring_boot.entities.enums;

public enum OrderStatus {
    WAITING_PAYMENT(1),
    PAID(2),
    SHIPPED(3),
    DELIVERED(4),
    CANCELED(5);


    //Para poder colocar o código nos enums ^^ 
    private int code;

    private OrderStatus(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static OrderStatus valueOf(int code){
        for(OrderStatus value : OrderStatus.values()){
            if (value.getCode() == code) return value;
        }
        throw new IllegalArgumentException("Esse código é inválido");
    }
}

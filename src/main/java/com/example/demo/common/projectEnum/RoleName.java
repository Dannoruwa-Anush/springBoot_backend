package com.example.demo.common.projectEnum;

public enum RoleName {
    ADMIN, MANAGER, CASHIER, CUSTOMER;

    public String getRoleName() {
        /*
         * The name() method is already provided by Java for enums, and it returns the
         * name of the enum constant in uppercase (the same as how it's declared).
         */
        return name();
    }
}

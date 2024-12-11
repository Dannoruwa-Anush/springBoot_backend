package com.example.demo.common.customException;

public class FirstLoginCustomException extends RuntimeException{
    
    public FirstLoginCustomException(String message){
        super(message);
    }
}

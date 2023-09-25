/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.cirom.Exception;

/**
 *
 * @author ciro-
 */
public class ErrorException extends RuntimeException{
     public ErrorException(String message) {
        super(message);
    }
}

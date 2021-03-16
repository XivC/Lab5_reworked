package com.xivs.dataTransfer;

/**
 * Упаковщик для Request-Response
 * Все объекты, передаваемые по Request-Response являются наследниками DataTransference
 * @param <T>
 */
public abstract class DataTransference<T>{
    public abstract T getObject();

}

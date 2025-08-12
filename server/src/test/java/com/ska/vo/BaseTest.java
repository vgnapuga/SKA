package com.ska.vo;


public interface BaseTest<T> {
    
    void testCreateNull();
    void testCreateBlank();
    void testCreateValid(T validValue);

}

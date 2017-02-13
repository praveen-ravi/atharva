package com.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 16733 on 12/02/17.
 */
 class ReflectionTest {

    public static ReflectionTest getInstance(){
        return new ReflectionTest();
    }

    void print(){
        System.out.println("Printing");
    }
}

public class TestReflection{
     public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class c = Class.forName("com.test.ReflectionTest");
         Method m= c.getDeclaredMethod("ReflectionTest");
         ReflectionTest obj = (ReflectionTest) m.invoke(null,null);
         obj.print();
     }
}

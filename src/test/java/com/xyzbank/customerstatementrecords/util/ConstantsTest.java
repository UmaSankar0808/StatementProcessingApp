package com.xyzbank.customerstatementrecords.util;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConstantsTest {

    @Test
    @DisplayName("Should not allow instantiation of Constants.java")
    public void privateConstructorTest() throws Throwable {
        final Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        UnsupportedOperationException exception =
                (UnsupportedOperationException) assertThrows(InvocationTargetException.class,
                        () -> constructor.newInstance()).getTargetException();
        assertThat(exception.getMessage(), containsString("You are not allowed to instantiate"));
    }

}
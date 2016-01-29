/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.enu;

import java.util.stream.Stream;

/**
 *
 * @author srey
 */
public enum FontConstants {
    LF_FACESIZE(32),
    
    LF_FULLFACESIZE(64),
    
    /**
     * Convenient enum to refer null option or the constant in not valid.
     */
    LF_NULL(-1);
    
    private final int value;

    private FontConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static FontConstants fromValue(int value) {
        return Stream.of(FontConstants.values()).
                filter(fc -> fc.value == value).
                findFirst().
                orElse(LF_NULL);
    }
}

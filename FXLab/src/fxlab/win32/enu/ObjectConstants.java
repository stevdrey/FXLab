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
public enum ObjectConstants {
    /**
     * Returns the current selected bitmap.
     */
    OBJ_BITMAP(7),
    
    /**
     * Returns the current selected brush.
     */
    OBJ_BRUSH(2),
    OBJ_DC(3),
    OBJ_ENHMETADC(12),
    OBJ_ENHMETAFILE(13),
    
    /**
     * Returns the current selected font.
     */
    OBJ_FONT(6),
    
    /**
     * Convenient enum to refer null option or the constant in not valid.
     */
    OBJ_NULL(-1);
    
    private final int value;

    private ObjectConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static ObjectConstants fromValue(int value) {
        return Stream.of(ObjectConstants.values()).
                filter(oc -> oc.value == value).
                findFirst().
                orElse(OBJ_NULL);
    }
}

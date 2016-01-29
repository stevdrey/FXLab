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
public enum FontWeightConstants {
    FW_DONTCARE(0),
    
    FW_THIN(100),
    
    FW_EXTRALIGHT(200),
    
    FW_ULTRALIGHT(200),
    
    FW_LIGHT(300),
    
    FW_NORMAL(400),
    
    FW_REGULAR(400),
    
    FW_MEDIUM(400),
    
    FW_SEMIBOLD(600),
    
    FW_DEMIBOLD(600),
    
    FW_BOLD(700),
    
    FW_EXTRABOLD(800),
    
    FW_ULTRABOLD(800),
    
    FW_HEAVY(900),
    
    FW_BLACK(900);
    
    private final int value;

    private FontWeightConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static FontWeightConstants fromValue(int value) {
        return Stream.of(FontWeightConstants.values()).
                filter(f -> f.value == value).
                findFirst().
                orElse(FW_DONTCARE);
    }
    
    public static FontWeightConstants fromName(String name) {
        return Stream.of(FontWeightConstants.values()).
                filter(f -> f.name().toLowerCase().contentEquals(name.toLowerCase())).
                findFirst().
                orElse(FW_DONTCARE);
    }
}
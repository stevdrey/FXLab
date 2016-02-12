/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.enu;

/**
 *
 * @author srey
 */
public enum SystemParamInfoConstants {
    SPI_GETNONCLIENTMETRICS(41);
    
    private final int value;

    private SystemParamInfoConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
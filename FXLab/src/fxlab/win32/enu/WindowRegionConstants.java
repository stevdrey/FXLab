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
public enum WindowRegionConstants {
    /**
     * The region is more than one rectangle.
     */
    COMPLEXREGION(3),
    
    /**
     * The region is a single rectangle.
     */
    SIMPLEREGION(2),
    
    /**
     * The region is empty.
     */
    NULLREGION(1),
    
    /**
     * The specified window does not have a region, or an error occurred while attempting to return the region.
     */
    ERROR(0);
    
    private final int value;

    private WindowRegionConstants(int value) {
        this.value = value;
    }
    
    public static WindowRegionConstants fromValue(int value) {
        return Stream.of(WindowRegionConstants.values()).
                filter(wrc -> wrc.value == value).
                findFirst().
                orElse(ERROR);
    }
}

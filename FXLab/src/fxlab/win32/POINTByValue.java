/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author srey
 */
public class POINTByValue extends Structure implements Structure.ByValue {

    public int x, y;

    public POINTByValue() {
    }

    public POINTByValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("x", "y");
    }

}

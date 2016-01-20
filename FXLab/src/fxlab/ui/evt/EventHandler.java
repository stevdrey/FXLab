/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.ui.evt;

import org.jnativehook.NativeInputEvent;

/**
 *
 * @author srey
 * @param <T>
 */
public interface EventHandler <T extends NativeInputEvent> {
    public void handle(T t);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.ui.evt;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

/**
 *
 * @author srey
 */
public class MouseListerImpl implements NativeMouseListener{
    private EventHandler<NativeMouseEvent> release;

    public MouseListerImpl(EventHandler<NativeMouseEvent> release) {
        super();
        
        this.setOnRelease(release);
    }

    public MouseListerImpl() {
        this(null);
    }
    
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (this.release != null)
            this.release.handle(e);
    }
    
    public final void setOnRelease(EventHandler<NativeMouseEvent> release) {
        this.release= release;
    }
}

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
    private EventHandler<NativeMouseEvent> click;

    public MouseListerImpl(EventHandler<NativeMouseEvent> click) {
        super();
        
        this.setOnClick(click);
    }

    public MouseListerImpl() {
        this(null);
    }
    
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        if (this.click != null)
            this.click.handle(e);
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
    }
    
    public final void setOnClick(EventHandler<NativeMouseEvent> click) {
        this.click= click;
    }
}

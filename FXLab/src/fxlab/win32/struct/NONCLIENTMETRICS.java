/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.struct;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author srey
 */
public class NONCLIENTMETRICS extends Structure implements Structure.ByReference {
    public int iBorderWidth;
    public int iScrollWidth;
    public int iScrollHeight;
    public int iCaptionWidth;
    public int iCaptionHeight;
    public int iSmCaptionWidth;
    public int iSmCaptionHeight;
    public int iMenuWidth;
    public int iMenuHeight;
    public int iPaddedBorderWidth;
    
    public LOGFONT lfCaptionFont;
    public LOGFONT lfSmCaptionFont;
    public LOGFONT lfMenuFont;
    public LOGFONT lfStatusFont;
    public LOGFONT lfMessageFont;

    public NONCLIENTMETRICS(int iBorderWidth, int iScrollWidth, int iScrollHeight, int iCaptionWidth, 
            int iCaptionHeight, int iSmCaptionWidth, int iSmCaptionHeight, int iMenuWidth, 
            int iMenuHeight, int iPaddedBorderWidth, LOGFONT lfCaptionFont, LOGFONT lfSmCaptionFont, 
            LOGFONT lfMenuFont, LOGFONT lfStatusFont, LOGFONT lfMessageFont) {
        super();
        
        this.iBorderWidth = iBorderWidth;
        this.iScrollWidth = iScrollWidth;
        this.iScrollHeight = iScrollHeight;
        this.iCaptionWidth = iCaptionWidth;
        this.iCaptionHeight = iCaptionHeight;
        this.iSmCaptionWidth = iSmCaptionWidth;
        this.iSmCaptionHeight = iSmCaptionHeight;
        this.iMenuWidth = iMenuWidth;
        this.iMenuHeight = iMenuHeight;
        this.iPaddedBorderWidth = iPaddedBorderWidth;
        this.lfCaptionFont = lfCaptionFont;
        this.lfSmCaptionFont = lfSmCaptionFont;
        this.lfMenuFont = lfMenuFont;
        this.lfStatusFont = lfStatusFont;
        this.lfMessageFont = lfMessageFont;
    }

    public NONCLIENTMETRICS() {
        super();
    }
    
    @Override
    protected List getFieldOrder() {
        return Arrays.asList("iBorderWidth", "iScrollWidth", "iScrollHeight", 
                "iCaptionWidth", "iCaptionHeight", "iSmCaptionWidth", "iSmCaptionHeight",
                "iMenuWidth", "iMenuHeight", "iPaddedBorderWidth", "lfCaptionFont",
                "lfSmCaptionFont", "lfMenuFont", "lfStatusFont", "lfMessageFont");
    }
    
}
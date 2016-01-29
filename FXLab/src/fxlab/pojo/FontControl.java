/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.pojo;

import fxlab.win32.enu.FontWeightConstants;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author srey
 */
public class FontControl {
    private StringProperty faceName= null;
    
    private IntegerProperty height= null;
    private IntegerProperty width= null;
    private IntegerProperty orientation= null;
    
    private BooleanProperty italic= null;
    private BooleanProperty underline= null;
    private BooleanProperty strikeOut= null;
    
    private ObjectProperty<FontWeightConstants> weight= null;

    public FontControl(String faceName, int height, int width, int orientation, 
            FontWeightConstants weight, boolean italic, boolean strikeOut, boolean underline) {
        super();
        
        this.setFaceName(faceName);
        this.setHeight(height);
        this.setItalic(italic);
        this.setOrientation(orientation);
        this.setStrikeOut(strikeOut);
        this.setUnderline(underline);
        this.setWeight(weight);
        this.setWidth(width);
    }
    
    public FontControl(String faceName, int height, int width, int orientation, 
            int weight, boolean italic, boolean strikeOut, boolean underline) {
        this(faceName, height, width, orientation, FontWeightConstants.fromValue(weight), 
                italic, strikeOut, underline);
    }

    public FontControl(String faceName, int height, int width, int orientation, 
            int weight) {
        this(faceName, height, width, orientation, weight, false, false, false);
    }

    public FontControl(String faceName) {
        this(faceName, 0, 0, 0, 0);
    }

    public FontControl() {
        this("");
    }

    public String getFaceName() {
        return this.faceName != null ? this.faceName.getValueSafe() : "";
    }
    
    public final void setFaceName(String faceName) {
        if (this.faceName == null)
            this.faceName= new SimpleStringProperty(this, "faceName", faceName);
        
        else
            this.faceName.setValue(faceName);
    }
    
    public int getHeight() {
        return this.height != null ? this.height.getValue() : 0;
    }
    
    public final void setHeight(int height) {
        if (this.height == null)
            this.height= new SimpleIntegerProperty(this, "height", height);
        
        else
            this.height.setValue(height);
    }
    
    public int getWidth() {
        return this.width != null ? this.width.getValue() : 0;
    }
    
    public final void setWidth(int width) {
        if (this.width == null)
            this.width= new SimpleIntegerProperty(this, "width", width);
        
        else
            this.width.setValue(width);
    }
    
    public int getOrientation() {
        return this.orientation != null ? this.orientation.getValue() : 0;
    }
    
    public final void setOrientation(int orientation) {
        if (this.orientation == null)
            this.orientation= new SimpleIntegerProperty(this, "orientation", orientation);
    }
    
    public boolean isItalic() {
        return this.italic != null ? this.italic.getValue() : false;
    }
    
    public final void setItalic(boolean italic) {
        if (this.italic == null)
            this.italic= new SimpleBooleanProperty(this, "italic", italic);
    }
    
    public boolean isUnderline() {
        return this.underline != null ? this.underline.getValue() : false;
    }
    
    public final void setUnderline(boolean underline) {
        if (this.underline == null)
            this.underline= new SimpleBooleanProperty(this, "underline", underline);
        
        else
            this.underline.setValue(underline);
    }
    
    public boolean isStrikeOut() {
        return this.strikeOut != null ? this.strikeOut.getValue() : false;
    }
    
    public final void setStrikeOut(boolean strikeOut) {
        if (this.strikeOut == null)
            this.strikeOut= new SimpleBooleanProperty(this, "strikeOut", strikeOut);
        
        else
            this.strikeOut.setValue(strikeOut);
    }
    
    public FontWeightConstants getWeigth() {
        return this.weight != null ? this.weight.getValue() : FontWeightConstants.FW_DONTCARE;
    }
    
    public final void setWeight(FontWeightConstants weight) {
        if (this.weight == null)
            this.weight= new SimpleObjectProperty<>(this, "weight", weight);
        
        else
            this.weight.setValue(weight);
    }
    
    public final void setWeight(int weight) {
        this.setWeight(FontWeightConstants.fromValue(weight));
    }

    public StringProperty faceNameProperty() {
        return this.faceName;
    }
    
    public IntegerProperty heightProperty() {
        return this.height;
    }
    
    public IntegerProperty widthProperty() {
        return this.width;
    }
    
    public IntegerProperty orientationProperty() {
        return this.orientation;
    }
    
    public BooleanProperty italicProperty() {
        return this.italic;
    }
    
    public BooleanProperty strikeOutProperty() {
        return this.strikeOut;
    }
    
    public BooleanProperty underlineProperty() {
        return this.underline;
    }
    
    public ObjectProperty<FontWeightConstants> weightPorperty() {
        return this.weight;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof FontControl)
            return ((FontControl) obj).getFaceName().equals(this.getFaceName());
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.faceName);
        hash = 59 * hash + Objects.hashCode(this.height);
        hash = 59 * hash + Objects.hashCode(this.width);
        hash = 59 * hash + Objects.hashCode(this.orientation);
        hash = 59 * hash + Objects.hashCode(this.italic);
        hash = 59 * hash + Objects.hashCode(this.underline);
        hash = 59 * hash + Objects.hashCode(this.strikeOut);
        hash = 59 * hash + Objects.hashCode(this.weight);
        return hash;
    }
}
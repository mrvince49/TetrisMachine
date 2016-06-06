/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Color;
import net.sourceforge.jetris.Figure;

class FigureI
extends Figure {
	private static int[] arrn = {0,1,2,3};
    protected FigureI() {
       /* int[] arrn = new int[4];
        arrn[1] = 1;
        arrn[2] = 2;
        arrn[3] = 3;*/
        super(new int[4], arrn);
    }

    protected void rotationRight() {
        int[] tmp = this.arrX;
        this.arrX = this.arrY;
        this.arrY = tmp;
    }

    protected void rotationLeft() {
        this.rotationRight();
    }

    protected int getGridVal() {
        return 1;
    }

    protected Color getGolor() {
        return COL_I;
    }
    
    public String toString()
    {
    	return "I";
    }
}
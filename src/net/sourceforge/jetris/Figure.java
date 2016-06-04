/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Color;

public abstract class Figure {
    protected static final int I = 1;
    protected static final int T = 2;
    protected static final int O = 3;
    protected static final int L = 4;
    protected static final int J = 5;
    protected static final int S = 6;
    protected static final int Z = 7;
    protected static final Color COL_I = Color.RED;
    protected static final Color COL_T = Color.GRAY;
    protected static final Color COL_O = Color.CYAN;
    protected static final Color COL_L = Color.ORANGE;
    protected static final Color COL_J = Color.MAGENTA;
    protected static final Color COL_S = Color.BLUE;
    protected static final Color COL_Z = Color.GREEN;
    public int[] arrX;
    public int[] arrY;
    public int offsetX;
    public int offsetY;
    public int offsetXLast;
    public int offsetYLast;

    protected Figure(int[] arrX, int[] arrY) {
        this.arrX = arrX;
        this.arrY = arrY;
        this.offsetY = 0;
        this.offsetYLast = 0;
        this.offsetX = 4;
        this.offsetXLast = 4;
    }
    
    protected Figure()
    {
    	
    }

    protected int getMaxRightOffset() {
        int r = Integer.MIN_VALUE;
        int i = 0;
        while (i < this.arrX.length) {
            if (r < this.arrX[i]) {
                r = this.arrX[i];
            }
            ++i;
        }
        return r + this.offsetX;
    }

    protected void setOffset(int x, int y) {
        this.offsetXLast = this.offsetX;
        this.offsetYLast = this.offsetY;
        this.offsetX = x;
        this.offsetY = y;
    }

    protected void resetOffsets() {
        this.offsetYLast = 0;
        this.offsetXLast = 0;
        this.offsetY = 0;
        this.offsetX = 0;
    }

    protected abstract void rotationRight();

    protected abstract void rotationLeft();

    protected abstract int getGridVal();

    protected abstract Color getGolor();
}
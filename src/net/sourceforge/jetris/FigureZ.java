/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Color;
import net.sourceforge.jetris.Figure;

class FigureZ
extends Figure {
    private int[][] rotations;
    private int curRotation;
    private static int[] arrn = {0,1,1,2};
    private static int[] arrn2 = {0,0,1,1};
    protected FigureZ() {
       /* int[] arrn = new int[4];
        arrn[1] = 1;
        arrn[2] = 1;
        arrn[3] = 2;
        int[] arrn2 = new int[4];
        arrn2[2] = 1;
        arrn2[3] = 1;*/
        super(arrn, arrn2);
        this.rotations = new int[4][4];
        int[] arrn3 = new int[4];
        arrn3[1] = 1;
        arrn3[2] = 1;
        arrn3[3] = 2;
        this.rotations[0] = arrn3;
        int[] arrn4 = new int[4];
        arrn4[2] = 1;
        arrn4[3] = 1;
        this.rotations[1] = arrn4;
        int[] arrn5 = new int[4];
        arrn5[0] = 1;
        arrn5[2] = 1;
        this.rotations[2] = arrn5;
        int[] arrn6 = new int[4];
        arrn6[1] = 1;
        arrn6[2] = 1;
        arrn6[3] = 2;
        this.rotations[3] = arrn6;
        this.curRotation = 0;
    }

    protected void rotationRight() {
        if (this.curRotation == 0) {
            this.arrX = this.rotations[2];
            this.arrY = this.rotations[3];
            this.curRotation = 1;
        } else {
            this.arrX = this.rotations[0];
            this.arrY = this.rotations[1];
            this.curRotation = 0;
        }
    }

    protected void rotationLeft() {
        this.rotationRight();
    }

    protected int getGridVal() {
        return 7;
    }

    protected Color getGolor() {
        return COL_Z;
    }
}
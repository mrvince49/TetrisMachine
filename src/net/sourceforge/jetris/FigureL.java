/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Color;

import net.sourceforge.jetris.Figure;

class FigureL
extends Figure {
    private int[][] rotations;
    private int curRotation;
    private static int[] arrn = {0,0,0,1};
    private static int[] arrn2 = {0,1,2,2};

    protected FigureL() {
        /*int[] arrn = new int[4];
        arrn[3] = 1;
        int[] arrn2 = new int[4];
        arrn2[1] = 1;
        arrn2[2] = 2;
        arrn2[3] = 2;*/
        super(arrn, arrn2);
        this.rotations = new int[8][4];
        int[] arrn3 = new int[4];
        arrn3[3] = 1;
        this.rotations[0] = arrn3;
        int[] arrn4 = new int[4];
        arrn4[1] = 1;
        arrn4[2] = 2;
        arrn4[3] = 2;
        this.rotations[1] = arrn4;
        int[] arrn5 = new int[4];
        arrn5[2] = 1;
        arrn5[3] = 2;
        this.rotations[2] = arrn5;
        int[] arrn6 = new int[4];
        arrn6[1] = 1;
        this.rotations[3] = arrn6;
        int[] arrn7 = new int[4];
        arrn7[1] = 1;
        arrn7[2] = 1;
        arrn7[3] = 1;
        this.rotations[4] = arrn7;
        int[] arrn8 = new int[4];
        arrn8[2] = 1;
        arrn8[3] = 2;
        this.rotations[5] = arrn8;
        int[] arrn9 = new int[4];
        arrn9[1] = 1;
        arrn9[2] = 2;
        arrn9[3] = 2;
        this.rotations[6] = arrn9;
        int[] arrn10 = new int[4];
        arrn10[0] = 1;
        arrn10[1] = 1;
        arrn10[3] = 1;
        this.rotations[7] = arrn10;
        this.curRotation = 0;
    }

    protected void rotationRight() {
        if (this.curRotation == 0) {
            this.arrX = this.rotations[2];
            this.arrY = this.rotations[3];
            this.curRotation = 1;
        } else if (this.curRotation == 1) {
            this.arrX = this.rotations[4];
            this.arrY = this.rotations[5];
            this.curRotation = 2;
        } else if (this.curRotation == 2) {
            this.arrX = this.rotations[6];
            this.arrY = this.rotations[7];
            this.curRotation = 3;
        } else if (this.curRotation == 3) {
            this.arrX = this.rotations[0];
            this.arrY = this.rotations[1];
            this.curRotation = 0;
        }
    }

    protected void rotationLeft() {
        if (this.curRotation == 0) {
            this.arrX = this.rotations[7];
            this.arrY = this.rotations[6];
            this.curRotation = 3;
        } else if (this.curRotation == 3) {
            this.arrX = this.rotations[4];
            this.arrY = this.rotations[5];
            this.curRotation = 2;
        } else if (this.curRotation == 2) {
            this.arrX = this.rotations[2];
            this.arrY = this.rotations[3];
            this.curRotation = 1;
        } else if (this.curRotation == 1) {
            this.arrX = this.rotations[0];
            this.arrY = this.rotations[1];
            this.curRotation = 0;
        }
    }

    protected int getGridVal() {
        return 4;
    }

    protected Color getGolor() {
        return COL_L;
    }
    
    public String toString()
    {
    	return "L";
    }
}
/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Color;
import net.sourceforge.jetris.Figure;

class FigureO
extends Figure {
	private static int[] arrn = {0, 0, 1, 1};
	private static int[] arrn2 = {0,1,0,1};
    protected FigureO() {
        /*int[] arrn = new int[4];
        arrn[2] = 1;
        arrn[3] = 1;
        int[] arrn2 = new int[4];
        arrn2[1] = 1;
        arrn2[3] = 1;*/
        super(arrn, arrn2);
    }

    protected void rotationRight() {
    }

    protected void rotationLeft() {
    }

    protected int getGridVal() {
        return 3;
    }

    protected Color getGolor() {
        return COL_O;
    }
}
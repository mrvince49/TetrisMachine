/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.util.Random;
import net.sourceforge.jetris.Figure;
import net.sourceforge.jetris.FigureI;
import net.sourceforge.jetris.FigureJ;
import net.sourceforge.jetris.FigureL;
import net.sourceforge.jetris.FigureO;
import net.sourceforge.jetris.FigureS;
import net.sourceforge.jetris.FigureT;
import net.sourceforge.jetris.FigureZ;

public class FigureFactory {
    Random r = new Random();
    private int[] counts = new int[7];
    private int lastLastOne;
    private int lastOne;

    FigureFactory() {
    }

    Figure getRandomFigure() {
        Figure f;
        int i = this.r.nextInt(7);
        while (this.lastLastOne == this.lastOne && this.lastOne == i + 1) {
            i = this.r.nextInt(7);
        }
        switch (i) {
            case 0: {
                f = new FigureI();
                break;
            }
            case 1: {
                f = new FigureT();
                break;
            }
            case 2: {
                f = new FigureO();
                break;
            }
            case 3: {
                f = new FigureL();
                break;
            }
            case 4: {
                f = new FigureJ();
                break;
            }
            case 5: {
                f = new FigureS();
                break;
            }
            default: {
                f = new FigureZ();
            }
        }
        this.lastLastOne = this.lastOne;
        this.lastOne = i + 1;
        int[] arrn = this.counts;
        int n = i;
        arrn[n] = arrn[n] + 1;
        i = this.r.nextInt(4);
        int j = 0;
        while (j < i) {
            f.rotationRight();
            ++j;
        }
        figureGenerated();
        return f;
    }
    
    public static boolean figureGenerated()
    {
    	return true;
    }

    protected int[] getCounts() {
        return this.counts;
    }

    protected void resetCounts() {
        int i = 0;
        while (i < this.counts.length) {
            this.counts[i] = 0;
            ++i;
        }
    }
}
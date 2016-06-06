/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import net.sourceforge.jetris.Figure;
import net.sourceforge.jetris.io.HiScore;
import main.TetrisML;

public class TetrisGrid
implements Serializable {
    static final String DAT_FILE = "JETRIS.DAT";
    public LinkedList<int[]> gLines = new LinkedList<int []>();  // OH GOD WHY WE CHANGED IT TO PUBLIC PLEASE DON'T DESTROY OUR CODE
    private int lines;
    private int score;
    private int[] dropLines;
    private int level;
    HiScore[] hiScore;

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    TetrisGrid() {
        super();
        int i = 0;
        while (i < 20) {
            this.gLines.add(new int[10]);
            ++i;
        }
        this.score = 0;
        this.lines = 0;
        this.dropLines = new int[4];
        /*try {
            this.hiScore = HiScore.load("JETRIS.DAT");
            return;
        }
        catch (Exception e) {
            this.hiScore = new HiScore[3];
            i = 0;
            //** 
            while (i < this.hiScore.length)
       // }
lbl-1000: // 1 sources:
        {
            this.hiScore[i] = new HiScore();
            this.hiScore[i].name = "<empty>";
            ++i;
            continue;
        }
//lbl22: // 1 sources:
        //File f = new File("JETRIS.DAT");
       /* try {
            HiScore.write(this.hiScore, new File ("JETRIS.DAT"));
            return;
        }
        catch (Exception e1) 
        {
            JOptionPane.showMessageDialog(null, "Could not load HiScore!", "Error", 0);
        } //NOAH WHY*/
    }

    boolean addFigure(Figure f) {
        int j = 0;
        while (j < f.arrX.length) {
            if (f.arrY[j] + f.offsetY >= 20) {
                f.setOffset(f.offsetXLast, f.offsetYLast);
                this.addFiguretoGrid(f);
                this.eliminateLines();
                return true;
            }
            if (this.gLines.get(f.arrY[j] + f.offsetY)[f.arrX[j] + f.offsetX] != 0) {
                f.setOffset(f.offsetXLast, f.offsetYLast);
                this.addFiguretoGrid(f);
                this.eliminateLines();
                return true;
            }
            ++j;
        }
        return false;
    }
    // OH NO WHAT HAVE I DONE
    public boolean isNextMoveValid(Figure f, int xOffset, int yOffset) {
        boolean b = true;
        try {
            int j = 0;
            while (j < f.arrX.length) {
                if (this.gLines.get(f.arrY[j] + yOffset)[f.arrX[j] + xOffset] != 0) {
                    b = false;
                }
                ++j;
            }
            return b;
        }
        catch (Exception e) {
        	
            return false;
        }
    }

    private void addFiguretoGrid(Figure f) {
        int j = 0;
        while (j < f.arrX.length) {
            this.gLines.get((int)(f.arrY[j] + f.offsetY))[f.arrX[j] + f.offsetX] = f.getGridVal();
            ++j;
        }
    }

    private void eliminateLines() {
        int lines = 0;
        Iterator<int[]> iter = this.gLines.iterator();
        while (iter.hasNext()) {
            int[] el = iter.next();
            boolean isFull = true;
            int j = 0;
            while (j < 10) {
                if (el[j] == 0) {
                    isFull = false;
                }
                ++j;
            }
            if (!isFull) continue;
            iter.remove();
            ++lines;
        }
        
        switch (lines) {
            case 1: {
                this.score += 100 + 5 * this.level;
                break;
            }
            case 2: {
                this.score += 400 + 20 * this.level;
                break;
            }
            case 3: {
                this.score += 900 + 45 * this.level;
                break;
            }
            case 4: {
                this.score += 1600 + 80 * this.level;
            }
        }
        this.lines += lines;
        this.level = this.lines / 10;
        if (this.level > 20) {
            this.level = 20;
        }
        if (lines > 0) {
            int[] arrn = this.dropLines;
            int n = lines - 1;
            arrn[n] = arrn[n] + 1;
        }
        int i = 0;
        while (i < lines) {
            this.gLines.add(0, new int[10]);
            ++i;
        }
    }

    boolean isGameOver(Figure f) {
        return !this.isNextMoveValid(f, 4, 0);
    }

    int getLevel() {
        return this.level;
    }

    int getLines() {
        return this.lines;
    }

    int getScore() {
        return this.score;
    }

    int[] getDropLines() {
        return this.dropLines;
    }

    void resetStats() {
        this.level = 0;
        this.score = 0;
        this.lines = 0;
        int i = 0;
        while (i < this.dropLines.length) {
            this.dropLines[i] = 0;
            ++i;
        }
    }

    int updateHiScore() {
        int i = 0;
        while (i < this.hiScore.length) {
            HiScore s = this.hiScore[i];
            if (s.score < this.score || s.score == this.score && s.lines >= this.lines) {
                switch (i) {
                    case 0: {
                        s = this.hiScore[1];
                        this.hiScore[1] = this.hiScore[0];
                        this.hiScore[2] = s;
                        this.hiScore[0] = s = new HiScore();
                        break;
                    }
                    case 1: {
                        this.hiScore[2] = s;
                        this.hiScore[1] = s = new HiScore();
                    }
                }
                s.score = this.score;
                s.lines = this.lines;
                return i;
            }
            ++i;
        }
        return -1;
    }

    void saveHiScore(String Name, int pos) {
        File f = new File("JETRIS.DAT");
        try {
            this.hiScore[pos].name = Name;
            HiScore.write(this.hiScore, f);
        }
        catch (Exception e1) {
            JOptionPane.showMessageDialog(null, "Could not save HiScore!", "Error", 0);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int[] arr : this.gLines) {
            int j = 0;
            while (j < arr.length) {
                sb.append(arr[j]);
                ++j;
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
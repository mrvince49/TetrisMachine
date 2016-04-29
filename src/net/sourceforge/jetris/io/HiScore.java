/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class HiScore
implements Serializable {
    public int score;
    public int lines;
    public int h;
    public int m;
    public int s;
    public String name;

    public static void write(HiScore[] score, File file) throws IOException {
        FileOutputStream fs = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        int i = 0;
        while (i < score.length) {
            os.writeObject(score[i]);
            ++i;
        }
        os.close();
        os = null;
        fs = null;
    }

    public static HiScore[] load(String file) throws ClassNotFoundException, IOException {
        HiScore[] r = new HiScore[3];
        FileInputStream fs = new FileInputStream(file);
        ObjectInputStream is = new ObjectInputStream(fs);
        int i = 0;
        while (i < r.length) {
            HiScore res;
            r[i] = res = (HiScore)is.readObject();
            ++i;
        }
        is.close();
        is = null;
        fs = null;
        return r;
    }
}
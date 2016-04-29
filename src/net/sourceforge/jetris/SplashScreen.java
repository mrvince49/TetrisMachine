/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.PrintStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import net.sourceforge.jetris.JetrisMainFrame;

public class SplashScreen
extends JWindow {
    private static final int W = 567;
    private static final int H = 191;

    SplashScreen() {
        this.getContentPane().add((Component)new JLabel(new ImageIcon(JetrisMainFrame.loadImage("splash.png"))), "Center");
        this.setSize(new Dimension(567, 191));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - 283, screenSize.height / 2 - 95);
        this.setVisible(true);
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException e) {
            e.printStackTrace(System.out);
        }
    }
}
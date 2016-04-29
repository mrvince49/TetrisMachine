/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

public class HTMLLink
extends JLabel
implements MouseListener {
    private String url;
    private boolean copyToClipBoard;
    private boolean startBrowser;

    public HTMLLink(String url, boolean isMail) {
        super(url);
        String string = this.url = isMail ? "mailto:" + url : url;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            this.copyToClipBoard = false;
            this.startBrowser = true;
            this.setToolTipText(url);
        } else {
            this.copyToClipBoard = true;
            this.startBrowser = false;
            this.setToolTipText("Copy link to Clipboard");
        }
        this.setForeground(Color.BLUE);
        this.addMouseListener(this);
    }

    private void startBrowser() {
        String cmdLine = "cmd.exe /c start " + this.url;
        try {
            Process p = Runtime.getRuntime().exec(cmdLine);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyToClipboard() {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection t = new StringSelection(this.url);
        c.setContents(t, new ClipboardOwner(){

            public void lostOwnership(Clipboard c, Transferable t) {
            }
        });
    }

    public void mouseClicked(MouseEvent arg0) {
        if (this.copyToClipBoard) {
            this.copyToClipboard();
        }
        if (this.startBrowser) {
            this.startBrowser();
        }
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
        this.setForeground(Color.RED);
    }

    public void mouseExited(MouseEvent arg0) {
        this.setForeground(Color.BLUE);
    }

}
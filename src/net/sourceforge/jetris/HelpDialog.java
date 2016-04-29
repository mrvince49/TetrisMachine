/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import res.ResClass;

public class HelpDialog {
    private JFrame owner;
    private JScrollPane scroll;
    private static final String NAME = "JETRIS HELP";
    static /* synthetic */ Class class$0;

    HelpDialog(JFrame owner) {
        this.owner = owner;
        try {
            JEditorPane doc = new JEditorPane(ResClass.class.getResource("help.html"));
            doc.setEditable(false);
            this.scroll = new JScrollPane(doc);
            doc.setPreferredSize(new Dimension(400, 450));
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    void show() {
        JOptionPane.showMessageDialog(this.owner, this.scroll, "JETRIS HELP", -1);
    }
}
/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris;
//NOTE: THE HI SCORE METHOD IN THIS CLASS HAS BEEN DISABLED TO PREVENT ERRORS
// IF WE CAN WE WILL TRY AND FIX IT
import java.awt.BorderLayout;
import main.TetrisML;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.TetrisML;
import net.sourceforge.jetris.Figure;
import net.sourceforge.jetris.FigureFactory;
import net.sourceforge.jetris.FigureI;
import net.sourceforge.jetris.FigureJ;
import net.sourceforge.jetris.FigureL;
import net.sourceforge.jetris.FigureO;
import net.sourceforge.jetris.FigureS;
import net.sourceforge.jetris.FigureT;
import net.sourceforge.jetris.FigureZ;
import net.sourceforge.jetris.HTMLLink;
import net.sourceforge.jetris.HelpDialog;
import net.sourceforge.jetris.SplashScreen;
import net.sourceforge.jetris.TetrisGrid;
import net.sourceforge.jetris.io.HiScore;
import net.sourceforge.jetris.io.PublishHiScore;
import res.ResClass;

public class JetrisMainFrame
extends JFrame {
    private static final String NAME = "JETRIS 1.1";
    private static final int CELL_H = 24;
    private Font font;
    private JPanel playPanel;
    private JLabel score;
    private JLabel lines;
    private JLabel time;
    private JLabel[] statsF;
    private JLabel[] statsL;
    private JLabel levelLabel;
    private JLabel hiScoreLabel;
    private JPanel[][] cells;
    public TetrisGrid tg; // OH GOD WHAT HAVE WE DONE
    private JPanel[][] next;
    private int nextX;
    private int nextY;
    public Figure f;  // OH NO NOT AGAIN ITS THE BRIAN GEMBARA COMPLEX
    private Figure fNext;
    private FigureFactory ff;
    public boolean isNewFigureDropped; // NO I DESERVE THIS THE GUY SPELLED "DROPPED" AS "DROPED" PLEBIANS, SURROUNDED BY PLEBIANS
    public boolean isGameOver; // Its an addiction, I have to feed it
    public boolean isPause; // Well I can always do another one... TO FEED MY ADDICTION!!!
    private Color nextBg;
    private TimeThread tt;
    private KeyListener keyHandler;
    private JPanel about;
    private JMenuItem jetrisRestart;
    private JMenuItem jetrisPause;
    private JMenuItem jetrisHiScore;
    private JMenuItem jetrisExit;
    private JMenuItem helpAbout;
    private JMenuItem helpJetris;
    private HelpDialog helpDialog;
    private JPanel hiScorePanel;
    private PublishHandler pH;

    public JetrisMainFrame() {
        super("JETRIS 1.1");
        SplashScreen sp = new SplashScreen();
        this.setIconImage(JetrisMainFrame.loadImage("jetris16x16.png"));
        this.keyHandler = new KeyAdapter(){

            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == 65 || code == 37) {
                    JetrisMainFrame.this.moveLeft();
                } else if (code == 68 || code == 39) {
                    JetrisMainFrame.this.moveRight();
                } else if (code == 83 || code == 40) {
                    JetrisMainFrame.this.moveDown();
                } else if (code == 87 || code == 38) {
                    JetrisMainFrame.this.rotation();
                } else if (code == 32) {
                    JetrisMainFrame.this.moveDrop();
                }
            }
        };
        this.addKeyListener(this.keyHandler);
        this.pH = new PublishHandler();
        this.font = new Font("Dialog", 0, 12);
        this.tg = new TetrisGrid();
        this.ff = new FigureFactory();
        this.nextBg = new Color(238, 238, 238);
        this.initMenu();
        JPanel all = new JPanel(new BorderLayout());
        all.add((Component)this.getStatPanel(), "West");
        all.add((Component)this.getPlayPanel(), "Center");
        all.add((Component)this.getMenuPanel(), "East");
        all.add((Component)this.getCopyrightPanel(), "South");
        this.setDefaultCloseOperation(3);
        this.getContentPane().add((Component)all, "Center");
        this.pack();
        this.setResizable(false);
        this.fNext = this.ff.getRandomFigure();
        this.dropNext();
        GridThread gt = new GridThread();
        this.tt = new TimeThread();
        gt.start();
        this.tt.start();
        this.addWindowFocusListener(new WindowFocusListener(){

            public void windowGainedFocus(WindowEvent arg0) {
            }

            public void windowLostFocus(WindowEvent arg0) {
                JetrisMainFrame.access$27(JetrisMainFrame.this, true);
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);
        this.setVisible(true);
        sp.setVisible(false);
        sp.dispose();
    }

    private void initMenu() {
        MenuHandler mH = new MenuHandler();
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        JMenu mJetris = new JMenu();
        menu.add(mJetris);
        mJetris.setText("Jetris");
        mJetris.setMnemonic('J');
        this.jetrisRestart = new JMenuItem("Restart");
        mJetris.add(this.jetrisRestart);
        this.setKeyAcceleratorMenu(this.jetrisRestart, 82, 0);
        this.jetrisRestart.addActionListener(mH);
        this.jetrisRestart.setMnemonic('R');
        this.jetrisPause = new JMenuItem("Pause");
        mJetris.add(this.jetrisPause);
        this.setKeyAcceleratorMenu(this.jetrisPause, 80, 0);
        this.jetrisPause.addActionListener(mH);
        this.jetrisPause.setMnemonic('P');
        mJetris.addSeparator();
        this.jetrisHiScore = new JMenuItem("HiScore...");
        mJetris.add(this.jetrisHiScore);
        this.setKeyAcceleratorMenu(this.jetrisHiScore, 72, 0);
        this.jetrisHiScore.addActionListener(mH);
        this.jetrisHiScore.setMnemonic('H');
        mJetris.addSeparator();
        this.jetrisExit = new JMenuItem("Exit");
        mJetris.add(this.jetrisExit);
        this.setKeyAcceleratorMenu(this.jetrisExit, 27, 0);
        this.jetrisExit.addActionListener(mH);
        this.jetrisExit.setMnemonic('X');
        JMenu mHelp = new JMenu();
        menu.add(mHelp);
        mHelp.setText("Help");
        mHelp.setMnemonic('H');
        this.helpJetris = new JMenuItem("Jetris Help");
        mHelp.add(this.helpJetris);
        this.setKeyAcceleratorMenu(this.helpJetris, 112, 0);
        this.helpJetris.addActionListener(mH);
        this.helpJetris.setMnemonic('J');
        this.helpAbout = new JMenuItem("About");
        mHelp.add(this.helpAbout);
        this.helpAbout.addActionListener(mH);
        this.helpAbout.setMnemonic('A');
    }

    private void setKeyAcceleratorMenu(JMenuItem mi, int keyCode, int mask) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, mask);
        mi.setAccelerator(ks);
    }

    private JPanel getPlayPanel() {
        this.playPanel = new JPanel();
        this.playPanel.setLayout(new GridLayout(20, 10));
        this.playPanel.setPreferredSize(new Dimension(240, 480));
        this.cells = new JPanel[20][10];
        int i = 0;
        while (i < 20) {
            int j = 0;
            while (j < 10) {
                this.cells[i][j] = new JPanel();
                this.cells[i][j].setBackground(Color.WHITE);
                this.cells[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                this.playPanel.add(this.cells[i][j]);
                ++j;
            }
            ++i;
        }
        return this.playPanel;
    }

    private JPanel getMenuPanel() {
        JPanel r = new JPanel();
        BoxLayout rL = new BoxLayout(r, 1);
        r.setLayout(rL);
        r.setBorder(new EtchedBorder());
        Dimension ra = new Dimension(5, 0);
        this.next = new JPanel[4][4];
        JPanel nextP = new JPanel();
        nextP.setLayout(new GridLayout(4, 4));
        Dimension d = new Dimension(72, 72);
        nextP.setMinimumSize(d);
        nextP.setPreferredSize(d);
        nextP.setMaximumSize(d);
        int i = 0;
        while (i < 4) {
            int j = 0;
            while (j < 4) {
                this.next[i][j] = new JPanel();
                nextP.add(this.next[i][j]);
                ++j;
            }
            ++i;
        }
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(new JLabel("NEXT:"));
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        r.add(nextP);
        r.add(Box.createRigidArea(new Dimension(100, 10)));
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(new JLabel("HI-SCORE:"));
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        this.hiScoreLabel = new JLabel("");// + this.tg.hiScore[0].score
        this.hiScoreLabel.setForeground(Color.RED);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(this.hiScoreLabel);
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        r.add(Box.createVerticalStrut(5));
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(new JLabel("SCORE:"));
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        this.score = new JLabel("0");
        this.score.setForeground(Color.BLUE);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(this.score);
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(new JLabel("LINES:"));
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        this.lines = new JLabel("0");
        this.lines.setForeground(Color.BLUE);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(this.lines);
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(new JLabel("LEVEL:"));
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        this.levelLabel = new JLabel("1");
        this.levelLabel.setForeground(Color.BLUE);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(this.levelLabel);
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(new JLabel("TIME:"));
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        this.time = new JLabel("00:00:00");
        this.time.setForeground(Color.BLUE);
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        jp.add(this.time);
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        r.add(Box.createVerticalGlue());
        r.add(this.addHelpPanel("A or \u2190 - Left"));
        r.add(this.addHelpPanel("D or \u2192 - Right"));
        r.add(this.addHelpPanel("W or \u2191 - Rotate"));
        r.add(this.addHelpPanel("S or \u2193 - Down"));
        r.add(this.addHelpPanel("Space - Drop"));
        r.add(Box.createRigidArea(new Dimension(0, 10)));
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        JButton restartBut = new JButton("Restart");
        restartBut.setToolTipText("Press 'R'");
        restartBut.setFocusable(false);
        restartBut.addKeyListener(this.keyHandler);
        restartBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                JetrisMainFrame.this.restart();
            }
        });
        d = new Dimension(90, 30);
        restartBut.setMinimumSize(d);
        restartBut.setPreferredSize(d);
        restartBut.setMaximumSize(d);
        jp.add(restartBut);
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        r.add(Box.createRigidArea(new Dimension(0, 5)));
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(ra));
        JButton pauseBut = new JButton("Pause");
        pauseBut.setToolTipText("Press 'P'");
        pauseBut.setFocusable(false);
        pauseBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                JetrisMainFrame.this.pause();
            }
        });
        pauseBut.setMinimumSize(d);
        pauseBut.setPreferredSize(d);
        pauseBut.setMaximumSize(d);
        jp.add(pauseBut);
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        return r;
    }

    private JPanel addHelpPanel(String help) {
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(new Dimension(5, 0)));
        JLabel jL = new JLabel(help);
        jL.setFont(this.font);
        jL.setForeground(Color.GRAY);
        jp.add(jL);
        jp.add(Box.createHorizontalGlue());
        return jp;
    }

    private JPanel getCopyrightPanel() {
        JPanel r = new JPanel(new BorderLayout());
        BoxLayout rL = new BoxLayout(r, 0);
        r.setLayout(rL);
        r.setBorder(new EtchedBorder());
        r.add(Box.createRigidArea(new Dimension(32, 0)));
        JLabel jL = new JLabel("Copyright (c) 2006 Nikolay G. Georgiev ");
        jL.setFont(this.font);
        HTMLLink email = new HTMLLink("ngg@users.sourceforge.net", true);
        email.setFont(this.font);
        r.add(jL);
        r.add(email);
        return r;
    }

    private JPanel getStatPanel() {
        JPanel statFP;
        int h = 12;
        JPanel r = new JPanel();
        BoxLayout rL = new BoxLayout(r, 1);
        r.setLayout(rL);
        r.setBorder(new EtchedBorder());
        Dimension d = new Dimension(4 * h, 4 * h);
        this.statsF = new JLabel[7];
        this.statsL = new JLabel[4];
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, 2));
        jp.add(Box.createRigidArea(new Dimension(5, 0)));
        jp.add(new JLabel("STATISTICS: "));
        jp.add(Box.createHorizontalGlue());
        r.add(jp);
        r.add(Box.createRigidArea(new Dimension(0, 5)));
        int k = 0;
        while (k < 7) {
            Figure f;
            JPanel[][] fig = new JPanel[4][4];
            JPanel figP = new JPanel();
            statFP = new JPanel();
            statFP.setLayout(new BoxLayout(statFP, 2));
            figP.setLayout(new GridLayout(4, 4));
            figP.setMinimumSize(d);
            figP.setPreferredSize(d);
            figP.setMaximumSize(d);
            int i = 0;
            while (i < 4) {
                int j = 0;
                while (j < 4) {
                    fig[i][j] = new JPanel();
                    fig[i][j].setBackground(this.nextBg);
                    figP.add(fig[i][j]);
                    ++j;
                }
                ++i;
            }
            switch (k + 1) {
                case 1: {
                    f = new FigureI();
                    f.setOffset(2, 0);
                    break;
                }
                case 2: {
                    f = new FigureT();
                    f.setOffset(1, 1);
                    break;
                }
                case 3: {
                    f = new FigureO();
                    f.setOffset(1, 1);
                    break;
                }
                case 5: {
                    f = new FigureJ();
                    f.setOffset(1, 1);
                    break;
                }
                case 4: {
                    f = new FigureL();
                    f.setOffset(1, 1);
                    break;
                }
                case 6: {
                    f = new FigureS();
                    f.setOffset(1, 1);
                    break;
                }
                default: {
                    f = new FigureZ();
                    f.setOffset(1, 1);
                }
            }
            i = 0;
            while (i < 4) {
                fig[f.arrY[i] + f.offsetY][f.arrX[i] + f.offsetX].setBackground(f.getGolor());
                fig[f.arrY[i] + f.offsetY][f.arrX[i] + f.offsetX].setBorder(BorderFactory.createBevelBorder(0));
                ++i;
            }
            statFP.add(figP);
            statFP.add(new JLabel("  X  "));
            this.statsF[k] = new JLabel("0");
            this.statsF[k].setForeground(Color.BLUE);
            statFP.add(this.statsF[k]);
            r.add(statFP);
            ++k;
        }
        r.add(Box.createRigidArea(new Dimension(100, 15)));
        int i = 0;
        while (i < this.statsL.length) {
            statFP = new JPanel();
            statFP.setLayout(new BoxLayout(statFP, 2));
            switch (i) {
                case 0: {
                    statFP.add(new JLabel("  Single  X  "));
                    break;
                }
                case 1: {
                    statFP.add(new JLabel("Double  X  "));
                    break;
                }
                case 2: {
                    statFP.add(new JLabel("  Triple  X  "));
                    break;
                }
                default: {
                    statFP.add(new JLabel("  Tetris  X  "));
                }
            }
            this.statsL[i] = new JLabel("0");
            this.statsL[i].setForeground(Color.BLUE);
            statFP.add(this.statsL[i]);
            r.add(statFP);
            r.add(Box.createRigidArea(new Dimension(0, 5)));
            ++i;
        }
        return r;
    }

    static Image loadImage(String imageName) {
        try {
            BufferedImage im = ImageIO.read(new BufferedInputStream(new ResClass().getClass().getResourceAsStream(imageName)));
            return im;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    private synchronized void nextMove() {
        int tmp;
        this.f.setOffset(this.nextX, this.nextY);
        if (this.tg.addFigure(this.f)) {
            this.dropNext();
            this.f.setOffset(this.nextX, this.nextY);
            this.paintTG();
        } else {
            this.clearOldPosition();
        }
        this.paintNewPosition();
        /*if (this.isGameOver && (tmp = this.tg.updateHiScore()) >= 0) {
            String s;
            while ((s = JOptionPane.showInputDialog(this, "Enter Your Name...\nMust be between 1 and 10 charachters long", "New HiScore " + (tmp + 1) + ". Place", -1)) != null && (s.length() < 1 || s.length() > 10)) {
            }
            if (s == null) {
                s = "<empty>";
            }
            this.tg.saveHiScore(s, tmp);
            if (tmp == 0) {
                this.hiScoreLabel.setText("" + this.tg.hiScore[0].score);
            }
        }*/
    }

    private void clearOldPosition() {
        int j = 0;
        while (j < 4) {
            this.cells[this.f.arrY[j] + this.f.offsetYLast][this.f.arrX[j] + this.f.offsetXLast].setBackground(Color.WHITE);
            this.cells[this.f.arrY[j] + this.f.offsetYLast][this.f.arrX[j] + this.f.offsetXLast].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            ++j;
        }
    }

    private void paintNewPosition() {
        int j = 0;
        while (j < 4) {
            this.cells[this.f.arrY[j] + this.f.offsetY][this.f.arrX[j] + this.f.offsetX].setBackground(this.f.getGolor());
            this.cells[this.f.arrY[j] + this.f.offsetY][this.f.arrX[j] + this.f.offsetX].setBorder(BorderFactory.createBevelBorder(0));
            ++j;
        }
    }

    private void paintTG() {
        int i = 0;
        for (int[] arr : this.tg.gLines) {
            int j = 0;
            while (j < arr.length) {
                if (arr[j] != 0) {
                    Color c;
                    switch (arr[j]) {
                        case 1: {
                            c = Figure.COL_I;
                            break;
                        }
                        case 2: {
                            c = Figure.COL_T;
                            break;
                        }
                        case 3: {
                            c = Figure.COL_O;
                            break;
                        }
                        case 5: {
                            c = Figure.COL_J;
                            break;
                        }
                        case 4: {
                            c = Figure.COL_L;
                            break;
                        }
                        case 6: {
                            c = Figure.COL_S;
                            break;
                        }
                        default: {
                            c = Figure.COL_Z;
                        }
                    }
                    this.cells[i][j].setBackground(c);
                    this.cells[i][j].setBorder(BorderFactory.createBevelBorder(0));
                } else {
                    this.cells[i][j].setBackground(Color.WHITE);
                    this.cells[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                }
                ++j;
            }
            ++i;
        }
    }

    private void showNext(Figure f) {
        int i = 0;
        while (i < 4) {
            int j = 0;
            while (j < 4) {
                this.next[i][j].setBackground(this.nextBg);
                this.next[i][j].setBorder(BorderFactory.createEmptyBorder());
                ++j;
            }
            ++i;
        }
        int j = 0;
        while (j < f.arrX.length) {
            this.next[f.arrY[j]][f.arrX[j]].setBackground(f.getGolor());
            this.next[f.arrY[j]][f.arrX[j]].setBorder(BorderFactory.createBevelBorder(0));
            ++j;
        }
    }

    private void dropNext() {
    	// The moment a figure is dropped, the computer reads the game state of the board
    	try
    	{
    	TetrisML.readGameState();
    	}
    	catch(Exception e)
    	{
    		//e.printStackTrace();
    	}
    	
        if (this.isGameOver) {
            return;
        }
        this.nextX = 4;
        this.nextY = 0;
        this.score.setText("" + this.tg.getScore());
        this.lines.setText("" + this.tg.getLines());
        this.levelLabel.setText(String.valueOf(this.tg.getLevel()) + " / 20");
        this.f = this.fNext;
        this.fNext = this.ff.getRandomFigure();
        this.showNext(this.fNext);
        this.isGameOver = this.tg.isGameOver(this.f);
        this.isNewFigureDropped = true;
        this.updateStats();
        
        // The figure is being dropped so we need to update the figure in the Machine Learning class
        try
        {
        	TetrisML.getFigure();
        }
        catch (Exception e)
        {
        	//e.printStackTrace();
        }
    }

    private void moveLeft() {
        if (this.isGameOver || this.isPause) {
            return;
        }
        if (this.nextX - 1 >= 0 && this.tg.isNextMoveValid(this.f, this.f.offsetX - 1, this.f.offsetY)) {
            --this.nextX;
            this.nextMove();
        }
    }

    private void moveRight() {
        if (this.isGameOver || this.isPause) {
            return;
        }
        if (this.f.getMaxRightOffset() + 1 < 10 && this.tg.isNextMoveValid(this.f, this.f.offsetX + 1, this.f.offsetY)) {
            ++this.nextX;
            this.nextMove();
        }
    }

    private synchronized void moveDown() {
        if (this.isGameOver || this.isPause) {
            return;
        }
        ++this.nextY;
        this.nextMove();
    }

    private synchronized void moveDrop() {
        if (this.isGameOver || this.isPause) {
            return;
        }
        this.f.offsetYLast = this.f.offsetY;
        this.f.offsetXLast = this.f.offsetX;
        this.clearOldPosition();
        while (this.tg.isNextMoveValid(this.f, this.f.offsetX, this.f.offsetY)) {
            this.f.setOffset(this.f.offsetX, this.f.offsetY + 1);
        }
        this.tg.addFigure(this.f);
        this.paintTG();
        this.dropNext();
        this.nextMove();
    }

    private synchronized void rotation() {
        if (this.isGameOver || this.isPause) {
            return;
        }
        int j = 0;
        while (j < this.f.arrX.length) {
            this.cells[this.f.arrY[j] + this.f.offsetY][this.f.arrX[j] + this.f.offsetX].setBackground(Color.WHITE);
            this.cells[this.f.arrY[j] + this.f.offsetY][this.f.arrX[j] + this.f.offsetX].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            ++j;
        }
        this.f.rotationRight();
        if (!this.tg.isNextMoveValid(this.f, this.f.offsetX, this.f.offsetY)) {
            this.f.rotationLeft();
        }
        this.nextMove();
    }

    private synchronized void pause() {
        this.isPause = !this.isPause;
    }

    public void restart() {
        int i = 0;
        while (i < 20) {
            int j = 0;
            while (j < 10) {
                this.tg.gLines.get((int)i)[j] = 0;
                this.cells[i][j].setBackground(Color.WHITE);
                this.cells[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                ++j;
            }
            ++i;
        }
        this.ff.resetCounts();
        this.isGameOver = false;
        this.isPause = false;
        this.fNext = this.ff.getRandomFigure();
        this.tt.resetTime();
        this.time.setText("00:00:00");
        this.tg.resetStats();
        this.dropNext();
        this.nextMove();
    }

    private void updateStats() {
        int i = 0;
        while (i < this.statsF.length) {
            this.statsF[i].setText("" + this.ff.getCounts()[i]);
            ++i;
        }
        i = 0;
        while (i < this.statsL.length) {
            this.statsL[i].setText("" + this.tg.getDropLines()[i]);
            ++i;
        }
    }

    private void doHelp() {
        if (this.helpDialog == null) {
            this.helpDialog = new HelpDialog(this);
        }
        this.helpDialog.show();
    }

    private void doAbout() {
        if (this.about == null) {
            this.setAboutPanel();
        }
        JOptionPane.showMessageDialog(this, this.about, "ABOUT", -1, new ImageIcon(JetrisMainFrame.loadImage("jetris.png")));
    }

    private void setAboutPanel() {
        this.about = new JPanel();
        this.about.setLayout(new BoxLayout(this.about, 1));
        JLabel jl = new JLabel("<HTML><B>JETRIS 1.1</B> Copyright (c) 2006 Nikolay G. Georgiev</HTML>");
        jl.setFont(this.font);
        this.about.add(jl);
        this.about.add(Box.createVerticalStrut(10));
        jl = new JLabel("WEB PAGE:");
        jl.setFont(this.font);
        this.about.add(jl);
        HTMLLink hl = new HTMLLink("http://jetris.sf.net", false);
        hl.setFont(this.font);
        this.about.add(hl);
        this.about.add(Box.createVerticalStrut(20));
        jl = new JLabel("<HTML>This program is released under the Mozilla Public License 1.1 .<BR> A copy of this is included with your copy of JETRIS<BR>and can also be found at:</HTML>");
        jl.setFont(this.font);
        this.about.add(jl);
        this.about.add(jl);
        hl = new HTMLLink("http://www.opensource.org/licenses/mozilla1.1.php", false);
        hl.setFont(this.font);
        this.about.add(hl);
    }

    private void showHiScore() {
        this.setHiScorePanel();
        JOptionPane.showMessageDialog(this, this.hiScorePanel, "HI SCORE", -1, new ImageIcon(JetrisMainFrame.loadImage("jetris32x32.png")));
        this.hiScorePanel = null;
    }

    private void setHiScorePanel() {
        this.hiScorePanel = new JPanel(new BorderLayout());
        Object[] colNames = new String[]{"Place", "Points", "Lines", "Name"};
        Object[][] data = new String[this.tg.hiScore.length + 1][colNames.length];
        data[0] = colNames;
        int i = 0;
        while (i < this.tg.hiScore.length) {
            data[i + 1] = new String[colNames.length];
            data[i + 1][0] = String.valueOf(i + 1) + ".";
            data[i + 1][1] = "" + this.tg.hiScore[i].score;
            data[i + 1][2] = "" + this.tg.hiScore[i].lines;
            data[i + 1][3] = this.tg.hiScore[i].name;
            ++i;
        }
        JTable table = new JTable(data, colNames);
        table.setAutoResizeMode(0);
        table.setBackground(new Color(230, 255, 255));
        table.setEnabled(false);
        this.hiScorePanel.add((Component)table, "Center");
        JButton jb = new JButton("Publish HiScore Online");
        jb.addActionListener(this.pH);
        this.hiScorePanel.add((Component)jb, "South");
    }

    static /* synthetic */ void access$3(JetrisMainFrame jetrisMainFrame, boolean bl) {
        jetrisMainFrame.isNewFigureDropped = bl;
    }

    static /* synthetic */ void access$7(JetrisMainFrame jetrisMainFrame, int n) {
        jetrisMainFrame.nextY = n;
    }

    static /* synthetic */ void access$27(JetrisMainFrame jetrisMainFrame, boolean bl) {
        jetrisMainFrame.isPause = bl;
    }

    private class GridThread
    extends Thread {
        private int count;

        GridThread() {
            this.count = 0;
        }

        public void run() {
            try {
                do {
                    if (JetrisMainFrame.this.isGameOver || JetrisMainFrame.this.isPause) {
                        Thread.sleep(50);
                        continue;
                    }
                    if (JetrisMainFrame.this.isNewFigureDropped) {
                        JetrisMainFrame.access$3(JetrisMainFrame.this, false);
                        this.count = 0;
                        JetrisMainFrame.this.nextMove();
                        continue;
                    }
                    Thread.sleep(50);
                    this.count += 50;
                    if (this.count + 50 * JetrisMainFrame.this.tg.getLevel() < 1100) continue;
                    this.count = 0;
                    JetrisMainFrame jetrisMainFrame = JetrisMainFrame.this;
                    JetrisMainFrame.access$7(jetrisMainFrame, jetrisMainFrame.nextY + 1);
                    JetrisMainFrame.this.nextMove();
                } while (true);
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private class TimeThread
    extends Thread {
        private int hours;
        private int min;
        private int sec;
        private int count;

        TimeThread() {
        }

        private void incSec() {
            ++this.sec;
            if (this.sec == 60) {
                this.sec = 0;
                ++this.min;
            }
            if (this.min == 60) {
                this.min = 0;
                ++this.hours;
            }
        }

        private void resetTime() {
            this.sec = 0;
            this.min = 0;
            this.hours = 0;
        }

        public void run() {
            try {
                do {
                    Thread.sleep(50);
                    if (JetrisMainFrame.this.isGameOver) {
                        Graphics g = JetrisMainFrame.this.playPanel.getGraphics();
                        Font font = new Font(g.getFont().getFontName(), 1, 24);
                        g.setFont(font);
                        g.drawString("GAME OVER", 47, 250);
                        continue;
                    }
                    if (JetrisMainFrame.this.isPause) {
                        JetrisMainFrame.this.time.setText("PAUSED");
                        continue;
                    }
                    if (this.count >= 1000) {
                        this.count = 0;
                        this.incSec();
                        JetrisMainFrame.this.time.setText(this.toString());
                        continue;
                    }
                    this.count += 50;
                } while (true);
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (this.hours < 10) {
                sb.append('0');
            }
            sb.append(this.hours);
            sb.append(':');
            if (this.min < 10) {
                sb.append('0');
            }
            sb.append(this.min);
            sb.append(':');
            if (this.sec < 10) {
                sb.append('0');
            }
            sb.append(this.sec);
            return sb.toString();
        }
    }

    private class PublishHandler
    implements ActionListener {
        PublishHandler() {
        }

        public void actionPerformed(ActionEvent ae) {
            JButton jb = (JButton)ae.getSource();
            PublishThread pt = new PublishThread(jb);
            pt.start();
        }
    }

    private class PublishThread
    extends Thread {
        private JButton but;

        PublishThread(JButton source) {
            this.but = source;
        }

        public void run() {
            this.but.setEnabled(false);
            boolean b = false;
            try {
                int i = 0;
               /* while (i < JetrisMainFrame.access$5((JetrisMainFrame)JetrisMainFrame.this).hiScore.length) {
                    PublishHiScore.publish(JetrisMainFrame.access$5((JetrisMainFrame)JetrisMainFrame.this).hiScore[i]);
                    ++i;
                }*/
            }
            catch (Exception e) {
                e.printStackTrace();
                b = true;
                JOptionPane.showMessageDialog(JetrisMainFrame.this.hiScorePanel, "Could not publish HiScore online!\nTry again later!", "ERROR", 0);
            }
            if (!b) {
                JOptionPane.showMessageDialog(JetrisMainFrame.this.hiScorePanel, "Publishing HiScore was successfull :)", "HI SCORE", 1);
            }
            this.but.setEnabled(true);
        }
    }

    private class MenuHandler
    implements ActionListener {
        MenuHandler() {
        }

        public void actionPerformed(ActionEvent e) {
            try {
                JMenuItem tmp = (JMenuItem)e.getSource();
                if (tmp == JetrisMainFrame.this.jetrisRestart) {
                    JetrisMainFrame.this.restart();
                } else if (tmp == JetrisMainFrame.this.jetrisPause) {
                    JetrisMainFrame.this.pause();
                } else if (tmp == JetrisMainFrame.this.jetrisHiScore) {
                    JetrisMainFrame.this.showHiScore();
                } else if (tmp == JetrisMainFrame.this.jetrisExit) {
                    System.exit(0);
                } else if (tmp == JetrisMainFrame.this.helpJetris) {
                    JetrisMainFrame.this.doHelp();
                } else if (tmp == JetrisMainFrame.this.helpAbout) {
                    JetrisMainFrame.this.doAbout();
                }
            }
            catch (Exception exc) {
                exc.printStackTrace(System.out);
            }
        }
    }

}
package main;
// Michael Vinciguerra and Noah Goldstein
// Senior Project
// 4/20/16
// Mr. Gill
// Machine Learning to play Tetris,
// a popular yet simple retro game
//**************************************************

import net.sourceforge.jetris.JetrisMainFrame;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TestrisMachine 
{
	public static void main(String[] args)
	{
		TetrisML myMachine = new TetrisML();
		
		while (true)
		{
			while (!TetrisML.mf.isGameOver)
			{
				if (!TetrisML.mf.isPause)
					myMachine.run();
			}
			
			System.out.println("***************** Restarting Game *****************");
			
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			TetrisML.mf.restart();
		}
	}
}
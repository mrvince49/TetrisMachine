package main;
// Michael Vinciguerra and Noah Goldstein
// Mr. Gill
// 4/20/16
// This class creates and trains the robot that will play the game
//******************************************************************

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import net.sourceforge.jetris.Figure;
import net.sourceforge.jetris.FigureFactory;
import net.sourceforge.jetris.JetrisMainFrame;

public class TetrisML 
{
	private Robot myRobot;
	public static JetrisMainFrame mf;
	public static final int[] myKeys = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_SPACE};
	public static boolean[][] board;
	public static boolean[][] prevBoard;
	public static int[][][] moves;
	public static int[] positiveMoves = {1, 4}; // setting piece down, clearing rows
	public static int[] negativeMoves = {-3, -2}; // hole formed, height penalty
	public static int numHoles, numPrevHoles;

	public TetrisML()
	{
		// This try-catch creates the robot that will play the game
		// A Robot is an object that can press buttons on a keyboard from
		// within the code
		// There are special characters which the robot can use, which all
		// belong to the KeyEvent class
		// The characters are held in the array "myKeys", as they are the only buttons
		// that the robot can press to progress in the game
		try
		{
			myRobot = new Robot();
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}

		/**
		 * This following line of code will retrieve the Jetris game from the project.
		 * The machine learning algorithm will utilize a reward system where good actions
		 * teach the robot to perform those actions again, while bad actions will be penalized
		 * which will cause the robot to avoid those actions.
		 */
		mf = new JetrisMainFrame();
		board = new boolean[20][10];
		prevBoard = new boolean[20][10];
		
		/**
		 * Moves represents a four dimensional array, where the number in each dimension corresponds to
		 * a different move the computer can make.  The first dimension will hold a value indicating the number
		 * of left moves made in a current move.  The second dimension will hold a value indicating the number 
		 * of right moves made in a current move.  The third dimension will hold a value indicating the number of 
		 * rotations the piece has undergone in the move.  
		 * Max number of left moves from original state: 4
		 * Max number of right moves from original state: 5
		 * Max number of rotations before completing a cycle: 3
		 **/
		moves = new int[4][5][3];
		
		numHoles = 0;
		numPrevHoles = 0;
	}

	// Assume at start that the game state is an empty board
	public void run()
	{
		// every time a new figure is generated, read the game state
		// the readGameState method is called in the JetrisMainFrame class by the method "dropNext"
		try 
		{
				Thread.sleep(10);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		myRobot.keyPress(KeyEvent.VK_SPACE);
		myRobot.keyRelease(KeyEvent.VK_SPACE);	
	}

	/**
	 * Retrieves the current status of the game board
	 * 
	 * @return Returns a two dimensional boolean array resembling the 20 x 10 Tetris grid, 
	 * showing which spaces are taken (value of 1 or true)
	 * and which spaces are empty (value of 0 or false)
	 */
	// 1 (true) is space taken, 0 (false) is an empty space
	public static void readGameState()
	{
		prevBoard = board;
		board = new boolean[20][10];

		LinkedList<int[]> grid = mf.tg.gLines;

		for (int i = 0; i < grid.size(); i++)
		{
			for (int x = 0; x < grid.get(i).length; x++)
			{
				if(grid.get(i)[x]!=0)
					board[i][x]=true;
				else
					board[i][x]=false;
			}
		}
		System.out.println("Game State has been read");

		/*for(int i = 0; i < board.length; i++)
		{
			for (int x = 0; x < board[i].length; x++)
			{
				if(board[i][x])
					System.out.print("1");
				else
					System.out.print("0");
			}
			System.out.println();
		}
		System.out.println("Previous Board State: ");
		for(int i = 0; i < prevBoard.length; i++)
		{
			for (int x = 0; x < prevBoard[i].length; x++)
			{
				if(prevBoard[i][x])
					System.out.print("1");
				else
					System.out.print("0");
			}
			System.out.println();
		}*/
	}

	public static void ReadGene(int gene, Robot myRobot)
	{
		int left,turn,right;

		left=gene%1000/100;
		turn=gene%100/10;
		right=gene%10;

		turn=turn%4;

		if(right>left)
		{
			right=right-left;
			left=0;
		}
		if(left>right)
		{
			left=left-right;
			right=0;
		}
		if(right==left)
		{
			right=0;
			left=0;
		}
		//left loop
		for(int i=0;i<left;i++)
		{
			myRobot.keyPress(KeyEvent.VK_LEFT);
			myRobot.keyRelease(KeyEvent.VK_LEFT);
		}
		//turn loop
		for(int i=0;i<turn;i++)
		{
			myRobot.keyPress(KeyEvent.VK_UP);
			myRobot.keyRelease(KeyEvent.VK_UP);
		}
		//right loop
		for(int i=0;i<right;i++)
		{
			myRobot.keyPress(KeyEvent.VK_RIGHT);
			myRobot.keyRelease(KeyEvent.VK_RIGHT);
		}
		myRobot.keyPress(KeyEvent.VK_SPACE);
	}
	
	public static int fitness(int gene)
	{
		int fitness = 0;
		
		/**
		 * Fitness is a calculation of a move's potential: a good move will have higher fitness 
		 * while a bad move will have lower fitness.
		 */
		
		fitness = rowsCleared() * positiveMoves[1];
		
		return fitness;
	}
	
	/**
	 * There are three types of holes: one in the middle of the board, one on an outside area (on the side),
	 * and one on the bottom (Creating a hole on the bottom of the board)
	 * This method determines whether any of these configurations are present after a piece is dropped
	 * so that a penalty may be appropriated
	 * @return An integer value indicating the number of holes in the board
	 */
	public static void getHoles()
	{
		int count = 0;
		
		// x = rows, y = columns
		for (int x = 0; x < board.length; x++) // rows down
		{
			for (int y = 0; y < board[x].length; y++) // columns across
			{
				if (board[x][y] == false) // false indicates a possible hole, then we must check around the area
				{
					if (x - 1 >= 0 && x + 1 < 20 && y + 1 < 10 && y - 1 >= 0) //to ensure we don't go out of bounds, middle hole
					{
						if (board[x - 1][y] && board[x][y + 1] && board[x][y - 1] && board[x + 1][y])
						{
							count++;
						}
					}
					else 
					{
						if (y - 1 < 0) // indicates a possible hole on left side
						{
							if (x + 1 < 20 && x - 1 >= 0) // indicates a possible hole on left side but not on bottom
							{
								if (board[x - 1][y] && board[x - 1][y+1] && board[x][y+1] && board[x + 1][y + 1] && board[x+1][y])
									count++;
							}
							else if (x + 1 == 20) // indicates a possible hole in left bottom corner
							{
								if (board[x - 1][y] && board[x - 1][y + 1] && board[x][y + 1])
									count++;
							}
						}
						else if (y + 1 == 10) // indicates a possible hole on right side
						{
							if (x + 1 < 20 && x - 1 >= 0) // indicates a possible hole on right side but not on bottom
							{
								if (board[x + 1][y] && board[x + 1][y - 1] && board[x][y - 1] && board[x + 1][y - 1] && board[x + 1][y])
									count++;
							}
							else if(x + 1 == 20) // indicates a possible hole in right bottom corner
							{
								if (board[x - 1][y] && board[x - 1][y - 1] && board[x][y - 1])
									count++;
							}
						}
						else if (x + 1 == 20)// bottom hole?
						{
							if (board[x][y - 1] && board[x - 1][y - 1] && board[x - 1][y] && board[x - 1][y + 1] && board[x][y + 1])
								count++;
						}
					}
				}
			}
		}
		
		numPrevHoles = numHoles;
		numHoles = count;
	}
}

/*
 * public static int getHoles()
	{
		int count = 0;
		
		// x = rows, y = columns
		for (int x = 0; x < board.length; x++) // rows down
		{
			for (int y = 0; y < board[x].length; y++) // columns across
			{
				if (board[x][y] == false) // false indicates a possible hole, then we must check around the area
				{
					if (x - 1 >= 0 && x + 1 < 20 && y + 1 < 10 && y - 1 >= 0) //to ensure we don't go out of bounds, middle hole
					{
						if (board[x - 1][y] && board[x][y + 1] && board[x][y - 1] && board[x + 1][y])
						{
							count++;
						}
					}
					else 
					{
						if (y - 1 < 0) // indicates a possible hole on left side
						{
							if (x + 1 < 20 && x - 1 >= 0) // indicates a possible hole on left side but not on bottom
							{
								if (board[x - 1][y] && board[x - 1][y+1] && board[x][y+1] && board[x + 1][y + 1] && board[x+1][y])
									count++;
							}
							else if (x + 1 == 20) // indicates a possible hole in left bottom corner
							{
								if (board[x - 1][y] && board[x - 1][y + 1] && board[x][y + 1])
									count++;
							}
						}
						else if (y + 1 == 10) // indicates a possible hole on right side
						{
							if (x + 1 < 20 && x - 1 >= 0) // indicates a possible hole on right side but not on bottom
							{
								if (board[x + 1][y] && board[x + 1][y - 1] && board[x][y - 1] && board[x + 1][y - 1] && board[x + 1][y])
									count++;
							}
							else if(x + 1 == 20) // indicates a possible hole in right bottom corner
							{
								if (board[x - 1][y] && board[x - 1][y - 1] && board[x][y - 1])
									count++;
							}
						}
						else if (x + 1 == 20)// bottom hole?
						{
							if (board[x][y - 1] && board[x - 1][y - 1] && board[x - 1][y] && board[x - 1][y + 1] && board[x][y + 1])
								count++;
						}
					}
				}
			}
		}
		
		return count;
	}
}
 */
//Plan of action
// Represent actions by integers
// Store those integers
// Make decisions based on that data
// Either breed new data or create random mutation
// Run this many times until it fully learns
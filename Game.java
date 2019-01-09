/** RGB Main Game Class
 * @author Jiawen Wang
 * @version last updated June 16
 * The main class of the RGB game
 */

//Imports
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Game extends JFrame
{

	/**
	 * Sets up the frame name, frame location and layout
	 */
	public Game()
	{
		// Set up the frame and the grid
		super("RGB");
		setLocation(10, 10);

		// Set up for the game area
		Container contentPane = getContentPane();
		contentPane.add(new GameArea(), BorderLayout.CENTER);
	}

	// Inner class for the maze area
	private class GameArea extends JPanel
	{
		// All arrays and values pertaining to the images needed in the game.
		private final int IMAGE_SIZE = 32;
		private Image[] gridImages;// For the walls, doors, and paths
		// Each level will have its own unique map
		private String[] mapFiles = { "default.txt", "maze.txt",
				"pseudoButtons.txt", "canvas.txt", "default.txt", "level6.txt",
				"default.txt", "default.txt", "default.txt", "default.txt" };
		private Image[] questionImages;// Each level will have a unique question
		private Image[] buttonImages;// The images for the three buttons, both
										// pressed and not pressed
		private Image[] hintImages;// Each level will have its own hint

		// Each level will need its own image(s)
		// Each row of the 2D array is for its corresponding level and each
		// element in that row is an image
		private Image[][] levelImages;
		private Image[] results;
		// Parallel to above array but has 2 elements for every element above.
		// The first of the two is the picture's column, the second is its row
		private int[][] imagePositions = { { 8, 8 }, {}, {}, { 11, 8 },
				{ 8, 7, 8, 17, 13, 17, 18, 17 }, {}, { 8, 10 }, { 8, 4 } };

		// All other images including the player's image, the paint palette, and
		// the different screens
		private Image[] playerImage;
		private Image paintPalette;
		private Image warningImage, questionMark, helpScreen;
		private Image hintOn, hintOff, helpOn, helpOff, onSound, offSound,
				introScreen;

		// All audio variables
		private AudioClip backGroundSound, buttonSound, bloop, hint;

		// The different constant values of the different paints and buttons
		private final int RED_PAINT = 10;
		private final int GREEN_PAINT = 11;
		private final int BLUE_PAINT = 12;
		private final int NO_PAINT = 0;
		private final int RED_BUTTON = 1;// Will help with the buttons to draw
		private final int GREEN_BUTTON = 3;
		private final int BLUE_BUTTON = 5;
		private final int FAKE_RED_BUTTON = 7;// Will help with the buttons to
												// draw
		private final int FAKE_GREEN_BUTTON = 8;
		private final int FAKE_BLUE_BUTTON = 9;
		private final int HIDDEN_PATH = 4;

		// The following constant integers and integer arrays help keep track of
		// the player's response to calculate their personality colour at the
		// very end
		private final int RED_CHOICE = 1;
		private final int BLUE_CHOICE = 2;
		private final int GREEN_CHOICE = 3;
		private int colourResult;
		// Each element in this array pertains to which choice to add to each
		// level if the player chooses the 'yes' door
		private final int[] yesChoice = { 0, BLUE_CHOICE, RED_CHOICE,
				BLUE_CHOICE, GREEN_CHOICE, BLUE_CHOICE, RED_CHOICE,
				BLUE_CHOICE, BLUE_CHOICE };
		// Will keep track of the choice values to add to if the player chooses
		// the 'no' door
		private final int[] noChoice = { 0, RED_CHOICE, GREEN_CHOICE,
				RED_CHOICE, RED_CHOICE, GREEN_CHOICE, GREEN_CHOICE,
				GREEN_CHOICE, 0 };

		// Will keep track of the different scores (index 1 being red choice's
		// total, index 2 being blue's, etc.).
		private int[] choiceScores;

		// Imports the Arial font for the number written on the buttons
		private final Font BUTTON_FONT = new Font("Arial.ttf", Font.PLAIN, 11);

		// The different constants that will aid in collision and help with
		// repainting the grid based on the text post
		private final int EMPTY = 0;
		private final int WALL = 1;
		private final int DOOR = 2;

		// Current level, total number of levels, whether or not level is
		// finished
		private int level = 1;
		private final int NO_OF_LEVELS = 8;
		private boolean levelCompleted;

		// Booleans that will handle the different menus
		private boolean warningMessage, hintScreenOn, helpScreenOn, soundOn,
				questionScreen, introOn, resultOn;

		// Variables to keep track of the grid and the player position
		private char[][] grid;
		private int currentRow;
		private int currentColumn;

		private char currentPaint;

		private int paletteRow, paletteColumn;
		private boolean palette;

		// Create all objects and creates the buttons' array
		private Button buttons[];
		QuestionMark qMark;
		Button redButton;
		Button greenButton;
		Button blueButton;

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Constructs a new MazeArea object
		 */
		public GameArea()
		{
			// Initialize the image arrays for the grid, hints, buttons, player,
			// questions, individual level pictures, and final results
			gridImages = new Image[200];
			hintImages = new Image[11];
			buttonImages = new Image[7];
			playerImage = new Image[13];
			questionImages = new Image[11];
			results = new Image[4];
			buttons = new Button[3];
			levelImages = new Image[10][5];

			// Declares the initial value of each choice's score
			choiceScores = new int[4];

			// These will keep track of the different images for each individual
			// level.
			// Each row in the array is for each level and each column within
			// that row will refer to a picture

			// Assign all the images for the different levels
			// This will be parallel to the array of constants imagePositions,
			// which will tell the paint component where to draw the different
			// images
			levelImages[0][0] = new ImageIcon("level1_1.png").getImage();
			levelImages[3][0] = new ImageIcon("level4_1.png").getImage();
			levelImages[4][0] = new ImageIcon("level5_1.gif").getImage();
			levelImages[4][1] = new ImageIcon("level5_2.png").getImage();
			levelImages[4][2] = new ImageIcon("level5_3.png").getImage();
			levelImages[4][3] = new ImageIcon("level5_3.png").getImage();
			levelImages[6][0] = new ImageIcon("level7_1.png").getImage();
			levelImages[7][0] = new ImageIcon("wordsearch.png").getImage();

			// These are the different images for the grid such as walls, doors,
			// and paint, the path, and buttons
			gridImages[0] = new ImageIcon("path.png").getImage();
			gridImages[WALL] = new ImageIcon("wall.png").getImage();
			gridImages[DOOR] = new ImageIcon("door.png").getImage();
			gridImages[RED_PAINT] = new ImageIcon("redPaint.png").getImage();
			gridImages[GREEN_PAINT] = new ImageIcon("greenPaint.png")
					.getImage();
			gridImages[BLUE_PAINT] = new ImageIcon("bluePaint.png").getImage();
			gridImages[4] = new ImageIcon("path.png").getImage();
			gridImages[7] = new ImageIcon("redButton.png").getImage();
			gridImages[8] = new ImageIcon("greenButton.png").getImage();
			gridImages[9] = new ImageIcon("blueButton.png").getImage();

			// The images for red, green, and blue buttons and their
			// corresponding pressed images
			buttonImages[RED_BUTTON] = new ImageIcon("redButton.png")
					.getImage();
			buttonImages[RED_BUTTON + 1] = new ImageIcon("pressedRed.png")
					.getImage();
			buttonImages[GREEN_BUTTON] = new ImageIcon("greenButton.png")
					.getImage();
			buttonImages[GREEN_BUTTON + 1] = new ImageIcon("pressedGreen.png")
					.getImage();
			buttonImages[BLUE_BUTTON] = new ImageIcon("blueButton.png")
					.getImage();
			buttonImages[BLUE_BUTTON + 1] = new ImageIcon("pressedBlue.png")
					.getImage();
			buttons[0] = redButton;
			buttons[1] = blueButton;
			buttons[2] = greenButton;

			// Initializes the music and sound effects
			// "Planetarium Suite" composed by Chris Lennertz, Copyright:
			// Nettwork Music Group
			backGroundSound = Applet
					.newAudioClip(getCompleteURL("Planetarium Suite.wav"));
			// Sound effects from freesounds.org
			buttonSound = Applet.newAudioClip(getCompleteURL("button.wav"));
			bloop = Applet.newAudioClip(getCompleteURL("Bloop.wav"));
			hint = Applet.newAudioClip(getCompleteURL("Cowbell.wav"));

			// Loads up all the player images and its painted versions
			playerImage[0] = new ImageIcon("ghostBlank.gif").getImage();
			playerImage[10] = new ImageIcon("ghostRed.gif").getImage();
			playerImage[11] = new ImageIcon("ghostGreen.gif").getImage();
			playerImage[12] = new ImageIcon("ghostBlue.gif").getImage();

			// Loads up any other images such as the paint palette and all the
			// menu screens and warnings.
			paintPalette = new ImageIcon("palette.png").getImage();
			warningImage = new ImageIcon("warningMessage.png").getImage();
			questionMark = new ImageIcon("questionMark.gif").getImage();
			helpScreen = new ImageIcon("helpScreen.gif").getImage();
			introScreen = new ImageIcon("intro.png").getImage();
			hintOn = new ImageIcon("hintOn.png").getImage();
			hintOff = new ImageIcon("hintOff.png").getImage();
			helpOn = new ImageIcon("helpOff.png").getImage();
			helpOff = new ImageIcon("helpOn.png").getImage();
			offSound = new ImageIcon("soundOff.png").getImage();
			onSound = new ImageIcon("soundOn.png").getImage();

			// Load the 8 hint screens and question screens, with each level
			// having its own hint and its own question
			hintImages[1] = new ImageIcon("hint1.png").getImage();
			hintImages[2] = new ImageIcon("hint2.png").getImage();
			hintImages[3] = new ImageIcon("hint3.png").getImage();
			hintImages[4] = new ImageIcon("hint4.png").getImage();
			hintImages[5] = new ImageIcon("hint5.png").getImage();
			hintImages[6] = new ImageIcon("hint6.png").getImage();
			hintImages[7] = new ImageIcon("hint7.png").getImage();
			hintImages[8] = new ImageIcon("hint8.png").getImage();
			questionImages[1] = new ImageIcon("question1.png").getImage();
			questionImages[2] = new ImageIcon("question2.png").getImage();
			questionImages[3] = new ImageIcon("question3.png").getImage();
			questionImages[4] = new ImageIcon("question4.png").getImage();
			questionImages[5] = new ImageIcon("question5.png").getImage();
			questionImages[6] = new ImageIcon("question6.png").getImage();
			questionImages[7] = new ImageIcon("question7.png").getImage();
			questionImages[8] = new ImageIcon("question8.png").getImage();

			// Load up the different screens for the three possible personality
			// results
			results[1] = new ImageIcon("redChoice.png").getImage();
			results[2] = new ImageIcon("greenChoice.png").getImage();
			results[3] = new ImageIcon("blueChoice.png").getImage();

			// Loads up a new level with that level's text file, setting the
			// size of the grid array
			newLevel(mapFiles[level - 1]);

			// Set the image height and width based on the path image size
			// Also sizes this panel based on the image and grid size
			Dimension size = new Dimension(grid[0].length * IMAGE_SIZE,
					grid.length * IMAGE_SIZE);
			this.setPreferredSize(size);

			// Sets all default values for when a game is started
			// The intro/backstory screen will appear first, followed by the
			// help screen. The sound will be turned on at the start of the game
			soundOn = true;
			introOn = true;
			helpScreenOn = true;
			backGroundSound.loop();

			// Sets up for keyboard input (arrow keys) on this panel
			this.setFocusable(true);
			this.addKeyListener(new KeyHandler());
			this.requestFocusInWindow();
		}

		// Gets the URL needed for audio clips
		public URL getCompleteURL(String fileName)
		{
			try
			{
				return new URL("file:" + System.getProperty("user.dir") + "/"
						+ fileName);
			}
			catch (MalformedURLException e)
			{
				System.err.println(e.getMessage());
			}
			return null;
		}

		/**
		 * Draws the value on a button
		 * 
		 * @param g
		 * @param button
		 * @param level
		 */
		public void drawNumbers(Graphics g, Button button)
		{
			// Gets the column and row of each button and draws the appropriate
			// value. Will be drawn slightly to the left if the number is two
			// digits (to centre)
			if (level != 3)
			{
				if (button.getCurrentValue() < 10)
					g.drawString("" + button.getCurrentValue(), IMAGE_SIZE
							* button.getColumn() + 14,
							IMAGE_SIZE * button.getRow() + IMAGE_SIZE / 2);
				else
					g.drawString("" + button.getCurrentValue(), IMAGE_SIZE
							* button.getColumn() + 11,
							IMAGE_SIZE * button.getRow() + IMAGE_SIZE / 2);
			}
			// Level 3 requires the values to not be shown on the button itself;
			// it should be hidden. The numbers will appear on the bottom wall
			// instead
			else
				g.drawString("" + button.getCurrentValue(),
						IMAGE_SIZE * button.getColumn() + 14, IMAGE_SIZE
								* (button.getRow() + 5) + IMAGE_SIZE / 2);
		}

		/**
		 * Repaint the tile map with the new/changed images, paths, walls,
		 * doors, characters, and buttons
		 * 
		 * @param g The Graphics context
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			// Redraw the grid with the corresponding array
			for (int row = 0; row < grid.length; row++)
				for (int column = 0; column < grid[0].length; column++)
				{
					// Put a path underneath everywhere
					g.drawImage(gridImages[EMPTY], column * IMAGE_SIZE, row
							* IMAGE_SIZE, this);
					char imageNo = grid[row][column];
					g.drawImage(gridImages[imageNo], column * IMAGE_SIZE, row
							* IMAGE_SIZE, this);

				}

			// Draw all images of the level using the image's index within the
			// 2D array along with its positions from another parallel 2D array
			// (imagePositions)
			for (int image = 0; levelImages[level - 1][image] != null; image++)
			{
				g.drawImage(levelImages[level - 1][image],
						imagePositions[level - 1][image * 2] * IMAGE_SIZE,
						imagePositions[level - 1][image * 2 + 1] * IMAGE_SIZE,
						this);
			}

			// Draw the yes and no above the doors
			g.drawString("YES", 11 * IMAGE_SIZE + 5, IMAGE_SIZE);
			g.drawString("NO", 13 * IMAGE_SIZE + 9, IMAGE_SIZE);

			// Draw the appropriate states of all three buttons
			g.drawImage(buttonImages[redButton.drawButton()],
					redButton.getColumn() * IMAGE_SIZE, redButton.getRow()
							* IMAGE_SIZE, this);
			g.drawImage(buttonImages[greenButton.drawButton()],
					greenButton.getColumn() * IMAGE_SIZE, greenButton.getRow()
							* IMAGE_SIZE, this);
			g.drawImage(buttonImages[blueButton.drawButton()],
					blueButton.getColumn() * IMAGE_SIZE, blueButton.getRow()
							* IMAGE_SIZE, this);

			// Deals the the appearance of disappearance of certain elements
			// Draws the question mark if the buttons are at the correct value
			if (qMark.isVisible() && levelCompleted)
				g.drawImage(questionMark, qMark.getColumn() * IMAGE_SIZE - 16,
						qMark.getRow() * IMAGE_SIZE - 16, this);

			// Draws a palette if the level needs it
			if (palette)
				g.drawImage(paintPalette, IMAGE_SIZE * paletteColumn,
						IMAGE_SIZE * paletteRow, this);

			// Draws the actual question if the question screen is on
			if (questionScreen)
				g.drawImage(questionImages[level], IMAGE_SIZE * 8,
						IMAGE_SIZE * 7, this);

			// Draw the moving player on its appropriate spot within the grid
			g.drawImage(playerImage[currentPaint], currentColumn * IMAGE_SIZE
					- 16, (currentRow) * IMAGE_SIZE - 26, this);

			// Draws the current value of each button
			g.setColor(Color.black);
			g.setFont(BUTTON_FONT);
			drawNumbers(g, redButton);
			drawNumbers(g, greenButton);
			drawNumbers(g, blueButton);

			// Draws the credits on the last level
			if (level == 8)
				g.drawString("Game by: Jiawen Wang", IMAGE_SIZE * 10 + 16,
						IMAGE_SIZE * 13 + 10);

			// /////Controls all screens
			// Display the warning message if the player has not yet completed
			// the level.
			if (warningMessage)
				g.drawImage(warningImage, IMAGE_SIZE * 8, IMAGE_SIZE * 4, this);

			// Handles the drawing of all the menu screens including the
			// level-not-completed warning screen, hint screens for each level,
			// whether or not the sound is on, the help screen, the intro
			// screen, and the final results screen
			if (hintScreenOn)
			{
				g.drawImage(hintOn, 0, 0, this);
				g.drawImage(hintImages[level], IMAGE_SIZE * 8, IMAGE_SIZE * 4,
						this);
			}
			else
				g.drawImage(hintOff, 0, 0, this);
			if (soundOn)
			{
				g.drawImage(onSound, 0, 0, this);
			}
			else
				g.drawImage(offSound, 0, 0, this);
			if (helpScreenOn)
			{
				g.drawImage(helpOn, 0, 0, this);
				g.drawImage(helpScreen, IMAGE_SIZE * 3 - 16, IMAGE_SIZE * 3,
						this);
			}
			else
				g.drawImage(helpOff, 0, 0, this);
			if (introOn)
				g.drawImage(introScreen, 0, 0, this);
			if (resultOn)
				g.drawImage(results[colourResult], 0, 0, this);

		} // paint component method

		public void newLevel(String mazeFileName)
		{
			// Set the default values for each level (no palette, no paint)
			palette = false;
			currentPaint = NO_PAINT;

			// Each of the following will levels will have its own starting
			// column, starting row, button position and values, question
			// mark position, and possible palette positions
			if (level == 1 || level == 3)
			{
				currentRow = 5;
				currentColumn = 5;
				redButton = new Button(10, 17, 5, 2, 1);
				greenButton = new Button(12, 17, 5, 1, 3);
				blueButton = new Button(14, 17, 5, 2, 5);
				qMark = new QuestionMark(17, 18);

			}
			else if (level == 2)
			{
				currentRow = 2;
				currentColumn = 22;
				redButton = new Button(8, 20, 5, 2, 1);
				greenButton = new Button(12, 20, 5, 5, 3);
				blueButton = new Button(16, 20, 5, 3, 5);
				qMark = new QuestionMark(17, 18);
			}
			else if (level == 4)
			{

				currentRow = 8;
				currentColumn = 10;
				redButton = new Button(10, 21, 5, 1, 1);
				greenButton = new Button(12, 21, 5, 3, 3);
				blueButton = new Button(14, 21, 5, 4, 5);
				qMark = new QuestionMark(17, 18);
				palette = true;
				paletteRow = 7;
				paletteColumn = 7;
				currentPaint = RED_PAINT;

			}
			if (level == 5)
			{
				currentRow = 5;
				currentColumn = 5;
				redButton = new Button(7, 17, 9, 5, 1);
				greenButton = new Button(12, 17, 9, 3, 3);
				blueButton = new Button(17, 17, 9, 7, 5);
				qMark = new QuestionMark(12, 5);

			}
			if (level == 6)
			{
				currentRow = 2;
				currentColumn = 1;
				redButton = new Button(22, 4, 20, 5, 1);
				greenButton = new Button(22, 10, 20, 13, 3);
				blueButton = new Button(22, 16, 20, 8, 5);
				qMark = new QuestionMark(22, 5);

			}
			if (level == 7)
			{
				currentRow = 5;
				currentColumn = 5;
				redButton = new Button(10, 17, 26, 18, 1);
				greenButton = new Button(12, 17, 26, 7, 3);
				blueButton = new Button(14, 17, 26, 2, 5);
				qMark = new QuestionMark(9, 8);

			}
			if (level == 8 || level == 9)
			{
				currentRow = 5;
				currentColumn = 5;
				redButton = new Button(10, 17, 9, 2, 1);
				greenButton = new Button(12, 17, 9, 2, 3);
				blueButton = new Button(14, 17, 9, 3, 5);
				qMark = new QuestionMark(9, 8);
				palette = true;
				paletteColumn = 6;
				paletteRow = 5;
			}
			// The final free draw mode
			if (level == 10)
			{
				currentRow = 5;
				currentColumn = 5;
				redButton = new Button(-1, 17, 99, 100, 1);
				greenButton = new Button(-1, 18, 99, 100, 3);
				blueButton = new Button(-1, 19, 99, 100, 5);
				qMark = new QuestionMark(9, 8);
				palette = true;
				paletteColumn = 1;
				paletteRow = 2;
			}

			// First hides the question mark until the user has dialed the
			// correct numbers
			qMark.makeInvisible();
			qMark.unobtain();

			// Every level starts off with the level not completed
			levelCompleted = false;

			// Will handle all the screens/options that can be turned on and off
			warningMessage = false;
			hintScreenOn = false;
			questionScreen = false;
			helpScreenOn = false;
			resultOn = false;

			// Load up the file for the particular level
			try
			{
				// Find the size of the file first to size the array
				// Standard Java file input (better than hsa.TextInputFile)
				BufferedReader mazeFile = new BufferedReader(new FileReader(
						mazeFileName));

				// Assume file has at least 1 line
				int noOfRows = 1;
				String rowStr = mazeFile.readLine();
				int noOfColumns = rowStr.length();

				while ((mazeFile.readLine()) != null)
				{
					noOfRows++;
				}
				mazeFile.close();

				// Set up the array
				grid = new char[noOfRows][noOfColumns];

				// Load in the file data into the grid (Need to re-open first)
				mazeFile = new BufferedReader(new FileReader(mazeFileName));
				for (int row = 0; row < grid.length; row++)
				{
					rowStr = mazeFile.readLine();
					for (int column = 0; column < grid[0].length; column++)
					{
						grid[row][column] = (char) (rowStr.charAt(column) - '0');
					}
				}

			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(this, mazeFileName
						+ " not a valid maze file",
						"Message - Invalid Maze File",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		}

		/**
		 * Restarts the game
		 */
		public void restartGame()
		{

			// Sets all colour choices to zero to prepare for a new result
			for (int choice = 1; choice < choiceScores.length; choice++)
			{
				choiceScores[choice] = 0;
			}

			// Sets the level to one and turns off the result screen
			level = 1;
			resultOn = false;
			newLevel(mapFiles[level - 1]);

		}

		// Inner class to handle key events
		private class KeyHandler extends KeyAdapter
		{
			public void keyPressed(KeyEvent event)
			{
				// This handles the instant disappearing of numbers as soon as
				// the player moves (level 6). Sets the whole grid (save for the
				// walls on four side) to zero
				if (level == 6)
				{
					for (int row = 2; row < grid.length - 1; row++)
					{
						for (int column = 1; column < grid[row].length - 1; column++)
						{
							if (grid[row][column] == 1)
								grid[row][column] = 0;
						}
					}
				}

				// Will move the player left, right or down based on the arrow
				// key
				// pressed if there is no wall in that direction
				if (event.getKeyCode() == KeyEvent.VK_LEFT
						&& grid[currentRow][currentColumn - 1] != 1)
				{
					currentColumn--;
				}

				if (event.getKeyCode() == KeyEvent.VK_RIGHT
						&& grid[currentRow][currentColumn + 1] != 1)
				{
					currentColumn++;
				}

				if (event.getKeyCode() == KeyEvent.VK_DOWN
						&& grid[currentRow + 1][currentColumn] != 1)
				{
					currentRow += 1;
				}

				// Pressing up will either signify that the player wants to move
				// up or that they want to enter a door.
				if (event.getKeyCode() == KeyEvent.VK_UP
						&& grid[currentRow - 1][currentColumn] != 1)
				{

					currentRow -= 1;

					// This will handle the movement through the doors

					// Displays a warning message if the player is trying to
					// enter a door without having completed the level or
					// obtaining the question mark
					if (currentRow == 1
							&& (currentColumn == 11 || currentColumn == 13)
							&& (!levelCompleted || !qMark.isObtained()))
						warningMessage = true;
					// If the player has obtained the question mark, entering
					// either the 'yes' door or 'no' door will add one to the
					// appropriate colour and enters the next level
					else if (levelCompleted && qMark.isObtained())
					{
						if (currentRow == 1 && currentColumn == 11)
						{
							choiceScores[yesChoice[level]]++;
							level++;
							newLevel(mapFiles[level - 1]);
						}
						else if (currentRow == 1 && currentColumn == 13)
						{
							choiceScores[noChoice[level]]++;
							level++;
							newLevel(mapFiles[level - 1]);
						}
					}

				}

				if (event.getKeyCode() == KeyEvent.VK_SPACE)
				{
					// Will add values to the appropriate button
					redButton.pressButton(currentRow, currentColumn);
					blueButton.pressButton(currentRow, currentColumn);
					greenButton.pressButton(currentRow, currentColumn);

					// If the player is standing on any of the palette squares
					// (the palette's square position or the three squares
					// below it), the player will be painted that particular
					// colour
					if (currentColumn == paletteColumn
							&& currentRow == paletteRow)
						currentPaint = RED_PAINT;
					else if (currentColumn == paletteColumn
							&& currentRow == paletteRow + 1)
						currentPaint = GREEN_PAINT;
					else if (currentColumn == paletteColumn
							&& currentRow == paletteRow + 2)
						currentPaint = BLUE_PAINT;
					else if (currentColumn == paletteColumn
							&& currentRow == paletteRow + 3)
						currentPaint = NO_PAINT;

					// Paints the grid the colour the character is, erases if
					// the character is not painted
					if (currentPaint == RED_PAINT || currentPaint == BLUE_PAINT
							|| currentPaint == GREEN_PAINT)
					{
						grid[currentRow][currentColumn] = (char) currentPaint;
						bloop.play();
					}
					else if (palette)
						grid[currentRow][currentColumn] = 0;

					// When the player figures out to 'paint outside the box',
					// the whole grid will turn the currently painted colour,
					// revealing the numbers required for level completion
					if (level == 4)
					{
						// Player needs to be outside of the already-drawn box
						if (currentPaint != 0
								&& (currentColumn < 6 || currentColumn > 18
										|| currentRow < 5 || currentRow > 12))
						{
							// Paints the whole map the colour except the
							// numbers tiles that will spell out the clue
							for (int row = 2; row < grid.length - 1; row++)
								for (int column = 1; column < grid[row].length - 1; column++)
									if (grid[row][column] != HIDDEN_PATH)
										grid[row][column] = currentPaint;
						}
					}

				}

				if (event.getKeyCode() == KeyEvent.VK_H)
				{
					if (hintScreenOn)
						hintScreenOn = false;
					else
					{
						hintScreenOn = true;
						hint.play();
					}

				}
				// Deals with the help screen
				if (event.getKeyCode() == KeyEvent.VK_J)
				{
					if (helpScreenOn)
						helpScreenOn = false;
					else
						helpScreenOn = true;
					bloop.play();

				}
				// The level is completed and the question mark automatically
				// shows up when all three buttons are at the correct value
				if (redButton.isCorrectValue() && blueButton.isCorrectValue()
						&& greenButton.isCorrectValue() && !qMark.isObtained())
				{
					qMark.makeVisible();
					levelCompleted = true;
				}

				// Obtains the question mark if the player's position matches
				// that of the questions mark, the level is is completed, and
				// the question mark has not yet been obtained
				if (currentColumn == qMark.getColumn()
						&& currentRow == qMark.getRow() && levelCompleted
						&& !qMark.isObtained())
				{
					qMark.makeInvisible();
					qMark.obtain();
					questionScreen = true;
					bloop.play();

				}

				// Restarts the level by resetting the map
				if (event.getKeyCode() == KeyEvent.VK_R)
				{
					bloop.play();
					newLevel(mapFiles[level - 1]);
				}

				// This will restart the game if the final result screen is on
				// and the player presses 'N' for a new game
				if (event.getKeyCode() == KeyEvent.VK_N && resultOn)
				{
					bloop.play();
					restartGame();
				}

				// Deals with level two's inverse function (switches walls and
				// paths)
				if (level == 2 && event.getKeyCode() == KeyEvent.VK_I)
				{
					// Inverses the whole grid
					for (int row = 2; row < grid.length - 1; row++)
					{
						for (int column = 1; column < grid[row].length - 1; column++)
						{
							// Turns all the ones of the array to zeroes and
							// vice versa (leaving the doors alone)
							if (grid[row][column] == 0)
								grid[row][column] = 1;
							else if (grid[row][column] == 1)
								grid[row][column] = 0;

						}
					}
				}

				// This will handle the looping and muting of the background
				// music, which can be toggled on or off with the 'S' key
				if (event.getKeyCode() == KeyEvent.VK_S)
				{
					if (soundOn)
						soundOn = false;
					else
					{
						soundOn = true;
						backGroundSound.loop();
					}
				}
				if (!soundOn)
					backGroundSound.stop();

				// Will play a short pressing sound every time a button is
				// pressed.
				if ((redButton.isPressed() || greenButton.isPressed() || blueButton
						.isPressed()) && soundOn)
					buttonSound.play();

				// This will handle level three's erasing of the pseudo buttons
				// The button will disappear as soon as the player stands on it,
				// setting that element in the grid array to zero
				if (level == 3)
				{
					if (grid[currentRow][currentColumn] >= FAKE_RED_BUTTON
							&& grid[currentRow][currentColumn] <= FAKE_BLUE_BUTTON)
						grid[currentRow][currentColumn] = 0;
				}

				// Deals the with toggling of the hint, intro, warning, and
				// questions screens which can all be turned off with the enter
				// key
				if (event.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (warningMessage || hintScreenOn || questionScreen
							|| introOn)
					{
						warningMessage = false;
						hintScreenOn = false;
						introOn = false;
						questionScreen = false;
						bloop.play();
					}
					// The player will be taken to the free draw mode (level 10)
					// if the final result screen is on and the player presses
					// enter
					if (resultOn)
					{
						level++;
						resultOn = false;
						newLevel(mapFiles[level - 1]);
						bloop.play();
					}
				}

				// When the player surpasses the number of levels, the player
				// has completed the game and the personality results are
				// calculated
				if (level == NO_OF_LEVELS + 1)
				{
					resultOn = true;
					if (choiceScores[RED_CHOICE] > choiceScores[GREEN_CHOICE]
							&& choiceScores[RED_CHOICE] > choiceScores[BLUE_CHOICE])
						colourResult = RED_CHOICE;
					else if (choiceScores[GREEN_CHOICE] > choiceScores[BLUE_CHOICE])
						colourResult = GREEN_CHOICE;
					else
						colourResult = BLUE_CHOICE;
				}
				// Repaints the grid after every press of a key
				repaint();
			}
		}
	}

	// Sets up the main frame for the Game
	public static void main(String[] args)
	{
		Game frame = new Game();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	} // main method
} // Game class


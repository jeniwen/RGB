/**
 * The Button class
 * @author Jiawen
 * @version June 16 2014
 * Will handle the position of the button, the button's values, and its state
 */
public class Button
{
	// The button's position on the map (column and row)
	private int column;
	private int row;

	// All variables pertaining to the button's value
	private int currentValue;
	private int maxValue;
	private int correctValue;

	// Whether or not the button is pressed and its colour
	private boolean pressed;
	private int colour;

	/**
	 * Creates a new Button object
	 * 
	 * @param column The column within the grid the button is in
	 * @param row The row within the grid the button is in
	 * @param maxValue The highest value a button can reach; will reset to zero
	 *            if reached
	 * @param correctValue The value the player must achieve to be correct
	 * @param colour The colour of the button (1 for red, 3 for green, 5 for
	 *            blue)
	 */
	public Button(int column, int row, int maxValue, int correctValue,
			int colour)
	{
		// Assign all values based on parameters
		this.column = column;
		this.row = row;
		this.colour = colour;
		this.maxValue = maxValue;
		this.correctValue = correctValue;

		// Set the default value of the button to zero and default state to not
		// pressed
		this.currentValue = 0;
		this.pressed = false;
	}

	/**
	 * Returns the current row the button is in
	 * 
	 * @return the current row the button is in
	 */
	public int getRow()
	{
		return this.row;
	}

	/**
	 * Returns the current column the button is in
	 * 
	 * @return the button's current column
	 */
	public int getColumn()
	{
		return this.column;
	}

	/**
	 * Returns the button's state
	 * 
	 * @return Returns whether or not the button is pressed
	 */
	public boolean isPressed()
	{
		if (this.pressed)
			return true;

		return false;
	}

	/**
	 * Unpresses the button (drawing purposes only)
	 */
	public void unpress()
	{
		this.pressed = false;
	}

	/**
	 * Returns the current value the button is at
	 * 
	 * @return the button's current value
	 */
	public int getCurrentValue()
	{
		return currentValue;
	}

	/**
	 * Determines whether or not the button is at its correct value
	 * 
	 * @return whether or not the button's correct value has been reached
	 */
	public boolean isCorrectValue()
	{
		if (this.currentValue == this.correctValue)
			return true;

		else
			return false;

	}

	/**
	 * Presses the button if the player is on the right spot and adds one to the
	 * button's value
	 * 
	 * @param currentRow The row the player is standing on
	 * @param currentColumn The column the player is standing on
	 */
	public void pressButton(int currentRow, int currentColumn)
	{
		// Presses the button if the player is standing on the button
		if (currentColumn == this.getColumn() && currentRow == this.getRow())
		{
			// Adds one as long as the button's max value has been reached and
			// resets to one if it has
			this.pressed = true;
			if (this.currentValue < maxValue)
				this.currentValue++;
			else
				this.currentValue = 0;
		}
	}

	/**
	 *  Tells the paint component how to draw the button: pressed or unpressed
	 *  (For graphical purposes only)
	 * @return the button's assigned number or the button's assigned number +1
	 *         based on whether or not the button is pressed
	 */
	public int drawButton()
	{
		if (!this.pressed)
			return this.colour;

		this.unpress();
		return this.colour + 1;
	}
}
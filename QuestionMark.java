/**
 * The question mark class
 * 
 * @author Jiawen
 * @version June 16 2014 Will handle whether or not the question mark is
 *          visible, whether it has been obtained, and its position
 */
public class QuestionMark
{
	private boolean visible;
	private int column, row;
	private boolean obtained;

	/**
	 * Constructs a new question mark class
	 * 
	 * @param column
	 * @param row
	 */
	public QuestionMark(int column, int row)
	{
		// Obtains the question mark's position from parameters
		this.row = row;
		this.column = column;

		// The question mark is not visible and not obtained at the start of
		// each level by default
		this.obtained = false;
		this.visible = false;
	}

	/**
	 * Makes the question mark appear
	 */
	public void makeVisible()
	{
		this.visible = true;
	}

	/**
	 * Hides the question mark
	 */
	public void makeInvisible()
	{
		this.visible = false;
	}

	/**
	 * Returns whether or not the question mark is visible
	 * @return
	 */
	public boolean isVisible()
	{
		if (this.visible)
			return true;

		return false;
	}

	/**
	 * Returns the question mark's row
	 * @return the question mark's row
	 */
	public int getRow()
	{
		return this.row;
	}

	/**
	 * Returns the question mark's column
	 * @return the question mark's column
	 */
	public int getColumn()
	{
		return this.column;
	}

	/**
	 * Obtains the question mark
	 */
	public void obtain()
	{
		this.obtained = true;
	}

	/**
	 * Drops the question mark for a new level
	 */
	public void unobtain()
	{
		this.obtained = false;
	}

	/**
	 * Checks whether or not the question mark has been obtained
	 * @return whether or not the question mark has been obtained
	 */
	public boolean isObtained()
	{
		if (this.obtained)
			return true;
		return false;
	}

}

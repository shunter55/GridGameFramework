package Framework;

/**
 * Location class.
 * @author Stephen Kobata
 * @version 10/26/15
 */
public final class Location
{
    private final int xCoord;
    private final int yCoord;

    /**
     * Constructor.
     * @param xVal x-coordinate.
     * @param yVal y-coordinate.
     */
    public Location(int xVal, int yVal)
    {
        this.xCoord = xVal;
        this.yCoord = yVal;
    }

    /**
     * Get x value.
     * @return the x value.
     */
    public int getX()
    {
        return xCoord;
    }

    /**
     * Get y value.
     * @return the y value.
     */
    public int getY()
    {
        return yCoord;
    }

    /**
     * Overrides equals method.
     * @return true if equals false otherwise.
     * @param o object to compare to.
     */
    @Override
    public boolean equals(Object o)
    {
        // Compare same instance.
        if (!(o instanceof Location))
        {
            return false;
        }
        // Compare x.
        if (xCoord != ((Location)o).getX())
        {
            return false;
        }
        // Compare y.
        if (yCoord != ((Location)o).getY())
        {
            return false;
        }
        return true;
    }
}

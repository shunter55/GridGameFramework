package Hurkle;

import Framework.*;

/**
 * A Piece in the Hurkle game.
 */
class HurklePiece 
{
    // A Piece can be hidden, empty, or the Hurkle
    enum Piece
    {
        empty('x'),
        hurkle('H'),
        hidden('.');        
        private final char symbol;
        private Piece(char letter)
        {
            symbol = letter;
        }
        public String toString()
        {
            return "   " + symbol;
        }
    }// end enum

    private Piece piece;
    private boolean hidden;
    private int guess;
    
    /** Construct a hidden empty piece */
    public HurklePiece()
    {
        hidden = true;
        piece = Piece.empty;
    }

    /** Construct a hidden piece for the hurkle */
    public HurklePiece(boolean hurkle)
    {
        hidden = true;
        piece = Piece.hurkle;
    }

    /** Copy constructor */
    public static HurklePiece copy(HurklePiece original)
    {
        HurklePiece newInstance = new HurklePiece();
        // Since these fields immutable, we can safely copy the references.
        newInstance.piece = original.piece;
        newInstance.hidden = original.hidden;
        newInstance.guess = original.guess;
        return newInstance;
    }
    
    /** Reveal this piece */
    public void reveal()
    {
        hidden = false;
    }

    /** See if this piece is the hurkle */
    public boolean isHurkle()
    {
        return piece == Piece.hurkle;
    }
    
    /** Set the guess number for this piece.
     * @param guessNumber the number of the guess this piece was played.
     * @pre 0 < guessNumber <= max guesses
     */
    public void setGuess(int number)
    {
        this.guess = number;
    }
     
    /** Return a printable version of this piece. */
    public String toString()
    {
        String result;
        if (hidden)
        {
            result = Piece.hidden.toString();
        }
        else
        {
            // Display the guess number for empty spots
            if (piece == Piece.empty)
            {
                result = "   " + guess; 
            }
            // Display the hurkle
            else
            {
                result=Piece.hurkle.toString();  
            }
            // In event of an internal error, show an exclamation mark.
            if (piece == null) result = "!";
        }
        return result;
    }
}

package pokerExceptions;

public class MaxNumberOfPlayersException extends Exception {

    public MaxNumberOfPlayersException() { super(); }
    public MaxNumberOfPlayersException(String details){
        super(details);
    }
}

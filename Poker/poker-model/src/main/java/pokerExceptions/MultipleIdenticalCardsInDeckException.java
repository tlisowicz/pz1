package pokerExceptions;

public class MultipleIdenticalCardsInDeckException extends Exception{

    public MultipleIdenticalCardsInDeckException(){ super(); }

    public MultipleIdenticalCardsInDeckException(String details){
        super(details);
    }
}

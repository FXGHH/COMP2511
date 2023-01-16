package dungeonmania;

public interface GameSubject {
    
	public void attach(GameObserver o);
	public void detach(GameObserver o);
	public void notifyObservers();

}

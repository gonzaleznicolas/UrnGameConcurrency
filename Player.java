
public class Player extends Thread{
	
	private int id;
	private Urn urn;
	
	public Player(int identification, Urn u) {
		this.id = identification;
		this.urn = u;
	}
	
	public void run() {
		try {
			boolean stillInIt = true;
			while(stillInIt) {
				stillInIt = !urn.play(id);
				if(urn.playersLeftInTheGame == 0)
					break;
				if(!stillInIt)
					urn.setUpNextRound();
			}
			System.out.println(String.format("Player %d exiting the game", id));
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}

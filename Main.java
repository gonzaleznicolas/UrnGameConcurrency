
public class Main {

	public static int N = 10;
	
	public static void main(String[] args) {
		
		Urn urn = new Urn(N);
		urn.setUpNextRound();
		
		Player[] players = new Player[N];
		for(int i = 0; i<N; i++) {
			players[i] = new Player(i, urn);
		}
		
		for(int i = 0; i<N; i++)
			players[i].start();
		
	}
	
}

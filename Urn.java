import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Urn {
	
	public int playersLeftInTheGame;
	private int redBall;
	private int ballAssigner;
	private Random r;
	private ReentrantLock l = new ReentrantLock(true);
	private Condition allPlayersHaveDrawn = l.newCondition();
	private boolean nextRoundReady = false;
	private Condition condNextRoundReady = l.newCondition();
	
	public Urn(int n) {
		this.playersLeftInTheGame = n;
		r = new Random();
	}
	
	public void setUpNextRound() {
		try {
			l.lock();

			ballAssigner = playersLeftInTheGame-1;
			redBall = r.nextInt(playersLeftInTheGame);
			nextRoundReady = true;
			condNextRoundReady.signalAll();
			
			System.out.println(String.format("There are %d players in this round. The red ball is ball #%d", playersLeftInTheGame, redBall));

		}finally {
			l.unlock();
		}
	}

	// executed by a player
	public boolean play(int id) throws InterruptedException {
		int ballDrawn;
		try {
			l.lock();
			
			while(!nextRoundReady)
				condNextRoundReady.await();
				
			
			ballDrawn = ballAssigner;
			
			System.out.println(String.format("Player %d has drawn ball %d", id, ballDrawn));
			
			ballAssigner--;
			
			if(ballAssigner < 0)
				allPlayersHaveDrawn.signalAll();
			//Thread.sleep(500);
			
			while(ballAssigner >= 0)
				allPlayersHaveDrawn.await();
			
			if(ballDrawn == this.redBall) {
				if (playersLeftInTheGame == 1)
					System.out.println(String.format("Player %d: I WIN!!!", id));
				else
					System.out.println(String.format("Player %d: I lose :(", id));
				playersLeftInTheGame--;
				nextRoundReady = false;
				return true;
			}
			else {
				return false;
			}
			
		}finally {
			l.unlock();
		}
	}
}

package threads;

public class IdThread extends Thread {

	long _sleepTime;

	public IdThread(long sleepTime) {
		_sleepTime = sleepTime;
	}

	public void run() {
		try {
			System.out.println(currentThread().getName());
			Thread.sleep(_sleepTime);
			System.out.println(currentThread().getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

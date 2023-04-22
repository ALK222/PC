package locks;

public class Lock2 implements Lock {
	
	private volatile int _last;
	
	private volatile boolean _locked;
	
	public Lock2()
	{
		_last = -1;
		_locked = false;
	}

	@Override
	public int takeLock(int pid) {
		
		while(_locked);
		while(_last == pid);
		
		_locked = true;
		return 0;
	}

	@Override
	public int releaseLock(int pid) {
		_locked = false;
		_last = pid;
		return 0;
	}

}

package io.snow.core.aio;


public class SendRunnable implements Runnable {
	
	private AioConnect connect;
	private byte[] data;
	
	public SendRunnable(AioConnect connect, byte[] data) {
		this.connect = connect;
		this.data = data;
	}
	
	@Override
	public void run() {
		connect.write(data);
	}

}

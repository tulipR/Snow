package io.snow.core.nio;


public class IoFilterAdapter implements IoFilter {

	@Override
	public void messageReceived(NextFilter nextFilter, String message) throws Exception {
		nextFilter.messageReceived(message);
	}

	@Override
	public void messageWrite(NextFilter nextFilter, String message) throws Exception {
		nextFilter.messageWrite(message);
	}

}

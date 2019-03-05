package io.snow.core.nio;


public class IoFilterAdapter implements IoFilter {

	@Override
	public void messageReceived(NioConnect connect, NextFilter nextFilter, Object message) throws Exception {
		nextFilter.messageReceived(connect, message);
	}

	@Override
	public void messageWrite(NioConnect connect, NextFilter nextFilter, Object message) throws Exception {
		nextFilter.messageWrite(connect, message);
	}

}

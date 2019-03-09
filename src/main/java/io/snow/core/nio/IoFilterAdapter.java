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

	@Override
	public void connectCreated(NioConnect connect,NextFilter nextFilter) {
		nextFilter.connectCreated(connect);
	}

	@Override
	public void connectClosed(NioConnect connect,NextFilter nextFilter) {
		nextFilter.connectClosed(connect);
	}

	@Override
	public void connectAbnomalClosed(NioConnect connect,NextFilter nextFilter, Exception exception) {
		nextFilter.connectAbnormalClosed(connect, exception);
	}

}

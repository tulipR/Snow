package io.snow.core.nio;

/**
 * 
 * @author zhangliang 2018.11.23
 *
 */
public interface IoFilter {

	void connectCreated(NioConnect connect, NextFilter nextFilter);
	
	void connectClosed(NioConnect connect, NextFilter nextFilter);
	
	void connectAbnomalClosed(NioConnect connect, NextFilter nextFilter, Exception exception);
	
	void messageReceived(NioConnect connect, NextFilter nextFilter, Object message) throws Exception;

	void messageWrite(NioConnect connect, NextFilter nextFilter, Object message) throws Exception;

	interface NextFilter {
		
		void connectCreated(NioConnect connect);
		
		void connectClosed(NioConnect connect);
		
		void connectAbnormalClosed(NioConnect connect, Exception exception);
		
		void messageReceived(NioConnect connect, Object message);

		void messageWrite(NioConnect connect, Object message);
	}
}

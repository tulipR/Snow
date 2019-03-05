package io.snow.core.nio;

/**
 * 
 * @author zhangliang 2018.11.23
 *
 */
public interface IoFilter {

	void messageReceived(NioConnect connect, NextFilter nextFilter, Object message) throws Exception;

	void messageWrite(NioConnect connect, NextFilter nextFilter, Object message) throws Exception;

	interface NextFilter {
		void messageReceived(NioConnect connect, Object message);

		void messageWrite(NioConnect connect, Object message);
	}
}

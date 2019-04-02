package io.snow.core.nio;

/**
 * 
 * @author zhangliang 2018.11.23
 *
 */
public interface IoFilter {
	
	void messageReceived(NextFilter nextFilter, Object message) throws Exception;

	void messageWrite(NextFilter nextFilter, String message) throws Exception;

	interface NextFilter {
		void messageReceived(Object message);

		void messageWrite(String  message);
	}
}

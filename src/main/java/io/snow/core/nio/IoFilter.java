package io.snow.core.nio;

/**
 * 
 * @author zhangliang 2018.11.23
 *
 */
public interface IoFilter {

	/** 连接创建 */
	void connectCreated(NioConnect connect, NextFilter nextFilter);
	
	/** 连接正常关闭 */
	void connectClosed(NioConnect connect, NextFilter nextFilter);
	
	/** 连接非正常关闭 */
	void connectAbnomalClosed(NioConnect connect, NextFilter nextFilter, Exception exception);
	
	/** 接受到消息 */
	void messageReceived(NioConnect connect, NextFilter nextFilter, Object message) throws Exception;

	/** 写消息 */
	void messageWrite(NioConnect connect, NextFilter nextFilter, Object message) throws Exception;

	interface NextFilter {
		
		/** 连接创建 */
		void connectCreated(NioConnect connect);
		
		/** 连接正常关闭 */
		void connectClosed(NioConnect connect);
		
		/** 连接非正常关闭 */
		void connectAbnormalClosed(NioConnect connect, Exception exception);
		
		/** 接受到消息 */
		void messageReceived(NioConnect connect, Object message);

		/** 写消息 */
		void messageWrite(NioConnect connect, Object message);
	}
}

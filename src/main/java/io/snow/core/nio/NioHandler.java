package io.snow.core.nio;

/**
 * 
 * @author zhangliang	2019.02.26
 *
 */
public interface NioHandler {
	
	void received(NioConnect connect, Message message);
	
	void write(NioConnect connect, Object message);
	
	void connected(NioConnect connect);
	
	/** 正常关闭 */
	void close(NioConnect connect);
	
	/** 异常关闭 */
	void abnormalClose(NioConnect connect, Exception e);
}

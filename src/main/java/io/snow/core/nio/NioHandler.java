package io.snow.core.nio;

/**
 * 
 * @author zhangliang	2019.02.26
 *
 */
public interface NioHandler<T> {
	
	void received(NioConnect connect, MessagePacket<T> message);
	
	void write();
	
	void connected();
	
	/** 正常关闭 */
	void close();
	
	/** 异常关闭 */
	void abnormalClose(NioConnect connect, Exception e);
}
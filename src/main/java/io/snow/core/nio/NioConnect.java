package io.snow.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author zhangliang	2019.02.25
 *
 */
public class NioConnect {
	
	private ByteBuffer readBuff = ByteBuffer.allocate(1024);

	private ByteBuffer writeBuff = ByteBuffer.allocate(1024);
	
	private SocketChannel socketChannel;
	
	/** id生成器 */
    private static AtomicLong idGenerator = new AtomicLong(0);
    
    /** 连接id */
    private final long id;
	
	/**
	 * 构造函数
	 * @param socketChannel 不能为空
	 */
	public NioConnect(SocketChannel socketChannel) {
		if (socketChannel== null) {
			throw new NullPointerException("socketChannel can not be null");
		}
		this.socketChannel = socketChannel;
		id = idGenerator.incrementAndGet();
	}
	
	/** 获得id */
	public long getId() {
		return id;
	}
	
	/**
	 * 获得当前连接的SocketChannel
	 * @return 
	 */
	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}
	
	/**
	 * 获得当前连接的read ByteBuffer
	 * @return
	 */
	protected ByteBuffer getReadByteBuffer() {
		return readBuff;
	}
	
	/**
	 * 获得当前连接的write ByteBuffer
	 * @return
	 */
	protected ByteBuffer getWriteByteBuffer() {
		return writeBuff;
	}

	public void close() throws IOException {
		socketChannel.close();
	}

	public void abnormalClose(Exception e) throws IOException {
		socketChannel.close();
	}
}

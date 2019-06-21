package io.snow.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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
	
	private SelectionKey selectionKey;
	
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
	
	/** 初始化连接 */
	public void init(Selector selector) throws IOException {
		socketChannel.configureBlocking(false);
		// 当selector阻塞时，此方法也将阻塞
		selectionKey = socketChannel.register(selector,SelectionKey.OP_READ,this);
	}
	
	/** 获得当前连接的read ByteBuffer */
	protected ByteBuffer getReadByteBuffer() {
		return readBuff;
	}
	
	/** 获得当前连接的write ByteBuffer */
	protected ByteBuffer getWriteByteBuffer() {
		return writeBuff;
	}
	
	/** 读数据 */
	protected int read(ByteBuffer data) throws IOException {
		return socketChannel.read(data);
	}
	
	/** 写数据 */
	protected int write(ByteBuffer data) throws IOException {
		return socketChannel.write(data);
	}

	public void close() throws IOException {
		socketChannel.close();
	}

	public void abnormalClose(Exception e) throws IOException {
		socketChannel.close();
	}
	
	public void setOpWrite() {
		int interestOps=selectionKey.interestOps();
		if ((interestOps & SelectionKey.OP_WRITE) == 0) {
            selectionKey.interestOps(interestOps | SelectionKey.OP_WRITE);
        }
	}
	
	public void cleanOpWrite() {
		final int interestOps = selectionKey.interestOps();
        if ((interestOps & SelectionKey.OP_WRITE) != 0) {
            selectionKey.interestOps(interestOps & ~SelectionKey.OP_WRITE);
        }
	}
}

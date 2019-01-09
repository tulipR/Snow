package io.snow.core.aio;

import java.io.IOException;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;

import io.snow.core.ServerGroupContext;

/**
 * TCP连接的包装类
 * 
 * @author zhangliang	2018.08.23
 *
 */
public class AioConnect {

	/** socket连接 */
	private AsynchronousSocketChannel socketChannel;
	
	/** 用来做读取操作的ByteBuffer */
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	
	/** 用来做读取操作的ByteBuffer */
	private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
	
	/** 服务器全局资源 */
	private ServerGroupContext groupContext;
	
	/** 当前连接是否存活 */
	private boolean active = false;
	
	/** 最后一次发送消息的时间 */
	private long lastSendTime;
	
	/** 最后一次接收消息的时间 */
	private long lastReceiveTime;
	
	/** 读取数据次数 */
	private int readTimes;

	public AioConnect(AsynchronousSocketChannel socketChannel, ServerGroupContext groupContext) {
		this.socketChannel = socketChannel;
		this.groupContext = groupContext;
		lastReceiveTime = System.currentTimeMillis();
		active = true;
		try {
			socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, false);
			Set<SocketOption<?>> supportedOptions = socketChannel.supportedOptions();
			for (SocketOption<?> option : supportedOptions) {
				Object object = socketChannel.getOption(option);
				System.out.println(option.name() + ":" + object);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public AsynchronousSocketChannel getSocketChannel() {
		return socketChannel;
	}

	public ServerGroupContext getGroupContext() {
		return groupContext;
	}

	public long getLastSendTime() {
		return lastSendTime;
	}

	public long getLastReceiveTime() {
		return lastReceiveTime;
	}

	public boolean isActive() {
		return active;
	}

	public ByteBuffer getReadBuffer() {
		return readBuffer;
	}

	public ByteBuffer getWriteBuffer() {
		return writeBuffer;
	}

	/** 主动关闭连接 */
	public void close() {
		try {
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		active = false;
	}

	public void write(byte[] data) {
		lastReceiveTime = System.currentTimeMillis();
		System.out.println(new String(data));
		writeBuffer.put(data);
		socketChannel.write(writeBuffer, this, groupContext.getWriteCompletionHandler());
	}

	public void read() {
		System.out.println( "第" + ++readTimes + "此读取数据，数据大小 ：" + readBuffer.position());
		readBuffer.flip();
		Decode.decode(this);//进行解包
		// TODO 非完整包的处理方式有待优化
		//暂时默认完整包的数据长度比buffer.size()小
		if (readBuffer.hasRemaining()) {
			//非完整包，直接压缩buffer，继续写入
			readBuffer.compact();
		} else {
			//全部读完，清除所有状态，重新写入
			readBuffer.clear();
		}
		socketChannel.read(readBuffer, this, groupContext.getReadCompletionHandler());
	}

	@Override
	public String toString() {
		try {
			return socketChannel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

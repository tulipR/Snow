package io.snow.core.nio;

/**
 * 
 * @author zhangliang	2019.02.27
 *
 */
public abstract class AbsNioHandler<T> implements NioHandler
{

	@SuppressWarnings("unchecked")
	@Override
	public void received(NioConnect connect, Message message) {
		received(connect,(MessagePacket<T>)message);
	}

	public abstract void write(NioConnect connect, Object message);
	
	public abstract void connected(NioConnect connect);

	public abstract void close(NioConnect connect);

	public abstract void abnormalClose(NioConnect connect,Exception e);
	
	public abstract void received(NioConnect connect, MessagePacket<T> message);
}

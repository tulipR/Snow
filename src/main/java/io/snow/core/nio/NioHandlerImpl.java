package io.snow.core.nio;

/**
 * 
 * @author zhangliang
 *
 */
public class NioHandlerImpl extends AbsNioHandler<String> {


	@Override
	public void received(NioConnect connect, MessagePacket<String> message) {
		System.out.println("服务器接收到消息"+message);
	}

	@Override
	public void write(NioConnect connect, Object messahe) {
		
	}

	@Override
	public void connected(NioConnect connect) {
		System.out.println("创建连接");
	}

	@Override
	public void close(NioConnect connect) {
		System.out.println("服务器正常关闭");
	}

	@Override
	public void abnormalClose(NioConnect connect, Exception e) {
		System.out.println("服务器异常关闭"+e);
	}
}

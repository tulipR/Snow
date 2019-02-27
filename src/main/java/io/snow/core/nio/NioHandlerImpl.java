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
	public void write() {
		
	}

	@Override
	public void connected() {
		
	}

	@Override
	public void close() {
		System.out.println("服务器正常关闭");
	}

	@Override
	public void abnormalClose(NioConnect connect, Exception e) {
		System.out.println("服务器异常关闭"+e);
	}

}

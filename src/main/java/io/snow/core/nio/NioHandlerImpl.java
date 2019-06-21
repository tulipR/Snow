package io.snow.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 
 * @author zhangliang
 *
 */
public class NioHandlerImpl extends AbsNioHandler<String> {


	@Override
	public void received(NioConnect connect, MessagePacket<String> message) {
		for(int i=0;i<1000;i++) {
			try
			{
				int write=connect.write(ByteBuffer.wrap(message.getMessage().getBytes()));
				System.out.println("id:"+connect.getId()+",第"+i+"次发送数据："+write);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
//		System.out.println("服务器接收到消息"+message);
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

package io.snow.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Set;

/**
 * 
 * 	客户端连接回调类
 * 
 * @author zhangliang 2018.08.23
 *
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, ServerGroupContext> {

	@Override
	public void completed(AsynchronousSocketChannel socketChannel, ServerGroupContext groupContext) {
		groupContext.getServerSocketChannel().accept(groupContext, this);
		AioConnect connect = new AioConnect(socketChannel, groupContext);
		groupContext.getHandler().connect(connect);
		try {
			groupContext.getConnectMap().put(socketChannel.getRemoteAddress().toString(), connect);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socketChannel.read(connect.getReadBuffer(), connect, groupContext.getReadCompletionHandler());

	}

	@Override
	public void failed(Throwable exc, ServerGroupContext attachment) {
		System.out.println("连接失败");

	}

}

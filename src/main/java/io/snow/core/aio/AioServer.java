package io.snow.core.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;

import io.snow.core.ServerGroupContext;

public class AioServer {
	private ServerGroupContext groupContext;
	
	private AsynchronousChannelGroup group;
	
	private AsynchronousServerSocketChannel serverSocketChannel;
	
	public AioServer(ServerGroupContext groupContext) {
		this.groupContext = groupContext;
	}
	
	public void start() {
		try {
			group = AsynchronousChannelGroup.withFixedThreadPool(10, new ThreadFactory() {
                byte index = 0;

                public Thread newThread(Runnable r) {
                    return new Thread(r, "io-snow:AIO-" + (++index));
                }
            });
			serverSocketChannel = AsynchronousServerSocketChannel.open(group);
			Set<SocketOption<?>> supportedOptions = serverSocketChannel.supportedOptions();
			for (SocketOption<?> option : supportedOptions) {
				System.out.println(option.name() + ":" + serverSocketChannel.getOption(option));
			}
//			serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1000);
			groupContext.setServerSocketChannel(serverSocketChannel);
			if (groupContext.getIp() != null) {
				serverSocketChannel.bind(new InetSocketAddress(groupContext.getIp(), groupContext.getPort()));
			} else {
				serverSocketChannel.bind(new InetSocketAddress(groupContext.getPort()));
			}
			serverSocketChannel.accept(groupContext, groupContext.getAcceptCompletionHandler());
			System.out.println("---------------服务器启动----------------");
			while (true) {
				ArrayBlockingQueue<MessagePacket<String>> messageQueue = groupContext.getMessageQueue();
				MessagePacket<String> poll = null;
				try {
					poll = messageQueue.take();
					System.out.println(poll.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}

package io.snow.core.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import io.snow.core.codec.ProtocolCodecFilterImpl;

/**
 * 
 * @author zhangliang 2019.02.25
 *
 */
public class NioServer {

	private NioHandler handler;
	
	private final FilterChain filterChain = new FilterChain();

	public NioServer handler(NioHandler handler) {
		if (handler == null) {
			throw new NullPointerException("handler can not be null");
		}
		this.handler = handler;
		return this;
	}
	
	/** 在过滤链末尾添加过滤器 */
	public void addLast(String name,IoFilter filter) {
		filterChain.addLast(name,filter);
	}
	
	/** 在过滤链开头添加过滤器 */
	public void addFrist(String name,IoFilter filter) {
		filterChain.addFrist(name,filter);
	}

	private Selector selector;

	public void start() throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(8888));
		this.selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		new Thread(new Acceptor()).start();
	}

	private class Acceptor implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					int select = selector.select();
					if (select > 0) {
						Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
						while (iterator.hasNext()) {
							SelectionKey next = iterator.next();
							iterator.remove();
							initAndStartProcessor(next);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void initAndStartProcessor(SelectionKey selectionKey) throws IOException {
			if (!selectionKey.isAcceptable())
				return;
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			handler.connected();
			socketChannel.configureBlocking(false);
			new Thread(new NioProcessor(socketChannel,handler,filterChain)).start();
		}
	}

	public static void main(String[] args) throws IOException {
		NioServer nioServer = new NioServer();
		nioServer.handler(new NioHandlerImpl()).addLast("codec",new ProtocolCodecFilterImpl());
	}

}

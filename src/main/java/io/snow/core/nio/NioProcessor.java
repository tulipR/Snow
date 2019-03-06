package io.snow.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author zhangliang 2019.02.25
 *
 */
public class NioProcessor {

	private Selector selector;

    private final Queue<NioConnect> newSessions = new ConcurrentLinkedQueue<>();

    private final Queue<NioConnect> removingSessions = new ConcurrentLinkedQueue<>();

    private final Queue<NioConnect> flushingSessions = new ConcurrentLinkedQueue<>();
	
	private FilterChain filterChain;
	
	/**
	 * 默认无参构造器
	 */
	public NioProcessor() {
		
	}
	
	public NioProcessor(SocketChannel socketChannel,FilterChain filterChain) {
		this.filterChain = filterChain;
		try {
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private class Processor implements Runnable {
		
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
							if (next.isReadable()) readC(next);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	

	private void readC(SelectionKey selectionKey) {
		selectionKey.attachment()
		try {
			ByteBuffer readBuff = connect.getReadByteBuffer();
			int read = connect.getSocketChannel().read(readBuff);
			if (read == -1) {
				connect.close();
			} else {
				readBuff.flip();
				filterChain.fireMessageReceived(connect,readBuff);
				// TODO 非完整包的处理方式有待优化
				// 暂时默认完整包的数据长度比buffer.size()小
				if (readBuff.hasRemaining()) {
					// 非完整包，直接压缩buffer，继续写入
					readBuff.compact();
				} else {
					// 全部读完，清除所有状态，重新写入
					readBuff.clear();
				}
			}
		} catch (IOException e) {
			try {
				connect.abnormalClose(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}

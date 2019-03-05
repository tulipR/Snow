package io.snow.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 
 * @author zhangliang 2019.02.25
 *
 */
public class NioProcessor implements Runnable {

	private Selector selector;

	private NioConnect connect;
	
	private FilterChain filterChain;
	
	public NioProcessor(SocketChannel socketChannel,FilterChain filterChain) {
		connect = new NioConnect(socketChannel);
		this.filterChain = filterChain;
		try {
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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
						if (next.isReadable()) readC();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void readC() {
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

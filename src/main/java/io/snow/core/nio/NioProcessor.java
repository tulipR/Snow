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

	private NioHandler handler;

	private SocketChannel socketChannel;

	private ByteBuffer readBuff = ByteBuffer.allocate(1024);

	private ByteBuffer writeBuff = ByteBuffer.allocate(1024);

	private Selector selector;

	private NioConnect connect;

	public NioProcessor(SocketChannel socketChannel, NioHandler handler) {
		this.handler = handler;
		this.socketChannel = socketChannel;
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
						if (next.isReadable()) {
							read();
						} else if (next.isWritable()) {

						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void read() {
		try {
			int read = socketChannel.read(readBuff);
			if (read == -1) {
				socketChannel.close();
				handler.close();
			} else {
				readBuff.flip();
				Decode.decode(this);// 进行解包
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
				socketChannel.close();
				handler.abnormalClose(connect, e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public ByteBuffer getReadBuffer() {
		return readBuff;
	}

	public void receive(MessagePacket<String> messagePacket) {
		handler.received(connect, messagePacket);
	}
}

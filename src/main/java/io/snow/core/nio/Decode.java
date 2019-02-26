package io.snow.core.nio;

import java.nio.ByteBuffer;

/**
 * TCP解包，包格式为 前4个字节代表包完整数据长度，后面的为数据。
 * 
 * @author zhangliang	2018.08.26
 *
 */
public class Decode {

	public static void decode(NioProcessor processor) {
		ByteBuffer readBuffer = processor.getReadBuffer();
		int length;
		short messgaeId;
		byte[] data = new byte[1024];
		while (hasComplementMessage(readBuffer)) {
			length = readBuffer.getInt();
			messgaeId = readBuffer.getShort();
			readBuffer.get(data, 0, length - 6);
			processor.receive(new MessagePacket<String>(messgaeId, new String(data), null));
		}
	}
	
	/** 判断剩下的数据能否组成完整数据包 */
	private static boolean hasComplementMessage(ByteBuffer byteBuffer) {
		if (byteBuffer.remaining() < 4) {
			System.out.println("长度不足4解包");
			return false;
		}
		int length = byteBuffer.getInt(byteBuffer.position());
		if (byteBuffer.limit() - byteBuffer.position() >= length) {
			return true;
		}
		System.out.println("达不到真实数据长度解包");
		return false;
	}
}
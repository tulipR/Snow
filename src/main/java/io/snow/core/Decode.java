package io.snow.core;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * TCP解包，包格式为 前4个字节代表包完整数据长度，后面的为数据。
 * 
 * @author zhangliang	2018.08.26
 *
 */
public class Decode {

	public static void decode(AioConnect connect) {
		ServerGroupContext groupContext = connect.getGroupContext();
		ArrayBlockingQueue<MessagePacket<String>> messageQueue = groupContext.getMessageQueue();
		ByteBuffer readBuffer = connect.getReadBuffer();
		int length;
		MessagePacket<String> messagePacket;
		short messgaeId;
		byte[] data = new byte[1024];
		while (hasComplementMessage(readBuffer)) {
			length = readBuffer.getInt();
			messgaeId = readBuffer.getShort();
			readBuffer.get(data, 0, length - 6);
			messagePacket = new MessagePacket<>(messgaeId, new String(data), connect.toString());
			messageQueue.add(messagePacket);
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

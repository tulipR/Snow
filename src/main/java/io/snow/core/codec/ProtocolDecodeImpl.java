package io.snow.core.codec;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import io.snow.core.nio.MessagePacket;

/**
 * TCP解包，包格式为 前4个字节代表包完整数据长度，后面的为数据。
 * 
 * @author zhangliang	2018.08.26
 *
 */
public class ProtocolDecodeImpl implements ProtocolDecode{

	
	@Override
	public LinkedList<Object> decode(ByteBuffer readBuffer) {
		LinkedList<Object> linkedList = new LinkedList<>();
		int length;
		short messgaeId;
		byte[] data = new byte[1024];
		while (hasComplementMessage(readBuffer)) {
			length = readBuffer.getInt();
			messgaeId = readBuffer.getShort();
			readBuffer.get(data, 0, length - 6);
			MessagePacket<String> messagePacket=new MessagePacket<String>(messgaeId, new String(data), null);
			linkedList.offer(messagePacket);
		}
		return linkedList;
	}
	
	/** 判断剩下的数据能否组成完整数据包 */
	private boolean hasComplementMessage(ByteBuffer byteBuffer) {
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

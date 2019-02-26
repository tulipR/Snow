package io.snow.core.nio;

public class MessagePacket<T> {
	
	private String connectInfo;

	private short messageId;
	
	private T message;

	public MessagePacket(short messageId, T message, String connectInfo) {
		this.messageId = messageId;
		this.message = message;
		this.connectInfo = connectInfo;
	}
	
	
	public short getMessageId() {
		return messageId;
	}

	public void setMessageId(short messageId) {
		this.messageId = messageId;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessagePacket [connectInfo=" + connectInfo + ", messageId=" + messageId + ", message=" + message + "]";
	}
	
	
}

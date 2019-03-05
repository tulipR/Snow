package io.snow.core.codec;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import io.snow.core.nio.IoFilterAdapter;
import io.snow.core.nio.NioConnect;

/**
 * 
 * @author zhangliang	2019.03.05
 *
 */
public abstract class ProtocolCodecFilter extends IoFilterAdapter{
	
	public abstract ProtocolDecode getDecode();
	
	public abstract ProtocolEncode getEncode();

	@Override
	public void messageReceived(NioConnect connect, NextFilter nextFilter,Object message) throws Exception {
		if (!(message instanceof ByteBuffer)) {
			super.messageReceived(connect, nextFilter,message);
		} 
		ProtocolDecode decode = getDecode();
		LinkedList<Object> list=decode.decode((ByteBuffer)message);
		while(!list.isEmpty()) {
			nextFilter.messageReceived(connect, list.poll());
		}
	}
	
	@Override
	public void messageWrite(NioConnect connect, NextFilter nextFilter,Object message) throws Exception {
		super.messageWrite(connect, nextFilter,message);
	}
}

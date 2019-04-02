package io.snow.core.codec;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import io.snow.core.nio.IoFilter;
import io.snow.core.nio.IoFilterAdapter;
import io.snow.core.nio.IoFilter.NextFilter;

/**
 * 
 * @author zhangliang	2019.03.05
 *
 */
public abstract class ProtocolCodecFilter extends IoFilterAdapter{
	
	public abstract ProtocolDecode getDecode();
	
	public abstract ProtocolEncode getEncode();

	@Override
	public void messageReceived(NextFilter nextFilter,Object message) throws Exception {
		if (!(message instanceof ByteBuffer)) {
			super.messageReceived(nextFilter,message);
		} 
		ProtocolDecode decode = getDecode();
		LinkedList<Object> list=decode.decode((ByteBuffer)message);
		while(!list.isEmpty()) {
			nextFilter.messageReceived(list.poll());
		}
	}
	
	@Override
	public void messageWrite(NextFilter nextFilter,String message) throws Exception {
		super.messageWrite(nextFilter,message);
	}
}

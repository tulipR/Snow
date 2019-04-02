package io.snow.core.codec;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * 
 * @author zhangliang	2019.03.05
 *
 */
public interface ProtocolDecode {
	
	LinkedList<Object> decode(ByteBuffer data);
}

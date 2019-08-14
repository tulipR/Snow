package core.test;

import java.io.IOException;

import io.snow.core.codec.ProtocolCodecFilterImpl;
import io.snow.core.nio.NioHandlerImpl;
import io.snow.core.nio.NioServer;

/**
 * 
 * @author zhangliang	2019.05.30
 *
 */
public class NioServerTest {
	
	public static void main(String[] args) throws IOException {
		NioServer nioServer = new NioServer(1);
		nioServer.handler(new NioHandlerImpl())
				 .addLast("codec", new ProtocolCodecFilterImpl());
		nioServer.start();
	}
}

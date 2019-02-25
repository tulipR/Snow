package io.snow.core.aio;

import java.util.concurrent.TimeUnit;

import io.snow.core.ServerConfig;
import io.snow.core.ServerGroupContext;
import io.snow.core.aio.AioServer;
import io.snow.core.aio.SendUtils;

public class Main {
	public static void main(String[] args) {
		ServerConfig serverConfig = ServerConfig.newServerConfigBuilder(null, (short) 8888).build();
		
		ServerGroupContext groupContext = new ServerGroupContext(serverConfig , new HandlerImpl());
		new AioServer(groupContext).start();
//		while (true) {
//			try {
//				TimeUnit.SECONDS.sleep(3);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
////			SendUtils.sendToAll(groupContext, "zhangsan".getBytes());
//		}
	}
}

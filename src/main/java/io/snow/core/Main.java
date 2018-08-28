package io.snow.core;

import java.util.concurrent.TimeUnit;

import io.snow.core.AioServer;
import io.snow.core.SendUtils;
import io.snow.core.ServerConfig;
import io.snow.core.ServerGroupContext;

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

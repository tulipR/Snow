package io.snow.core.aio;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SendUtils {

	private static Executor executor = Executors.newFixedThreadPool(10);
	
	public static void sendToAll(ServerGroupContext context, byte[] data) {
		ConcurrentHashMap<String,AioConnect> connectMap = context.getConnectMap();
		connectMap.forEach((address, aioConnect) -> {
			executor.execute(new SendRunnable(aioConnect,data));
		});
	}
}

package io.snow.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HeartCheckThread implements Runnable {
	
	private long checkTime;
	private ConcurrentHashMap<String, AioConnect> connectMap;

	public void setConnectMap(ConcurrentHashMap<String, AioConnect> connectMap) {
		this.connectMap = connectMap;
	}

	public void setCheckTime(long checkTime) {
		this.checkTime = checkTime;
	}

	@Override
	public void run() {
		if (checkTime == 0 || connectMap == null) {
			return;
		}
		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(checkTime / 2);
				//ֻ只关闭tcp连接，并不释放aioConnect
				connectMap.forEach((address, aioConnect) -> {
					if (aioConnect.isActive()) {
						long lastReceiveTime = aioConnect.getLastReceiveTime();
						if (System.currentTimeMillis() - lastReceiveTime > checkTime) {
							System.out.println(address + "心跳检测失败，服务器主动关闭连接");
							aioConnect.close();
						} else {
							System.out.println(address + "心跳检测");
						}
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}

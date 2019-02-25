package io.snow.core;

/**
 *  
 *  服务器配置类
 *  
 * @author zhangliang	2018.08.23
 *
 */
public class ServerConfig {

	/** ip */
	private final String ip;
	
	/** 端口 */
	private final short port;
	
	/** 心跳间隔 */
	private int heartTime;
	
	/** 是否进行心跳检查 */
	private boolean heartCheck;

	public String getIp() {
		return ip;
	}

	public short getPort() {
		return port;
	}

	/** 是否进行心跳检查，默认为false */
	public boolean isHeartCheck() {
		return heartCheck;
	}

	/** 获得心跳检查时间，默认为5 * 1000 毫秒 */
	public int getHeartTime() {
		return heartTime;
	}

	private ServerConfig(ServerConfigBuilder builder) {
		this.ip = builder.ip;
		this.port = builder.port;
		this.heartCheck = builder.heartCheck;
		this.heartTime = builder.heartTime;
	}

	public static class ServerConfigBuilder {

		private final String ip;
		private final short port;

		/** 默认时间为5秒 */
		private int heartTime = 5 * 1000;

		/** 默认关闭心跳检测 */
		private boolean heartCheck = false;

		private ServerConfigBuilder(String ip, short port) {
			this.ip = ip;
			this.port = port;
		}
		
		public ServerConfigBuilder heartTime(int heartTime) {
			this.heartTime = heartTime;
			return this;
		}
		
		public ServerConfigBuilder heartCheck(int heartTime) {
			this.heartTime = heartTime;
			return this;
		}
		
		public ServerConfig build() {
			return new ServerConfig(this);
		}
	}

	public static ServerConfigBuilder newServerConfigBuilder(String ip, short port) {
		return new ServerConfigBuilder(ip, port);
	}
}

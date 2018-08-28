package io.snow.core;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ServerGroupContext {
	
	private AcceptCompletionHandler acceptCompletionHandler;
	private ReadCompletionHandler readCompletionHandler;
	private WriteCompletionHandler writeCompletionHandler;
	private AsynchronousServerSocketChannel serverSocketChannel;
	private IOHandler handler;
	
	/** 服务器配置 */
	private ServerConfig serverConfig;
	
	/** 服务器连接缓存 */
	private ConcurrentHashMap<String, AioConnect> connectMap = new ConcurrentHashMap<>();
	
	/** 服务器接收消息队列 */
	private ArrayBlockingQueue<MessagePacket<String>> messageQueue = new ArrayBlockingQueue<>(1024 * 256);
	
	
	public ArrayBlockingQueue<MessagePacket<String>> getMessageQueue() {
		return messageQueue;
	}

	public ConcurrentHashMap<String, AioConnect> getConnectMap() {
		return connectMap;
	}
	
	public void setServerSocketChannel(AsynchronousServerSocketChannel serverSocketChannel) {
		this.serverSocketChannel = serverSocketChannel;
	}

	public AsynchronousServerSocketChannel getServerSocketChannel() {
		return serverSocketChannel;
	}
	
	public AcceptCompletionHandler getAcceptCompletionHandler() {
		return acceptCompletionHandler;
	}
	
	public ReadCompletionHandler getReadCompletionHandler() {
		return readCompletionHandler;
	}
	
	public WriteCompletionHandler getWriteCompletionHandler() {
		return writeCompletionHandler;
	}
	
	public String getIp() {
		return serverConfig.getIp();
	}
	
	public int getPort() {
		return serverConfig.getPort();
	}
	
	public IOHandler getHandler() {
		return handler;
	}
	
	public ServerGroupContext(ServerConfig serverConfig, IOHandler handler) {
		this.serverConfig = serverConfig;
		this.handler = handler;
		init();
	}
	
	private void init() {
		acceptCompletionHandler = new AcceptCompletionHandler();
		readCompletionHandler = new ReadCompletionHandler();
		writeCompletionHandler = new WriteCompletionHandler();
		
		//是否开启心跳检测
		if (serverConfig.isHeartCheck()) {
			HeartCheckThread heartCheck = new HeartCheckThread();
			heartCheck.setConnectMap(connectMap);
			heartCheck.setCheckTime(serverConfig.getHeartTime());
			Thread heartCheckThread = new Thread(heartCheck);
			heartCheckThread.start();
		}
	}
}

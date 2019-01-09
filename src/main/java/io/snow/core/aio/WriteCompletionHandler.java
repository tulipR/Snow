package io.snow.core.aio;

import java.nio.channels.CompletionHandler;

public class WriteCompletionHandler implements CompletionHandler<Integer, AioConnect>{

	public void completed(Integer result, AioConnect attachment) {
		System.out.println("发送成功");
	}

	public void failed(Throwable exc, AioConnect attachment) {
		// TODO Auto-generated method stub
		
	}

}

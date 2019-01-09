package io.snow.core.aio;

import java.nio.channels.CompletionHandler;

/**
 * 
 * <br>客户端正常关闭连接时：</br>会发送FIN数据进行第一次挥手，服务器接受到这个数据后，
 * 会返回一个ACK数据进行第二次挥手，然后触发completed()方法。
 * 然后服务器会发送一个FIN数据进行第三次挥手，然后触发failed()方法。
 * 
 * <br>不正常关闭连接时:</br>只会触发failed()方法
 * 
 * @author zhangliang	2018.08.20
 *
 */

public class ReadCompletionHandler implements CompletionHandler<Integer, AioConnect>{

	public void completed(Integer result, final AioConnect aioSessio) {
		if (result < 0) {
			aioSessio.close();
			System.out.println("客户端关闭了");
		}
		aioSessio.read();
	}

	public void failed(Throwable exc, AioConnect aioSessio) {
		if (aioSessio.isActive()) {
			aioSessio.close();
		}
		System.out.println(exc);
		System.out.println("客户端关闭了");
	}

}

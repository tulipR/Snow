package core.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketOption;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ClientTest {

	public static void main(String[] args) throws IOException {
//		for (int i = 0; i < 100; i++) {
//			try {
//				TimeUnit.MILLISECONDS.sleep(250);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			new Thread(new client()).start();
//		}
	}
	
	static class client implements Runnable{

		@Override
		public void run() {
			try {
				Socket socket = new Socket();
//				SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
//				Set<SocketOption<?>> supportedOptions = socketChannel.supportedOptions();
//				for (SocketOption<?> option : supportedOptions) {
//					System.out.println(option.name() + ":" + socketChannel.getOption(option));
//				}
				
				socket.connect(new InetSocketAddress("127.0.0.1", 8888));
				System.out.println("连接成功");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
//			socket.setReceiveBufferSize(1000);
				InputStream inputStream = socket.getInputStream();
				DataOutputStream outtputStream = new DataOutputStream(socket.getOutputStream());
//			outputStream.write("zhangsan".getBytes());
//			byte[] buff = new byte[1024];
//			while (true) {
//				if (inputStream.read(buff) != -1) {
//					System.out.println(new String(buff));
//				}
//			}
				

//			
//			Scanner scanner = new Scanner(System.in);
//			String message;
				int i = 0;
				StringBuilder builder;
				byte[] bytes;
//				while (true) {
					builder = new StringBuilder();
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
					builder.append("第 " + i + "条数据      ");
					builder.append("<br>客户端正常关闭连接时：</br>会发送FIN数据进行第一次挥手，服务器接受到这个数据后，\r\n" + 
							" * 会返回一个ACK数据进行第二次挥手，然后触发completed()方法。\r\n * 然后服务器会发送一个FIN数据进行第三次挥手，然后触发failed()方法不正常关闭连接时:</br>只会触发failed()方法");
					bytes = builder.toString().getBytes();
					System.out.println(bytes.length);
					outtputStream.writeInt(bytes.length + 2 + 4);
					outtputStream.writeShort(i);
					outtputStream.write(bytes);
//					if (i >= 10000) {
//						break;
//					}
//				}
				Thread.sleep(500000);
				inputStream.read();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}

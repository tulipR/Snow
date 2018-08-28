package io.snow;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 8888);
//		socket.setReceiveBufferSize(1000);
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
//		outputStream.write("zhangsan".getBytes());
//		byte[] buff = new byte[1024];
//		while (true) {
//			if (inputStream.read(buff) != -1) {
//				System.out.println(new String(buff));
//			}
//		}

//		
//		Scanner scanner = new Scanner(System.in);
//		String message;
		int i = 0;
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		while (true) {

			try {
				TimeUnit.MILLISECONDS.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			
			outputStream.write(byteBuffer.putInt(i).array());
			if (i >= 5) {
				break;
			}
			byteBuffer.clear();
		}
		inputStream.read();
	}

	private static byte[] getBytes(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (value >> (24 - i * 8));
		}
		return b;
	}
}

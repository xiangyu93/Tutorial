package MySocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class MyServerSocketListener extends Thread {
	Handler handler;
	String IP;
	private static final int DISCONNECTED = 4;

	public MyServerSocketListener(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(8888);
			while (true) {
				Socket socket = serverSocket.accept();
				IP = socket.getInetAddress().getHostAddress();
				Message msg = Message.obtain();
				msg.what = 0x14;
				msg.obj = IP;
				handler.sendMessage(msg);
				MyServerSocket MSS = new MyServerSocket(socket);
				MSS.start();
				MyServerSocketManager.getMyServerSocketManager().add(MSS);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

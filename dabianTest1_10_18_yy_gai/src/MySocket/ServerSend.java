package MySocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import android.os.Environment;
import android.util.Log;

public class ServerSend extends Thread {
	Vector<MyServerSocket> vector;
	MyServerSocket ms = null;
	Socket s;

	public ServerSend() {
		// TODO Auto-generated constructor stub
		this.vector = MyServerSocketManager.getMyServerSocketManager().vector;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "答辩" + File.separator + "Scores.txt");
		if (!file.getParentFile().exists()) {// 文件不存在啊
			file.getParentFile().mkdirs();// 创建文件夹
		}
		FileInputStream fis = null;
		for (int i = 0; i < vector.size(); i++) {
			try {
				ms = vector.get(i);
				s = ms.ServerS;
				Log.i("ServerSend====ip", s.getInetAddress().getHostAddress()+"");
				Log.i("ServerSend====vectorSize", vector.size()+"");
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				OutputStream out = s.getOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = fis.read(buf)) != -1) {
					out.write(buf, 0, len);
					out.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}

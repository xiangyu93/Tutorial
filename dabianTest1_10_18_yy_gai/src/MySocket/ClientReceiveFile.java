package MySocket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

import util.ClientUtil;
import android.os.Environment;
import android.util.Log;

public class ClientReceiveFile extends Thread {
	ClientUtil clientUtil = null;
	StringBuffer sb = null;
	FileWriter out = null;

	@Override
	public void run() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "���" + File.separator + "Scores.txt");
		if (!file.getParentFile().exists()) {// �ļ������ڰ�
			file.getParentFile().mkdirs();// �����ļ���
		} else {
			try {
				clientUtil = ClientUtil.getClientUtil();
				sb = clientUtil.getSb();
				Log.i("===ClientRecieveFile==sb===", sb + "");
				out = new FileWriter(file,false);// true
				out.write(sb.toString());// �����ݱ�Ϊ�ַ����󱣴�
				out.flush();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}

}

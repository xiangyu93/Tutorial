package MySocket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint("WorldReadableFiles")
public class saveAllScores {
	JSONObject js;
	double TotalScore;
	double AvScore1;
	double AvScore2;
	double AvScore3;
	String groupName;
	MyServerSocketManager MSSM = MyServerSocketManager
			.getMyServerSocketManager();

	public void SaveAllScores() {
		MSSM.Caculate();
		this.AvScore1 = MSSM.AvScore1;
		this.AvScore2 = MSSM.AvScore2;
		this.AvScore3 = MSSM.AvScore3;
		this.TotalScore = MSSM.totalScore;
		js = new JSONObject();
		Log.i("TotalScore====", TotalScore + "");
		Log.i("AvScore1====", AvScore1 + "");
		Log.i("AvScore2====", AvScore2 + "");
		Log.i("AvScore3====", AvScore3 + "");
		try {
			js.put("AvScore1", AvScore1);
			js.put("AvScore2", AvScore2);
			js.put("AvScore3", AvScore3);
			js.put("TotalScore", TotalScore);
			js.put("groupName", groupName);
			Log.i("saveALLScores", js.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// �����ڲ���
			return;
		}
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "���" + File.separator + "Scores.txt");
		if (!file.getParentFile().exists()) {// �ļ������ڰ�
			file.getParentFile().mkdirs();// �����ļ���
		}
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(file, true));// true																	// ��ʾ���ļ�ĩβ׷������
			out.println(js.toString());// �����ݱ�Ϊ�ַ����󱣴�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public void setGroupName(String group_name) {
		this.groupName = group_name;
	}
}

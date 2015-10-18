package MySocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class GetAllScoresFromFile {

	private JSONObject[] js;
	private String[] data;
	private double[] TotalScore;
	private double[] AvScore1;
	private double[] AvScore2;
	private double[] AvScore3;
	private String[] groupName;
	private BufferedReader br;
	public final static GetAllScoresFromFile gasf = new GetAllScoresFromFile();

	public static GetAllScoresFromFile get_GetAllScoresFromFile() {
		return gasf;
	}

	public double[] getTotalScore() {
		return TotalScore;
	}

	public double[] getAvScore1() {
		return AvScore1;
	}

	public double[] getAvScore2() {
		return AvScore2;
	}

	public double[] getAvScore3() {
		return AvScore3;
	}

	public String[] getGroupName() {
		return groupName;
	}

	public void getData() {
		List<String> list = new ArrayList<String>();
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "答辩" + File.separator + "Scores.txt");
		if (!file.getParentFile().exists()) {
			System.out.println("错误！！！文件不存在！");
		}
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		js = new JSONObject[list.size()];
		data = new String[list.size()];
		AvScore1 = new double[list.size()];
		AvScore2 = new double[list.size()];
		AvScore3 = new double[list.size()];
		groupName = new String[list.size()];
		TotalScore = new double[list.size()];
		for (int x = 0; x < list.size(); x++) {
			try {
				data[x] = list.get(x);
				js[x] = new JSONObject(data[x]);
				groupName[x] = js[x].getString("groupName");
				AvScore1[x] = js[x].getDouble("AvScore1");
				AvScore2[x] = js[x].getDouble("AvScore2");
				AvScore3[x] = js[x].getDouble("AvScore3");
				TotalScore[x] = js[x].getDouble("TotalScore");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

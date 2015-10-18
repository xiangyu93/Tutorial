package dabian.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/*
	 * ���С����Ϣ
	 */
	public void addGroup(Group group) {
		db.beginTransaction();// ��ʼ����
		try {
			ContentValues cv = new ContentValues();
			cv.put("_id", group._id);
			cv.put("group_members", group.group_members);
			cv.put("group_project", group.group_project);
			db.insert("MyGroup", null, cv);
			db.setTransactionSuccessful();// ����������ɹ����
		} finally {
			db.endTransaction();// ��������
		}
	}

	/*
	 * �����ί��Ϣ
	 */
	public void addJudger(Judger judger) {
		db.beginTransaction();// ��ʼ����
		try {
			ContentValues cv = new ContentValues();
			cv.put("_id", judger._id);
			cv.put("judger_name", judger.judger_name);
			cv.put("judger_introduce", judger.judger_introduce);
			db.insert("judger", null, cv);
			db.setTransactionSuccessful();// ����������ɹ����
		} finally {
			db.endTransaction();// ��������
		}
	}

	/*
	 * �޸�С����Ϣ
	 */
	public void updateGroup(Group group) {
		ContentValues cv = new ContentValues();
		cv.put("_id", group._id);
		cv.put("group_members", group.group_members);
		cv.put("group_project", group.group_project);
		db.update("mygroup", cv, "_id=?",
				new String[] { String.valueOf(group._id)});
	}

	/*
	 * �޸���ί��Ϣ
	 */
	public void updateJudger(Judger judger) {
		ContentValues cv = new ContentValues();
		cv.put("_id", judger._id);
		cv.put("judger_name", judger.judger_name);
		cv.put("judger_introduce", judger.judger_introduce);
		db.update("judger", cv, "_id = ?",
				new String[] { judger._id});
	}

	/*
	 * ɾ��С����Ϣ
	 */
	public void deleteGroup(Group group) {
		db.delete("MyGroup", "_id=?",
				new String[] { String.valueOf(group._id) });
	}

	/*
	 * ɾ����ί��Ϣ
	 */
	public void deleteJudger(Judger judger) {
		db.delete("judger", "_id=?", new String[] { judger._id });
	}

	/*
	 * ��ѯ����С����Ϣ, ���ؽ���б��嵥
	 * 
	 * @return List<Group>
	 */
	public Group queryGroup(int Position) {
		Group group = new Group();
		Cursor cursor = db.query("MyGroup", null, null, null, null, null, null);
		cursor.moveToPosition(Position);
		group._id = cursor.getInt(cursor.getColumnIndex("_id"));
		group.group_members = cursor.getString(cursor
				.getColumnIndex("group_members"));
		group.group_project = cursor.getString(cursor
				.getColumnIndex("group_project"));
		cursor.close();
		return group;
	}

	public Cursor GroupSelect() {
		Cursor cursor = db.query("MyGroup", null, null, null, null, null, null);
		return cursor;
	}

	/*
	 * query all groups, return cursor
	 * 
	 * @return Cursor
	 */

	/*
	 * ��ѯ������ί��Ϣ, ���ؽ���б��嵥
	 * 
	 * @return List<Judger>
	 */
	public Judger queryJudger(int position) {
		Cursor c = queryTheJudgerCursor();
		c.moveToPosition(position);
		Judger judger = new Judger();
		judger._id = c.getString(c.getColumnIndex("_id"));
		judger.judger_name = c.getString(c.getColumnIndex("judger_name"));
		judger.judger_introduce = c.getString(c
				.getColumnIndex("judger_introduce"));
		c.close();
		return judger;
	}

	/*
	 * query all judgers, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheJudgerCursor() {
		Cursor cursor = db.query("judger", null, null, null, null, null, null);
		return cursor;
	}

	/*
	 * �ر����ݿ�
	 */
	public void closeDB() {
		db.close();
	}

}

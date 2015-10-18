package dabian.db;

import java.io.Serializable;

public class Group implements Serializable {

	public int _id;
	public String group_members;
	public String group_project;

	public Group() {
		// TODO Auto-generated constructor stub
	}

	public Group(int _id, String group_members, String group_project) {
		// TODO Auto-generated constructor stub
		this._id = _id;
		this.group_members = group_members;
		this.group_project = group_project;
	}
}

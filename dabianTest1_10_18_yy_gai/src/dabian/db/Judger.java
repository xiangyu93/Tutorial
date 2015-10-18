package dabian.db;

import java.io.Serializable;

public class Judger implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String _id;
	public String judger_name;
	public String judger_introduce;

   public Judger() {
		// TODO Auto-generated constructor stub
	}

	public Judger(String _id,String judger_name, String judger_introduce) {
		// TODO Auto-generated constructor stub
		this._id = _id;
		this.judger_name = judger_name;
		this.judger_introduce = judger_introduce;
	}

}

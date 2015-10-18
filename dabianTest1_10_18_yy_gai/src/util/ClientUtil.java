package util;

import java.io.BufferedReader;
import java.io.InputStream;

public class ClientUtil {
	InputStream is = null;
	BufferedReader br = null;
	StringBuffer sb = null;
	public static final ClientUtil clientUtil = new ClientUtil();

	public static ClientUtil getClientUtil() {
		return clientUtil;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public InputStream getIs() {
		return is;
	}

	public void setBr(BufferedReader br) {
		this.br = br;
	}

	public BufferedReader getBr() {
		return br;
	}
	public void setSb(StringBuffer sb) {
		this.sb = sb;
	}
	public StringBuffer getSb() {
		System.out.print("========util=========" + sb);
		return sb;
	}
}

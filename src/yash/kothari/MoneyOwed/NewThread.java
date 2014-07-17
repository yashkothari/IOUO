package yash.kothari.MoneyOwed;

import android.content.Intent;
import android.app.Activity;

public class NewThread extends Activity implements Runnable {

	private Intent intent;
	private int EDIT_REQUEST;
	
	public NewThread(Intent intent, int request) {
		this.intent = intent;
		this.EDIT_REQUEST = request;
	}
	@Override
	public void run() {
		startActivityForResult(intent, EDIT_REQUEST);
	}

}

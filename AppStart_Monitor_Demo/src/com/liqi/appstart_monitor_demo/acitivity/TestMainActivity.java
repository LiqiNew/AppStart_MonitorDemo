package com.liqi.appstart_monitor_demo.acitivity;

import com.liqi.appstart_monitor_demo.R;
import com.liqi.appstart_monitor_demo.obj.RunnableObj;
import com.liqi.appstart_monitor_demo.utils.StaticFileUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestMainActivity extends Activity implements OnClickListener {
	private Button button1, button2, button3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main_activity);
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			new Thread(new RunnableObj(this)).start();
			break;
		case R.id.button2:
			// 特别提示：因为本软件不是流氓软件，所以按照正规的android安全流程来执行的。要想让辅助启动心跳机制，就必须要让辅助APP打开一次。
			openApp();
			break;
		case R.id.button3:
			System.exit(0);
			break;
		}
	}

	/**
	 * 打开辅助APP
	 */
	private void openApp() {
		// 安装完毕
		if (StaticFileUtils.isAppInstalled(this, RunnableObj.OPENAPPPAG)) {

			// 打开新的APP
			ComponentName comp = new ComponentName(RunnableObj.OPENAPPPAG,
					RunnableObj.OPENAPPVIEW);
			this.startActivity(StaticFileUtils.startActivityNew(comp));
		} else
			Log.e("TestMainActivity", "监听软件没有安装");
	}
}

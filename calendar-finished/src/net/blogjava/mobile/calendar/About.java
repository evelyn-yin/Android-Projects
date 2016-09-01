package net.blogjava.mobile.calendar;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
/*
 * @about
 */

public class About extends Activity
{
	private TextView tvAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		tvAbout = (TextView) findViewById(R.id.tvAbout);
		String about = "基于Android系统的日历\n\n";
		about += "姓名：印隽\n\n";
		about += "学号：090900303\n\n";
		about += "指导老师：廖小飞\n\n";
		about += "邮箱：amelia_yinjun@gmail.com";
		tvAbout.setText(about);
	}
}

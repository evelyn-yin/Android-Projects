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
		String about = "����Androidϵͳ������\n\n";
		about += "������ӡ��\n\n";
		about += "ѧ�ţ�090900303\n\n";
		about += "ָ����ʦ����С��\n\n";
		about += "���䣺amelia_yinjun@gmail.com";
		tvAbout.setText(about);
	}
}

package net.blogjava.mobile.calendar;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;
//日历之星期
public class Week extends CalendarParent
{
	private String[] weekNames;
	private int weekNameColor;

	public Week(Activity activity, View view)
	{
		super(activity, view);
		weekNameColor = activity.getResources().getColor(R.color.weekname_color);
		//获得周名称的颜色（周一至周五）
		weekNames = activity.getResources().getStringArray(R.array.week_name);
		//获得周名称（以数组性质返回）
		paint.setTextSize(weekNameSize);
		//设置周名称的字体大小
	}

	@Override
	public void draw(Canvas canvas)
	{

		float left = borderMargin;
		float top = borderMargin;
		float everyWeekWidth = (view.getMeasuredWidth() -  borderMargin * 2)/ 7;
		float everyWeekHeight = everyWeekWidth;
		
		paint.setFakeBoldText(true);
		for (int i = 0; i < weekNames.length; i++)
		{
			if(i == 0 || i == weekNames.length - 1)
				paint.setColor(sundaySaturdayColor);
			//由于周六、周日的文字颜色在其他地方要用到
			//所以sundaySaturdayColor在CalendarParent类中获得
			else
				paint.setColor(weekNameColor);
			left = borderMargin + everyWeekWidth * i
					+ (everyWeekWidth - paint.measureText(weekNames[i])) / 2;
			canvas.drawText(weekNames[i], left, top + paint.getTextSize()+weekNameMargin, paint);
			//开始绘制周名称
		}

	}

}

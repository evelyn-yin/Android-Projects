package net.blogjava.mobile.calendar;

import java.util.ArrayList;
import net.blogjava.mobile.calendar.interfaces.CalendarElement;
import android.app.Activity;
import android.graphics.Canvas; 
import android.view.View;
/*
 * 日历
 */
public class Calendar extends CalendarParent 
{
	private ArrayList<CalendarElement> elements = new ArrayList<CalendarElement>();
    public Grid grid;
	public Calendar(Activity activity, View view)
	{	
		super(activity,view);
		//设置日历边框的绘制数据
		elements.add(new Border(activity, view));
		//设置日历内容中周名的绘制数据
		elements.add(new Week(activity, view));
		grid = new Grid(activity, view);
		//设置日历中日期文字的绘制数据
		elements.add(grid);
	}

	@Override
	public void draw(Canvas canvas)
	{
		for (CalendarElement ce : elements)
			ce.draw(canvas);
	}

}

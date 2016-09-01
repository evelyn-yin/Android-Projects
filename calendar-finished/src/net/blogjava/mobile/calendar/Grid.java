package net.blogjava.mobile.calendar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import net.blogjava.mobile.calendar.db.DBService;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;
//日历网格
public class Grid extends CalendarParent implements Serializable
{
	public static DBService dbService;


	private String[] days = new String[42];
	// true表示有记录，false表示没有记录
	private boolean[] recordDays = new boolean[42];
	private String[] monthNames = new String[12];
	private TextView tvMsg1;
	private TextView tvMsg2;
	private TextView tvMsg3;
	private int dayColor;
	private int innerGridColor;
	private int prevNextMonthDayColor;
	private int currentDayColor;
	private int todayColor;
	private int todayBackgroundColor;
	private int sundaySaturdayPrevNextMonthDayColor;
	private float daySize;
	private float dayTopOffset;
	private float currentDaySize;
	private float cellX = -1, cellY = -1;



	// 从0开始
	private int currentRow, currentCol;
	private boolean redrawForKeyDown = false;

	// 当前年和月
	public int currentYear, currentMonth;
	// 上月或下月选中的天
	public int currentDay = -1, currentDay1 = -1, currentDayIndex = -1;
	private java.util.Calendar calendar = java.util.Calendar.getInstance();

	public void setCurrentRow(int currentRow)
	{
		if (currentRow < 0)
		{
			currentMonth--;
			if (currentMonth == -1)
			{
				currentMonth = 11;
				currentYear--;
			}
			currentDay = getMonthDays(currentYear, currentMonth) + currentDay
					- 7;
			currentDay1 = currentDay;
			cellX = -1;
			cellX = -1;
			view.invalidate();
			return;

		}
		else if (currentRow > 5)
		{
			int n = 0;
			for (int i = 35; i < days.length; i++)
			{
				if (!days[i].startsWith("*"))
					n++;
				else
					break;
			}
			currentDay = 7 - n + currentCol + 1;
			currentDay1 = currentDay;
			currentMonth++;
			if (currentMonth == 12)
			{
				currentMonth = 0;
				currentYear++;
			}
			cellX = -1;
			cellX = -1;
			view.invalidate();
			return;
		}
		this.currentRow = currentRow;
		redrawForKeyDown = true;
		view.invalidate();
	}

	private void getRecordDays()
	{
		int beginIndex = 8;
		int endIndex = 7;
		int beginDayIndex = 0;
		if (currentMonth > 9)
		{
			beginIndex = 9;
			endIndex = 8;
		}
		String sql = "select substr(record_date," + beginIndex
				+ ") from t_records where substr(record_date, 1, " + endIndex
				+ ")='" + currentYear + "-" + currentMonth
				+ "-' group by substr(record_date, 1)";
		for (int i = 0; i < recordDays.length; i++)
			recordDays[i] = false;
		for (int i = 0; i < days.length; i++)
		{
			if (!days[i].startsWith("*"))
			{
				beginDayIndex = i;
				break;
			}
		}
		Cursor cursor = dbService.execSQL(sql);
		while (cursor.moveToNext())
		{

			int day = cursor.getInt(0) - 1;

			recordDays[beginDayIndex + day] = true;
		}
		if (days[0].startsWith("*"))
		{
			int prevYear = currentYear, prevMonth = currentMonth - 1;
			if (prevMonth == -1)
			{
				prevMonth = 11;
				prevYear--;
			}
			int minDay = Integer.parseInt(days[0].substring(1));
			sql = "select substr(record_date," + beginIndex
			+ ") from t_records where substr(record_date, 1, " + endIndex
			+ ")='" + prevYear + "-" + prevMonth 
			+ "-' and cast(substr(record_date," + beginIndex
			+ ") as int) >= " + minDay + " group by substr(record_date, 1)";
			cursor = dbService.execSQL(sql);
			while (cursor.moveToNext())
			{

				int day = cursor.getInt(0);
				recordDays[day - minDay] = true;
			}
		}
		if (days[days.length - 1].startsWith("*"))
		{
			int nextYear = currentYear, nextMonth = currentMonth + 1;
			if (nextMonth == 12)
			{
				nextMonth = 0;
				nextYear++;
			}
			int maxDay = Integer.parseInt(days[days.length - 1].substring(1));
			sql = "select substr(record_date," + beginIndex
			+ ") from t_records where substr(record_date, 1, " + endIndex
			+ ")='" + nextYear + "-" + nextMonth 
			+ "-' and cast(substr(record_date," + beginIndex
			+ ") as int) <= " + maxDay + " group by substr(record_date, 1)";
			cursor = dbService.execSQL(sql);
			while (cursor.moveToNext())
			{

				int day = cursor.getInt(0);
				recordDays[days.length - (maxDay - day) - 1] = true;
			}
		}

	}

	public void setCurrentCol(int currentCol)
	{
		if (currentCol < 0)
		{
			if (currentRow == 0)
			{

				currentMonth--;

				if (currentMonth == -1)
				{
					currentMonth = 11;
					currentYear--;
				}
				currentDay = getMonthDays(currentYear, currentMonth);
				currentDay1 = currentDay;
				cellX = -1;
				cellX = -1;
				view.invalidate();
				return;
			}

			else
			{
				currentCol = 6;
				setCurrentRow(--currentRow);

			}
		}
		else if (currentCol > 6)
		{
			currentCol = 0;
			setCurrentRow(++currentRow);

		}
		this.currentCol = currentCol;
		redrawForKeyDown = true;
		view.invalidate();
	}

	public int getCurrentRow()
	{
		return currentRow;
	}

	public int getCurrentCol()
	{
		return currentCol;
	}

	public void setCellX(float cellX)
	{

		this.cellX = cellX;
	}

	public void setCellY(float cellY)
	{

		this.cellY = cellY;
	}
	/*下面来编写一个getMonthDays 方法， 该方法用来获得指定
	月份的天数。这个方法也是绘制指定月份的日历的基础， 代码
	如下：*/

	private int getMonthDays(int year, int month)
	{
		month++;
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
			{
				return 31;
			}
			case 4:
			case 6:
			case 9:
			case 11:
			{
				return 30;
			}
			case 2:
			{
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
					return 29;
				else
					return 28;
			}
		}
		return 0;
	}
	/*下面到了关键时刻， 需要基础出当前月的天数， 以及上一
	月和下一月落在本月初和本月末的天数。并将其放在days 数
	组中。代码如下*/

	private void calculateDays()
	{
		calendar.set(currentYear, currentMonth, 1);// 将当前日历设为指定月份的第一天

		int week = calendar.get(calendar.DAY_OF_WEEK);// 获得指定月份的第1 天是当前周的第几天
		int monthDays = 0;
		int prevMonthDays = 0;

		monthDays = getMonthDays(currentYear, currentMonth);// 获得当前月有多少天
		if (currentMonth == 0)// 如果当前月是一年中的第一个月，则获得上一年最后一月
			                  //（也就是12 月）的天数

			prevMonthDays = getMonthDays(currentYear - 1, 11);
		else // 否则，获得指定月上一月的天数
			prevMonthDays = getMonthDays(currentYear, currentMonth - 1);

		for (int i = week, day = prevMonthDays; i > 1; i--, day--)
		{
			days[i - 2] = "*" + String.valueOf(day);
		}
		// 设置指定月（在这里是当前月）的天数
		for (int day = 1, i = week - 1; day <= monthDays; day++, i++)
		{
			days[i] = String.valueOf(day);
			if (day == currentDay)
			{
				currentDayIndex = i;// 获得当前日在days 数组中的索引

			}
		}
		// 设置下一月显示在本月日历后面的天数
		for (int i = week + monthDays - 1, day = 1; i < days.length; i++, day++)
		{
			days[i] = "*" + String.valueOf(day);
		}

	}

	public Grid(Activity activity, View view)
	{
		super(activity, view);
		if (dbService == null)
		{
			dbService = new DBService(activity);
		}

		tvMsg1 = (TextView) activity.findViewById(R.id.tvMsg1);
		tvMsg2 = (TextView) activity.findViewById(R.id.tvMsg2);
		dayColor = activity.getResources().getColor(R.color.day_color);
		//日期文本的颜色（白色）
		todayColor = activity.getResources().getColor(R.color.today_color);
		//今天的日期文本的颜色（白色）
		todayBackgroundColor = activity.getResources().getColor(
				R.color.today_background_color);
		//今天的日期文本边框的颜色（红色）
		innerGridColor = activity.getResources().getColor(
				R.color.inner_grid_color);
		//日历网格线颜色（白色）
		prevNextMonthDayColor = activity.getResources().getColor(
				R.color.prev_next_month_day_color);
		//上月和下月日期文字颜色（灰色），单击这些日期时，会自动跳到上月和下月
		currentDayColor = activity.getResources().getColor(
				R.color.current_day_color);
		//当前日期文字的颜色（红色）
		sundaySaturdayPrevNextMonthDayColor = activity.getResources().getColor(
				R.color.sunday_saturday_prev_next_month_day_color);
		//周六、周日文字颜色（暗红色）
		daySize = activity.getResources().getDimension(R.dimen.day_size);
		//日期字体尺寸
		dayTopOffset = activity.getResources().getDimension(
				R.dimen.day_top_offset);
		//日期文字距当前网格顶端的偏移量，用于微调日期文字的位置
		currentDaySize = activity.getResources().getDimension(
				R.dimen.current_day_size);
		//当前日期文字尺寸
		monthNames = activity.getResources().getStringArray(R.array.month_name);
		//月份名称
		paint.setColor(activity.getResources().getColor(R.color.border_color));
		currentYear = calendar.get(calendar.YEAR);
		currentMonth = calendar.get(calendar.MONTH);
	}

	private boolean isCurrentDay(int dayIndex, int currentDayIndex,
			Rect cellRect)
	{
		boolean result = false;
		if (redrawForKeyDown == true)
		{
			result = dayIndex == (7 * ((currentRow > 0) ? currentRow : 0) + currentCol);
			if (result)
				redrawForKeyDown = false;

		}
		else if (cellX != -1 && cellY != -1)
		{
			if (cellX >= cellRect.left && cellX <= cellRect.right
					&& cellY >= cellRect.top && cellY <= cellRect.bottom)
			{
				result = true;
			}
			else
			{
				result = false;
			}
		}
		else
		{
			result = (dayIndex == currentDayIndex);

		}
		if (result)
		{
			if (currentRow > 0 && currentRow < 6)
			{
				currentDay1 = currentDay;

			}
			currentDayIndex = -1;
			cellX = -1;
			cellY = -1;

		}
		return result;
	}

	// 更新当前日期的信息
	private void updateMsg(boolean today)
	{
		String monthName = monthNames[currentMonth];
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(currentYear, currentMonth, currentDay);

		dateString = sdf.format(calendar.getTime());
		String lunarStr = "";


		monthName += "   本月第" + calendar.get(java.util.Calendar.WEEK_OF_MONTH)
				+ "周";
		tvMsg1.setText(monthName);
		if (today)
			dateString += "(今天)";
		dateString += "   本年第" + calendar.get(java.util.Calendar.WEEK_OF_YEAR)
				+ "周";
		tvMsg2.setText(dateString);


	}

	public boolean inBoundary()
	{
		if (cellX < borderMargin
				|| cellX > (view.getMeasuredWidth() - borderMargin)
				|| cellY < top
				|| cellY > (view.getMeasuredHeight() - borderMargin))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	float top, left;

	@Override
	public void draw(Canvas canvas)
	{
		left = borderMargin;
		top = borderMargin + weekNameSize + weekNameMargin * 2 + 4;
		float calendarWidth = view.getMeasuredWidth() - left * 2;
		float calendarHeight = view.getMeasuredHeight() - top - borderMargin;
		float cellWidth = calendarWidth / 7;
		float cellHeight = calendarHeight / 6;
		paint.setColor(innerGridColor);
		canvas.drawLine(left, top, left + view.getMeasuredWidth()
				- borderMargin * 2, top, paint);
		
		for (int i = 1; i < 6; i++)
		{
			 canvas.drawLine(left, top + (cellHeight) * i, left +
			 calendarWidth,
			 top + (cellHeight) * i, paint);
		}
		//画横线
		for (int i = 1; i < 7; i++)
		{
			 canvas.drawLine(left + cellWidth * i, top, left + cellWidth * i,
			 view.getMeasuredHeight() - borderMargin, paint);
		}

		// 画竖线

		calculateDays();
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		//生成当前月需要显示的日期文本，并将结果保存在days变量中
		int day = calendar.get(calendar.DATE);
		//获得当前日期的天
		int myYear = calendar.get(calendar.YEAR), myMonth = calendar
				.get(calendar.MONTH);
		//获得当前日期的月和年
		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				1);
		int week = calendar.get(calendar.DAY_OF_WEEK);
		int todayIndex = week + day - 2;
		boolean today = false;
		if (currentDayIndex == -1)
		{
			currentDayIndex = todayIndex;

		}
		boolean flag = false;
		getRecordDays();
		//在ondraw 方法中添加绘制日历的代码（2）：
		for (int i = 0; i < days.length; i++)
		{
			today = false;
			int row = i / 7;//计算当前行
			int col = i % 7;//计算当前列
			String text = String.valueOf(days[i]);
			if ((i % 7 == 0 || (i - 6) % 7 == 0) && text.startsWith("*"))
			{
				paint.setColor(sundaySaturdayPrevNextMonthDayColor);
			}
			// 如果日历日期为上月和下月的周六、周日或days数组中所有以（*）开头的日期
			else if (i % 7 == 0 || (i - 6) % 7 == 0)
			{
				paint.setColor(sundaySaturdayColor);
			}
			// 如果日历日期为当前月的周六、周日
			else if (text.startsWith("*"))
			{
				paint.setColor(prevNextMonthDayColor);
			}
			//如果日历日期为上月和下月的周一至周五
			else
			{
				paint.setColor(dayColor); 
			//如果日历日期为当前月的普通日期
			}
			text = text.startsWith("*") ? text.substring(1) : text;
			// 去掉上月一下月日期前的星号
			Rect dst = new Rect();
			dst.left = (int) (left + cellWidth * col);
			dst.top = (int) (top + cellHeight * row);
			dst.bottom = (int) (dst.top + cellHeight + 1);
			dst.right = (int) (dst.left + cellWidth + 1);
			String myText = text;
			if (recordDays[i])
				myText = "*" + myText;
			//如果当前日期包含记录信息，在日期文字前加星号（*）
			paint.setTextSize(daySize);
			float textLeft = left + cellWidth * col
					+ (cellWidth - paint.measureText(myText)) / 2;
			float textTop = top + cellHeight * row
					+ (cellHeight - paint.getTextSize()) / 2 + dayTopOffset;
			if (myYear == currentYear && myMonth == currentMonth
					&& i == todayIndex)
			{
				paint.setTextSize(currentDaySize);
				//设置日期文字大小
				paint.setColor(todayBackgroundColor);
				//设置日期文字边框颜色
				dst.left += 1;
				dst.top += 1;
				canvas.drawLine(dst.left, dst.top, dst.right, dst.top, paint);
				canvas.drawLine(dst.right, dst.top, dst.right, dst.bottom,
						paint);
				canvas.drawLine(dst.right, dst.bottom, dst.left, dst.bottom,
						paint);
				canvas.drawLine(dst.left, dst.bottom, dst.left, dst.top, paint);
				//恢复日期文字颜色
				paint.setColor(todayColor);
				today = true;
			}
			// 绘制表示当前日历的图像，在日期文字周围绘制边框
			if (isCurrentDay(i, currentDayIndex, dst) && flag == false)
			{
				if (days[i].startsWith("*"))
				{
					if (i > 20)//下月
					{
						currentMonth++;
						if (currentMonth == 12)
						{
							currentMonth = 0;
							currentYear++;
						}
						view.invalidate();
						//刷新当前日历，重新显示下月的日历
					}
					else					// 上月
					{
						currentMonth--;
						if (currentMonth == -1)
						{
							currentMonth = 11;
							currentYear--;
						}
						view.invalidate();
						//刷新当前日历，重新显示上月的日历
					}
					currentDay = Integer.parseInt(text);
					currentDay1 = currentDay;
					cellX = -1;
					cellY = -1;
					break;

				}
				//如果单击当前月中显示的上月或下月日期时，自动显示上月或下月的日历
				else
				{
					paint.setTextSize(currentDaySize);
					flag = true;
					Bitmap bitmap = BitmapFactory.decodeResource(activity
							.getResources(), R.drawable.day);
					//获得背景图资源
					Rect src = new Rect();
					src.left = 0;
					src.top = 0;
					src.right = bitmap.getWidth();
					src.bottom = bitmap.getHeight();
					canvas.drawBitmap(bitmap, src, dst, paint);
					// 绘制背景图
					paint.setColor(currentDayColor);
					currentCol = col;
					currentRow = row;
					currentDay = Integer.parseInt(text);
					currentDay1 = currentDay;
					updateMsg(today);
					//更新日里头信息
				}
			}
			canvas.drawText(myText, textLeft, textTop, paint);

		}

	}

}

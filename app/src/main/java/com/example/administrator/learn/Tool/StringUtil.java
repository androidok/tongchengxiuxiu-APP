package com.example.administrator.learn.Tool;

import android.text.TextUtils;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with Www.XinAiRi.Com User: XinAiRi.Com - 水中之星 Date: 13-4-24 Time:
 * 上午11:37 Description:
 */
public class StringUtil
{

	/**
	 * 判断字符串是否为空,或者是否为空串
	 * 
	 * @param str
	 * @return
	 */
	public boolean isEmpty(String str)
	{

		if (str != null && str.length() > 0)
		{
			return true;
		}

		return false;

	}

	/**
	 * 通过type类型,把string转换成相应的类型
	 */
	public Object convertToObjectByType(Type type, String value)
	{

		Object o = value;

		if (!isEmpty((String) o))
		{
			return value;
		}

		if (type == int.class || type == Integer.class)
		{
			o = toInt(value);
		}

		else if (type == boolean.class || type == Boolean.class)
		{
			o = toBoolean(value);
		}

		else if (type == float.class || type == Float.class)
		{
			o = toFloat(value);
		}

		else if (type == double.class || type == Double.class)
		{
			o = toDouble(value);
		}

		else if (type == long.class || type == Long.class)
		{
			o = toLong(value);
		}

		else if (type == Date.class)
		{
			o = toDate(value, "yyyy-MM-dd'T'hh:mm:ss");
		}

		// ...待扩充

		return o;
	}
	
	private Object toDouble(String value)
	{
		return toDouble(value, 0);
	}

	private Object toDouble(String value, double i)
	{
		try
		{
			return Double.valueOf(value);
		} catch (Exception e)
		{
			e.printStackTrace();
			return i;
		}
	}
	//字符串转换成Float
	private Object toFloat(String value)
	{
		return toFloat(value, 0f);
	}

	private Object toFloat(String value, float i)
	{
		try
		{
			return Float.valueOf(value);
		} catch (Exception e)
		{
			e.printStackTrace();
			return i;
		}
	}

	public int toInt(String value)
	{
		return toInt(value, 0);
	}

	public int toInt(String value, int defaultValue)
	{
		try
		{
			return Integer.valueOf(value);
		} catch (Exception e)
		{
			e.printStackTrace();
			return defaultValue;
		}
	}

	public long toLong(String value)
	{
		return toLong(value, 0);
	}

	public long toLong(String value, long defaultValue)
	{
		try
		{
			return Long.valueOf(value);
		} catch (Exception e)
		{
			e.printStackTrace();
			return defaultValue;
		}
	}

	public boolean toBoolean(String value)
	{
		return toBoolean(value, false);
	}

	public boolean toBoolean(String value, boolean defaultValue)
	{
		try
		{
			return Boolean.valueOf(value);
		} catch (Exception e)
		{
			e.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * 字符串转换到时间格式
	 * 
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间 转换出错时返回当前日期
	 * @throws ParseException
	 *             转换异常
	 */
	public Date toDate(String dateStr, String formatStr)
	{
		if (dateStr.equals("") || dateStr == null)
			return null;
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try
		{
			date = sdf.parse(dateStr);
		} catch (ParseException e)
		{
			date = new Date(new Date().getDate());
		}
		return date;
	}
    //字符串转换成时间
	public Date toDate(String dateStr)
	{
		return toDate(dateStr, "yyyy-MM-dd");
	}

	/**
	 * 格式化float 保留i位小数
	 * 
	 * @param f
	 * @param i
	 * @return
	 */
	public float round(float f, int i)
	{
		float y = (float) Math.pow(10, i);
		return Math.round(f * y) / y;
	}

	public String dateToString(Date date)
	{
		SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		String str = temp.format(date);
		return str;
	}

	public String dateToShortDateString(Date date)
	{
		if (date == null)
			return "";
		try
		{
			SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd");
			String str = temp.format(date);
			return str;
		} catch (Exception ex)
		{
			return "";
		}

	}

	public String dateToShortDateString(Date date, String format)
	{
		if (date == null)
			return "";
		SimpleDateFormat temp = new SimpleDateFormat(format);
		String str = temp.format(date);
		return str;
	}

	/**
	 * JAVA截取字符串右侧指定长度的字符串
	 * 
	 * @param input
	 *            输入字符串
	 * @param count
	 *            截取长度
	 * @return 截取字符串
	 */
	public String right(String input, int count)
	{
		if (input == null)
			return "";
		if (input.equals(""))
			return "";
		count = (count > input.length()) ? input.length() : count;
		return input.substring(input.length() - count, input.length());
	}

	// 获取年、月、日
	public int getDate(Date thisDate, String format)
	{
		if (thisDate == null)
			return -1;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateStr = formatter.format(thisDate);
		return Integer.parseInt(dateStr);
	}

	// 获取email的正确格式
	public boolean isEmailValid(String email)
	{
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())
		{
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 验证手机格式
	 */
	public boolean isMobileNO(String mobiles)
	{
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * 生成UUID
	 */
	public String getUUID()
	{
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	/**
	 * 去空
	 */
	public String clearNUll(String value)
	{
		String value1 = value.replace("&nbsp;", "");
		return value1;
	}

	/**
	 * 时间截取
	 */
	public String time_format(String time)
	{
		String value1 = time.substring(0, 10);
		return value1;
	}

	/* 时间戳转换成字符窜 */
	public String getDateToString(long time)
	{
		Date d = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(d);
	}

	/**
	 * 如果字符串为空,返回空串,不为空返回字符串的值
	 * 
	 * @param str
	 */
	public String formatStrNull(String str)
	{
		return str == null ? "" : str;
	}

	/**
	 * 判断sd卡是否存在
	 * 
	 * @param context
	 * @return
	 */

	/**************************************** Md5加密 ***********************************/

	public static String MD5Encode(String s) throws NoSuchAlgorithmException
	{
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		md5.update(s.getBytes());
		String md = convertBytesToHex(md5.digest());
		return md;
	}

	public static String convertBytesToHex(byte[] bs)
	{
		// final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
		// '8',
		// '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		final char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		if (bs == null)
		{
			return null;
		}
		int len = bs.length;
		char result[] = new char[2 * len];
		int k = 0;

		for (int i = 0; i < len; i++)
		{
			byte b = bs[i];

			result[k++] = hexDigits[b >>> 4 & 0xf];
			result[k++] = hexDigits[b & 0xf];
		}
		return new String(result);
	}
}

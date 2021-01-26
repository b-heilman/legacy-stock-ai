package myCB.stock;

import java.io.*;
import java.util.*;


public class StockDay implements Serializable
	{
	private static final long serialVersionUID = 2L;
	String Date,Open,Close,High,Low,Vol;
	
	public StockDay(String Line)
		{
		StringTokenizer token = new StringTokenizer(Line,"|");
		Date = token.nextToken().trim();
		Open = token.nextToken().trim();
		High = token.nextToken().trim();
		Low = token.nextToken().trim();
		Close = token.nextToken().trim();
		Vol = token.nextToken().trim();
		}
	
	public StockDay(String D, String O, String C, String H, String L,String V)
		{
		Date = D.trim();
		Open = O.trim();
		Close = C.trim();
		High = H.trim();
		Low = L.trim();
		Vol = V.trim();
		}
	
	public boolean equals(StockDay that)
	{
		return (this.Open.equalsIgnoreCase(that.Open) &&
				this.Open.equalsIgnoreCase(that.Open) &&
				this.Open.equalsIgnoreCase(that.Open) &&
				this.Open.equalsIgnoreCase(that.Open));
	}
	public String getDate()
		{
		return Date;
		}
	public String getOpen()
		{
		return Open;
		}
	public String getClose()
		{
		return Close;
		}
	public String getHigh()
		{
		return High;
		}
	public String getLow()
		{
		return Low;
		}
	public String getVolume()
		{
		return Vol;
		}
	public String save()
		{
		return (Date+"|"+Open+"|"+High+"|"+Low+"|"+Close+"|"+Vol+"|");
		}
	public String[] toArray()
		{
		String[] hold = new String[6];
		hold[0] = (Date);
		hold[1] = (Open);
		hold[2] = (High);
		hold[3] = (Low);
		hold[4] = (Close);
		hold[5] = (Vol);
		return hold;
		}
	public float[] toValues()
		{
		float[] hold = new float[4];
		
		hold[0] = Float.valueOf(Open);
		hold[1] = Float.valueOf(High);
		hold[2] = Float.valueOf(Low);
		hold[3] = Float.valueOf(Close);
		
		return hold;
		}
	
	public float toVolume()
		{
		if (Vol.indexOf(',') >= 0)
			{
			StringTokenizer tokens = new StringTokenizer(Vol,",");
			String temp = new String();
			while(tokens.hasMoreTokens())
				temp += tokens.nextToken();
			return Float.valueOf(temp);
			}
		return Float.valueOf(Vol);
		}
	//-------------
	//
	//-------------
	public static String[] headers()
		{
		String[] hold = new String[6];
		hold[0] = ("Date");
		hold[1] = ("Open");
		hold[2] = ("High");
		hold[3] = ("Low");
		hold[4] = ("Close");
		hold[5] = ("Volume");
		return hold;
		}
	}
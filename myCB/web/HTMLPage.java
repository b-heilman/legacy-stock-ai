package myCB.web;

import java.io.*;
import java.net.*;
import myCB.data.*;

public class HTMLPage extends Page
	{
//-------------------
// 
//-------------------		
	public HTMLPage()
		{
		super();
		}
//-------------------
// 
//-------------------		
	public HTMLPage(MyList O)
		{
		super.copy(O);
		}
//-------------------
// Places a tag on each line individually
//-------------------
	public void parseFormat()
		{
		/*
		int place;
		for(start();!done();next())
			{
			place = endTag();
			if (startOfTag(place+1) > place && place != -1)
				{
				breakLine(startOfTag(place+1));
				}
			}
		*/	
		int b, e;
		for(start();!done();next())
			{
			do
				{
				b = startOfTag();
				if (b != -1)
					{
					e = endOfTag(b);
					breakLine(e+1);
					if (b != 0)
						{
						breakLine(b);
						next();
						}
					next();
					}
				}while(b != -1 && !done());
			}
		}

//-------------------
// returns true of false if the tag located is infact a tag
//-------------------
	public boolean isTag(int place)
		{
		return (place == startOfTag(place) &&
				place < endOfTag(place));
		}
//-------------------
// returns true of false if the tag located is infact a Starting tag
//-------------------
	public boolean isStartTag(int place)
		{
		try
			{
			return (place == startOfTag(place) &&
					place < endOfTag(place) &&
					charFollowing(place) != '/');
			}
		catch (Exception ex)
			{
			return false;
			}
		}
//-------------------
// returns true of false if the tag located is infact a Ending tag
//-------------------
	public boolean isEndTag(int place)
		{
		try
			{
			return (place == startOfTag(place) &&
					place < endOfTag(place) &&
					charFollowing(place) == '/');
			}
		catch (Exception ex)
			{
			return false;
			}
		}
//-------------------
// returns the place of the first end tag in the statement
//-------------------
	public int endTag()
		{
		int search = startOfTag();
		
		while(search != -1)
			{
			if (isEndTag(search))
				return search;
				
			search = startOfTag(endOfTag(search));
			}
			
		return search;
		}
//-------------------
// looks to see if a tag begining can be found begining at the location passed in
//	returns:
//		1 to Size - 1 if the begining of a tag is located
//	     	-1 if no tag start is found 
//-------------------	
	public int startOfTag(int startAt)
		{
		int place;
		
		if(startAt < 0 || startAt > ((String)get()).length())
			return -1;
			
		place = foundHere('<',startAt);
		while((foundHere("'>'") == place - 1 || foundHere("\">\"") == place - 1)
			&& place != 0)
			{
			place = foundHere('<',place+1);
			}
		return place;
		}
//-------------------
// looks to see if a tag begining can be found
//	returns:
//		1 to Size - 1 if the begining of a tag is located
//	     	-1 if no tag start is found 
//-------------------	
	public int startOfTag()
		{
		int place;
			
		place = foundHere('<');
		while((foundHere("'>'") == place - 1 || foundHere("\">\"") == place - 1)
			&& place != 0)
			{
			place = foundHere('<',place+1);
			}
		return place;
		}
//-------------------
// looks to see if a tag closure can be found begining at the location passed in
//	returns:
//		1 to Size - 1 if the begining of a tag is located
//	     	-1 if no tag start is found 
//-------------------	
	public int endOfTag(int startAt)
		{
		int place;
		
		if(startAt < 0 || startAt > ((String)get()).length())
			return -1;
			
		place = foundHere('>',startAt);
		while((foundHere("'>'") == place - 1 || foundHere("\">\"") == place - 1)
			&& place != 0)
			{
			place = foundHere('>',place+1);
			}
		return place;
		}
//-------------------
// looks to see if a tag start can be found begining at the location passed in
//	returns:
//		1 to Size - 1 if the begining of a tag is located
//	     	-1 if no tag start is found 
//-------------------	
	public int startOfTag(String find)
		{
		int placeHolder = -1;
		String foundHolder =  "  ";
		
		do
			{
			placeHolder = startOfTag(placeHolder+1);
			foundHolder = wordFollowing(placeHolder,'>');
			}while(!foundHolder.equalsIgnoreCase(find) && placeHolder > -1 );

		if (placeHolder > -1)
			return placeHolder;
		else
			return -1;
		}
//-------------------
// looks to see if a tag closure can be found begining at the location passed in
//	returns:
//		1 to Size - 1 if the begining of a tag is located
//	     	-1 if no tag start is found 
//-------------------	
	public int endOfTag(String find)
		{
		int placeHolder = -1;
		String foundHolder =  "  ";
		
		do
			{
			placeHolder = startOfTag(placeHolder+1);
			foundHolder = wordFollowing(placeHolder,'>');
			}while(!foundHolder.equalsIgnoreCase(find) && placeHolder > -1 );

		if (placeHolder > -1)
			return endOfTag(placeHolder);
		else
			return -1;
		}
//-------------------
// looks to see if a tag closure can be found.
//	returns:
//		1 to Size - 1 if the begining of a tag is located
//	     	-1 if no tag start is found 
//-------------------	
	public int endOfTag()
		{
		int place;
		
		place = foundHere('>');
		while((foundHere("'>'") == place - 1 || foundHere("\">\"") == place - 1)
			&& place != 0)
			{
			place = foundHere('>',place+1);
			}
		return place;
		}
//-------------------
// locates the next use of a tag in the code
//-------------------	
	public boolean locateNextTag(String find)
		{
		for(;!done();next())
			{
			if(lineHasTag(find))
				return true;
			}
		return false;
		}
//-------------------
// locates the first occurance of a tag in the code
//-------------------	
	public boolean locateTag(String find)
		{
		for(start();!done();next())
			{
			if(lineHasTag(find))
				return true;
			}
		return false;
		}
//-------------------
// only for use of TagHandler class to find Tags in Code
//-------------------	
	public boolean lineHasTag(String find)
		{
		int placeHolder = -1;
		String foundHolder =  "  ";
		
		do
			{
			placeHolder = startOfTag(placeHolder+1);
			foundHolder = wordFollowing(placeHolder,'>');
			}while(!foundHolder.equalsIgnoreCase(find) && placeHolder > -1 );

		return (placeHolder > -1);
		}
//-------------------
// removes all of the tags from the page
//-------------------	
	public void delNBSP()
		{
		for (start();!done();next())
			eraseWord("&nbsp;");
		}
//-------------------
// removes all of the tags from the line
//-------------------	
	public void delControl()
		{
		int start;
		int end;
		
		do
		{
		start = startOfTag();
		end = endOfTag();
		if (start > -1)
			{
			if (end > -1)
				{
				erase(start,end+1);
				}
			else
				{
				erase(start,((String)get()).length());
				next();
				while((end = endOfTag()) == -1)
					{
					remove();
					next();
					}
				erase(0,end+1);
				}
			}
		}while (start > -1);
		}
//-------------------
// loads a html page from the a URL sent into the function
//-------------------	
	public void access(String Target)
		throws FileNotFoundException
		{
		URL TargetURL;
		InputStreamReader Reader;
		try
			{
			TargetURL = new URL(Target);
			URLConnection Link = TargetURL.openConnection(); 
			Reader = new InputStreamReader(Link.getInputStream());
		
			BufferedReader File = new BufferedReader(Reader);
			String Line;
			while ((Line = File.readLine()) != null) 
				{
				push_back(Line);
				} 
			File.close();
			}
		catch (FileNotFoundException ex)
			{
			throw ex;
			}
		catch (IOException ex) 
			{
			} 
		}
	}
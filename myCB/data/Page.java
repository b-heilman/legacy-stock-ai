package myCB.data;

import java.io.*;
import java.util.*;

public class Page extends MyList
	{
//-------------------
// creates a page type by copying a previous one
//-------------------	
	public Page(Page In)
		{
		super(In);
		}
//-------------------
// creates a blank page
//-------------------	
	public Page()
		{
		super();
		}
//-------------------
// Returns a deep copy of the page
//-------------------	
	public boolean findDC(String O)
		{
		int start;
		int end;
		
		for(start();!done();next())
			{
			String temp = new String((String)get());
			end = O.length();
			for (start = 0; end+start <= temp.length(); start++)
				{
				if ((temp.substring(start,end+start)).compareToIgnoreCase(O) == 0)
					return true;
				}
			}
		return false;
		}
//-------------------
// Returns a deep copy of the page
//-------------------	
	public boolean findHereDC(String O)
		{
		int start;
		int end;
		
		String temp = new String((String)get());
		end = O.length();
		for (start = 0; end+start <= temp.length(); start++)
			{
			if ((temp.substring(start,end+start)).compareToIgnoreCase(O) == 0)
				return true;
			}
			
		return false;
		}
//-------------------
// Returns a deep copy of the page
//-------------------	
	public Page getCopy()
		{
		Page temp = new Page();
		
		for(start();!done();next())
			{
			temp.push_back(get());
			}
			
		return temp;
		}
//-------------------
// Inserts a pages data into the system at the place pointed to by get
//-------------------	
	public void paste(Page In)
		{
		for(In.start();!In.done();In.next())
			{
			insert(In.get());
			next();
			}
		}
//-------------------
// Inserts a pages data into the system at the place pointed to by get
//-------------------	
	public void append(Page In)
		{
		for(In.start();!In.done();In.next())
			{
			push_back(In.get());
			next();
			}
		}
//-------------------
// Erases characters from the string.  Starts at From and up to and including To
//-------------------	
	public void erase(int From, int To)
		{
		StringBuffer hold = new StringBuffer((String)get());
		hold.delete(From, To);
		replace(hold.toString());
		}
//-------------------
// Erases characters from the string.  Starts at From and up to and including To
//-------------------	
	public void eraseWord(String find)
		{
		int hold;
		
		do
			{
			hold = foundHere(find);
			if(hold > -1)
				{
				erase(hold,hold+find.length());
				}
			}while(hold > -1);
		}	
//-------------------
// removes all unneeded white spaces from the page
//-------------------	
	public void clean()
		{
		replace(((String)get()).trim());
		}
//-------------------
// removes any blank lines from the page
//-------------------	
	public void removeBlanks()
		{
		if(((String)get()).length() == 0)
			{
			remove();
			prev();
			}
		}
//-------------------
// loads a page from a file on the computer
//-------------------	
	public void load(String Name) throws FileNotFoundException
		{
		try
			{
			BufferedReader File = new BufferedReader(new FileReader(Name));
			String Line;
			while ((Line = File.readLine()) != null) 
				{
				push_back(Line);
				} 
			File.close();
			}
		catch (java.io.FileNotFoundException ex)
			{
			throw new FileNotFoundException();
			}
		catch (java.io.IOException ex) 
			{
			
			} 
		}
//-------------------
// saves the page to a file on the computer
//-------------------	
	public void save(String Name)
		{
		try
			{
			PrintWriter File = new PrintWriter(new BufferedWriter(new FileWriter(Name)));
			
			for(start();!done();next())
				{
				File.println(get());
				}
			File.close();
			}
		catch (java.io.IOException ex) 
			{
			System.out.println("IOException has occured"); 
			}  
		}
//-------------------
// returns the next character following a given point
//-------------------
	public char charFollowing(int x) throws Exception
		{
		int y;
		if(x < 0 || x > ((String)get()).length())
			throw new Exception(); // define better later
			
		for(x++;Character.isWhitespace(((String)get()).charAt(x)) 
				&& ((String)get()).length() > x;x++);
		
		if(x < 0 || x > ((String)get()).length())
			throw new Exception(); // define better later
			
		return ((String)get()).charAt(x);
		}
//-------------------
// returns the word following a given location
//-------------------	
	public String wordFollowing(int x)
		{
		int y;
		if(x < 0 || x > ((String)get()).length())
			return "";
			
		for(x++;Character.isWhitespace(((String)get()).charAt(x)) 
				&& ((String)get()).length() > x;x++);
		
		if(x == ((String)get()).length())
			{
			next();
			return wordFollowing(0);
			}
		else
			{
			for(y=x;!(Character.isWhitespace(((String)get()).charAt(y))) 
				&& ((String)get()).length() > y;y++);
			
			return ((String)get()).substring(x,y+1);
			}
			
		}
//-------------------
// returns the word following a given location, but will stop at a given character
//-------------------	
	public String wordFollowing(int x, char stop)
		{
		int y;
		if(x < 0 || x > ((String)get()).length())
			return "";
			
		for(x++;Character.isWhitespace(((String)get()).charAt(x)) 
				&& ((String)get()).length() > x;x++);
		
		if(x == ((String)get()).length())
			{
			next();
			return wordFollowing(0);
			}
		else
			{			
			for(y=x;!(Character.isWhitespace(((String)get()).charAt(y))) 
				&& (((String)get()).charAt(y)) != stop
				&& ((String)get()).length()-1 > y;y++);
			
			return ((String)get()).substring(x,y);
			}
			
		}
//-------------------
// looks to see if a character is found on this line, starting at the given place
// 	returns:
//		-1 if the character can be found no where on the line
//		0 to size-1 if the character is found
//-------------------	
	public int foundHere(char Find, int Start)
		{
		return (((String)get()).indexOf(Find, Start));
		}
//-------------------
// looks to see if a string is found on this line, starting at the given place
// 	returns:
//		-1 if the string can be found no where on the line
//		0 to size-1 if the string is found
//-------------------	
	public int foundHere(String Find, int Start)
		{
		return (((String)get()).indexOf(Find, Start));
		}
//-------------------
// looks to see if a character is found on this line, starting at the begining of the line
// 	returns:
//		-1 if the character can be found no where on the line
//		0 to size-1 if the character is found
//-------------------	
	public int foundHere(char Find)
		{
		return (((String)get()).indexOf(Find));
		}
//-------------------
// looks to see if a string is found on this line, starting at the begining of the line
// 	returns:
//		-1 if the string can be found no where on the line
//		0 to size-1 if the string is found
//-------------------	
	public int foundHere(String Find)
		{
		return (((String)get()).indexOf(Find));
		}
//-------------------
// breaks off a segment of the line and pushes it to the spot below it.  The pointer is kept at the original spot, not the broken segment.
//-------------------	
	public void breakLine(int spot)
		{
		//---------------------
		if (spot < ((String)get()).length() && spot > 0)
			{
			String part = ((String)get()).substring(spot,((String)get()).length());
			erase(spot,((String)get()).length());
			next();
			insert(part);
			prev();
			}
		//---------------------
		}
//-------------------
// breaks the page up so that ever white space creates a new line. don't know why useful, but I had
// a reason at the time
//-------------------	
	public void seperate()
		{
		String hold;
		StringTokenizer temp;
		for (start();!done();)
			{
			// break string up into pieces
			temp = new StringTokenizer((String)get());
			remove();
			while (temp.hasMoreTokens()) 
				{ 
				insert(temp.nextToken());
				next();
				}
			//jump to next string
			}
		}
//-------------------
// breaks the page up so that ever white space creates a new line. don't know why useful, but I had
// a reason at the time
//-------------------	
	public void seperate(String x)
		{
		String hold;
		StringTokenizer temp;
		for (start();!done();)
			{
			// break string up into pieces
			temp = new StringTokenizer((String)get(),x);
			remove();
			while (temp.hasMoreTokens()) 
				{ 
				insert(temp.nextToken());
				next();
				}
			//jump to next string
			}
		}
	public void oneLine()
		{
		String hold = "";
		for(start();!done();next())
			hold = hold + get();
			
		reset();
		
		push_front(hold);
		}
	}
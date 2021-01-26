package myCB.data;

import java.io.*;
import java.util.*;

/*----------------------------------------------------------------
This is a wrapper class that will allow the LinkedList class in string form to be used as a list from the C++ STL.
A few additions have been added so that the list can be easily traversed by using a lone for loop.
-----------------------------------------------------------------*/
	
public class IndexList extends MyList
	{
	LinkedList<String> Index;	// Index to the data which is being controlled
//-------------------
// Creates a new linked list and sets the place to zero.  A error can occur if you try to access this list without data
// being in it, but that just follows what the class it's wrapping does.
//-------------------		
	public IndexList()
		{
		super();
		Index = new LinkedList<String>();
		}
//-------------------
// Creates a new linked list and sets the place to zero.  A error can occur if you try to access this list without data
// being in it, but that just follows what the class it's wrapping does.  This is the copy constructor
//-------------------		
	public IndexList(IndexList O)
		{
		copy(O);
		}
//------------------
// Returns a deep copy of the object 
//------------------		
	public void copy(IndexList O)
		{
		super.copy(O);
		Index = (LinkedList<String>)O.Index.clone();
		}		
//-------------------
// Access the object which the pointer is pointing to
//-------------------	
	public Object getTag()
		{
		return Index.get(Place);
		}
//-------------------
// inserts the object into the place, shifting the rest of the list down.  the object once there is now place + 1
//-------------------	
	public void insert(String tag,Object Line)
		{
		Index.add(Place,tag);
		Data.add(Place,Line);
		}
//-------------------
// Pushes an object to the front of the list
//-------------------	
	public void push_front(String tag,Object Line)
		{
		Index.addFirst(tag);
		Data.addFirst(Line);
		}
//-------------------
// Pushes an object to the back of the list
//-------------------	
	public void push_back(String tag,Object Line)
		{
		Index.addLast(tag);
		Data.addLast(Line);
		}
//-------------------
// Removes the object the pointer is pointing to
//-------------------	
	public void remove()
		{
		Data.remove(Place);
		Index.remove(Place);
		}
//-------------------
// removes the head of the list
//-------------------	
	public void pop_front()
		{
		Data.removeFirst();
		Index.removeFirst();
		}
//-------------------
// removes the tail of the list
//-------------------	
	public void pop_back()
		{
		Data.removeLast();
		Index.removeLast();
		}
//-------------------
// clears the contents of the entire list
//-------------------	
	public void reset()
		{
		Data.clear();
		Index.clear();
		}
//-------------------
// dumps the contents of the list to the given stream
//-------------------	
	public void dump(PrintStream Out)
		{
		for(start();!done();next())
			{
			Out.println(getTag());
			Out.println(get());
			}
		}
//-------------------
// dumps the contents of the list to the given PrintWriter
//-------------------	
	public void dump(PrintWriter Out)
		{
		for(start();!done();next())
			{
			Out.println(getTag());
			Out.println(get());
			}
		}
//-------------------
// dumps the contents of the list to the System output
//-------------------	
	public void show()
		{
		for(start();!done();next())
			{
			System.out.println(getTag());
			System.out.println(get());
			}
		}
//	-------------------
//	 Finds the first occurance of the Tag in the list
//	-------------------		
		public boolean findTag(String find)
			{
			for(start();!done();next())
				{
				if (getTag().equals(find))
					return true;
				}
			return false;
			}
//-------------------
// Finds the first occurance of the Tag in the list
//-------------------		
	public void firstTag(String find)
		{
		for(start();!done();next())
			{
			if (getTag().equals(find))
				return;
			}
		}
//-------------------
// Finds the next occurance of the tag in the list
//-------------------		
	public void nextTag(String find)
		{
		for(next();!done();next())
			if (getTag().equals(find))
				return;
		}
//-------------------
// returns true if the tag has been found
//-------------------		
	public boolean foundTag()
		{
		return !done();
		}
	public String[] getIndexs()
		{
		int i = 0;
		String[] list = new String[size()];
		
		for(start(); !done(); next())
			{
			list[i] = (String)getTag();
			i++;
			}
			
		return list;		
		}
	}
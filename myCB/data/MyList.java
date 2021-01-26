package myCB.data;

import java.io.*;
import java.util.*;

/*----------------------------------------------------------------
This is a wrapper class that will allow the LinkedList class in string form to be used as a list from the C++ STL.
A few additions have been added so that the list can be easily traversed by using a lone for loop.
-----------------------------------------------------------------*/
	
public class MyList extends Object implements Serializable
	{
	LinkedList Data;	// The data which is being wrapped by the list
	/* ListIterator itt = list.listIterator(); */
	int Place;			// The point in the list which the commands are now focusing on
	LinkedList Memory;			// Stores the list Place
//-------------------
// Creates a new linked list and sets the place to zero.  A error can occur if you try to access this list without data
// being in it, but that just follows what the class it's wrapping does.
//-------------------		
	public MyList()
		{
		Data = new LinkedList();
		Place = 0;
		Memory = new LinkedList();
		}
//-------------------
// Creates a new linked list and sets the place to zero.  A error can occur if you try to access this list without data
// being in it, but that just follows what the class it's wrapping does.  This is the copy constructor
//-------------------		
	public MyList(MyList O)
		{
		Data = new LinkedList();
		for(O.start(); !O.done(); O.next())
			{
			push_back(((Object)O.get()));
			}
		Place = 0;
		Memory = new LinkedList();
		}
//------------------
// Returns a deep copy of the object 
//------------------		
	public void copy(MyList O)
		{
		Data = new LinkedList();
		for(O.start(); !O.done(); O.next())
			{
			push_back(((Object)O.get()));
			}
		Place = 0;
		Memory = new LinkedList();
		}		
//-------------------
// Will cycle through the list to see if it can find an object in the list, leaving the pointer at that location.
// Returns:
// 	True - if an object has been found
// 	False - if the desired object can not be located
//-------------------	
	public boolean find(Object Target)
		{
		for(start();!done();next())
			{	
			System.out.println(get()+" -- "+Target);
			if(get().equals(Target))
				return true;
			}
		return false;
		}
//-------------------
// Will remove what the pointer value is pointing to and then place the object sent to in back in its stead.
//-------------------	
	public void replace(Object thing)
		{
		remove();
		insert(thing);
		}
//-------------------
// The initializer for a loop of the list.  Sets the place value to zero, initializing the pointer to the head.
//-------------------	
	public void start()
		{
		Place = 0;
		}
//-------------------
// The initializer for a loop of the list.  Sets the place value to zero, initializing the pointer to the head.
//-------------------	
	public void end()
		{
		Place = Data.size()-1;
		}
//-------------------
// Moves the pointer to the next object in the list
//-------------------	
	public void next()
		{
		Place++;
		}
//-------------------
// Moves the pointer to the previous object in the list
//-------------------	
	public void prev()
		{
		Place--;
		}
//-------------------
// Used to loop through the list.  Will check to see if the pointer has exceeded the boundries of the class.
//-------------------	
	public boolean done()
		{
		return (Place >= Data.size() || Place < 0);
		}
//-------------------
// Accesses the data from the front of the list. 
//-------------------	
	public Object front()
		{
		return Data.getFirst();
		}
//-------------------
// Accesses the data from the back of the list. 
//-------------------	
	public Object back()
		{
		return Data.getLast();
		}
//-------------------
// Access the object which the pointer is pointing to
//-------------------	
	public Object get()
		{
		return Data.get(Place);
		}
//-------------------
// Access the object which the pointer is pointing to
//-------------------	
	public void to(int x)
		{
		Place = x;
		}
//-------------------
// inserts the object into the place, shifting the rest of the list down.  the object once there is now place + 1
//-------------------	
	public void insert(Object Line)
		{
		if(Place < 0)
			push_front(Line);
		else
			Data.add(Place,Line);
		}
//-------------------
// Pushes an object to the front of the list
//-------------------	
	public void push_front(Object Line)
		{
		Data.addFirst(Line);
		}
//-------------------
// Pushes an object to the back of the list
//-------------------	
	public void push_back(Object Line)
		{
		Data.addLast(Line);
		}
//-------------------
// Removes the object the pointer is pointing to
//-------------------	
	public void remove()
		{
		Data.remove(Place);
		}
//-------------------
// removes the head of the list
//-------------------	
	public void pop_front()
		{
		Data.removeFirst();
		}
//-------------------
// removes the tail of the list
//-------------------	
	public void pop_back()
		{
		Data.removeLast();
		}
//-------------------
// clears the contents of the entire list
//-------------------	
	public void reset()
		{
		Data.clear();
		Place = 0;
		}
//-------------------
// returns the size of the list
//-------------------	
	public int size()
		{
		return Data.size();
		}
//-------------------
// dumps the contents of the list to the given stream
//-------------------	
	public void dump(PrintStream Out)
		{
		for(start();!done();next())
			Out.println(get());
		}
//-------------------
// dumps the contents of the list to the given PrintWriter
//-------------------	
	public void dump(PrintWriter Out)
		{
		for(start();!done();next())
			Out.println(get());
		}
//-------------------
// dumps the contents of the list to the System output
//-------------------	
	public void show()
		{
		for(start();!done();next())
			System.out.println(get());
		}
//-------------------
// Sets the place the list is at
//-------------------		
	public void mark()
		{
		Memory.addLast(new Integer(Place));
		}
//-------------------
// Reverts the list back to the location set by the mark
//-------------------		
	public boolean recall()
		{
		if (Memory == null)
			Memory = new LinkedList();
		if(Memory.size() > 0)
			{
			Place = ((Integer)Memory.getLast()).intValue();
			Memory.removeLast();
			return true;
			}
		else
			{
			Place = 0;
			return false;
			}
		}
	public int pointingAt()
		{
		return Place;
		}
		
	public MyList subList(int /*inclusive*/ from, int /*exclusive*/ to)
		{
		MyList temp = new MyList();
		
		for(Place = from; Place < to; Place++)
			{
			temp.push_back(get());
			}
			
		return temp;
		}
	public Object[] toArray()
		{
		return Data.toArray();
		}
	}
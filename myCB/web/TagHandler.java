package myCB.web;

import myCB.data.*;

public class TagHandler
	{
	IndexList Classified;
	String mem;
	HTMLPage Code;
	HTMLPage Ripped;
//-------------------
// establishes the code which is being handled and the resulting ripped page
//-------------------		
	public TagHandler(HTMLPage In)
		{
		Code = In; // Original Code Read In
		Ripped = new HTMLPage();
		Classified = new IndexList();
		}
//-------------------
// Allows for tha getting of ripped text
//-------------------
	public Page get_Ripped()
		{
		return Ripped;
		}
//-------------------
// pulls all of the data which is between the begining of the tag and the end of the tag
//	returns:
//		a page of code which existed between tags matching the string sent in
//-------------------	
	public void rip(String Tag)
		{
		// Get the body
		doRip(Tag);
		// Delete tags
		for(Ripped.start();!Ripped.done();Ripped.next())
			{ 
			while(Ripped.foundHere("<") > -1)
				Ripped.delControl();
			}		
		}
//-------------------
// pulls all of the data which is between the begining of the tag and the end of the tag
//	returns:
//		a page of code which existed between tags matching the string sent in
//-------------------	
	public void format_Ripped()
		{
		for(Ripped.start();!Ripped.done();Ripped.next())
			{ 
			Ripped.clean();
			Ripped.removeBlanks();
			}
		}
//-------------------
// pulls all of the data which is between the begining of the tag and the end of the tag
//	returns:
//		a page of code which existed on the HTML page
//-------------------	
	public void rip()
		{
		Ripped = Code;
		// Delete tags

		for(Ripped.start();!Ripped.done();Ripped.next())
			{ 
			while(Ripped.foundHere("<") > -1)
				Ripped.delControl();
			}
		}
//-------------------
// Pulls all Tag data from the Code and places it into Ripped
//-------------------	
	protected void doRip(String find)
		{
		boolean add = false;
		int count = 0;
		
		for(Code.start();!Code.done();Code.next())
			{
			if (Code.lineHasTag(find))
				{
				count++;
				add = true;
				}
			if(add)
				{
				Ripped.push_back(((String)Code.get()));
				}
			if (Code.lineHasTag("/"+find))
				{
				count--;
				if (count == 0)
					{
					add = false;
					}
				}
			}
		}
//-------------------
// Pulls all Tag data from the Code and places it into classified
//-------------------	
	private void classify(String find)
		{
		MyList stack =  new MyList();
		
		for(Code.start();!Code.done();)
			{
			if (Code.lineHasTag(find))
				{
				stack.push_front(new Integer(Code.pointingAt()));
				}
			if (Code.lineHasTag("/"+find))
				{
				Classified.push_back
				(find,Code.subList(((Integer)stack.front()).intValue(),
									Code.pointingAt()+1));
				stack.pop_front();
				Code.breakLine(Code.endOfTag("/"+find)+1);
				}
			else
				Code.next();
			}
		}
//-------------------
// Will set the pointer to the head of the classified list
//-------------------	
	public void set_classified(String find)
		{
		mem = find;
		classify(mem);
		Classified.firstTag(mem);
		}
//-------------------
// Sets the pointer to the next item of the classified list which matches the called for Tag
// returns false if it can not find another instance of that tag
//-------------------	
	public void next_classified()
		{
		if (mem == null)
			{
			// throw an exception later
			}
		Classified.nextTag(mem);
		}
//-------------------
// returns an HTMLPage which is indexed by the Tag
//-------------------
	public HTMLPage get_classified()
		{
		if (mem == null)
			{
			// throw an exception later
			}
		if (Classified.foundTag())
			{
			return (new HTMLPage((MyList)Classified.get()));
			}
		else
			{
			return null;
			}
		}
//-------------------
// everything classified goes onto one line
//-------------------
	public HTMLPage oneLine_classified(String find)
		{
		HTMLPage temp, hold = new HTMLPage();

		for (set_classified(find); get_classified() != null; next_classified())
			{
			temp = ((HTMLPage)get_classified());
			temp.oneLine();
			hold.push_back(temp.front());
			}
		hold.oneLine();
		return hold;
		}
//-------------------
// every classified item goes onto one line, but each item has its own line
//-------------------
	public HTMLPage onePage_classified(String find)
		{
		HTMLPage temp, hold = new HTMLPage();

		for (set_classified(find); get_classified() != null; next_classified())
			{
			temp = ((HTMLPage)get_classified());
			temp.oneLine();
			hold.append(temp);
			}
		return hold;
		}
	}
	
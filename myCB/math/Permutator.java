package myCB.math;

public class Permutator
	{
	private int[] data;
	private int left, right;
	public Permutator(int[] values)
		{
		data = values;
		left = 0;
		right = 1;
		}
	public Permutator(int values)
		{
		data = new int[values];
		for (int i = 0; i < data.length; i++)
			data[i] = i;
		
		left = 0;
		right = 1;
		}
	public void nextPermutation()
		{
		right++;
		if (right >= data.length)
			{
			left++;
			right = left + 1;
			}
		}

	public boolean morePermutations()
		{
		return (right < data.length);
		}

	public int getRight()
		{
		return data[right];
		}

	public int getLeft()
		{
		return data[left];
		}
	
	public int getCount()
		{
		return (int)((data.length-1 + 1) * (data.length-1)/(float)2);
		}
	}

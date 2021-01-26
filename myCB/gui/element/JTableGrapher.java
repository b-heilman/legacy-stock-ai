package myCB.gui.element;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import myCB.data.*;
import myCB.gui.*;
// Assumes the most recent data in the JTable is at the top
public class JTableGrapher implements myGUI, ItemListener
	{
	Grapher graphs;
	JFrame frame;
	String at;
	MyList checks;
	
	public JTableGrapher(JTable in, int skip, int limit)
		{
		frame = new JFrame();
		checks = null;
		int[] list;
		graphs = new Grapher();
		
		for (int i = skip; i < in.getColumnCount(); i++)
			{
			if (limit > in.getRowCount())
				limit = in.getRowCount();
			list = new int[limit];
			
			try
				{
				for (int j = 0; j < limit; j++)
					{
					String hold = (String)in.getValueAt(j,i);
					float p = (Float.valueOf(hold));
					list[limit-j-1] = (int)(p*100);
					}
				graphs.add(list,in.getColumnName(i), pickColor(i));
				}
			catch (Exception ex)
				{
				// whatever
				}
			}
		}
	
	public Component getPanel()
		{
		return frame.getContentPane();
		}
	
	public void draw()
		{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(BorderLayout.WEST, selection_Layout());
		frame.getContentPane().add(BorderLayout.CENTER, graphs);
		}
	
	private JPanel selection_Layout()
		{
		JPanel p = new JPanel();
		MyList hold;
		if (checks == null)
			{
			checks = new MyList();
			hold = graphs.getNames();
			for(hold.start();!hold.done();hold.next())
				{
				checks.push_back(new JCheckBox((String)hold.get()));
				((JCheckBox)checks.back()).addItemListener(this);
				}
			}
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		for(checks.start();!checks.done();checks.next())
			{
			p.add((JCheckBox)checks.get());
			}
		return p;
		}
	
	public void itemStateChanged(ItemEvent e)
		{
		if(((JCheckBox)e.getItem()).isSelected())
			graphs.show(((JCheckBox)e.getItem()).getText());
		else 
			graphs.hide(((JCheckBox)e.getItem()).getText());

		graphs.updateUI();
		}
	private Color pickColor(int i)
		{
		Color list[] = new Color[10];
		
		list[1] = new Color(255,0,0);
		list[2] = new Color(0,255,0);
		list[3] = new Color(0,0,255);
		list[4] = new Color(145,0,0);
		list[5] = new Color(0,145,0);
		list[6] = new Color(0,0,145);
		list[7] = new Color(100,255,100);
		list[8] = new Color(255,100,100);
		list[9] = new Color(100,100,255);
		list[0] = new Color(100,100,100);
		
		return list[i];
		}
	}

package myCB.gui;

import myCB.stock.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.swing.event.*;

public class portfolioGUI implements myGUI
	{
	JFrame frame;
	Portfolio Collection;
	JList list;
	stockDataGUI h;
	JScrollPane scroller;
	JTextField command, saveThis, loadThis;
	JButton enter, delThis, update, refresh, save, load;
	String active;
	
	public portfolioGUI(Portfolio in)
		{
		Collection = in;
		frame = new JFrame();
		
		Collection.firstStock();
		h = new stockDataGUI(Collection.getStock());
		active = Collection.getStock().getTicker();
		
		command = new JTextField(10);
		saveThis = new JTextField(20);
		loadThis = new JTextField(20);
		enter = new JButton("Add Stock");
		delThis = new JButton("Remove This Stock");
		update = new JButton("Update All");
		refresh = new JButton("Refresh All");
		save = new JButton("Save");
		load = new JButton("Load");
		
		setList();
		
		update.addActionListener(new Update());
		refresh.addActionListener(new Refresh());
		enter.addActionListener(new editListener());
		delThis.addActionListener(new delListener());
		save.addActionListener(new saveListener());
		load.addActionListener(new loadListener());
		}
	
	protected void focusHead()
		{
		Collection.firstStock();
		h = new stockDataGUI(Collection.getStock());
		active = Collection.getStock().getTicker();
		draw();
		}
	
	public void changeFocus(String target)
		{
		active = target;
		h= new stockDataGUI(Collection.findStock(target));
		draw();
		}
	
	protected void setList()
		{
		list = Collection.heldStock();
		list.setVisibleRowCount(3);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scroller = new JScrollPane(list);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		list.addListSelectionListener(new scrollListener());
		}
	
	public void draw()
		{
		JPanel panel = new JPanel();
		JPanel other = new JPanel();
		JLabel spacer = new JLabel("   ");
		
		panel.add(enter);
		panel.add(command);
		panel.add(spacer);
		panel.add(delThis);		
		panel.add(spacer);
		panel.add(scroller);
		
		other.setLayout(new BoxLayout(other, BoxLayout.Y_AXIS));
		other.add(refresh);
		other.add(update);
		panel.add(other);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().removeAll();
		frame.getContentPane().add(BorderLayout.NORTH, controlPanel());
		frame.getContentPane().add(BorderLayout.CENTER, h.getPanel());
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		
		frame.setSize(600,700);
		frame.setVisible(true);
		}	
	public JPanel controlPanel()
		{
		JPanel p = new JPanel();
		
		p.add(save);
		if (Collection.hasTitle())
			{
			JLabel l = new JLabel(Collection.getTitle());
			p.add(l);
			}
		else
			{
			p.add(saveThis);
			}
		p.add(load);
		p.add(loadThis);
		
		return p;
		}
	public Component getPanel()
		{
		return frame.getContentPane();
		}
	
	class loadListener implements ActionListener
		{
		public void actionPerformed(ActionEvent event)
			{
			try
				{
				String hold = loadThis.getText().trim();
				if (hold.length() > 0)
					{
				
					ObjectInputStream os = new ObjectInputStream(new FileInputStream(hold+".sbro"));
				
					Collection = (Portfolio)(os.readObject());
			
					os.close();
					}
				setList();
				focusHead();
				}
			catch (Exception ex)
				{
				System.out.println("Error loading the file");
				}
			}
		}
	
	class saveListener implements ActionListener
		{
		public void actionPerformed(ActionEvent event)
			{
			String tag = null;
			
			if (Collection.hasTitle())
				tag = Collection.getTitle();
			else
				{
				try
					{
					String hold = saveThis.getText();
					hold = hold.trim();
					if (hold.length() > 0)
						{
						tag = hold;
						Collection.setTitle(tag);
						}
					}
				catch (Exception ex)
					{
					System.out.println("Can't render tag from input");
					}
				}
			if (tag != null)
				{
				try 
					{
					ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(tag+".sbro"));
			
					os.writeObject(Collection);
					os.close();
					}
				catch (Exception ex)
					{
					System.out.println("File couldn't be saved");
					}
				}
			setList();
			draw();
			}
		}
	
	class scrollListener implements ListSelectionListener
		{
		public void valueChanged(ListSelectionEvent event)
			{
			if (!event.getValueIsAdjusting())
				{
				String selection = (String)list.getSelectedValue();
				if (selection != active)
					changeFocus(selection);
				}
			}
		}
	
	class editListener implements ActionListener
		{
		public void actionPerformed(ActionEvent event)
			{
			try
				{
				Collection.addStock(command.getText());
				
				setList();
				
				changeFocus(command.getText());
				
				command.setText("");
				command.requestFocus();
				}
			catch (Exception ex)
				{
				System.out.println("Sorry, can't add stock");
				System.out.println("**---------------**");
				ex.printStackTrace();
				}
			}
		}
	
	class delListener implements ActionListener
		{
		public void actionPerformed(ActionEvent event)
			{
			Collection.removeStock(active);
			
			setList();
			
			focusHead();
			}
		}
	
	class Refresh implements ActionListener
		{
		public void actionPerformed(ActionEvent event)
			{
			Collection.reload();
			draw();
			}
		}
	
	class Update implements ActionListener
		{
		public void actionPerformed(ActionEvent event)
			{
			Collection.update();
			focusHead();
			}
		}
	}

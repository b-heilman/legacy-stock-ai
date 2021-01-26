package myCB.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import myCB.gui.element.*;
import myCB.stock.*;

public class stockDataGUI implements myGUI
	{
	JFrame frame;
	JTable text;
	JTextField command;
	JButton go;
	StockData data;
	JLabel 	High, Low, Open, Close, Vol, aVol, High52, Spacer,
			Low52, mCap, PE, EPS, Bid, Ask, Est, Stock, Notice, Result;
	
	public stockDataGUI(StockData in)
		{
		data = in;
		
		frame = new JFrame();
		command = new JTextField(10);
		go = new JButton("Calc");
		accessStock();
		
		draw();
		}
		
	public void accessStock()
		{
		System.out.println("(stockDataGUI) accessing");
		
		
		text = data.spreadSheet();
		
		Result = new JLabel("");
		Notice = new JLabel("Value to invest:");
		Spacer = new JLabel("   ");
		Stock = new JLabel("Ticker Symbol: "+data.getTicker());
		High = new JLabel("High: "+0); 
		Low = new JLabel("Low: "+0); 
		Open = new JLabel("Open: "+0); 
		Close = new JLabel("Close: "+0); 
		Vol = new JLabel("Vol: "+0); 
		aVol = new JLabel("Ave Vol: "+ 0); 
		High52 = new JLabel("52 High: "+ 0); 
		Low52 = new JLabel("52 Low: "+ 0); 
		mCap = new JLabel("mCap: "+ 0); 
		PE = new JLabel("P/E: "+ 0); 
		EPS = new JLabel("EPS: "+ 0); 
		Bid = new JLabel("Bid: "+ 0); 
		Ask = new JLabel("Ask: "+ 0); 
		Est = new JLabel("Est: "+ 0);
		}
//text.setFont(new Font("Courier",Font.PLAIN,10));
	private JPanel hist_Layout()
		{
		JScrollPane scroller = new JScrollPane(text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setPreferredSize(new Dimension(400, 300));
		
		JPanel histPanel = new JPanel();
		
		histPanel.add(scroller);
	
		return histPanel;
		}

	private JPanel data_Layout()
		{
		JPanel dataPanel = new JPanel();
	
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.add(High); 
		dataPanel.add(Low); 
		dataPanel.add(Open); 
		dataPanel.add(Close); 
		dataPanel.add(Vol); 
		dataPanel.add(Spacer);
		dataPanel.add(Spacer);
		dataPanel.add(Spacer);
		dataPanel.add(aVol); 
		dataPanel.add(High52); 
		dataPanel.add(Low52); 
		dataPanel.add(mCap); 
		dataPanel.add(PE); 
		dataPanel.add(EPS);
		dataPanel.add(Bid);
		dataPanel.add(Ask);
		dataPanel.add(Est);
		return dataPanel;
		}
	private JPanel io_Layout()
		{
		JPanel ioPanel = new JPanel();
		ioPanel.add(Notice);
		ioPanel.add(command);
		ioPanel.add(go);
		ioPanel.add(Spacer);
		ioPanel.add(Result);
		ioPanel.add(Spacer);
		ioPanel.add(Stock);
		
		return ioPanel;
		}
	private JPanel graph_Layout()
		{
		JPanel graphPanel = new JPanel();
		
		JTableGrapher graph = new JTableGrapher(text,1,60);
		graph.draw();
		Component hold = graph.getPanel();
		hold.setPreferredSize(new Dimension(300, 200));

		graphPanel.add(hold);
		
		return graphPanel;
		}
	public void draw()
		{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel combine1 = new JPanel();
		JPanel combine = new JPanel();
		
		combine.add(data_Layout());
		combine.add(hist_Layout());
		
		combine1.setLayout(new BoxLayout(combine1, BoxLayout.Y_AXIS));
		combine1.add(io_Layout());
		combine1.add(combine);
		combine1.add(graph_Layout());
		go.addActionListener(new doListener());
		
		frame.getContentPane().add(BorderLayout.CENTER, combine1);
		}
	public void show()
		{
		//frame.setSize(600, 500);
		//frame.setVisible(true);
		}	
	public Component getPanel()
		{
		return frame.getContentPane();
		}
	class doListener implements ActionListener
		{
		public void actionPerformed(ActionEvent event)
			{
			float temp;
			
			try
				{
				System.out.println("(stockDataGUI) actionPerformed");
				
				temp = Float.valueOf(command.getText())
									/
					   Float.valueOf(0);
				
				Result.setText("You can buy "+(int)temp+" shares");
				command.requestFocus();
				}
			catch (Exception ex)
				{
				// shit happens
				}
			}
		}
	}
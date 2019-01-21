package com.carl.MarkNow.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import com.carl.MarkNow.model.Text;

public class MainView extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CatalogPanel catalogPanel;
	private EditPanel editPanel;
	private DisplayPanel displayPanel;
	private MenuBar menuBar;
	
	public MainView(Text text) {
// initialize
		setTitle("MarkNow");
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setLayout(new BorderLayout(15, 15));
// icon
		ImageIcon icon=new ImageIcon("src/image/icon.png");
		this.setIconImage(icon.getImage());
		
    	try {
    	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
		
// menu bar
        menuBar = new MenuBar(text);
//
        catalogPanel = new CatalogPanel(text);
        catalogPanel.setPreferredSize(new Dimension(300, 150));
        editPanel = new EditPanel(text);
        editPanel.setPreferredSize(new Dimension(750, 150));
        displayPanel = new DisplayPanel(text);
        displayPanel.setPreferredSize(new Dimension(750, 150));

// bottom 
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));       
        JLabel statusLabel = new JLabel("Powerby 3160105684 bds");
        statusPanel.add(statusLabel);

        setJMenuBar(menuBar);
        add(statusPanel, BorderLayout.SOUTH);
        add(catalogPanel, BorderLayout.WEST);
        add(displayPanel, BorderLayout.EAST);
        add(editPanel, BorderLayout.CENTER);
        
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        pack();
        setVisible(true);
	}
}

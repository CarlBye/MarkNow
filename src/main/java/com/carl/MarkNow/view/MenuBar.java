package com.carl.MarkNow.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.carl.MarkNow.model.Text;
import com.carl.MarkNow.net.Server;

public class MenuBar extends JMenuBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Text text;
	private JMenuItem menuItem1;
	private JMenuItem menuItem2;
	private JMenuItem menuItem3;
	private JMenuItem menuItem4;
	private JMenuItem menuItem5;
	private JMenuItem menuItem6;
	
	public MenuBar(Text t) {
		text = t;
		
        JMenu menu1 = new JMenu("File");
        
        menuItem1 = new JMenuItem("Open");
        menuItem1.addActionListener( new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				MenuBar.this.openFile();
			}
		} );
        	
        menuItem2 = new JMenuItem("Save");
        menuItem2.addActionListener( new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				MenuBar.this.saveFile();
			}
		} );
        	
        menuItem3 = new JMenuItem("Export");
        menuItem3.addActionListener( new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				MenuBar.this.exportFile();
			}
		} );
        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu1.add(menuItem3);
        
        JMenu menu2 = new JMenu("Collaboration");
        
        menuItem4 = new JMenuItem("New Server");
        menuItem4.addActionListener( new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				try {
					final int serverPort = Integer.parseInt(JOptionPane.showInputDialog("Please input server port"));
					(new Thread() {
						public void run() {
							try {
								Server server = new Server(serverPort);
								server.start();
							} catch (IOException e) {
								JOptionPane.showMessageDialog(null, "New a server failed");
								e.printStackTrace();
							}
						}
					}).start();
					JOptionPane.showMessageDialog(null, "New a server successfully");
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Invalid port");
				}							
			}
		} );
        
        menuItem5 = new JMenuItem("Connect To Server");
        menuItem5.addActionListener( new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				try {
					String connectIp = JOptionPane.showInputDialog("Please input IP you want to connect");
					int connectPort = Integer.parseInt(JOptionPane.showInputDialog("Please input port you want to connect"));
					try {
						text.getClient().connectToServer(connectIp, connectPort);
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "No such port");
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Connect failed");
						return;
					}
					JOptionPane.showMessageDialog(null, "Connect successfully");
				} catch (NumberFormatException e2){
					JOptionPane.showMessageDialog(null, "Invalid port");
				}
			}
		} );
        
        menuItem6 = new JMenuItem("Disconnect");
        menuItem6.addActionListener( new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				try {
					text.getClient().disconnect();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} );
        
        menu2.add(menuItem4);
        menu2.add(menuItem5);
        menu2.add(menuItem6);
        
        add(menu1);
        add(menu2);
	}
	
	private void openFile() {
		int value = JOptionPane.showConfirmDialog(null, "Do you want to save?", "MarkNow", 0);
		if(value == 0) {
			saveFile();
		}			
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open File");
			chooser.showOpenDialog(null);
			File file = chooser.getSelectedFile();
			
			if(file==null){
				JOptionPane.showMessageDialog(null, "Nothing selected!");
			}
			else {
				@SuppressWarnings("resource")
				BufferedReader bReader = new BufferedReader(new FileReader(file));
				StringBuffer sBuffer = new StringBuffer();
				String str;
				while((str=bReader.readLine()) != null) {
					sBuffer.append(str+'\n');
				}
				text.setTextContent(sBuffer.toString());
				text.getEditPanel().getTextArea().setText(text.getTextContent());
			}
			
		} 
		catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null,"Open Failed!");
		}
    }
	
    private void saveFile() {
        JFileChooser chooser=new JFileChooser();
        chooser.setDialogTitle("Save File");
        chooser.showSaveDialog(null);        
        File file = chooser.getSelectedFile();
        
        if(file == null) {
        	JOptionPane.showMessageDialog(null,"Nothing selected!");
        }
        else {
            try {            	
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                fos.write(text.getTextContent().getBytes(StandardCharsets.UTF_8));
                fos.close();
                JOptionPane.showMessageDialog(null,"Save Successfully!");
            }
            catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Save Failed!");
            }
        }
       
    }
    
	private void exportFile() {
        JFileChooser chooser=new JFileChooser();
        chooser.setDialogTitle("Export File");
        chooser.showSaveDialog(null);        
        File file = chooser.getSelectedFile();
        
        if(file == null) {
        	JOptionPane.showMessageDialog(null,"Nothing selected!");
        }
        else {
            try {
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                fos.write(text.toHtml().getBytes(StandardCharsets.UTF_8));
                fos.close();
                JOptionPane.showMessageDialog(null,"Export Successfully!");            	
            }
            catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Export Failed!");
            }
        }
       
    }
}

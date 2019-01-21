package com.carl.MarkNow.view;

import java.awt.BorderLayout;

//import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
//import javax.swing.event.CaretEvent;
//import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.carl.MarkNow.model.Text;

public class EditPanel extends JPanel {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Text text;
	private int caretPosition;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	public EditPanel(Text t) {
		caretPosition = 0;
		setLayout(new BorderLayout());
		text = t;
		text.setEditPanel(this);
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		textArea.getDocument().addDocumentListener( new DocumentListener() {	
			
			public void insertUpdate(DocumentEvent e) {
				text.setTextContent(textArea.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				text.setTextContent(textArea.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				text.setTextContent(textArea.getText());
			}			
			
		} );	
		
		textArea.addCaretListener( new CaretListener() {

			public void caretUpdate(CaretEvent e) {
				caretPosition = e.getDot();
			}
			
		} );
		
		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
	}
	
	public void setText(String t) {
		textArea.setText(t);
		try {
			textArea.setCaretPosition(caretPosition);
		} catch (IllegalArgumentException e) {
			textArea.setCaretPosition(textArea.getDocument().getLength());
	    }
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
}

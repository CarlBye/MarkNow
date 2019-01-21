package com.carl.MarkNow.model;

import java.util.concurrent.Semaphore;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.carl.MarkNow.net.Client;
import com.carl.MarkNow.view.CatalogPanel;
import com.carl.MarkNow.view.DisplayPanel;
import com.carl.MarkNow.view.EditPanel;

public class Text {
	
	private DisplayPanel displayPanel;
	private EditPanel editPanel;
	private CatalogPanel catalogPanel;
	private Client client;
	private String content;
	private Semaphore mutex;
	private boolean isLocked;

	public Text() {
	    content = "";
	    mutex = new Semaphore(1);
	    isLocked = false;
	}
	
	public void setTextContent(String c) {
		if (!isLocked) {
			try {
				mutex.acquire(1);
				content = c;
				client.commit();
				catalogPanel.updateTree();
				displayPanel.update();		
			} catch (InterruptedException e) {
				
			} finally {
				mutex.release(1);
			}
		}
	}
	
	public void updateFromServer(String c) {
		try {
			isLocked = true;
			mutex.acquire(1);
			content = c;
			catalogPanel.updateTree();
			displayPanel.update();
			editPanel.setText(content);
		} catch (InterruptedException e) {
			
		} finally {
			mutex.release(1);
			isLocked = false;
		}
	}
	
	public String toHtml() {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(content);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String html = renderer.render(document);
		return html;
	}

	public void setCatalogPanel(CatalogPanel cPanel) {
		catalogPanel = cPanel;
	}
	
	public void setEditPanel(EditPanel ePanel) {
		editPanel = ePanel;
	}

	public void setDisplayPanel(DisplayPanel dPanel) {
		displayPanel = dPanel;
	}
	
	public void setClient(Client c) {
		client = c;
	}
	
	public String getTextContent() {
		return this.content;
	}
	
	public EditPanel getEditPanel() {
		return editPanel;
	}
	
	public Client getClient() {
		return client;
	}
}

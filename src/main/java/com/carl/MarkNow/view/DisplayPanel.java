package com.carl.MarkNow.view;

import com.carl.MarkNow.model.Text;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class DisplayPanel extends JFXPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Text text;
	private WebView browser;
	
	public DisplayPanel(Text t) {
		text = t;
		text.setDisplayPanel(this);
		resetScene();
	}
	
	public void update() {
		Platform.runLater(new Runnable() {			
			public void run() {
				browser.getEngine().loadContent(text.toHtml());
			}			
		} );
	}
	
	private void resetScene() {
		Platform.runLater(new Runnable() {			
			public void run() {
				browser = new WebView();
				DisplayPanel.this.setScene(new Scene(browser));
			}			
		});
	}
}

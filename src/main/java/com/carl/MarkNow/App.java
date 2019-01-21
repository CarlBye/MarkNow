package com.carl.MarkNow;

import com.carl.MarkNow.model.Text;
import com.carl.MarkNow.net.Client;
import com.carl.MarkNow.view.MainView;

public class App 
{
    public static void main( String[] args )
    {
    	Text text = new Text();
    	@SuppressWarnings("unused")
		Client client = new Client(text);
    	@SuppressWarnings("unused")
		MainView mainView = new MainView(text);
    }
}

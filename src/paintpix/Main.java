package paintpix;

import java.awt.*;
import javax.swing.*;

import managers.DatabaseManager;

// entry point class
public class Main {
	
	public static void main(String[] args) {
		
	    EventQueue.invokeLater(new Runnable() {
	    	public void run() {
	    		DatabaseManager.connect("recent_files.db");
	        	try{
	        		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        	}catch (Exception e){

	        	}
	            MainFrame frame = new MainFrame();
	            frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
	    });
	}
}


package gui;
import javax.swing.SwingUtilities;

public class App {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {//run swing app in a special thread, more robust and approved manner 
			public void run() {
				new MainFrame();
			}
		});
				
	}

}

package gui;
import javax.swing.SwingUtilities;

public class App {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {//run swing app in a special thread, more robust and approved manner 
			public void run() {
				new MainFrame();
				System.out.println("Line 1");
				System.out.println("Line 2");
				System.out.println("Line 3");				
				System.out.println("Line 4");
				System.out.println("Line 5");
				System.out.println("Line 6");								
			}
		});
				
	}

}

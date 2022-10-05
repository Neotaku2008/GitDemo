package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import controller.Controller;

public class MainFrame extends JFrame {

	private Toolbar toolbar;
	private TextPanel textPanel;		
	private FormPanel formPanel;
	private JFileChooser fileChooser;
	private Controller controller;
	private TablePanel tablePanel;
	private PrefsDialog prefsDialog;
	private Preferences prefs;
		
	public MainFrame() {
		
		super("Demo Program");
		
		setLayout(new BorderLayout());
		
		toolbar = new Toolbar();
		textPanel = new TextPanel();		
		formPanel = new FormPanel();
		tablePanel = new TablePanel();		
		prefsDialog = new PrefsDialog(this);
		
		prefs = Preferences.userRoot().node("db");
		
		controller = new Controller();
		
		tablePanel.setData(controller.getPeople());
		
		tablePanel.setPersonTableListener(new PersonTableListener() {
			public void rowDeleted (int row) {
				controller.removePerson(row);
			}
		});
		
		prefsDialog.setPrefsListener(new PrefsListener() {
			public void preferencesSet(String user, String password, int port) {
				//System.out.println(user + ": " + password);
				prefs.put("user", user);
				prefs.put("password", password);
				prefs.putInt("port", port);
			}			
		});
				
		prefsDialog.setDefaults(prefs.get("user",""), prefs.get("password",""), prefs.getInt("port",3306));
		
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new PersonFileFilter());
				
		setJMenuBar(createMenubar());
		
		toolbar.setStringListener(new StringListener() {
			public void textEmitted(String text) {
				textPanel.appendText(text);
			}			
		});
		
		formPanel.setFormListener(new FormListener() {
			public void formEventOcurred(FormEvent e) {			
				controller.addPerson(e);
				tablePanel.refresh();
			}
		});
				
		add(formPanel, BorderLayout.WEST);		
		add(toolbar, BorderLayout.NORTH);		
		add(tablePanel, BorderLayout.CENTER);
				
		setMinimumSize(new Dimension(500, 400));//Minimum size for the main frame window
		setSize(600,500);//set window size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Close app when the "x" button is clicked
		setVisible(true);		
	}
	
	private JMenuBar createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem exportDataItem = new JMenuItem("Export Data...");
		JMenuItem importDataItem = new JMenuItem("Import Data...");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		fileMenu.add(exportDataItem);
		fileMenu.add(importDataItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
				
		JMenu windowMenu = new JMenu("Window");		
		JMenu showMenu = new JMenu("Show");	
		JMenuItem prefsItem = new JMenuItem("Preferences...");
						
		JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
		showFormItem.setSelected(true);
		
		showMenu.add(showFormItem);		
		windowMenu.add(showMenu);
		windowMenu.add(prefsItem);
				
		menuBar.add(fileMenu);
		menuBar.add(windowMenu);
		
		prefsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prefsDialog.setVisible(true);
			}			
		});
				
		showFormItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ev.getSource();
				formPanel.setVisible(menuItem.isSelected());
			}			
		});
		
		fileMenu.setMnemonic(KeyEvent.VK_F);//Virtual Key F -- Mnemonic
		exitItem.setMnemonic(KeyEvent.VK_X);//Virtual Key X -- Mnemonic
		
		prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));		
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));		
		importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		
		importDataItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						controller.loadFromFile(fileChooser.getSelectedFile());						
						tablePanel.refresh();						
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file", "Error", JOptionPane.ERROR_MESSAGE);						
					}
				}
			}			
		});
		
		exportDataItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						controller.saveToFile(fileChooser.getSelectedFile());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not save data from file", "Error", JOptionPane.ERROR_MESSAGE);						
					}
				}
			}			
		});		
		
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to exit?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);				
				if (action == JOptionPane.OK_OPTION) {
					System.exit(0);
				}								
			}
		});
		
		return menuBar;
	}
	
}
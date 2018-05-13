package com.usmcchaserlauncher.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import com.usmcchaserlauncher.controller.USMCChaserLauncherController;
import com.usmcchaserlauncher.model.ServerDetails;
import com.usmcchaserlauncher.model.VersionInfoResponse;
import com.usmcchaserlauncher.view.components.JNumberTextField;

import net.miginfocom.swing.MigLayout;


public class LauncherView
{
	private JFrame frmUsmcChaserLauncher;
	private JTextField chaserDemo149PathTextField;
	private JTextField chaserFullPathTextField;
	private JTextField chaserDemo144PathTextField;
	private JTextField connectIP;
	private JTextField connectPort;
	private final JRadioButton rdbtnFull = new JRadioButton("Full");
	private final JRadioButton rdbtnDemo149 = new JRadioButton("Demo 1.49");
	private final JRadioButton rdbtnDemo144 = new JRadioButton("Demo 1.44");
	private JTable table;
	private JTextField versionTextField;
	private JTextField txtWwwusmcchasercom;
	private JTextField txtJohnLenhart;
	private JTabbedPane tabbedPane;
	private JButton refreshServerBrowserBtn;
	
	private USMCChaserLauncherController launcherController;
	private String versionInfoEndpoint= "http://www.usmcchaser.com/ServerQuery/USMCChaserLauncher.php";
	private String serverQueryEndpoint = "http://www.usmcchaser.com/ServerQuery/ChaserServers.php";
	
	private static final String VERSION;
    private static final String[] SERVER_TABLE_COLUMNS;
    private static final String CHASER_DEMO_149_PATH_CONFIG_KEY;
    private static final String CHASER_FULL_PATH_CONFIG_KEY;
    private static final List<String> CHASER_DEMO_PATHS_TO_SEARCH;
    private static final List<String> CHASER_FULL_PATHS_TO_SEARCH;
    private static final Logger logger;
    
    static
    {
    	logger= Logger.getLogger(LauncherView.class);
    	VERSION = "1.0.0";
    	SERVER_TABLE_COLUMNS = new String[] {"IP", "Port", "Hostname", "Version", "Map", "Type", "Players", "Max Players", "Password", "Game"};
    	CHASER_DEMO_149_PATH_CONFIG_KEY = "app.paths.chaser-demo149";
    	CHASER_FULL_PATH_CONFIG_KEY = "app.paths.chaser-full";
    	
    	CHASER_DEMO_PATHS_TO_SEARCH = new ArrayList<String>();
    	CHASER_DEMO_PATHS_TO_SEARCH.add("Program Files (x86)" + File.separator + "Jowood" + File.separator + "Chaser MP Demo" + File.separator +"Chaser.exe");
    	CHASER_DEMO_PATHS_TO_SEARCH.add("Program Files" + File.separator + "Jowood" + File.separator + "Chaser MP Demo" + File.separator +"Chaser.exe");
    	CHASER_DEMO_PATHS_TO_SEARCH.add("Chaser MP Demo" + File.separator +"Chaser.exe");
    	CHASER_DEMO_PATHS_TO_SEARCH.add("Chaser" + File.separator +"Chaser MP Demo" + File.separator +"Chaser.exe");
    	
    	CHASER_FULL_PATHS_TO_SEARCH = new ArrayList<String>();
    	CHASER_FULL_PATHS_TO_SEARCH.add("Program Files (x86)" + File.separator + "Jowood" + File.separator + "Chaser" + File.separator +"Chaser.exe");
    	CHASER_FULL_PATHS_TO_SEARCH.add("Program Files" + File.separator + "Jowood" + File.separator + "Chaser" + File.separator +"Chaser.exe");
    	CHASER_FULL_PATHS_TO_SEARCH.add("Chaser" + File.separator +"Chaser.exe");
    }
    
	public LauncherView(String[] args)
	{
		initialize(args);
	}
	
	public void setVisible()
	{
		frmUsmcChaserLauncher.setVisible(true);
	}

	private void initialize(String[] args)
	{
		launcherController = new USMCChaserLauncherController();
		
		frmUsmcChaserLauncher = new JFrame();
		frmUsmcChaserLauncher.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) 
			{
				onWindowLoaded();
			}
		});
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex){
			showErrorDialog(frmUsmcChaserLauncher, "Error setting look and feel.", ex);
		}
		
		frmUsmcChaserLauncher.setIconImage(Toolkit.getDefaultToolkit().getImage(LauncherView.class.getResource("/favicon.png")));
		frmUsmcChaserLauncher.setTitle("USMC Chaser Launcher");
		frmUsmcChaserLauncher.setBounds(100, 100, 850, 265);
		frmUsmcChaserLauncher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUsmcChaserLauncher.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmUsmcChaserLauncher.getContentPane().add(tabbedPane);
		
		JPanel jplInnerPanel1 = createServerBrowserInnerTabPanePanel("Tab 1 Contains Tooltip and Icon");
		tabbedPane.addTab("Server Browser", null, jplInnerPanel1);
		tabbedPane.setSelectedIndex(0);
		JPanel jplInnerPanel2 = createSettingsInnerTabPanePanel("Tab 2 Contains Icon only");
		tabbedPane.addTab("Settings", null, jplInnerPanel2);
		
		//Initialize fields from properties
		try
		{
			Properties prop = launcherController.loadAppProperties();
			chaserFullPathTextField.setText(prop.getProperty(CHASER_FULL_PATH_CONFIG_KEY));
			chaserDemo149PathTextField.setText(prop.getProperty(CHASER_DEMO_149_PATH_CONFIG_KEY));
			chaserDemo144PathTextField.setText(prop.getProperty("app.paths.chaser-demo144"));
			if(prop.contains("app.server-version-endpoint"))
				versionInfoEndpoint = prop.getProperty("app.server-version-endpoint");
			if(prop.contains("app.server-query-endpoint"))
				serverQueryEndpoint = prop.getProperty("app.server-query-endpoint");
		}catch(Exception ex){
			showErrorDialog(frmUsmcChaserLauncher, "Error loading config.properties file", ex);
		}

		JPanel helpTabJPanel = new JPanel();
		tabbedPane.addTab("Help", null, helpTabJPanel, null);
		helpTabJPanel.setLayout(new GridLayout(0, 1));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		helpTabJPanel.add(panel);
		panel.setLayout(new MigLayout("", "[39px][206px]", "[20px][][]"));
		
		JLabel lblVersion = new JLabel("Version:");
		panel.add(lblVersion, "cell 0 0,alignx left,aligny center");
		
		versionTextField = new JTextField();
		versionTextField.setText(VERSION);
		panel.add(versionTextField, "cell 1 0");
		versionTextField.setEditable(false);
		versionTextField.setColumns(25);
		
		JLabel lblWebsite = new JLabel("Website:");
		panel.add(lblWebsite, "cell 0 1,alignx left,aligny center");
		
		txtWwwusmcchasercom = new JTextField();
		panel.add(txtWwwusmcchasercom, "cell 1 1");
		txtWwwusmcchasercom.setText("www.usmcchaser.com");
		txtWwwusmcchasercom.setEditable(false);
		txtWwwusmcchasercom.setColumns(25);
		
		JLabel lblCreator = new JLabel("Creator: ");
		panel.add(lblCreator, "cell 0 2");
		
		txtJohnLenhart = new JTextField();
		panel.add(txtJohnLenhart, "cell 1 2");
		txtJohnLenhart.setText("John Lenhart");
		txtJohnLenhart.setEditable(false);
		txtJohnLenhart.setColumns(25);

		//Join automatically if given command args
		if(args.length == 4)
		{
			String protocol = args[0];
			String serverIPToJoin = args[1];
			String serverPortToJoin = args[2];
			//String serverMapToJoin = args[3];
			//boolean isCalledFromCommandLine = true;
			
			try
			{
				String chaserPath = null;
				if(protocol.equalsIgnoreCase("chaserfull"))
					chaserPath = launcherController.getAppProperty(CHASER_FULL_PATH_CONFIG_KEY);
				else if(protocol.equalsIgnoreCase("chaserdemo149"))
					chaserPath = launcherController.getAppProperty(CHASER_DEMO_149_PATH_CONFIG_KEY);
				else if(protocol.equalsIgnoreCase("chaserdemo144"))
					chaserPath = launcherController.getAppProperty("app.paths.chaser-demo144");
				
				if(connectToChaserServer(null, chaserPath, serverIPToJoin, serverPortToJoin)){
					logger.info("Chaser started successfully in command line mode. Application is exiting...");
					System.exit(0);
				}
			}catch(Exception ex){
				logger.error("Error getting properties from config.properties file", ex);
			}
		}
	}
	
	private void onWindowLoaded()
	{
		try
		{
			//Find installed Chasers
			String chaserFullPath = launcherController.getAppProperty(CHASER_FULL_PATH_CONFIG_KEY);
			findChaserExecutable(false, chaserFullPath, CHASER_FULL_PATHS_TO_SEARCH, CHASER_FULL_PATH_CONFIG_KEY, chaserFullPathTextField);
			
			String chaserDemo149Path = launcherController.getAppProperty(CHASER_DEMO_149_PATH_CONFIG_KEY);
			findChaserExecutable(false, chaserDemo149Path, CHASER_DEMO_PATHS_TO_SEARCH, CHASER_DEMO_149_PATH_CONFIG_KEY, chaserDemo149PathTextField);
		}catch(Exception ex){
			logger.error("Error finding Chaser.exe", ex);
		}

		//On startup check for new version
		new SwingWorker<VersionInfoResponse, Void>()
		{
			@Override
			protected VersionInfoResponse doInBackground() throws Exception
			{
				return launcherController.getVersionInfo(versionInfoEndpoint);
			}
			
			@Override
			protected void done()
			{
				try
				{
					VersionInfoResponse  currentVersion = get();
					if(!currentVersion.getVersion().equalsIgnoreCase(VERSION))
					{
						versionTextField.setText(VERSION + " (Outdated - Please update)");
						if(currentVersion.getForceUpdate())
						{
							JOptionPane.showMessageDialog(frmUsmcChaserLauncher, "There is an updated verion of this launcher available, you MUST update to use this software: " + currentVersion.getDescription(), "Mandatory Update Available", JOptionPane.INFORMATION_MESSAGE);
							logger.info("Closing application due to forced update.");
							System.exit(0);
						}else{
							JOptionPane.showMessageDialog(frmUsmcChaserLauncher, "There is an updated version of this launcher available: " + currentVersion.getDescription(), "Update Available", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} catch (Exception ex)
				{
					showErrorDialog(frmUsmcChaserLauncher, "Error getting version information. Please check your network connection. The application will be aborted now.", ex);
					logger.info("Stop launcher because no internet connection was found.", ex);
					System.exit(0);
				}
			}
		}.execute();

		//Make a timer to update the server list
        Timer timer = new Timer();
        timer.schedule(new TimerTask() 
        {
            @Override
            public void run() 
            {
            	setRefreshBtnEnabled(false);
				new ServerListUpdaterWorker().execute();
            }
        }, 0, 15 * 1000);
	}
	
	protected JPanel createSettingsInnerTabPanePanel(String text) 
	{
		JPanel settingsJPlPanel = new JPanel();
		settingsJPlPanel.setLayout(new GridLayout(0, 1));
		
		JPanel panel_1 = new JPanel();
		settingsJPlPanel.add(panel_1);
		panel_1.setBorder(new TitledBorder(null, "Chaser Full", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(new MigLayout("", "[606px][61px][65px]", "[23px]"));
		
		chaserFullPathTextField = new JTextField();
		panel_1.add(chaserFullPathTextField, "pushx, growx");
		chaserFullPathTextField.setEditable(false);
		chaserFullPathTextField.setColumns(75);
		
		JButton button = new JButton("Select");
		panel_1.add(button, "cell 1 0,alignx left,aligny top");
		
		JButton btnSearch = new JButton("Search");
		panel_1.add(btnSearch, "cell 2 0,alignx left,aligny top");
		
		JPanel panel = new JPanel();
		settingsJPlPanel.add(panel);
		panel.setBorder(new TitledBorder(null, "Chaser Demo 1.49", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new MigLayout("", "[606px][61px][65px]", "[23px]"));
		
		chaserDemo149PathTextField = new JTextField();
		panel.add(chaserDemo149PathTextField, "pushx, growx");
		chaserDemo149PathTextField.setEditable(false);
		chaserDemo149PathTextField.setColumns(75);
		
		JButton btnSelect = new JButton("Select");
		panel.add(btnSelect, "cell 1 0,alignx left,aligny top");
		
		JButton button_2 = new JButton("Search");
		panel.add(button_2, "cell 2 0,alignx left,aligny top");
		
		JPanel panel_2 = new JPanel();
		settingsJPlPanel.add(panel_2);
		panel_2.setBorder(new TitledBorder(null, "Chaser Demo 1.44", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setLayout(new MigLayout("", "[606px][61px][65px]", "[23px]"));
		
		chaserDemo144PathTextField = new JTextField();
		panel_2.add(chaserDemo144PathTextField, "pushx, growx");
		chaserDemo144PathTextField.setEnabled(false);
		chaserDemo144PathTextField.setEditable(false);
		chaserDemo144PathTextField.setColumns(75);
		
		JButton button_1 = new JButton("Select");
		panel_2.add(button_1, "cell 1 0,alignx left,aligny top");
		button_1.setEnabled(false);
		
		JButton button_3 = new JButton("Search");
		panel_2.add(button_3, "cell 2 0,alignx left,aligny top");
		button_3.setEnabled(false);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					String chaserDemo149Path = launcherController.getAppProperty(CHASER_DEMO_149_PATH_CONFIG_KEY);
					findChaserExecutable(true, chaserDemo149Path, CHASER_DEMO_PATHS_TO_SEARCH, CHASER_DEMO_149_PATH_CONFIG_KEY, chaserDemo149PathTextField);
				}catch(Exception ex){
					showErrorDialog(frmUsmcChaserLauncher, "Error getting properties from config.properties file", ex);
				}
			}
		});
		
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String path = showFileDialog(frmUsmcChaserLauncher, "Chaser.exe (Chaser.exe)", "Find and select Chaser.exe for the 1.49 demo", "Chaser.exe");
					if(path != null)
					{
						launcherController.setAppProperty(CHASER_DEMO_149_PATH_CONFIG_KEY, path);
						chaserDemo149PathTextField.setText(path);
					}
				}catch(Exception ex){
					showErrorDialog(frmUsmcChaserLauncher, "Error getting properties from config.properties file", ex);
				}
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					String chaserFullPath = launcherController.getAppProperty(CHASER_FULL_PATH_CONFIG_KEY);
					findChaserExecutable(true, chaserFullPath, CHASER_FULL_PATHS_TO_SEARCH, CHASER_FULL_PATH_CONFIG_KEY, chaserFullPathTextField);
				}catch(Exception ex){
					showErrorDialog(frmUsmcChaserLauncher, "Error getting properties from config.properties file", ex);
				}
				
			}
		});
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try
				{
					String path = showFileDialog(frmUsmcChaserLauncher, "Chaser.exe (Chaser.exe)", "Find and select Chaser.exe for the full version", "Chaser.exe");
					if(path != null)
					{
						launcherController.setAppProperty(CHASER_FULL_PATH_CONFIG_KEY, path);
						chaserFullPathTextField.setText(path);
					}
				}catch(Exception ex){
					showErrorDialog(frmUsmcChaserLauncher, "Error setting property in config.properties file", ex);
				}
			}
		});
		
		return settingsJPlPanel;
	}
	
	
	protected JPanel createServerBrowserInnerTabPanePanel(String text) 
	{
		JPanel serverBrowserJPlPanel = new JPanel();
		serverBrowserJPlPanel.setLayout(new GridLayout(0, 1));
		ButtonGroup bG = new ButtonGroup();
        
		DefaultTableModel model = null;
		model = new DefaultTableModel();
		model.setColumnIdentifiers(SERVER_TABLE_COLUMNS);

		table = new JTable();
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				serverTableCellChanged();
			}
		});

		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent mouseEvent)
			{
				JTable table = (JTable) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1)
				{
					serverTableCellChanged();
					connectBtnClicked();
				}
			}
		});
		
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		TableColumnModel colModel=table.getColumnModel();
		//colModel.getColumn(1).setPreferredWidth(150);    
		colModel.getColumn(1).setPreferredWidth(50);    
		colModel.getColumn(2).setPreferredWidth(150);
		colModel.getColumn(5).setPreferredWidth(50);
		colModel.getColumn(6).setPreferredWidth(50);
		table.repaint();
		
		refreshServerBrowserBtn = new JButton("Refresh");
		refreshServerBrowserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton refreshServerBrowserBtn = (JButton)e.getSource();
				refreshServerBrowserBtn.setText("Loading...");
				refreshServerBrowserBtn.setEnabled(false);
				new ServerListUpdaterWorker().execute();
			}
		});

        JLabel l = new JLabel();
        l.setText("Loading...");
        l.setPreferredSize(l.getSize());
        l.setBounds(frmUsmcChaserLauncher.getWidth()/2 - 20, 100, 50, 20);
        l.setOpaque(true);
        l.setBackground(Color.WHITE);
        
        
		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		JPanel panel_3_1 = new JPanel();
		panel_3_1.setBorder(new TitledBorder(null, "Join Server", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		serverBrowserJPlPanel.add(panel_3_1);
		panel_3_1.setLayout(new MigLayout("", "[394px]", "[129px][]"));
		panel_3_1.add(tableScrollPane, "wrap, push, grow, span");
		panel_3_1.add(refreshServerBrowserBtn, "alignx right, wrap, span");
		Dimension d = table.getPreferredSize();
		tableScrollPane.setPreferredSize( new Dimension(d.width, table.getRowHeight() * 6));
		
		JLabel lblIp = new JLabel("IP:");
		panel_3_1.add(lblIp, "split 8");
		
		connectIP = new JNumberTextField();
		panel_3_1.add(connectIP);
		connectIP.setHorizontalAlignment(SwingConstants.LEFT);
		connectIP.setColumns(12);

		JLabel lblPort = new JLabel("Port: ");
		panel_3_1.add(lblPort);
		
		connectPort = new JNumberTextField();
		panel_3_1.add(connectPort);
		connectPort.setColumns(6);
		bG.add(rdbtnFull);
		panel_3_1.add(rdbtnFull);
		panel_3_1.add(rdbtnDemo149);
		rdbtnDemo149.setSelected(true);
		bG.add(rdbtnDemo149);
		panel_3_1.add(rdbtnDemo144);
		
				rdbtnDemo144.setEnabled(false);
				bG.add(rdbtnDemo144);
		
		JButton btnJoin = new JButton("Connect");
		panel_3_1.add(btnJoin);
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				connectBtnClicked();
			}

		});
		return serverBrowserJPlPanel;
	}
	
    private Object[][] getServerTableData()
	{
    	Object[][] tableData = null; 
    	try
    	{
	    	List<ServerDetails> details = launcherController.loadChaserServers(serverQueryEndpoint);
	    	
	    	tableData = new Object[details.size()][10]; 
	    	for(int i = 0; i < details.size(); i++)
	    	{
	    		ServerDetails detail = details.get(i);
	    		Object[] row = {detail.getIp(), detail.getPort(), detail.getHostname(), detail.getGameVersion(), detail.getMap(), detail.getGameType(), detail.getPlayerCount(), detail.getMaxPlayerCount(), detail.getPassword(), detail.getGame()};
	    		tableData[i] = row;
	    	}
    	}catch (Exception ex) {
    		showErrorDialog(frmUsmcChaserLauncher, "Error getting server table data.", ex);
		}
    	
       return tableData;
	}
    
    private void serverTableCellChanged()
    {
		if(table.getSelectedRow() == -1)
			return;
		 String server = (String) table.getValueAt(table.getSelectedRow(), 0);
		 Integer port = (Integer) table.getValueAt(table.getSelectedRow(), 1);
		 String game =  (String) table.getValueAt(table.getSelectedRow(), 9);
		 
		 connectIP.setText(server);
		 connectPort.setText(Integer.toString(port));
		 rdbtnDemo149.setSelected(game.equals("chaserd"));
		 rdbtnFull.setSelected(!game.equals("chaserd"));
		 //rdbtnDemo144.setSelected(b);
    }
	

	private void connectBtnClicked()
	{		
		try
		{
			String chaserPath = null;
			if(rdbtnFull.isSelected())
				chaserPath = launcherController.getAppProperty(CHASER_FULL_PATH_CONFIG_KEY);
			else if(rdbtnDemo149.isSelected())
				chaserPath = launcherController.getAppProperty(CHASER_DEMO_149_PATH_CONFIG_KEY);
			else if(rdbtnDemo149.isSelected())
				chaserPath = launcherController.getAppProperty("app.paths.chaser-demo144");
			
			String ip = connectIP.getText();
			String port = connectPort.getText();
			
			connectToChaserServer(frmUsmcChaserLauncher, chaserPath, ip, port);
		}catch(Exception ex){
			showErrorDialog(frmUsmcChaserLauncher, "Error getting property in config.properties file", ex);
		}
	}
	
	private boolean connectToChaserServer(JFrame frame, String chaserPath, String ip, String port)
	{

		if(chaserPath == null || chaserPath.trim().isEmpty()){
			JOptionPane.showMessageDialog(frame, "You must select your Chaser.exe to join first.", "Error", JOptionPane.ERROR_MESSAGE);
			tabbedPane.setSelectedIndex(1);
			return false;
		}
		if(ip == null || ip.trim().isEmpty()){
			JOptionPane.showMessageDialog(frame, "You must enter the IP of the server you wish to join.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(port == null || port.trim().isEmpty()){
			JOptionPane.showMessageDialog(frame, "You must enter the Port of the server you wish to join.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		chaserPath = chaserPath.trim();
		try
		{
			ProcessBuilder pb = new ProcessBuilder(chaserPath, "-connect", ip.trim() + ":" + port.trim());
			pb.directory(new File(chaserPath.substring(0, chaserPath.lastIndexOf("Chaser.exe"))));
			pb.start();
			return true;
		} catch (IOException ex)
		{
			showErrorDialog(frmUsmcChaserLauncher, "Unable to start Chaser.exe.", ex);
		}
		return false;
	}

	private void findChaserExecutable(boolean showDialogs, String chaserPath, List<String> pathsToSearch, String propertyToSet, JTextField fieldToSet)
	{
		
		if(chaserPath == null || chaserPath.trim().isEmpty())
		{
			String path = launcherController.findExecutable(pathsToSearch);
			if(path == null)
			{
				if(showDialogs)
					JOptionPane.showMessageDialog(frmUsmcChaserLauncher, "Unable to find Chaser.exe, please find it manually.", "Warning", JOptionPane.WARNING_MESSAGE);
			}else
			{
				try
				{
					fieldToSet.setText(path);
					launcherController.setAppProperty(propertyToSet, path);
					if(showDialogs)
						JOptionPane.showMessageDialog(frmUsmcChaserLauncher, "Chaser.exe was found successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
				}catch(Exception ex){
					if(showDialogs)
						showErrorDialog(frmUsmcChaserLauncher, "Error setting property in config.properties file", ex);
					else
						logger.error("Error setting property in config.properties file", ex);
				}
			}
		}else{
			if(showDialogs)
					JOptionPane.showMessageDialog(frmUsmcChaserLauncher, "Chaser.exe was already set.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private class ServerListUpdaterWorker extends SwingWorker<Object[][], Void>
	{
		/*Schedule a compute-intensive task in a background thread*/
		@Override
		protected Object[][] doInBackground() throws Exception
		{
			Object[][] data = getServerTableData();
			return data;
		}

		/* Run in event-dispatching thread after doInBackground() completes*/
		@Override
		protected void done()
		{
			try
			{
				Object[][] data = get();
				if(data == null)
				{
					throw new Exception("Unable to get Chaser server data." );
				}else
				{
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					if (model.getRowCount() > 0) {
					    for (int i = model.getRowCount() - 1; i > -1; i--) {
					    	model.removeRow(i);
					    }
					}
					
					for (Object row : data)
					{
						model.addRow((Object[]) row);
					}
					table.repaint();
					
				}

			} catch (Exception ex)
			{
				showErrorDialog(frmUsmcChaserLauncher, "Unable to get Chaser server data.", ex);
			}finally{
				setRefreshBtnEnabled(true);
			}
		}
	};
	
	private void showErrorDialog(JFrame parent, String message, Exception ex)
	{
		JOptionPane.showMessageDialog(parent, message +". Error: "+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		logger.error(message, ex);
	}
	
	public String showFileDialog(JFrame parent, String title, final String description, final String executable)
	{
		try
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileFilter()
			{
				
				@Override
				public String getDescription()
				{
					return description;
				}
				
				@Override
				public boolean accept(File f)
				{
				       if (f.isDirectory()) {
				           return true;
				       } else {
				           String filename = f.getName().toLowerCase();
				           return filename.equalsIgnoreCase(executable);
				       }
				}
			});
			chooser.setDialogTitle(title);
			chooser.showOpenDialog(parent);
			File selectedFile = chooser.getSelectedFile();
			if(selectedFile == null){
				JOptionPane.showMessageDialog(parent, "You must select " + executable +" to use this launcher. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
				return null;
			}else{
				return selectedFile.getCanonicalPath();
			}
		} catch (IOException e)
		{
			showErrorDialog(parent, "Unable to select file. Please try again.", e);
			return null;
		}
	}
	
	private void setRefreshBtnEnabled(boolean enabled){
		refreshServerBrowserBtn.setEnabled(enabled);
		refreshServerBrowserBtn.setText(enabled ? "Refresh": "Loading...");
	}
}

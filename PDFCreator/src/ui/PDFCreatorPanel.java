package ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * Add a scroll panel next to the activities panel to show what activities have been added.
 * Allow for activities to be clicked on and deleted or editted.
 * If needed to be editted, find that activity in arraylist and load all current info into textFields.
 * Then when save is hit, delete the original activity and add the new one (note: no longer same index of arraylist)
 * To delete just use index to retrieve and call remove() on list.
 * 
 * Maybe use the scroll panel as a list of checkboxes that have names as the activity descriptions first 30 characters or so.
 * Have a listener to get all clicked on items.
 */

public class PDFCreatorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton finishButton, addActivityButton, changeActivityButton, editActivityButton, deleteActivityButton;
	private JTextField saveAsF, dateF, custNameF, custCompNameF,
		custAddF, custCityF, custStateF, custZipF, quantityF,
		rateF, salesTaxF, depositF;
	private JPanel detailsPanel, custPanel, activityPanel, buttonPanel, activityContainer, sub1, sub2;
	private JFileChooser fileChooser;
	private JCheckBox sameAsNameF;
	private JRadioButton equip, material, labor;
	private JTextArea descriptionF, termsF;
	private JScrollPane scrollPane;
	private ButtonGroup group;
	private JLabel saveAsLabel, dateLabel, custNameLabel, custCompNameLabel, sameAsNameLabel,
		custAddLabel, custCityLabel, custStateLabel, custZipLabel, quantityLabel, activityErrLabel,
		rateLabel, salesTaxLabel, depositLabel, descriptionLabel, formErrLabel, typeLabel, termsLabel;
	private ArrayList<Activity> activities = new ArrayList<Activity>();
	private JList<Activity> list;
	private DefaultListModel<Activity> listModel;
	private int actIndex = 0;
	
	public PDFCreatorPanel() {
		super();
		//setPreferredSize(new Dimension(700, 700));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		listModel = new DefaultListModel<Activity>();
		list = new JList<Activity>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListListener());
		scrollPane = new JScrollPane(list);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		sub1 = new JPanel();
		sub1.setLayout(new BoxLayout(sub1, BoxLayout.X_AXIS));
		sub2 = new JPanel();
		sub2.setLayout(new BoxLayout(sub2, BoxLayout.X_AXIS));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		activityContainer = new JPanel();
		activityContainer.setLayout(new BoxLayout(activityContainer, BoxLayout.Y_AXIS));
		
		detailsPanel = new JPanel(new SpringLayout());
		//detailsPanel.setPreferredSize(new Dimension(300, 300));
		detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
		
		custPanel = new JPanel(new SpringLayout());
		custPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
		//custPanel.setPreferredSize(new Dimension(300, 300));
		
		activityPanel = new JPanel(new SpringLayout());
		activityContainer.setBorder(BorderFactory.createTitledBorder("Activities"));
		activityPanel.setPreferredSize(new Dimension(300, 300));
		
		finishButton = new JButton("Create Proposal");
		finishButton.addActionListener(new ButtonListener());
		
		ActivityListener actListen = new ActivityListener();
		addActivityButton = new JButton("Add");
		addActivityButton.addActionListener(actListen);
		changeActivityButton = new JButton("Save Change");
		changeActivityButton.addActionListener(actListen);
		changeActivityButton.setVisible(false);
		editActivityButton = new JButton("Edit");
		editActivityButton.addActionListener(actListen);
		editActivityButton.setVisible(false);
		deleteActivityButton = new JButton("Delete");
		deleteActivityButton.addActionListener(actListen);
		deleteActivityButton.setVisible(false);
		
		fileChooser = new JFileChooser(); 
	    fileChooser.setDialogTitle("Select A Directory To Save The Proposal");
	    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    fileChooser.setAcceptAllFileFilterUsed(false);
	    
	    sameAsNameF = new JCheckBox();
	    sameAsNameF.addActionListener(new CheckBoxListener());
	    sameAsNameLabel = new JLabel("Same As Customer Name", JLabel.LEADING);
	    sameAsNameLabel.setLabelFor(sameAsNameF);
		
		saveAsF = new JTextField(10);
		saveAsLabel = new JLabel("Save As Name: ", JLabel.TRAILING);
	    saveAsLabel.setLabelFor(saveAsF);
		
		depositF = new JTextField("50", 10);
		depositLabel = new JLabel("Deposit Amount %: ", JLabel.TRAILING);
		depositLabel.setLabelFor(depositF);
		
		salesTaxF = new JTextField("6.625", 10);
		salesTaxLabel = new JLabel("Sales Tax %: ", JLabel.TRAILING);
		salesTaxLabel.setLabelFor(salesTaxF);
		
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		dateF = new JTextField(date.format(formatter), 10);
		dateLabel = new JLabel("Date(mm/dd/yyyy): ", JLabel.TRAILING);
		dateLabel.setLabelFor(dateF);
		
		custNameF = new JTextField(10);
		custNameLabel = new JLabel("Customer Name: ", JLabel.TRAILING);
		custNameLabel.setLabelFor(custNameF);
		
		custCompNameF = new JTextField(10);
		custCompNameLabel = new JLabel("Company Name: ", JLabel.TRAILING);
		custCompNameLabel.setLabelFor(custCompNameF);
		
		custAddF = new JTextField(10);
		custAddLabel = new JLabel("Street Address: ", JLabel.TRAILING);
		custAddLabel.setLabelFor(custAddF);
		
		custCityF = new JTextField(10);
		custCityLabel = new JLabel("City: ", JLabel.TRAILING);
		custCityLabel.setLabelFor(custCityF);
		
		custZipF = new JTextField(10);
		custZipLabel = new JLabel("Zipcode: ", JLabel.TRAILING);
		custZipLabel.setLabelFor(custZipF);
		
		custStateF = new JTextField(10);
		custStateLabel = new JLabel("State: ", JLabel.TRAILING);
		custStateLabel.setLabelFor(custStateF);
		
		quantityF = new JTextField(10);
		quantityLabel = new JLabel("Quantity: ", JLabel.TRAILING);
		quantityLabel.setLabelFor(quantityF);
		
		rateF = new JTextField(10);
		rateLabel = new JLabel("Rate: ", JLabel.TRAILING);
		rateLabel.setLabelFor(rateF);
		
		descriptionF = new JTextArea(10, 10);
		descriptionF.setLineWrap(true);
		descriptionLabel = new JLabel("Description: ", JLabel.TRAILING);
		descriptionLabel.setLabelFor(descriptionF);
		
		termsF = new JTextArea(10, 30);
		termsF.setLineWrap(true);
		termsLabel = new JLabel("Terms: ", JLabel.TRAILING);
		termsLabel.setLabelFor(termsF);
		
		typeLabel = new JLabel("Type: ", JLabel.TRAILING);
		JPanel bp = new JPanel();
		equip = new JRadioButton("Equipment");
		material = new JRadioButton("Material");
		labor = new JRadioButton("Labor");
		equip.setSelected(true);
		
		group = new ButtonGroup();
		group.add(equip);
		group.add(material);
		group.add(labor);
		
		formErrLabel = new JLabel();
		formErrLabel.setForeground(Color.RED);
		activityErrLabel = new JLabel();
		activityErrLabel.setForeground(Color.RED);
		
		detailsPanel.add(saveAsLabel);
		detailsPanel.add(saveAsF);
		detailsPanel.add(dateLabel);
		detailsPanel.add(dateF);
		detailsPanel.add(salesTaxLabel);
		detailsPanel.add(salesTaxF);
		detailsPanel.add(depositLabel);
		detailsPanel.add(depositF);
		detailsPanel.add(termsLabel);
		detailsPanel.add(termsF);
		
		custPanel.add(custNameLabel);
		custPanel.add(custNameF);
		custPanel.add(custCompNameLabel);
		custPanel.add(custCompNameF);
		custPanel.add(sameAsNameLabel);
		custPanel.add(sameAsNameF);
		custPanel.add(custAddLabel);
		custPanel.add(custAddF);
		custPanel.add(custCityLabel);
		custPanel.add(custCityF);
		custPanel.add(custStateLabel);
		custPanel.add(custStateF);
		custPanel.add(custZipLabel);
		custPanel.add(custZipF);
		
		activityPanel.add(typeLabel);
		bp.add(equip);
		bp.add(material);
		bp.add(labor);
		activityPanel.add(bp);
		activityPanel.add(descriptionLabel);
		activityPanel.add(descriptionF);
		activityPanel.add(quantityLabel);
		activityPanel.add(quantityF);
		activityPanel.add(rateLabel);
		activityPanel.add(rateF);
		buttonPanel.add(addActivityButton);
		buttonPanel.add(editActivityButton);
		buttonPanel.add(changeActivityButton);
		buttonPanel.add(deleteActivityButton);
		activityContainer.add(activityErrLabel);
		activityContainer.add(activityPanel);
		activityContainer.add(buttonPanel);
		
		sub1.add(detailsPanel);
		sub1.add(custPanel);
		sub2.add(activityContainer);
		sub2.add(scrollPane);
		
		add(formErrLabel);
		add(sub1);
		add(sub2);
		add(finishButton);
		
		SpringUtilities.makeCompactGrid(detailsPanel,
                5, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		SpringUtilities.makeCompactGrid(custPanel,
      7, 2, //rows, cols
      6, 6,        //initX, initY
      6, 6);       //xPad, yPad
SpringUtilities.makeCompactGrid(activityPanel,
      4, 2, //rows, cols
      6, 6,        //initX, initY
      6, 6);       //xPad, yPad
	}
	
	private boolean isValidActivity() {
		try {
			if (descriptionF.getText().length() == 0)
				throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			activityErrLabel.setText("Activities must have descriptions.");
			return false;
		}
		try {
			int i = Integer.parseInt(quantityF.getText());
			if (i < 1)
				throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			activityErrLabel.setText("Quantity must be specified as a positive integer.");
			return false;
		}
		try {
			double i = Double.parseDouble(rateF.getText());
			if (i < 0)
				throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			activityErrLabel.setText("Rate must be specified as a positive number.");
			return false;
		}
		activityErrLabel.setText("");
		return true;
	}
	
	private boolean isValidForm() {
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox NVR 8th 2T8-POE/HDMI", 12, 12));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox NVR 8th 2T8-POE/HDMI", 1, 350));
//		activities.add(new Activity(Activity.EQUIPMENT, "TrendNet", 1, 200));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox 4MP Turret", 3, 115));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox 4MP Mini", 3, 115));
//		activities.add(new Activity(Activity.MATERIAL, "CAT6 Cable", 2, 200));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox Surface Mount Box", 1, 35));
//		activities.add(new Activity(Activity.LABOR, "Labor", 1, 100000));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox NVR 8th 2T8-POE/HDMI", 1, 350));
//		activities.add(new Activity(Activity.EQUIPMENT, "TrendNet", 1, 200));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox 4MP Turret", 3, 115));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox 4MP Mini", 3, 115));
//		activities.add(new Activity(Activity.MATERIAL, "CAT6 Cable", 2, 200));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox Surface Mount Box", 1, 35));
//		activities.add(new Activity(Activity.LABOR, "Labor", 1, 608));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox NVR 8th 2T8-POE/HDMI", 1, 350));
//		activities.add(new Activity(Activity.EQUIPMENT, "TrendNet", 1, 200));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox 4MP Turret", 3, 115));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox 4MP Mini", 3, 115));
//		activities.add(new Activity(Activity.MATERIAL, "CAT6 Cable", 2, 200));
//		activities.add(new Activity(Activity.EQUIPMENT, "WBox Surface Mount Box", 1, 35));
//		activities.add(new Activity(Activity.LABOR, "Labor", 1, 608));
		try {new BigDecimal(salesTaxF.getText());}
		catch (NumberFormatException e) {
			formErrLabel.setText("Sales Tax must be specified in a percentage.");
			return false;
		}
		try {new BigDecimal(depositF.getText());}
		catch (NumberFormatException e) {
			formErrLabel.setText("Deposit Amount must be specified in a percentage.");
			return false;
		}
		if (activities.size() == 0) {
			formErrLabel.setText("At least one activity must be added to the proposal.");
			return false;
		}
		if (saveAsF.getText().equals("") || dateF.getText().equals("") || custNameF.getText().equals("") || (custCompNameF.getText().equals("")&&!sameAsNameF.isSelected()) ||
				custAddF.getText().equals("") || custCityF.getText().equals("") || custStateF.getText().equals("") || custZipF.getText().equals("")) {
			formErrLabel.setText("All required fields must be filled to create the proposal.");
			return false;
		}
		formErrLabel.setText("");
		return true;
	}
	
	private class ListListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
            if (list.getSelectedIndex() == -1) {
                editActivityButton.setVisible(false);
                deleteActivityButton.setVisible(false);
                addActivityButton.setVisible(true);
                actIndex = -1;
            }
            else {
                actIndex = list.getSelectedIndex();
                editActivityButton.setVisible(true);
                deleteActivityButton.setVisible(true);
                addActivityButton.setVisible(false);
            }
            changeActivityButton.setVisible(false);
		}
	}
	
	private class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (sameAsNameF.isSelected())
				custCompNameF.setEnabled(false);
			else
				custCompNameF.setEnabled(true);
		}
	}
	
	private class ActivityListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Activity a;
			if (e.getSource() == addActivityButton) {
				if (isValidActivity()) {
					int type, quantity = Integer.parseInt(quantityF.getText());
					if (equip.isSelected())
						type = Activity.EQUIPMENT;
					else if (material.isSelected())
						type = Activity.MATERIAL;
					else
						type = Activity.LABOR;
					double rate = Double.parseDouble(rateF.getText());
					a = new Activity(type, descriptionF.getText(), quantity, rate);
					activities.add(a);
					listModel.addElement(a);
					descriptionF.setText("");
					quantityF.setText("");
					rateF.setText("");
					equip.setSelected(true);
					actIndex = activities.size();
				}
			}
			else {
				 a = listModel.get(actIndex);
				 if (e.getSource() == editActivityButton) {
					descriptionF.setText(a.getDescription());
					quantityF.setText(Integer.toString(a.getQuantity()));
					rateF.setText(Double.toString(a.getRate()));
					int type = a.getType();
					if (type == Activity.EQUIPMENT)
						equip.setSelected(true);
					else if (type == Activity.MATERIAL)
						material.setSelected(true);
					else
						labor.setSelected(true);
					changeActivityButton.setVisible(true);
				 }
				 else if (e.getSource() == changeActivityButton) {
					//Saving a change to an activity
				 }
				 else {
					actIndex = activities.indexOf(a);
					activities.remove(actIndex);
					listModel.remove(actIndex);
					descriptionF.setText("");
					quantityF.setText("");
					rateF.setText("");
					equip.setSelected(true);
					addActivityButton.setVisible(true);
					changeActivityButton.setVisible(false);
					editActivityButton.setVisible(false);
					deleteActivityButton.setVisible(false);
			 	 }
			}
			/*
			 * add a save file with special ext to hold data to recreate PDF information to be opened with executable
			 * Repopulate activities and list of editable activities.

			 * if edit is hit, enable change and disable all others
			 * possibly add a cancel button to clear info and reenable buttons to stating state.
			 * 
			 * JList will have corresponding indices to arraylist activities.
			 * change will use set(), add will use add(), delete will use remove().
			 * Make sure list is scroll pane if the list gets to long
			 * Use 
			 * 
			 * Logo is missing on Monica's Computer
			 * Maybe add customer database
			 * Maybe all possible if on jsp with servlet??
			 * Add way to edit already made PDF
			 */
		}
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (isValidForm() && fileChooser.showOpenDialog(detailsPanel) == JFileChooser.APPROVE_OPTION) {
				String directory = fileChooser.getSelectedFile().getAbsolutePath();
				String saveAsName = saveAsF.getText();
				String fileName = saveAsName.endsWith(".pdf") ? saveAsName : saveAsName+".pdf";
				//String fileName = "test.pdf";
				String terms = termsF.getText();
				//String terms = "$$$$$ is due upon execution of this agreement and $$$$$ is due within 10 calendar days of the successful installation of material and equipment listed above.";
				//double salesTax = 0.06875, depositAmt = 0.5;
				BigDecimal salesTax = new BigDecimal(salesTaxF.getText()), depositAmt = new BigDecimal(depositF.getText());
				salesTax = salesTax.divide(new BigDecimal("100"));
				depositAmt = depositAmt.divide(new BigDecimal("100"));
				//Customer cust = new Customer("Customer Name", "Company Name", "10 Main Ave", "City", "12345", "NJ");
				Customer cust = new Customer(custNameF.getText(), sameAsNameF.isSelected()?custNameF.getText():custCompNameF.getText(), custAddF.getText(), custCityF.getText(), custZipF.getText(), custStateF.getText());
				Proposal p = new Proposal(activities, salesTax, depositAmt, cust, dateF.getText(), terms);
				PDFCreator.createPDF(directory+"\\"+fileName, p);
				if (Desktop.isDesktopSupported()) {
				    try {
				        File myFile = new File(directory+"\\"+fileName);
				        Desktop.getDesktop().open(myFile);
				    } catch (IOException ex) {
				    	ex.printStackTrace();
				    }
				}
			}
		}
	}
}

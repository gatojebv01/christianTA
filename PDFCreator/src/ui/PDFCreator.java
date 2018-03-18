package ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreator {
	
	public static void createPDF(String filePath, Proposal proposal) {
		NumberFormat mf = NumberFormat.getCurrencyInstance(), pf = NumberFormat.getPercentInstance();
		mf.setRoundingMode(RoundingMode.HALF_UP);
		pf.setMaximumFractionDigits(3);
		Document document = new Document(PageSize.A4, 25, 25, 25, 25);
        try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
        document.open();
        PdfPTable table = new PdfPTable(2);
 
        table.setWidthPercentage(100);
        table.setSpacingBefore(0);
        table.setSpacingAfter(0);
        
        PdfPCell emptyCell = new PdfPCell(new Phrase(""));
        emptyCell.setBorder(Rectangle.NO_BORDER);
        Font bold = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
 
        PdfPCell cell = new PdfPCell(), logoCell;
        try {
			Image img = Image.getInstance("C:\\Users\\Monica\\Desktop\\tacLogo.png");
			img.scalePercent(80);
			logoCell = new PdfPCell(img);
			logoCell.setColspan(2);
			logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			logoCell.setPaddingBottom(20);
			logoCell.setBorder(Rectangle.NO_BORDER);
	        table.addCell(logoCell);
		} catch (BadElementException | IOException e1) {
			e1.printStackTrace();
		}

        Customer cust = proposal.getCust();
        String[] header = {cust.getName(), Proposal.companyName, cust.getCompanyName(), Proposal.address, cust.getAddress(), Proposal.apt,
        		cust.getCity()+", "+cust.getState()+" "+cust.getZip(), Proposal.city+", "+Proposal.state+" "+Proposal.zip,
        		"", Proposal.phone};
        
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(3);
        cell.setPhrase(new Phrase("Proposal For Service", bold));
        table.addCell(cell);
        table.addCell(emptyCell);
        cell.setPhrase(new Phrase(proposal.getDate(), bold));
        cell.setPaddingBottom(20);
        table.addCell(cell);
        table.addCell(emptyCell);
        cell.setPadding(3);
        for (String s : header) {
        	cell.setPhrase(new Phrase(s));
        	table.addCell(cell);
        }
        table.addCell(emptyCell);
        cell.setPaddingBottom(50);
        cell.setPhrase(new Phrase(Proposal.email));
    	table.addCell(cell);
    	cell.setPadding(5);
        
        PdfPTable activityTable = new PdfPTable(16);
        activityTable.setWidthPercentage(100);
        activityTable.setSpacingBefore(0);
        activityTable.setSpacingAfter(0);
        
        header = new String[4];
        header[0] = "Activity"; header[1] = "Qty"; header[2] =  "Rate"; header[3] = "Amount";
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.BOX);
        cell.setBackgroundColor(new BaseColor(200, 200, 200));
        for (int i = 0; i < header.length; i++) {
        	if (i % 4 == 0)
        		cell.setColspan(8);
        	else if ((i+3) % 4 == 0)
        		cell.setColspan(2);
        	else
        		cell.setColspan(3);
	        cell.setPhrase(new Phrase(header[i]));
	        activityTable.addCell(cell);
		}
        cell.setBackgroundColor(new BaseColor(255, 255, 255));
        ArrayList<Activity> activities = proposal.getActivities();
        String[] activitiesData = new String[activities.size()*4];
        for (int i = 0; i < activities.size(); i++) {
        	Activity a = activities.get(i);
        	String type;
        	if (a.getType() == Activity.EQUIPMENT)
        		type = "Equipment";
        	else if (a.getType() == Activity.MATERIAL)
        		type = "Material";
        	else
        		type = "Labor";
        	activitiesData[i*4] = type+"\n"+a.getDescription();
        	activitiesData[i*4+1] = String.valueOf(a.getQuantity());
        	activitiesData[i*4+2] = mf.format(a.getRate());
        	activitiesData[i*4+3] = mf.format((double)(a.getQuantity())*a.getRate());
		}
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        int i;
        for (i = 0; i < activities.size()*4-4; i++) {
        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        	if (i % 4 == 0) {
        		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        		cell.setColspan(8);
        	}
        	else if ((i+3) % 4 == 0)
        		cell.setColspan(2);
        	else
        		cell.setColspan(3);
    		cell.setPhrase(new Phrase(activitiesData[i]));
    		activityTable.addCell(cell);
    }
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        for (; i < activities.size()*4; i++) {
        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        	if (i % 4 == 0) {
        		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        		cell.setColspan(8);
        	}
        	else if ((i+3) % 4 == 0)
        		cell.setColspan(2);
        	else
        		cell.setColspan(3);
        	cell.setPhrase(new Phrase(activitiesData[i]));
	    	activityTable.addCell(cell);
        }
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        for (i = 0; i < 9; i++)
        	activityTable.addCell(emptyCell);
        cell.setPhrase(new Phrase("Subtotal:"));
        cell.setColspan(4);
        activityTable.addCell(cell);
        cell.setColspan(3);
        cell.setPhrase(new Phrase(mf.format(proposal.getSubtotal())));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        activityTable.addCell(cell);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        for (i = 0; i < 9; i++)
        	activityTable.addCell(emptyCell);
        cell.setPhrase(new Phrase("Sales Tax ("+pf.format(proposal.getSalesTax())+"):"));
        cell.setColspan(4);
        activityTable.addCell(cell);
        cell.setColspan(3);
        cell.setPhrase(new Phrase(mf.format(proposal.getTax())));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        activityTable.addCell(cell);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        for (i = 0; i < 9; i++)
        	activityTable.addCell(emptyCell);
        cell.setPhrase(new Phrase("Total:"));
        cell.setColspan(4);
        activityTable.addCell(cell);
        cell.setColspan(3);
        cell.setPhrase(new Phrase(mf.format(proposal.getTotal()), bold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        activityTable.addCell(cell);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        for (i = 0; i < 9; i++)
        	activityTable.addCell(emptyCell);
        cell.setPhrase(new Phrase("Deposit ("+pf.format(proposal.getDepositAmt())+"):"));
        cell.setColspan(4);
        activityTable.addCell(cell);
        cell.setColspan(3);
        cell.setPhrase(new Phrase(mf.format(proposal.getDeposit())));
        cell.setPaddingBottom(50);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        activityTable.addCell(cell);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        //Signature Lines
        PdfPTable footer = new PdfPTable(4);
        footer.setWidthPercentage(100);
        footer.setSpacingBefore(0);
        footer.setSpacingAfter(0);
        
        String terms = proposal.getTerms().length()==0 ? "" : "Terms: "+proposal.getTerms();
        
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        cell.setPhrase(new Phrase(terms));
        footer.addCell(cell);
        cell.setColspan(2);
        cell.setPhrase(new Phrase("Accepted By: ____________________"));
        footer.addCell(cell);
        cell.setPhrase(new Phrase("Accepted Date: ____________________"));
        footer.addCell(cell);
        try {
			document.add(table);
			document.add(activityTable);
			document.add(footer);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        document.close();
	}
}

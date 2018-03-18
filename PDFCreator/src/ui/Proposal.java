package ui;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Proposal {

	public static final String companyName = "Total Access Communications, LLC";
	public static final String address = "555 Goffle Road";
	public static final String apt = "Suite 106";
	public static final String city = "Ridgewood";
	public static final String state = "NJ";
	public static final String zip = "07450";
	public static final String phone = "(201) 696-9069x101";
	public static final String email = "support@totalaccessit.com";
	
	private ArrayList<Activity> activities;
	private BigDecimal salesTax, tax, depositAmt, total, subtotal, deposit;
	private Customer cust;
	private String date, terms;
	
	public Proposal(ArrayList<Activity> activities, BigDecimal salesTax, BigDecimal depositAmt, Customer cust, String date, String terms) {
		this.activities = activities;
		this.salesTax = salesTax;
		this.depositAmt = depositAmt;
		this.cust = cust;
		this.date = date;
		this.terms = terms;
		calculateTotal();
	}
	
	private void calculateTotal() {
		BigDecimal subtotal = new BigDecimal("0");
		for (Activity a : activities)
			subtotal = subtotal.add(BigDecimal.valueOf(a.getQuantity()).multiply(BigDecimal.valueOf(a.getRate())));
		setSubtotal(subtotal);	
	}

	public BigDecimal getTax() {
		return tax;
	}

	private void setTax(BigDecimal tax) {
		this.tax = tax;
		setTotal(subtotal.add(tax));
	}
	
	public String getTerms() {
		return terms;
	}
	
	public void setTerms(String terms) {
		this.terms = terms;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
		calculateTotal();
	}
	
	public void addActivity(Activity activity) {
		activities.add(activity);
		calculateTotal();
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	private void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(BigDecimal salesTax) {
		this.salesTax = salesTax;
		setTax(salesTax.multiply(subtotal));
	}

	public BigDecimal getDepositAmt() {
		return depositAmt;
	}

	public void setDepositAmt(BigDecimal depositAmt) {
		this.depositAmt = depositAmt;
		setDeposit(depositAmt.multiply(total));
	}

	public BigDecimal getTotal() {
		return total;
	}

	private void setTotal(BigDecimal total) {
		this.total = total;
		setDeposit(depositAmt.multiply(total));
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	private void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
		setTax(subtotal.multiply(salesTax));
	}

	public Customer getCust() {
		return cust;
	}

	public void setCust(Customer cust) {
		this.cust = cust;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}

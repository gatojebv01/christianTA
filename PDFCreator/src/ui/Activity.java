package ui;

public class Activity {

	public static final int EQUIPMENT = 1, MATERIAL=2, LABOR = 3;
	private int type, quantity;
	private String description;
	private double rate;
	
	public Activity(int type, String description, int quantity, double rate) {
		this.type = type;
		this.description = description;
		this.quantity = quantity;
		this.rate = rate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String toString() {
		return description+": "+quantity+" @ $"+rate;
	}
}

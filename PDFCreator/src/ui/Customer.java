package ui;

public class Customer {

	private String name, companyName, address, city, zip, state;
	
	public Customer(String name, String companyName, String address, String city, String zip, String state) {
		this.name = name;
		this.companyName = companyName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public Customer(String name, String address, String city, String zip, String state) {
		this(name, name, address, city, zip, state);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}

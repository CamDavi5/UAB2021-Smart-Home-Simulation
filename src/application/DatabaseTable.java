package application;

public class DatabaseTable {
	private String month;
	private int wattage, gallons, cost;
	
	// Standard object creator for DatabaseTable
	public DatabaseTable(String month, int wattage, int gallons, int cost) {
		this.month = new String(month);
		this.wattage = wattage;
		this.gallons = gallons;
		this.cost = cost;
	}
	
	// Getter and setter for the month column data
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	// Getter and setter for the wattage column data
	public int getWattage() {
		return wattage;
	}
	public void setWattage(int wattage) {
		this.wattage = wattage;
	}
	
	// Getter and setter for the gallons column data
	public int getGallons() {
		return gallons;
	}
	public void setGallons(int gallons) {
		this.gallons = gallons;
	}
	
	// Getter and setter for the cost column data
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
}

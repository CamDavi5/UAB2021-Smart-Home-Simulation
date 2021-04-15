package application;

public class UsageCalculations {

	// electrical wattage //
	public double lightWattage = 60;
	public double exhaustFanWattage = 30;
	public double HVACWattage = 3500;
	public double refridgeratorWattage = 150;
	public double microwaveWattage = 1100;
	public double waterHeaterWattage = 4500;
	public double stoveWattage = 3500;
	public double ovenWattage = 4000;
	public double livingRoomTVWattage = 636;
	public double bedroomTVWattage = 100;
	public double dishwasherWattage = 1800;
	public double clothesWasherWattage = 500;
	public double clothesDryerWattage = 3000;
	
	// water gallons used //
	public double showersGallons = 25;
	public double bathGallons = 30;
	public double dishwasherGallons = 6;
	public double clothesWasherGallons = 20;
			
	// electric use formulas //
	public double lightsUsage (double time) {
		double usage = (lightWattage * time)/1000;
		return usage;
	}
	
	public double applianceUsage (double watts, double time) {
		double usage = (watts * time)/1000;
		return usage;
	}
	
	public double hotwaterHeaterUsage (double watts, double waterUsedPercent, double gallonsUsed) {
		double usage = ((watts) * (4 * (gallonsUsed * waterUsedPercent) / 60) / 1000);
		return usage;
	}
	
	public double electricCost (double kilowattsUsed) {
		double cost = .12 * kilowattsUsed;
		return cost;
	}
	
	// water use formulas //
	public double waterCubicFeetUsage (double gallons) {
		double waterCubicFeet = (100 * gallons)/748;
		return waterCubicFeet;
	}
	
	public double waterCost (double cubicFeet) {
		double cost = (2.52 * cubicFeet)/100;
		return cost;
	}
	
	public double bathCost (double bathGallons, double hotwaterPercentage, double coldwaterPercentage) {
		double hotWaterCost = hotwaterHeaterUsage(waterHeaterWattage, hotwaterPercentage, bathGallons);
		double coldWaterCost = waterCost(waterCubicFeetUsage(coldwaterPercentage * bathGallons));
		double bathCost = hotWaterCost + coldWaterCost; 
		return bathCost;
	}
	
	public double showerCost (double showerGallons, double hotwaterPercentage, double coldwaterPercentage) {
		double hotWaterCost = hotwaterHeaterUsage(waterHeaterWattage, hotwaterPercentage, showerGallons);
		double coldWaterCost = waterCost(waterCubicFeetUsage(coldwaterPercentage * showerGallons));
		double showerCost = hotWaterCost + coldWaterCost; 
		return showerCost;
	}
	
	// formulas for appliances that use water and electricity simultaneously //
	public double dishwasherCost (double dishwasherGallons, double time) {
		double hotwaterCost = hotwaterHeaterUsage(dishwasherWattage, 100, dishwasherGallons);
		double dishwasherElectricCost = applianceUsage(dishwasherWattage, time);
		double dishwasherCost = hotwaterCost + dishwasherElectricCost;
		return dishwasherCost;
	}
	
	public double clothesWasherCost (double clothesWasherGallons, double time) {
		double hotwaterCost = hotwaterHeaterUsage(clothesWasherWattage, 100, clothesWasherGallons);
		double clothesWasherElectricCost = applianceUsage(clothesWasherWattage, time);
		double clothesWasherCost = hotwaterCost + clothesWasherElectricCost;
		return clothesWasherCost;
	}
}

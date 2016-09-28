package berlinTSP;

/*
 * City objects hold coordinates
 * distance function calculates straight line distance between self 
 *  and the city passed as parameter.
 * id returned as int
 * x and y coords returned as double
 */
public class City {
	int id;
	double x;
	double y;
	
	// constructor takes coordinates as String parameters
	// as read from file
	public City(String id, String x, String y) {
		this.id = Integer.parseInt(id);
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
	}
	
	public int getID() {
		return id;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	// straight line distance is calculated with pythagorean theorem
	public double distance(City city) {
		double deltaX = Math.abs(getX() - city.getX());
		double deltaY = Math.abs(getY() - city.getY());
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
	}
}

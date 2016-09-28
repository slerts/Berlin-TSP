package berlinTSP;

import java.util.ArrayList;

/*
 * City Maps hold all destinations which need to be reached
 * For TSP problem, these will be read from a text file: berlin52
 * 
 * cities are added by being passed as parameters
 * cities are returned by index in current list
 * list is returned as ArrayList of cities
 * size of list is returned as an int
 */

public class CityMap {
	private ArrayList<City> cityList;
	private boolean[] containsCity = new boolean[52];
	
	CityMap() {
		cityList = new ArrayList<City>();
	}
	
	CityMap(int mapSize) {
		cityList = new ArrayList<City>();
		for (int j = 0; j < mapSize; j++) {
			cityList.add(null);
		}
	}
	
	CityMap(ArrayList<City> cityList) {
		this.cityList = cityList;
		for (City city : cityList) {
			containsCity[city.getID() - 1] = true;
		}
	}
	
	public void addCity(City city) {
		cityList.add(city);
		containsCity[city.getID() - 1] = true;
	}
	
	public void addCity(City city, int i) {
		cityList.add(i, city);
		containsCity[city.getID() - 1] = true;
	}
	
	public void setCity(City city, int i) {
		cityList.set(i, city);
	}
	
	public City getCity(int i) {
		return cityList.get(i);
	}
	
	public ArrayList<City> getList() {
		return cityList;
	}
	
	public int listSize() {
		return cityList.size();
	}
	
	public boolean hasCity(int i) {
		return containsCity[i];
	}
	
	public boolean hasCity(City city) {
		if (cityList.contains(city)) {
			return true;
		}
		return false;
	}
	
	
}

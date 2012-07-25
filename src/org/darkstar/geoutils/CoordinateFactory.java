/**
 * 
 */
package org.darkstar.geoutils;

/**
 * @author mattevigo
 *
 */
public class CoordinateFactory 
{
	/* WGS84 constants */
	public static final double SM_A = 6378137.0;
	public static final double SM_B = 6356752.314;
	public static final double SM_ECC_SQUARED = 6.69437999013e-03;
	public static final double UTM_SCALE_FACTOR = 0.9996;

	/* Gauss-Boaga constants */
	public static final double ROMA40_X = 14.0;
	public static final double ROMA40_Y = 1000027.0;

	public static final double DEFAULT_UTM_ZONE = 32.0;
	public static final Hemisphere DEFAULT_HEMISPHERE = Hemisphere.SOUTH;

	private Hemisphere hemisphere = DEFAULT_HEMISPHERE;
	private double utmZone = DEFAULT_UTM_ZONE;

	public enum Hemisphere
	{
		SOUTH, NORTH;
	}

	public Hemisphere getHemisphere() {
		return hemisphere;
	}

	public void setHemisphere(Hemisphere hemisphere) {
		this.hemisphere = hemisphere;
	}

	public double getUtmZone() {
		return utmZone;
	}

	public void setUtmZone(double utmZone) {
		this.utmZone = utmZone;
	}
	
	public CoordinateFactory()
	{
		super();
	}
	
	/**
	 * Init an object Coordinate from polar latitude and longitude in the following format:
	 * 43d 47' 49.3" e 11d 13' 01.0"
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws CoordinateException
	 */
	public Coordinate getCoordinateFromPolar(String latitude, String longitude) throws CoordinateException
	{	
		return new Coordinate(latitude, longitude, this.hemisphere, this.utmZone);
	}
}

/**
 * 
 */
package org.darkstar.geoutils;

/**
 * 
 * @author Matteo Vigoni <m.vigoni@gmail.com>
 *
 */
public class Coordinate 
{	
	private String latitude;
	private String longitude;

	private double tmN;
	private double tmE;

	private double utmX;
	private double utmY;

	private CoordinateFactory.Hemisphere hemisphere;

	private double utmZone;

	/**
	 * Inizializza un oggetto Coordinate a partire dalle coordinate polari
	 * espresse nel formato 43d 47' 49.3" e 11d 13' 01.0"
	 * 
	 * @param latitude
	 * @param longitude
	 * @throws CoordinateException se il formato della latitudine o della longitudine non è valido
	 * 			o il formato numerico è errato
	 */
	protected Coordinate(String latitude, String longitude, CoordinateFactory.Hemisphere hemisphere, double utmZone) throws CoordinateException
	{
		this.setHemisphere(hemisphere);
		this.setUtmZone(utmZone);
		
		if( !checkCoordinate(latitude, longitude) )
		{
			throw new CoordinateException("Latitude and/or Longitude value are not valid");
		}

		if( latitude == null || longitude == null )
		{
			this.latitude = "0d 0' 0\"";
			this.longitude = "0d 0' 0\"";			
		}
		else
		{
			this.latitude = latitude;
			this.longitude = longitude;			
		}

		OnChangePolar();
	}

	/**
	 * Coordinata X (northing) in proiezione TM
	 * 
	 * @return
	 */
	public double getNorthing()
	{
		return tmN;
	}

	/**
	 * Coordinata Y (easting) in proiezione TM
	 * 
	 * @return
	 */
	public double getEasting()
	{
		return tmE;
	}

	/**
	 * Coordinata X in proiezione UTM
	 * 
	 * @return
	 */
	public double getUTMX()
	{
		return utmX;
	}

	/**
	 * Coordinata Y in proiezione UTM
	 * 
	 * @return
	 */
	public double getUTMY()
	{
		return utmY;
	}

	/**
	 * Coordinata X in Gauss-Boaga (ROMA40)
	 * @return
	 */
	public double getRoma40X()
	{
		return utmX + CoordinateFactory.ROMA40_X;
	}

	/**
	 * Coordinata Y in Gauss-Boaga (ROMA40)
	 * @return
	 */
	public double getRoma40Y()
	{
		return utmY + CoordinateFactory.ROMA40_Y;
	}

	// Events /////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void OnChangePolar() throws CoordinateException 
	{
		double[] ne = GeoUtils.polarToPlainTM(GeoUtils.convertToRad(this.latitude), GeoUtils.convertToRad(this.longitude), 0.0);

		this.tmE = ne[0];
		this.tmN = ne[1];
		
		double[] xy = GeoUtils.polarToPlainUTM(GeoUtils.convertToRad(this.latitude), GeoUtils.convertToRad(this.longitude), getUtmZone());

		this.utmY = xy[0];
		this.utmX = xy[1];
	}

	private void OnChangePlain() throws CoordinateException
	{
		//TODO
	}

	// Check dei dati //////////////////////////////////////////////////////////

	private boolean checkCoordinate(String latitude, String longitude) 
	{
		if( latitude == null || longitude == null ) return true;

		if( !latitude.contains("d") || !longitude.contains("d") ) return false;

		return true;		
	}

	// Getters & Setters ///////////////////////////////////////////////////////

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) throws CoordinateException 
	{
		this.latitude = latitude;
		OnChangePolar();
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) throws CoordinateException {
		this.longitude = longitude;
		OnChangePolar();
	}

	public CoordinateFactory.Hemisphere getHemisphere() {
		return hemisphere;
	}

	public void setHemisphere(CoordinateFactory.Hemisphere hemisphere) {
		this.hemisphere = hemisphere;
	}

	public double getUtmZone() {
		return utmZone;
	}

	public void setUtmZone(double utmZone) {
		this.utmZone = utmZone;
	}

	@Override
	public String toString()
	{
		return this.getClass().getName()+" Geo{lat:"+this.getLatitude()+" long:"+this.getLongitude()+"} Roma40{N:"+this.getRoma40X()+" E:"+this.getRoma40Y()+"} UTM-WGS84{utmX:"+this.getUTMX()+" utmY:"+this.getUTMY()+"}";
	}
}

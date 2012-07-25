package org.darkstar.geoutils;

/**
 * 
 * @author a.fastame
 * @created 10/02/2012
 * 
 *          La classe serve a convertire valori Gauss-Boaga in valori fissi per
 *          l'applicazione sinottico, data una bitmap di 20000 x 20000 pixel. I
 *          fattori di conversione sono stati calcolati in base alle coordinate
 *          geografiche X,Y della mappa rispetto alla JPG creata da Area3D
 *          Secondo i calcoli, la mappa rilevata ha origine 0,0 (alto a sx)
 *          nella coppia (deg,dec) X: 43.88697953 Y: 11.22026393. La coordinata
 *          geografica del punto 1,1 (basso a dx) X: 43.81733207 Y:11.19032571
 * 
 */
public class GeoConverter {
	public static final double X_ORIGIN_COORD = 43.88697953d;
	public static final double Y_ORIGIN_COORD = 11.22026393d;

	// public static final float X_FACTOR = 348.2373f;
	// public static final float Y_FACTOR = 149.6911f;

	public static final double X_FACTOR = 0.6964746d;
	public static final double Y_FACTOR = 0.2993822d;

	private static final int SIDE_PIXELS = 1000;

	/**
	 * Data una stringa di tipo (stretto) 43d 48' 17,0340000000334" (l'ultimo
	 * doppio apice sta per 'secondi') ritorna un fattore intero che e' la
	 * coordinata dell'asse richiesto (X o Y). esempio di chiamata:
	 * GeoConverter.convertFromDegMinSec( "43d 48' 13,6560000000441\"",GeoConverter.X_ORIGIN_COORD,GeoConverter.X_FACTOR)
	 * 
	 * @param source
	 * @param factor
	 * @return
	 */
	public static int convertFromDegMinSec(String source, double coord, double factor) {
		// 43d 48' 17,0340000000334"
				int dIndex = source.indexOf('d'), apexIndex = source.indexOf('\'');
				double degs, mins, secs;

				
				degs = Double.parseDouble(source.substring(0, dIndex));

				
				mins = Double.parseDouble(source.substring(dIndex + 1,
						source.indexOf('\''))) / 60;

				
				secs = Double.parseDouble(source.replace(',','.').substring(apexIndex + 1,
						source.length() - 1)) / 3600;
		return convertFromDegDecimals(degs+mins+secs, coord, factor);
	}

	/**
	 * Data una stringa espressa in gradi,decimali (es: 43.81733207) ritorna un
	 * fattore intero che e' la coordinata dell'asse richiesto. Esempio di
	 * chiamata:
	 * convertFromDegDecimals("43.81733207", GeoConverter.X_ORIGIN_COORD, GeoConverter.X_FACTOR)
	 * 
	 * @param source
	 * @param factor
	 * @return
	 */
	public static int convertFromDegDecimals(double source, double coord, double factor) {

		double quoziente = SIDE_PIXELS / factor;
		double res = ((Math.abs(source-coord) * quoziente));
		return (int) res;

	}
}

/**
 * 
 */
package org.darkstar.geoutils;

/**
 * @author Matteo Vigoni <m.vigoni@gmail.com>
 *
 */
public class GeoUtils 
{
	/**
	 * Data una stringa di tipo (stretto) 43d 48' 17,0340000000334" (l'ultimo
	 * doppio apice sta per 'secondi') ritorna un fattore intero che e' la
	 * coordinata dell'asse richiesto (X o Y). esempio di chiamata:
	 * GeoUtils.convertFromDegMinSec( "43d 48' 13,6560000000441\"",GeoConverter.X_ORIGIN_COORD,GeoConverter.X_FACTOR)
	 * 
	 * @param source
	 * @param factor
	 * @return
	 * @throws CoordinateException 
	 */
	public static double convertToDeg(String sessagesimalAngle) throws CoordinateException 
	{
		// 43d 48' 17,0340000000334"
		int dIndex = sessagesimalAngle.indexOf('d'), apexIndex = sessagesimalAngle.indexOf('\'');
		double degs, mins, secs;

		try 
		{
			degs = Double.parseDouble(sessagesimalAngle.substring(0, dIndex));


			mins = Double.parseDouble(sessagesimalAngle.substring(dIndex + 1,
					sessagesimalAngle.indexOf('\''))) / 60;


			secs = Double.parseDouble(sessagesimalAngle.replace(',','.').substring(apexIndex + 1,
					sessagesimalAngle.length() - 1)) / 3600;
		} 
		catch (NumberFormatException e) 
		{
			throw new CoordinateException("Formato errato: "+e.getMessage());
		}
		
		return degs+mins+secs;
	}
	
	/**
	 * Converte un angolo espresso in decimali in sessagesimali nel formato 43d 48' 17,0340000000334" 
	 * @param degAngle
	 * @return
	 */
	public static String convertToDms(double degAngle)
	{
		String dmsAngle = "";
		double deg, min, sec;
		
		deg = Math.floor(degAngle);
		dmsAngle += deg+"d ";
		
		min = Math.floor((degAngle - deg)*60);
		dmsAngle += min+"' ";
		
		sec = Math.floor((degAngle - deg - min/60)*3600);
		dmsAngle += sec+"\"";
		
		return dmsAngle.replace(".", ",");
	}
	
	/**
	 * Converte un angolo decimale in radianti
	 * 
	 * @param degAngle
	 * @return
	 */
	public static double convertToRad(double degAngle)
	{
		return degAngle * Math.PI / 180;
	}
	
	/**
	 * Converte un angolo sessagesimale in radianti
	 * 
	 * @param sessagesimalAngle
	 * @return
	 */
	public static double convertToRad(String sessagesimalAngle) throws CoordinateException
	{
		return convertToRad(convertToDeg(sessagesimalAngle));
	}
	
	/**
	 * Converte un angolo in radianti in gradi decimali
	 * 
	 * @param radAngle
	 * @return
	 */
	public static double convertToDeg(double radAngle)
	{
		return radAngle / Math.PI * 180;
	}
	
	/**
	 * Calcola la distanza ellissoidale dall'equatore di un punto ad una certa latitudine
	 * 
	 * @param phi
	 * @return
	 */
	public static double arcLengthOfMeridian(double phi)
	{
		double alpha, beta, gamma, delta, epsilon, n;
		double result;
		
		// Calcolo di n
		n = (CoordinateFactory.SM_A - CoordinateFactory.SM_B) / (CoordinateFactory.SM_A + CoordinateFactory.SM_B);
		
		// Calcolo di alpha
		alpha = ( (CoordinateFactory.SM_A + CoordinateFactory.SM_B) / 2 ) * ( 1.0 + (Math.pow(n, 2.0) / 4.0) + (Math.pow(n, 4.0) / 64.0) );
		
		// Calcolo di beta
		beta = (-3.0 * n / 2.0) + (9.0 * Math.pow(n, 3.0) / 16.0) + (-3.0 * Math.pow(n, 5.0) / 32.0);
		
		// Calcolo di gamma
		gamma = (15.0 * Math.pow(n, 2.0) / 16.0) + (-15.0 * Math.pow(n, 4.0) / 32.0);
		
		// Calcolo di delta
		delta = (-35.0 * Math.pow(n, 3.0) / 48.0) + (105.0 * Math.pow(n, 5.0) / 256.0);
		
		// Calcolo di epsilon
		epsilon = (315.0 * Math.pow(n, 4.0) / 512.0);

		result  = alpha
				* ( phi + (beta 	* Math.sin(2.0 * phi))
						+ (gamma 	* Math.sin(4.0 * phi))
						+ (delta 	* Math.sin(6.0 * phi))
						+ (epsilon 	* Math.sin(8.0 * phi)) );

		return result;
	}
	
	/**
	 * Converte le coordinate polari in coordinate piane TM
	 * 
	 * @param phi		latitudine
	 * @param lambda	longitudine
	 * @param lambda0	longitudine 0
	 * @return			
	 */
	public static double[] polarToPlainTM(double phi, double lambda, double lambda0)
	{
		double N, nu2, ep2, t, t2, l;
		double l3coef, l4coef, l5coef, l6coef, l7coef, l8coef;
		//double tmp;
		
		double[] xy = new double[2];
		
		// Calcolo di ep2
		ep2 = (Math.pow(CoordinateFactory.SM_A, 2.0) - Math.pow(CoordinateFactory.SM_B, 2.0)) / Math.pow(CoordinateFactory.SM_B, 2.0);

		// Calcolo di nu2
		nu2 = ep2 * Math.pow(Math.cos(phi), 2.0);

		// Calcolo di N
		N = Math.pow(CoordinateFactory.SM_A, 2.0) / (CoordinateFactory.SM_B * Math.sqrt(1 + nu2));

		// Calcolo di t e t2
		t = Math.tan (phi);
		t2 = t * t;
		//tmp = (t2 * t2 * t2) - Math.pow(t, 6.0);

		// Calcolo di l
		l = lambda - lambda0;

		// Coefficienti
		l3coef = 1.0 - t2 + nu2;

		l4coef = 5.0 - t2 + 9 * nu2 + 4.0 * (nu2 * nu2);

		l5coef = 5.0 - 18.0 * t2 + (t2 * t2) + 14.0 * nu2
			- 58.0 * t2 * nu2;

		l6coef = 61.0 - 58.0 * t2 + (t2 * t2) + 270.0 * nu2
			- 330.0 * t2 * nu2;

		l7coef = 61.0 - 479.0 * t2 + 179.0 * (t2 * t2) - (t2 * t2 * t2);

		l8coef = 1385.0 - 3111.0 * t2 + 543.0 * (t2 * t2) - (t2 * t2 * t2);
		
		// Calcolo coordinata X (easting) 
		xy[0] = N * Math.cos(phi) * l
			+ (N / 6.0 * Math.pow(Math.cos(phi), 3.0) * l3coef * Math.pow(l, 3.0))
			+ (N / 120.0 * Math.pow (Math.cos (phi), 5.0) * l5coef * Math.pow(l, 5.0))
			+ (N / 5040.0 * Math.pow (Math.cos(phi), 7.0) * l7coef * Math.pow(l, 7.0));
		
		// Calcolo coordinata Y (northing)
		xy[1] = arcLengthOfMeridian(phi)
				+ (t / 2.0 * N * Math.pow(Math.cos(phi), 2.0) * Math.pow(l, 2.0))
				+ (t / 24.0 * N * Math.pow(Math.cos(phi), 4.0) * l4coef * Math.pow(l, 4.0))
				+ (t / 720.0 * N * Math.pow (Math.cos(phi), 6.0) * l6coef * Math.pow(l, 6.0))
				+ (t / 40320.0 * N * Math.pow (Math.cos(phi), 8.0) * l8coef * Math.pow(l, 8.0));
		
		return xy;
	}
	
	/**
	 * Restituisce il meridiano centrale per la zona selezionata in radianti
	 * 
	 * @param zone
	 * @return
	 */
	public static double UTMCentralMeridian(double zone)
	{
		return convertToRad( -183.0 + (zone * 6.0) );
	}
	
	/**
	 * Converte le coordinate polari in coordinate piane UTM
	 * 
	 * @param phi
	 * @param lambda
	 * @param zone
	 * @return
	 */
	public static double[] polarToPlainUTM(double phi, double lambda, double zone)
	{
		double [] ne = polarToPlainTM(phi, lambda, UTMCentralMeridian(zone));
		double [] xy = new double[2];
		
		xy[0] = ne[0] * CoordinateFactory.UTM_SCALE_FACTOR + 500000.0;
		xy[1] = ne[1] * CoordinateFactory.UTM_SCALE_FACTOR;
		
		if (xy[1] < 0.0)
		{
			xy[1] = xy[1] + 10000000.0;
		}
		
		return xy;
	}
	
	/**
	 * Converte le coordinate TM x e y in coordinate polari
	 * 
	 * @param x
	 * @param y
	 * @param lambda0
	 * @return
	 */
	public static double[] plainTMToPolar(double x, double y, double lambda0)
	{
		double phif, Nf, Nfpow, nuf2, ep2, tf, tf2, tf4, cf;
        double x1frac, x2frac, x3frac, x4frac, x5frac, x6frac, x7frac, x8frac;
        double x2poly, x3poly, x4poly, x5poly, x6poly, x7poly, x8poly;
        
        double[] philambda = new double[2];
    	
        /* Get the value of phif, the footpoint latitude. */
        phif =  footpointLatitude(y);
        	
        /* Precalculate ep2 */
        ep2 = (Math.pow (CoordinateFactory.SM_A, 2.0) - Math.pow (CoordinateFactory.SM_B, 2.0))
              / Math.pow (CoordinateFactory.SM_B, 2.0);
        	
        /* Precalculate cos (phif) */
        cf = Math.cos (phif);
        	
        /* Precalculate nuf2 */
        nuf2 = ep2 * Math.pow (cf, 2.0);
        	
        /* Precalculate Nf and initialize Nfpow */
        Nf = Math.pow (CoordinateFactory.SM_A, 2.0) / (CoordinateFactory.SM_B * Math.sqrt (1 + nuf2));
        Nfpow = Nf;
        	
        /* Precalculate tf */
        tf = Math.tan (phif);
        tf2 = tf * tf;
        tf4 = tf2 * tf2;
        
        /* Precalculate fractional coefficients for x**n in the equations
           below to simplify the expressions for latitude and longitude. */
        x1frac = 1.0 / (Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**2) */
        x2frac = tf / (2.0 * Nfpow);
        
        Nfpow *= Nf;   /* now equals Nf**3) */
        x3frac = 1.0 / (6.0 * Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**4) */
        x4frac = tf / (24.0 * Nfpow);
        
        Nfpow *= Nf;   /* now equals Nf**5) */
        x5frac = 1.0 / (120.0 * Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**6) */
        x6frac = tf / (720.0 * Nfpow);
        
        Nfpow *= Nf;   /* now equals Nf**7) */
        x7frac = 1.0 / (5040.0 * Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**8) */
        x8frac = tf / (40320.0 * Nfpow);
        
        /* Precalculate polynomial coefficients for x**n.
           -- x**1 does not have a polynomial coefficient. */
        x2poly = -1.0 - nuf2;
        
        x3poly = -1.0 - 2 * tf2 - nuf2;
        
        x4poly = 5.0 + 3.0 * tf2 + 6.0 * nuf2 - 6.0 * tf2 * nuf2
        	- 3.0 * (nuf2 *nuf2) - 9.0 * tf2 * (nuf2 * nuf2);
        
        x5poly = 5.0 + 28.0 * tf2 + 24.0 * tf4 + 6.0 * nuf2 + 8.0 * tf2 * nuf2;
        
        x6poly = -61.0 - 90.0 * tf2 - 45.0 * tf4 - 107.0 * nuf2
        	+ 162.0 * tf2 * nuf2;
        
        x7poly = -61.0 - 662.0 * tf2 - 1320.0 * tf4 - 720.0 * (tf4 * tf2);
        
        x8poly = 1385.0 + 3633.0 * tf2 + 4095.0 * tf4 + 1575 * (tf4 * tf2);
        	
        /* Calculate latitude */
        philambda[0] = phif + x2frac * x2poly * (x * x)
        	+ x4frac * x4poly * Math.pow (x, 4.0)
        	+ x6frac * x6poly * Math.pow (x, 6.0)
        	+ x8frac * x8poly * Math.pow (x, 8.0);
        	
        /* Calculate longitude */
        philambda[1] = lambda0 + x1frac * x
        	+ x3frac * x3poly * Math.pow (x, 3.0)
        	+ x5frac * x5poly * Math.pow (x, 5.0)
        	+ x7frac * x7poly * Math.pow (x, 7.0);
        	
		return philambda;
	}
	
	/**
	 * Footpoint latitude
	 * 
	 * @param y il nord della coordinata UTM in metri
	 * @return il valore della latitudine del footpoint iin radianti
	 */
	public static double footpointLatitude(double y)
	{
		double y_, alpha_, beta_, gamma_, delta_, epsilon_, n;
        double result;
        
        /* Precalculate n (Eq. 10.18) */
        n = (CoordinateFactory.SM_B - CoordinateFactory.SM_B) / (CoordinateFactory.SM_A + CoordinateFactory.SM_B);
        	
        /* Precalculate alpha_ (Eq. 10.22) */
        /* (Same as alpha in Eq. 10.17) */
        alpha_ = ((CoordinateFactory.SM_A + CoordinateFactory.SM_B) / 2.0)
            * (1 + (Math.pow(n, 2.0) / 4) + (Math.pow(n, 4.0) / 64));
        
        /* Precalculate y_ (Eq. 10.23) */
        y_ = y / alpha_;
        
        /* Precalculate beta_ (Eq. 10.22) */
        beta_ = (3.0 * n / 2.0) + (-27.0 * Math.pow(n, 3.0) / 32.0)
            + (269.0 * Math.pow (n, 5.0) / 512.0);
        
        /* Precalculate gamma_ (Eq. 10.22) */
        gamma_ = (21.0 * Math.pow(n, 2.0) / 16.0)
            + (-55.0 * Math.pow(n, 4.0) / 32.0);
        	
        /* Precalculate delta_ (Eq. 10.22) */
        delta_ = (151.0 * Math.pow(n, 3.0) / 96.0)
            + (-417.0 * Math.pow(n, 5.0) / 128.0);
        	
        /* Precalculate epsilon_ (Eq. 10.22) */
        epsilon_ = (1097.0 * Math.pow(n, 4.0) / 512.0);
        	
        /* Now calculate the sum of the series (Eq. 10.21) */
        result = y_ + (beta_ * Math.sin(2.0 * y_))
            + (gamma_ * Math.sin(4.0 * y_))
            + (delta_ * Math.sin(6.0 * y_))
            + (epsilon_ * Math.sin(8.0 * y_));
        
        return result;
	}
	
	/**
	 * Converte le coordinate UTM x e y in coordinate polari
	 * 
	 * @param x
	 * @param y
	 * @param lambda0
	 * @return
	 */
	public static double[] plainUTMToPolar(double x, double y, CoordinateFactory.Hemisphere coordinateHemisphere, double utmZone)
	{
		double cmeridian;
    	
        double _x = x - 500000.0;
        _x /= CoordinateFactory.UTM_SCALE_FACTOR;
        	
        /* If in southern hemisphere, adjust y accordingly. */
        if (coordinateHemisphere == CoordinateFactory.Hemisphere.SOUTH)
        {
        	y -= 10000000.0;
        }
        		
        double _y = y / CoordinateFactory.UTM_SCALE_FACTOR;
        
        cmeridian = UTMCentralMeridian (utmZone);
        
        double[] philambda = plainTMToPolar(_x, _y, cmeridian);
		
        return philambda;
	}
}

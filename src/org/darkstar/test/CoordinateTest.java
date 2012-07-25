/**
 * 
 */
package org.darkstar.test;

import org.darkstar.geoutils.Coordinate;
import org.darkstar.geoutils.CoordinateException;
import org.darkstar.geoutils.CoordinateFactory;

/**
 * @author Matteo Vigoni <m.vigoni@gmail.com>
 *
 */
public class CoordinateTest
{
	public static Coordinate P00;
	public static Coordinate P11;

	public static Coordinate A;
	public static Coordinate B;
	public static Coordinate C;
	public static Coordinate D;
	
	public static CoordinateFactory factory = new CoordinateFactory();
	
	static 
	{
		try 
		{
			P00 = factory.getCoordinateFromPolar("43d 47' 49.3\"", "11d 13' 01.0\"");
			P11 = factory.getCoordinateFromPolar("43d 49' 02.5\"", "11d 11' 25.2\"");

			A = factory.getCoordinateFromPolar("43d 47' 59.0\"", "11d 11' 11.9\"");
			B = factory.getCoordinateFromPolar("43d 49' 10.3\"", "11d 11' 14.5\"");
			C = factory.getCoordinateFromPolar("43d 49' 08.4\"", "11d 12' 52.9\"");
			D = factory.getCoordinateFromPolar("43d 47' 57.1\"", "11d 12' 50.3\"");
		} 
		catch (CoordinateException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{	
			System.out.println(">>> A "+factory.getCoordinateFromPolar(A.getLatitude(), A.getLongitude()));
			System.out.println(">>> B "+factory.getCoordinateFromPolar(B.getLatitude(), B.getLongitude()));
			System.out.println(">>> C "+factory.getCoordinateFromPolar(C.getLatitude(), C.getLongitude()));
			System.out.println(">>> D "+factory.getCoordinateFromPolar(D.getLatitude(), D.getLongitude()));
			System.out.println(">>> ");
			System.out.println(">>> 101  "+factory.getCoordinateFromPolar("43d 48' 15.1\"", "11d 12' 02.1\"").toString());
			System.out.println(">>> 105  "+factory.getCoordinateFromPolar("43d 48' 11.4\"", "11d 11' 56.9\"").toString());
			System.out.println(">>> 302  "+factory.getCoordinateFromPolar("43d 48' 11.6\"", "11d 11' 45.1\"").toString());
			System.out.println(">>> TH05 "+factory.getCoordinateFromPolar("43d 48' 21.5\"", "11d 11' 55.6\"").toString());
			System.out.println(">>> TH23 "+factory.getCoordinateFromPolar("43d 48' 40.3\"", "11d 12' 24.7\"").toString());
			System.out.println(">>> ");
			System.out.println(">>> T  "+factory.getCoordinateFromPolar("43d 48' 13.29\"", "11d 12' 04.66\"").toString());
		}
		catch (CoordinateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

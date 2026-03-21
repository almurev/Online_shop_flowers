package main;

import java.util.Scanner;

public class main_app {
	public static void main(String [] args) {
		Scanner in = new Scanner(System.in);
			
		int a = in.nextInt();
		byte b = in.nextByte();
		String c = in.nextLine();
		String s = in.next();
		double d = in.nextDouble();
		long e = in.nextLong();
		short f = in.nextShort();
			
		System.out.println(a+""+b+""+c+""+s+""+d+""+e+""+f);
	}
}

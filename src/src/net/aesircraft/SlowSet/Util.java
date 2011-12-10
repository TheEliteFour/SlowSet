package net.aesircraft.SlowSet;

import java.util.HashMap;

public class Util
{
	public static HashMap undos=new HashMap();
	public static boolean useable(int id){
		boolean useable=true;
		if (id==2)
			useable=false;
		if (id>6 && id<12)
			useable=false;
		if (id==16)
			useable=false;
		if (id==21)
			useable=false;
		if (id==27 || id==28)
			useable=false;
		if (id==32)
			useable=false;
		if (id==33)
			useable=false;
		if (id==34)
			useable=false;
		if (id==36)
			useable=false;
		if (id==43)
			useable=false;
		if (id==51)
			useable=false;
		if (id==55)
			useable=false;
		if (id==56)
			useable=false;
		if (id==59)
			useable=false;
		if (id==60)
			useable=false;
		if (id>61 && id<70)
			useable=false;
		if (id==71)
			useable=false;
		if (id>72 && id <76)
			useable=false;
		if (id==78)
			useable=false;
		if (id==79)
			useable=false;
		if (id==83)
			useable=false;
		if (id==90)
			useable=false;
		if (id>91 && id<98)
			useable=false;
		if (id==99)
			useable=false;
		if (id==100)
			useable=false;
		if (id==104)
			useable=false;
		if (id==105)
			useable=false;
		if (id>107&&id<112)
			useable=false;
		if (id==114)
			useable=false;
		if (id>118)
			useable=false;
		return useable;
	}
}

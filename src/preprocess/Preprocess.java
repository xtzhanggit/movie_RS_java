package preprocess;

import java.util.*;
import java.io.*;
import item.RateInfo;

public class Preprocess{
	public static List<RateInfo> doPreprocess(){
		List<RateInfo> rateInfo_list = new ArrayList<RateInfo>();
		try{
			LineNumberReader lineReader = new LineNumberReader(new FileReader("/home/xtzhang/movie_RS_java/data/ratings.dat"));
			String line = "";
			while ((line = lineReader.readLine()) != null){
				rateInfo_list.add(transRating(line));
			}
		}catch (Exception e){
	        e.printStackTrace();
		}
		return rateInfo_list;
	}

	private static RateInfo transRating(String line){
		String[] ra = line.split("::");
		RateInfo rateInfo = new RateInfo(Integer.parseInt(ra[0]),Integer.parseInt(ra[1]),Integer.parseInt(ra[2]));
		return rateInfo;
	}
}

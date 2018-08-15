import java.util.*;
import model.Cf;
import model.PersonalRank;

public class Manage{
	public static void main(String[] args){
		
		// 输入参数
		String model = args[0];
		int user_id = Integer.parseInt(args[1]);
		int size = Integer.parseInt(args[2]);
		
		// 模式选择
		if(model.equals("Cf")){
			Cf cf  = new Cf(user_id, size);
			List<Map.Entry<Integer,Double>> top_movies = cf.doRecommend(); 
			for(Map.Entry<Integer,Double> movie:top_movies)
				System.out.println(movie);
		}else if(model.equals("PersonalRank")){
			PersonalRank personalRank = new PersonalRank(user_id,size);
			List<Map.Entry<String,Double>> top_movies = personalRank.doRecommend();
			for(Map.Entry<String,Double> movie:top_movies)
				System.out.println(movie);
		}
		
	}
}


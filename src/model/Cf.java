package model;

import java.util.*;
import java.io.*;
import java.util.Map.Entry;
import item.RateInfo;
import preprocess.Preprocess;

public class Cf{

	private static List<RateInfo> rateInfo_list;
	private int user_id;
	private int size;
	
	public Cf(int user_id, int size){
		this.user_id = user_id;
		this.size = size;
	}
	
	public List<Map.Entry<Integer,Double>> doRecommend(){
		rateInfo_list = Preprocess.doPreprocess(); 
		List<Map.Entry<Integer,Double>> top_users = getTopUsers(this.user_id,this.size);
		Set<Integer> candidate_movies = getCandidateMovies(this.user_id);
		List<Map.Entry<Integer,Double>> top_movies = getTopMovies(top_users,candidate_movies,this.size);
		return top_movies;
	}
	
	// top_movies
	private static List<Map.Entry<Integer,Double>> getTopMovies(List<Map.Entry<Integer,Double>> top_users, Set<Integer> candidate_movies, int movie_size){
		Map<Integer,Double> top_movies = new TreeMap<Integer,Double>(); 
		for(int movie_id:candidate_movies){
			double interest = 0;
			for(Map.Entry<Integer,Double> user:top_users){
				int tmp_rate = getCertainRate(user.getKey(),movie_id);
				interest += (tmp_rate * user.getValue() / 5);
			}
			top_movies.put(movie_id,interest);		
		}
		List<Map.Entry<Integer,Double>> sorted_movies = mapSort(top_movies);
		return sorted_movies.subList(0,movie_size);
	}
	
	// candidate_movies
	private static Set<Integer> getCandidateMovies(int user_id){
		Set<Integer> target_movies = getMovies(user_id);
		
		Set<Integer> candidate_movies = new HashSet<Integer>();
		Set<Integer> other_user_id = getOtherUser_id(user_id);
		Set<Integer> tmp_movies = new HashSet<Integer>();
		for(int id:other_user_id){
			tmp_movies = getMovies(id);
			candidate_movies.addAll(tmp_movies);
		}
		
		candidate_movies.removeAll(target_movies);
		return candidate_movies;
	}
	
	// top_users
	private static List<Map.Entry<Integer,Double>> getTopUsers(int user_id,int user_size){
		Set<Integer> target_movies = getMovies(user_id);
		Set<Integer> other_user_id = getOtherUser_id(user_id);
		
		// other_movies
		Map<Integer,Double> other_movies = new TreeMap<Integer,Double>(); 
		for(int id:other_user_id){
			Set<Integer> tmp_movies = getMovies(id);
			double similarity = cosineSim(target_movies,tmp_movies);
			other_movies.put(id,similarity);
		}
		
		// sorted_movies
		List<Map.Entry<Integer,Double>> sorted_movies = mapSort(other_movies);
        
		return sorted_movies.subList(0,user_size);
	}
	
	// similarity
	private static double cosineSim(Set<Integer> x,Set<Integer> y){
		Set<Integer> tmp = new TreeSet<Integer>();
		tmp.addAll(x);
		tmp.retainAll(y);
		double denominator = tmp.size();
		double molecular = Math.sqrt(x.size() * y.size());
		return denominator / molecular;
	}
	
	// select movies accroding to user_id
	private static Set<Integer> getMovies(int user_id){
		Set<Integer> movies = new HashSet<Integer>();
		for(RateInfo rateInfo:rateInfo_list){
			if(rateInfo.getUser_id() == user_id)
				movies.add(rateInfo.getMovie_id());
		}
		return movies;
	}
	
	// other_user_id
	private static Set<Integer> getOtherUser_id(int user_id){
		Set<Integer> other_user_id = new HashSet<Integer>();
		for(RateInfo rateInfo:rateInfo_list){
			int id = rateInfo.getUser_id();
			if(id != user_id)
				other_user_id.add(id);
		}
		return other_user_id;
	}
	
	// sort
	private static List<Map.Entry<Integer,Double>> mapSort(Map<Integer,Double> map){
		List<Map.Entry<Integer,Double>> sorted_map = new ArrayList<>(map.entrySet()); // map to list
		Collections.sort(sorted_map, new Comparator<Map.Entry<Integer,Double>>() {
            public int compare(Map.Entry<Integer,Double> o1, Map.Entry<Integer,Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return sorted_map;
	}
	
	// select certain rate
	private static int getCertainRate(int user_id, int movie_id){
		int rate = 0;
		if(getMovies(user_id).contains(movie_id)){
			for(RateInfo rateInfo:rateInfo_list){
				if((rateInfo.getUser_id() == user_id)&&(rateInfo.getMovie_id() == movie_id)){
					rate = rateInfo.getRate();
					break;
				}
			}
		}
		return rate;
	}
}


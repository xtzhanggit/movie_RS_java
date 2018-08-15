package item;

import java.util.*;
import java.io.*;
import item.RateInfo;
import preprocess.Preprocess;

public class GenGraph{
	private String graph_path;
	private List<RateInfo> rateInfo_list;
	private Map<String, Map<String,Integer>> graph;
	
	public GenGraph(String graph_path){
		this.graph_path = graph_path;
		this.rateInfo_list = Preprocess.doPreprocess();
		genGraph();
	}
	
	// save
	public void saveGraphToTxt(){
		try{
            FileOutputStream outStream = new FileOutputStream(this.graph_path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(this.graph);
            outStream.close();
        }catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	// genGraph
	private void genGraph(){
		Set<Integer> user_ids = this.getUserIDs();
		Set<Integer> movie_ids = this.getMovieIDs();
		this.graph = new HashMap<String, Map<String,Integer>>();
		for(int user_id:user_ids) graph.put("user_"+user_id, this.genUserMap(user_id));
		for(int movie_id:movie_ids) graph.put("movie_"+movie_id, this.genMovieMap(movie_id));
	}
	
	// getUserIDs
	private Set<Integer> getUserIDs(){
		Set<Integer> user_ids = new HashSet<>(); 
		for(RateInfo rateInfo:this.rateInfo_list) user_ids.add(rateInfo.getUser_id());
		return user_ids;
	} 
	
	// getMovieIDs
	private Set<Integer> getMovieIDs(){
		Set<Integer> movie_ids = new HashSet<>(); 
		for(RateInfo rateInfo:this.rateInfo_list) movie_ids.add(rateInfo.getMovie_id());
		return movie_ids;
	} 
	
	// genUserMap
	private Map<String,Integer> genUserMap(int user_id){
		Set<Integer> movie_ids = this.getMovies(user_id);
		Map<String,Integer> graph_map = new HashMap<String,Integer>();
		for(int movie_id:movie_ids) graph_map.put("movie_"+movie_id,1);
		return graph_map;
	}
	
	// genMovieMap
	private Map<String,Integer> genMovieMap(int movie_id){
		Set<Integer> user_ids = this.getUsers(movie_id);
		Map<String,Integer> graph_map = new HashMap<String,Integer>();
		for(int user_id:user_ids) graph_map.put("user_"+user_id,1);
		return graph_map;
	}
	
	// select users accroding to movie_id
	private Set<Integer> getUsers(int movie_id){
		Set<Integer> users = new HashSet<Integer>();
		for(RateInfo rateInfo:this.rateInfo_list){
			if(rateInfo.getMovie_id() == movie_id)
				users.add(rateInfo.getUser_id());
		}
		return users;
	}
	
	// select movies accroding to user_id
	private Set<Integer> getMovies(int user_id){
		Set<Integer> movies = new HashSet<Integer>();
		for(RateInfo rateInfo:this.rateInfo_list){
			if(rateInfo.getUser_id() == user_id)
				movies.add(rateInfo.getMovie_id());
		}
		return movies;
	}
	  
}

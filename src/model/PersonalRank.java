package model;

import java.io.*;
import java.util.*;
import item.GenGraph;
import item.RateInfo;
import preprocess.Preprocess;

public class PersonalRank{
	private static String graph_path = "/home/xtzhang/movie_RS_java/data/graph.txt"; 
	private static List<RateInfo> rateInfo_list;
	
	public Map<String, Map<String,Integer>> graph;
	private double alpha = 0.6;
	private String user_id;
	private int maxCycles = 20;
	private int top_n;
	
	public PersonalRank(int user_id,int top_n){
		loadGraph();
		this.user_id = "user_" + user_id;
		this.top_n = top_n;
	}
    
    // recommend
    public List<Map.Entry<String,Double>> doRecommend(){
    	File file = new File(graph_path);	
		if (file.exists()) System.out.println("graph exists");
		else{
			System.out.println("graph not exists");
			GenGraph genGraph = new GenGraph(graph_path);
			genGraph.saveGraphToTxt();
		}
		
		List<Map.Entry<String,Double>> sorted_rank = this.doRank();
		List<Map.Entry<String,Double>> final_rank = this.doDelete(sorted_rank);
		
		return final_rank.subList(0,this.top_n);
    }
    
    // delete
    public List<Map.Entry<String,Double>> doDelete(List<Map.Entry<String,Double>> sorted_rank){
    	int user_id = Integer.parseInt(this.user_id.replaceAll("user_",""));
		rateInfo_list = Preprocess.doPreprocess(); 
		Set<String> target_movies = getMovies(user_id);
		
		Iterator<Map.Entry<String,Double>> iterator = sorted_rank.iterator();
		while(iterator.hasNext()){
			Map.Entry<String,Double> temp = iterator.next();
			String key = temp.getKey();
			if((key.indexOf("user_") != -1) || (target_movies.contains(key)))
				iterator.remove();
		}
		return sorted_rank;
    }
    
    // rank
    public List<Map.Entry<String,Double>> doRank(){
    	Map<String,Double> rank = new TreeMap<String,Double>();
    	for(String key:graph.keySet()) rank.put(key,0.0000);
    	rank.put(this.user_id,1.0000);
    	
    	for(int step=0;step<this.maxCycles;step++){
    		Map<String,Double> tmp = new TreeMap<String,Double>();
    		for(String key:graph.keySet()) tmp.put(key,0.0000);
    		
			for(Map.Entry<String, Map<String,Integer>> i:this.graph.entrySet()){
				String key = i.getKey();
				Map<String,Integer> value = i.getValue();
				for(String j:value.keySet()){
					if(!tmp.containsKey(j)) tmp.put(j,0.0000);
					double past_value = tmp.get(j);
					tmp.put(j,past_value + this.alpha * rank.get(key) / value.size());
				}
			}
			tmp.put(this.user_id, tmp.get(user_id) + 1 - this.alpha);
			rank = tmp;
    	}
    	List<Map.Entry<String,Double>> sorted_rank = mapSort(rank);
		return sorted_rank;
    }
    
    // loadGraph
    private void loadGraph(){
		try{
		    FileInputStream freader = new FileInputStream(graph_path);
		    ObjectInputStream objectInputStream = new ObjectInputStream(freader);
		    HashMap<String,Object> map = new HashMap<String,Object>();
		    this.graph = (HashMap<String, Map<String,Integer>>)objectInputStream.readObject();
		}catch (Exception e){
		    e.printStackTrace();
		}
    }
    
    // sort
	private static List<Map.Entry<String,Double>> mapSort(Map<String,Double> map){
		List<Map.Entry<String,Double>> sorted_map = new ArrayList<>(map.entrySet()); // map to list
		Collections.sort(sorted_map, new Comparator<Map.Entry<String,Double>>() {
            public int compare(Map.Entry<String,Double> o1, Map.Entry<String,Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return sorted_map;
	}
    
    // select movies accroding to user_id
	private static Set<String> getMovies(int user_id){
		Set<String> movies = new HashSet<String>();
		for(RateInfo rateInfo:rateInfo_list){
			if(rateInfo.getUser_id() == user_id)
				movies.add("movie_" + Integer.toString(rateInfo.getMovie_id()));
		}
		return movies;
	}
}

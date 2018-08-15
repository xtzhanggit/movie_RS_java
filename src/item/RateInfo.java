package item;

public class RateInfo{
	private int user_id;
	private int movie_id;
	private int rate;
	
	public RateInfo(){}
	
	public RateInfo(int user_id,int movie_id,int rate){
		this.user_id = user_id;
		this.movie_id = movie_id;
		this.rate = rate;
	}
	
	public int getUser_id(){
		return this.user_id;
	}

	public int getMovie_id(){
		return this.movie_id;
	}
	
	public int getRate(){
		return this.rate;
	}
}

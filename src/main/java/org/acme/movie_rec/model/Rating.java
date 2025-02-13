package org.acme.movie_rec.model;

public class Rating {
 
	private Integer userId;
	private Integer movieId;
	private Double rating;
			
	public Rating(Integer userId, Integer movieId, Double rating) {
		super();
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getMovieId() {
		return movieId;
	}
	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
}
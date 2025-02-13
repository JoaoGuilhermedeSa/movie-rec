package org.acme.movie_rec.model;

import java.util.List;

public class Movie {

	private Integer userId;
	private Integer movieId;
	private Double rating;
	private String title;
	private List<String> cast;
	private String director;
	private List<String> keywords;

	public Movie(Integer userId, Integer movieId, Double rating, String title, List<String> cast, String director,
			List<String> keywords) {
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
		this.title = title;
		this.cast = cast;
		this.director = director;
		this.keywords = keywords;
	}

	public Movie(Integer userId, Integer movieId, Double rating) {
		super();
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
	}
	
	public Movie(Integer movieId, String title) {
		super();
		this.title = title;
		this.movieId = movieId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getCast() {
		return cast;
	}

	public void setCast(List<String> cast) {
		this.cast = cast;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	
}
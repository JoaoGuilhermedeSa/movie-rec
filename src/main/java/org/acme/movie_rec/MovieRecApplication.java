package org.acme.movie_rec;

import org.acme.movie_rec.filter.RecomendationFilter;
import org.acme.movie_rec.filter.impl.CollaborativeFilter;

public class MovieRecApplication {

	public static void main(String[] args) {

		RecomendationFilter filter = new CollaborativeFilter();
		filter.doFilter();

	}

}

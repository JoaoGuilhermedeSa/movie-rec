package org.acme.movie_rec.similarity;

import org.acme.movie_rec.model.Movie;
import org.apache.commons.text.similarity.JaccardSimilarity;

public class MovieSimilarity {
	public static double calculateSimilarity(Movie movie1, Movie movie2) {
		JaccardSimilarity jaccard = new JaccardSimilarity();

		// Similaridade de elenco
		double castSimilarity = jaccard.apply(String.join(" ", movie1.getCast()), String.join(" ", movie2.getCast()));

		// Similaridade de diretor
		double directorSimilarity = movie1.getDirector().equals(movie2.getDirector()) ? 1.0 : 0.0;

		// Similaridade de palavras-chave
		double keywordSimilarity = jaccard.apply(String.join(" ", movie1.getKeywords()),
				String.join(" ", movie2.getKeywords()));

		// Combinação das similaridades (ajuste os pesos conforme necessário)
		return 0.5 * castSimilarity + 0.3 * keywordSimilarity + 0.2 * directorSimilarity;
	}
}

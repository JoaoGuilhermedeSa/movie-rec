package org.acme.movie_rec.filter;

import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface RecomendationFilter {

	public Rating[] doFilter(Dataset<Row> dataSet, int userId, int pageSize);

}

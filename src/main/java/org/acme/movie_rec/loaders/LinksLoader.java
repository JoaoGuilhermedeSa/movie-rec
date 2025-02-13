package org.acme.movie_rec.loaders;

import static org.apache.spark.sql.functions.col;

import org.acme.movie_rec.helper.FileHelper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class LinksLoader {

	public static Dataset<Row> loadLinks(SparkSession spark, boolean useSmall) {

		System.out.println("Reading links. Use small = " + useSmall);

		Dataset<Row> response = spark.read().option("header", "true").option("inferSchema", "true")
				.csv(FileHelper.linksPath(useSmall))
				.select(col("movieId").cast("int"), col("imdbId").cast("int"), col("tmdbId").cast("int"));

		System.out.println("Loaded " + response.count() + " links lines");

		return response;
	}
}

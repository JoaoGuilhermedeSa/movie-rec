package org.acme.movie_rec.loaders;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;

import org.acme.movie_rec.helper.FileHelper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;

public class MovieLoader {

	public static Dataset<Row> loadMovies(SparkSession spark) {

		DataType genresSchema = DataTypes.createArrayType(DataTypes.createStructType(
				new StructField[] { DataTypes.createStructField("name", DataTypes.StringType, true) }));

		System.out.println("Reading movies.");
		
		Dataset<Row> response = spark.read().option("header", "true").option("inferSchema", "true")
				.csv(FileHelper.moviesPath()).withColumn("genres", from_json(col("genres"), genresSchema))
				.select(col("id").cast("int"), col("title"), col("genres"), col("popularity").cast("double"),
						col("vote_average").cast("double"), col("vote_count").cast("int"));
		// .filter("id IS NOT NULL");

		System.out.println("Loaded " + response.count() + " movies.");

		return response;
	}

}
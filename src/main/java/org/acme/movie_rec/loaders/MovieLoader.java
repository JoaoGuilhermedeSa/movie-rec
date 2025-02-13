package org.acme.movie_rec.loaders;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;

import java.util.List;

import org.acme.movie_rec.helper.FileHelper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class MovieLoader {

	public static Dataset<Row> loadMovies(SparkSession spark) {

		 Dataset<Row> movies = spark.read()
	                .option("header", "true")
	                .option("inferSchema", "false") // Disable schema inference
	                .csv(FileHelper.moviesPath());

	        // Define the schema for the 'genres' column (array of structs)
	        StructType genreSchema = DataTypes.createStructType(List.of(
	                DataTypes.createStructField("id", DataTypes.IntegerType, true), // id can be null
	                DataTypes.createStructField("name", DataTypes.StringType, true) // name can be null
	        ));
	        ArrayType genresArraySchema = DataTypes.createArrayType(genreSchema);

	        movies = movies.na().fill("", new String[]{"title"}); // Ensure you have no nulls in the title at least

	        // 2. Parse the JSON columns using from_json:
	        movies = movies.withColumn("genres", from_json(col("genres"), genresArraySchema));
	        
			// Cast columns to the appropiate types
			movies = movies.withColumn("id", col("id").cast(DataTypes.IntegerType));
	        movies = movies.withColumn("popularity", col("popularity").cast(DataTypes.DoubleType));
			movies = movies.withColumn("vote_average", col("vote_average").cast(DataTypes.DoubleType));
			movies = movies.withColumn("vote_count", col("vote_count").cast(DataTypes.IntegerType));
		System.out.println("Loaded " + movies.count() + " valid movies.");

		return movies;
	}

}
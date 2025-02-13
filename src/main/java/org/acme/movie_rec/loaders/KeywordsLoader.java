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

public class KeywordsLoader {

	public static Dataset<Row> loadKeywords(SparkSession spark) {
		
		DataType keywordsSchema = DataTypes.createArrayType(DataTypes.createStructType(
				new StructField[] { DataTypes.createStructField("name", DataTypes.StringType, true) }));

		System.out.println("Reading keywords.");

		 Dataset<Row> response = spark.read().option("header", "true").option("inferSchema", "true").csv(FileHelper.keywordsPath())
				.withColumn("keywords", from_json(col("keywords"), keywordsSchema)).select("id", "keywords");
		 
			System.out.println("Loaded " + response.count() + " keyword lines.");

		 
		 return response;
	}
}
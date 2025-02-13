package org.acme.movie_rec.loaders;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;

public class KeywordsLoader {
	private static final String KEYWORDS_CSV = "src/main/resources/keywords.csv";

	public static Dataset<Row> loadKeywords(SparkSession spark) {
		DataType keywordsSchema = DataTypes.createArrayType(DataTypes.createStructType(
				new StructField[] { DataTypes.createStructField("name", DataTypes.StringType, true) }));

		return spark.read().option("header", "true").option("inferSchema", "true").csv(KEYWORDS_CSV)
				.withColumn("keywords", from_json(col("keywords"), keywordsSchema)).select("id", "keywords");
	}
}
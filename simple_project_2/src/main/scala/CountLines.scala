/* CountLines.scala */
import org.apache.spark.sql.SparkSession

object CountLines {
  def main(args: Array[String]): Unit = {
    val logFile = "/opt/spark/examples/src/main/scala/org/apache/spark/examples/SparkRemoteFileTest.scala" // Should be some file on your system
    val spark = SparkSession.builder.appName("Count Lines").getOrCreate()
    val logData = spark.read.textFile(logFile).filter(line => line.contains("Spark")).cache()
    val result = logData.count()
    println(s"Lines with Spark: $result")
    spark.stop()
  }
}
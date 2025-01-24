Démarrer le shell scala interactif sur un noeud unique

```bash
docker run -it spark /opt/spark/bin/spark-shell
```

Sur Windows, pour accéder directement au conteneur

```bash
docker run --name myspark --rm -it -p 4040:4040 spark /opt/spark/bin/spark-shell
```


Récupérer l'adresse IP privée du conteneur

```bash
docker inspect myspark --format '{{.NetworkSettings.Networks.bridge.IPAddress}}'
```

Accéder via le navigateur à

http://ip:4040/

ou si mappage de port

http://localhost:4040

Consulter les fichiers disponibles en local dans le conteneur spark


```bash
docker exec -it beautiful_lamport bash
```



Script Scala pour démarrer

```scala
val textFile = spark.read.textFile("/opt/spark/examples/src/main/scala/org/apache/spark/examples/SparkRemoteFileTest.scala")

textFile.filter(line => line.contains("Spark")).count()

val countWords = (line: String) => line.split(" ").size

textFile.map(countWords).reduce((a,b) => if (a > b) a else b)


import java.lang.Math

textFile.map(countWords).reduce((a,b) => Math.max(a,b))

val wordCounts = textFile.flatMap(line => line.split(" ")).groupByKey(identity).count()

wordCounts.collect()

val linesWithSpark = textFile.filter(line => line.contains("Spark"))
linesWithSpark.count()

linesWithSpark.cache()

linesWithSpark.count()

```

Compiler l'application simple_project

```bash
sbt package
```

Compiler l'application simple_project depuis un conteneur Docker.

```bash
docker pull sbtscala/scala-sbt:eclipse-temurin-17.0.13_11_1.10.7_2.12.20
docker run --rm -v "$(pwd)/simple_project":/app -w /app sbtscala/scala-sbt:eclipse-temurin-17.0.13_11_1.10.7_2.12.20 sbt package
```

Déployer et exécuter l'application dans un conteneur Docker

```bash
docker cp simple_project/target/scala-2.12/simple-project_2.12-1.0.jar beautiful_lamport:/opt/spark/work-dir/

```

Utiliser l'application dans un container existant

```bash
docker exec -it beautiful_lamport /opt/spark/bin/spark-submit --class SimpleApp --master "local[4]" /opt/spark/work-dir/simple-project_2.12-1.0.jar
```

Utiliser l'application dans un nouveau container

```bash
docker run --rm --name spark_submit_tmp -v "$(pwd)/simple_project/target/scala-2.12":/opt/spark/work-dir -it spark /opt/spark/bin/spark-submit --class SimpleApp --master "local[4]" /opt/spark/work-dir/simple-project_2.12-1.0.jar
```
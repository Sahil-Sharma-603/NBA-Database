
build: basketball.class

basketball.class: basketball.java
	javac basketball.java

run: basketball.class
	java -cp .:mssql-jdbc-11.2.0.jre11.jar basketball.java

clean:
	rm basketball.class

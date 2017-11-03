default:
	javac -cp ".:./Dependencies/apache/commons-cli-1.3.1.jar:./Dependencies/ini4j/ini4j.jar" App.java 
	javac -cp ".:./Dependencies/apache/commons-cli-1.3.1.jar:./Dependencies/ini4j/ini4j.jar" ./Collapse/*.java
	javac -cp ".:./Dependencies/apache/commons-cli-1.3.1.jar:./Dependencies/ini4j/ini4j.jar" ./Minesweeper/*.java
	javac -cp ".:./Dependencies/apache/commons-cli-1.3.1.jar:./Dependencies/ini4j/ini4j.jar" ./Hurkle/*.java

clean:
	rm App.class
	rm Framework/*.class
	rm Collapse/*.class
	rm Minesweeper/*.class
	rm Hurkle/*.class

run:
	java -cp ".:./Dependencies/apache/commons-cli-1.3.1.jar:./Dependencies/ini4j/ini4j.jar" App -p Collapse
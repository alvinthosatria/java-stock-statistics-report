# Languages & Tools
Java 21, Maven, JUnit 5
To run coverage report please run

**mvn jacoco:prepare-agent test install jacoco:report**

Navigate to target -> site -> jacoco -> index.html

# Assumption

scandi.csv could be read from external sources, hence not included in this project. 

# Execute

To run successfully please include the scandi.csv inside this path **src/main/java/resources/** and update the environment variables respectively

# Report analysis

Navigate to this path **src/main/java/resources/report.csv** to find the report.

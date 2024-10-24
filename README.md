# Languages & Tools
Java 21, Maven, JUnit 5.

To run coverage report please run:

**mvn jacoco:prepare-agent test install jacoco:report**

Then navigate to target -> site -> jacoco -> index.html to find test coverage report

# Test coverage report reference

<img width="997" alt="Screenshot 2024-10-10 at 6 59 24 PM" src="https://github.com/user-attachments/assets/7f7df3bc-9046-4906-9f35-9dbb71cdf445">

# Assumption

Dataset could be read from external sources (e.g. cloud, database) hence not included in this project.

# Execute

To run successfully please include the dataset inside this path **src/main/java/resources/** and update the environment variables respectively

# Report analysis

Navigate to this path **src/main/java/resources/report.csv** to find the report.

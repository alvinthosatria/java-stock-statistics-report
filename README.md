# Languages & Tools
Java 21, Maven, JUnit 5
To run coverage report please run

**mvn jacoco:prepare-agent test install jacoco:report**

Navigate to target -> site -> jacoco -> index.html

# Sample report View

<img width="1014" alt="Screenshot 2024-10-09 at 5 21 54 PM" src="https://github.com/user-attachments/assets/ecd4ee6a-5207-4c18-9aaf-d734f5a390c0">
Link: https://docs.google.com/spreadsheets/d/1dqI90f4Zjja1ApYAggC2eMwOuyM_087l9RKWC6wvV54/edit?usp=sharing

Median time between trades is analyzed to be 0 as it usually indicates a highly active market where multiple trades occur simultaneously or within a very short time frame. 
Median time between tick changes is analyzed to be 0 as the data being measured has frequent and continuous changes, leading to many intervals of zero time between changes. 

# Test coverage report reference

<img width="997" alt="Screenshot 2024-10-10 at 6 59 24 PM" src="https://github.com/user-attachments/assets/00057df6-d204-4247-ac8e-78db5d7f1b7f">

# Assumption

scandi.csv could be read from external sources, hence not included in this project. 

# Execute

To run successfully please include the scandi.csv inside this path **src/main/java/resources/** and update the environment variables respectively

# Report analysis

Navigate to this path **src/main/java/resources/report.csv** to find the report.

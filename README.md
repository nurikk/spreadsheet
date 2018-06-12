# Pre requisites:
	```bash
	brew install maven
    ```

## Build
    ```bash
    mvn package
    ```
    
## Run
    takes 96 seconds to run on 2.9 GHz Intel Core i7
    ```bash
	time java -jar target/pw-cucumber-dbs-1.0-SNAPSHOT-jar-with-dependencies.jar -i ./mocks/100000cols.csv  -o ./out.csv
    ``` 
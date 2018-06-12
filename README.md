
[Original challenge text](https://github.com/blakeembrey/code-problems/tree/master/problems/spreadsheet)

A spreadsheet consists of a two-dimensional array of cells, labeled A0, A1, etc.

Rows are identified using letters, columns by numbers. Each cell contains either an integer (its value) or an expression. 

Expressions always start with a ‘=’ and can contain integers, cell references, operators ‘+’, ‘-‘, ‘*’, ‘/’ and parentheses ‘(‘, ‘)’ with the usual rules of evaluation.

Write a program to read the input from a file, evaluate the values of all the cells, and write the output to an output file.

The input and output files should be in CSV format.

For example, the following CSV input:
```csv
2,4,1,=A0+A1*A2
=A3*(A0+1),=B2,0,=A0+1
```
should produce the following output file:
```csv
2.00000,4.00000,1.00000,6.00000
18.00000,0.00000,0.00000,3.00000
```

# Pre requisites:
```bash
brew install maven
```

## Build
```bash
mvn package
```
    
## Run
takes 96 seconds to run on 2.9 GHz Intel Core i7 100_000cols*26 rows with 10% formulas
```bash
time java -jar target/pw-cucumber-dbs-1.0-SNAPSHOT-jar-with-dependencies.jar -i ./mocks/100000cols.csv  -o ./out.csv
``` 

# Mapping Protein Evolution

## Purpose

The purpose of this project is to simulate the algorithmic and experimental 
basis of phylogenetic analysis, and to demonstrate active site discovery.  

## Project Description

This project generates two trees from an input matrix. The matrix stores
the distances between sequences. "Distance" here is a representation of 
the genetic similarities of the sequences, and our goal is to connect 
the sequences in trees that group the sequences based on their genetic 
similarity.

Two trees are generated--UPGMA and Neighbor Joining Trees. Both have their
strengths and weaknesses in representing the sequence connections. They are
represented in Newick format in output files.

## Instructions

- This program is implemented using `java`. As a result, it will require a
Java Runtime Environment (JRE) in order to run
- If an input matrix is not provided, it can be generated using output from
ClustalW
    - Use ClustalW to peform a sequence alignment on your matrices by inputting
    a fasta file
    - Run `python clustal2matrix.py [clustalOutput] [matrixFile]` to generate
    a matrix and have it stored in `matrixFile`
- Run `javac Main.java` to compile the jave program and generate an executable
- Run `java Main.java`
- Enter the name of the input matrix file when prompted 

## Test Run

Matrix file called `matrix` is provided as follows:
```
       5
3MXE_A|PDB 
3MXE_B|PDB 0.0  
3PJ6_A|PDB 0.14  0.14  
3QIN_A|PDB 0.95  0.95  0.95  
3QIO_A|PDB 0.95  0.95  0.95  0.0
```

Run the following lines of code:

 `javac Main.java`
 `java Main.java`
 
```
    Enter File Name:
    matrix
```


The outputs are provided in two different files.
Below are the contents or matrixUPGA:
```
    ((3QIN_A|PDB*:0.0,3QIO_A|PDB*:0.0):0.0,(3PJ6_A|PDB*:0.0,(3MXE_A|PDB*:0.0,3MXE_B|PDB*:0.0):0.0):0.07):0.47500000000000003
```

Below are the contents or matrixNJ:
```
    root, 3QIN_A|PDB*3MXE_A|PDB*3MXE_B|PDB*, 0.0
    3PJ6_A|PDB*3QIO_A|PDB*, 0.0
```
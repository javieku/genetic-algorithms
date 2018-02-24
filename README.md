# About the project 
Some evolutionary algorithms implemented in java:

##  Basic genetic algorithm.

**Initialization**. An initial random population gets generated.
**Evaluation**. An adaptation function is applied to each chromosome in the population.
**Loop**. Next n iterations of the following steps:
- Selection An intermediate population gets generated by rulette wheel or tournament selection on 3-sized groups.
- Reproduction Some chromosomes of the intermediate population  are selected for reproduction depending on a
crossover probability. Then, chromosomes are taken in pairs, a one-point crossover is performed and and descendants and
progenitors are mixed in the resulting population where the best are selected (inclusion replacement).
- For each individual of the population part of its cromosom is mutated randomly.
- Evaluation. There a distinction between evaluation and adaption functions. On the evaluation function results a
correction factor is applied to guarantee that the selection process works properly.
There was a simple elitism mecanishm implemented where some of the best individuals of the population were saved before
the reproduction and merged again once this was done.

## Optimal group selection
Extends the evolutionary algorithm to implement an algorithm to optimize the student
  distribution in an automatic group assignment system.
## Koza's algorithm
Algorithm to make an artificial ant follow an irregular food path. Such as the following map where the
ones represent the food path.


The project also contains a GUI that shows statistics about the population evolution.
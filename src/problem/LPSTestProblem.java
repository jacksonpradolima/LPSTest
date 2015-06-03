/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import util.InstanceReader;

/**
 *
 * @author Prado Lima
 *
 */
/**
 * Class representing problem MutationTest. The problem consist of, given a set
 * of test suites, maximizing it coverage and minimizing the number of test
 * suites.
 */
public class LPSTestProblem extends Problem {

    private int[][] coverageMutantes;

    private int numberOfTestSuite;

    private int numberOfMutants;

    private int numberOfTestSuitePairs;

    private int numberOfPairs;

    private int[][] coveragePairwise;
    
    public void defaultJMetalSettings() {
        // JMetal's Settings
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        numberOfVariables_ = 1;
        length_ = new int[numberOfVariables_];
        length_[0] = numberOfTestSuite;
        problemName_ = "Mutant Test Problem";
        solutionType_ = new BinarySolutionType(this);
    }

    public LPSTestProblem(int numberOfTestSuite, int numberOfMutants, int[][] coverage) {
        this.coverageMutantes = coverage;
        this.numberOfMutants = numberOfMutants;
        this.numberOfTestSuite = numberOfTestSuite;

        defaultJMetalSettings();
    }

    public LPSTestProblem(String filename) {
        readMutants(filename);
        //readPairwise(filename);
        defaultJMetalSettings();
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        //variable = vector positions
        Binary s = (Binary) solution.getDecisionVariables()[0];

        // Maximizar o score de mutação
        solution.setObjective(0, getMutantionScore(s) * -1);
        
        // Minimizar o número de casos de teste 
        solution.setObjective(1, getNumberOfSelectedTestSuite(s));
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        Binary s = (Binary) solution.getDecisionVariables()[0];
        int numberOfSelectedTestSuite = getNumberOfSelectedTestSuite(s);
        
        // Ao menos um caso de teste deve estar selecionado
        if (numberOfSelectedTestSuite == 0) {
            int random = PseudoRandom.randInt(0, s.getNumberOfBits() - 1);
            s.setIth(random, true);
            evaluate(solution);
        }
    }

    private void readMutants(String filename) {
        // filename = instances/cas
        String path = String.format("%s/%s", filename, "PROD_MUTANTS_BinaryMatrix.txt");

        System.out.println("Nome do arquivo: " + path);
        // Read Instance's file
        InstanceReader reader = new InstanceReader(path);

        reader.open();
        this.numberOfTestSuite = reader.readInt();
        this.numberOfMutants = reader.readInt();
        this.coverageMutantes = reader.readIntMatrix(numberOfMutants, numberOfTestSuite, " ");
        reader.close();

        System.out.println("Número de  casos de teste (produtos): " + this.numberOfTestSuite);
        System.out.println("Número de mutantes: " + this.numberOfMutants);
    }

    private void readPairwise(String filename) {
        // filename = instances/cas
        String path = String.format("%s/%s", filename, "PROD_PAIRS_BinaryMatrix.txt");

        System.out.println("Nome do arquivo: " + path);
        // Read Instance's file
        InstanceReader reader = new InstanceReader(path);

        reader.open();
        this.numberOfTestSuitePairs = reader.readInt();
        this.numberOfPairs = reader.readInt();
        this.coveragePairwise = reader.readIntMatrix(numberOfPairs, numberOfTestSuitePairs, " ");
        reader.close();

        System.out.println("Número de casos de teste (produtos): " + this.numberOfTestSuitePairs);
        System.out.println("Número de pares: " + this.numberOfPairs);
    }

    public double getMutantionScore(Binary s) {
        int deadMutants = getNumberOfDifferentKilledMutants(s); //dm(p,t)
        double totalMutants = numberOfMutants; //m(p)
        return (double) deadMutants / (double) totalMutants; //ms(p,t)
    }

    public int getNumberOfSelectedTestSuite(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                total++;
            }
        }

        return total;
    }

    public int getNumberOfDifferentKilledMutants(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int[] visited = new int[numberOfMutants];
        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                // Test Suite was selected by metaheurist
                for (int j = 0; j < numberOfMutants; j++) {
                    if (coverageMutantes[j][i] == 1 && visited[j] == 0) {
                        // Test Suit has not yet been visited
                        visited[j] = 1;
                        total++;
                    }
                }
            }
        }

        return total;
    }

    public int getNumberOfTestSuite() {
        return numberOfTestSuite;
    }

    public void setNumberOfTestSuite(int numberOfTestSuite) {
        this.numberOfTestSuite = numberOfTestSuite;
    }

    public int getNumberOfMutants() {
        return numberOfMutants;
    }

    public void setNumberOfMutants(int numberOfMutants) {
        this.numberOfMutants = numberOfMutants;
    }

    public boolean isKilled(int testSuite, int mutant) {
        return coverageMutantes[mutant][testSuite] == 1;
    }
}

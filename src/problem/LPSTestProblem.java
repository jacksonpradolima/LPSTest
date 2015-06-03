/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
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
 * Class representing problem LPS Test. The problem consist of, given a set of
 * test suites, maximizing it coverage and minimizing the number of test suites.
 */
public class LPSTestProblem extends Problem {

    private int[][] coverageMutantes;

    private int numberOfTestSuite;

    private int numberOfMutants;

    private int numberOfPairs;

    private int[][] coveragePairwise;

    protected List<String[]> produto; //lista de caracteristicas por produto
    protected List<String> caract;  //lista de caracteristicas (distintas)
    protected List<int[]> prodCaract;  //lista de produtos x caracteriscas (binário)    
    private boolean _read;

    public void defaultJMetalSettings() {
        // JMetal's Settings
        numberOfObjectives_ = 5;
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
        this._read = false;
        this.produto = new ArrayList<String[]>();
        readMutants(filename);
        readPairwise(filename);
        readProd(filename);
        defaultJMetalSettings();
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        //variable = vector positions
        Binary s = (Binary) solution.getDecisionVariables()[0];

        // Minimizar o número de casos de teste 
        solution.setObjective(0, getTestScore(s));

        // Minimizar o custo de características
        solution.setObjective(1, getCostScore(s));

        // Maximizar o score de mutação
        solution.setObjective(2, getMutantionScore(s) * -1);

        // Maximizar o score de pairwise
        solution.setObjective(3, getPairwiseScore(s) * -1);

        // Maximizar a preferência do usuário
        solution.setObjective(4, getPreferenceScore(s) * -1);
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        Binary s = (Binary) solution.getDecisionVariables()[0];

        // Ao menos um caso de teste deve estar selecionado
        if (getNumberOfSelectedTestSuite(s) == 0) {
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
        this.numberOfTestSuite = reader.readInt();
        this.numberOfPairs = reader.readInt();
        this.coveragePairwise = reader.readIntMatrix(numberOfPairs, numberOfTestSuite, " ");
        reader.close();

        System.out.println("Número de casos de teste (produtos): " + this.numberOfTestSuite);
        System.out.println("Número de pares: " + this.numberOfPairs);
    }

    private void readProd(String filename) {
        // filename = instances/cas
        String path = String.format("%s/%s", filename, "PROD_");

        System.out.println("Nome do arquivo: " + path);

        // Read Instance's file
        InstanceReader reader = new InstanceReader(path);

        reader.open();
        String line = null;
        caract = new ArrayList<>();
        //primeira leitura do arquivo...
        //inicializa caract e produtos
        while ((line = reader.readLine()) != null) {
            String[] part = line.split(" : ");
            if (part.length > 1) {
                String[] mut = part[1].split(" , ");

                for (String s : mut) {
                    s = s.trim();
                    //cria uma lista de caracteríticas únicas
                    if (!caract.contains(s)) {
                        caract.add(s);
                    }
                }
                produto.add(mut);
            }
        }
        prodCaract = new ArrayList<>(produto.size());
        for (String[] prod : produto) {
            int[] aux = new int[caract.size()];
            for (String s : prod) {
                int index = caract.indexOf(s);
                if (index >= 0) {
                    aux[index] = 1;
                }
            }
            //adiciona o vetor para a lista
            prodCaract.add(aux);
        }
        reader.close();
        this._read = true;

        System.out.println("Número de casos de teste (produtos): " + produto.size());
        System.out.println("Número de características: " + caract.size());
    }

    public double getTestScore(Binary s) {
        return (double) (getNumberOfSelectedTestSuite(s) / this.numberOfTestSuite);
    }

    public double getMutantionScore(Binary s) {
        int deadMutants = getNumberOfDifferentKilledMutants(s); //dm(p,t)
        double totalMutants = numberOfMutants; //m(p)
        return (double) deadMutants / (double) totalMutants; //ms(p,t)
    }

    public double getPairwiseScore(Binary s) {
        return (double) getNumberOfDifferentPairwise(s) / this.numberOfPairs;
    }

    public double getCostScore(Binary s) {
        return getSumCost(s);
    }

    public double getPreferenceScore(Binary s) {
        return getSumPreference(s);
    }

    /**
     *
     * @param solution
     * @return Number of test cases (Products)
     */
    public double getNumberOfSelectedTestSuite(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                total++;
            }
        }

        return (double) total;
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

    public int getNumberOfDifferentPairwise(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int[] visited = new int[numberOfPairs];
        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                // Test Suite was selected by metaheurist
                for (int j = 0; j < numberOfPairs; j++) {
                    if (coveragePairwise[j][i] == 1 && visited[j] == 0) {
                        // Test Suit has not yet been visited
                        visited[j] = 1;
                        total++;
                    }
                }
            }
        }

        return total;
    }

    public double getSumCost(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int nCaract = GetNumCaracteristicas();

        int[] vetorCusto = InicializaVetorValor(nCaract);

        //produtos linhas x caracteristicas colunas        
        int[][] prodCaract = GetProdCaracteristicas();

        float[] custoProd = CalculaValorProduto(vetorCusto, prodCaract); //vetor com o custo de cada produto        

        double total = 0;

        int countProdSelected = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                // Pega o valor do custo do produto selecionado
                total = total + custoProd[i];
                countProdSelected++;
            }
        }

        return total / countProdSelected;
    }

    public double getSumPreference(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int nCaract = GetNumCaracteristicas();

        int[] vetorPreferencia = InicializaVetorValor(nCaract);

        //produtos linhas x caracteristicas colunas        
        int[][] prodCaract = GetProdCaracteristicas();

        float[] preferenciaProd = CalculaValorProduto(vetorPreferencia, prodCaract); //vetor com a preferencia de cada produto

        double total = 0;
        int countProdSelected = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                // Pega o valor da preferência do usuário para o produto selecionado
                total = total + preferenciaProd[i];
                countProdSelected++;
            }
        }

        return total / countProdSelected;
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

    public int getNumberOfPairs() {
        return numberOfPairs;
    }

    public void setNumberOfPairs(int numberOfPairs) {
        this.numberOfPairs = numberOfPairs;
    }

    public boolean isKilled(int testSuite, int mutant) {
        return coverageMutantes[mutant][testSuite] == 1;
    }

    public boolean isPairSelected(int testSuite, int pair) {
        return coveragePairwise[pair][testSuite] == 1;
    }

    /**
     *
     * @return matrix lista de produtos x caracteriscas
     */
    public int[][] GetProdCaracteristicas() {
        int[][] retorno = new int[produto.size()][caract.size()];

        for (int i = 0; i < prodCaract.size(); i++) {
            retorno[i] = prodCaract.get(i);
        }
        return retorno;
    }

    /**
     *
     * @return Número de características encontradas
     */
    public int GetNumCaracteristicas() {
        if (_read) {
            return caract.size();
        } else {
            return -1;
        }
    }

    /**
     *
     * @return número de produtos encontrados
     */
    public int GetNumProdutos() {
        if (_read) {
            return produto.size();
        } else {
            return -1;
        }
    }

    public int[] InicializaVetorValor(int nCaract) {
        int min = 1;
        int max = 5;

        Random rand = new Random();
        int[] vetorValor = new int[nCaract];
        for (int i = 0; i < nCaract; i++) {
            vetorValor[i] = rand.nextInt((max - min) + 1) + min;
        }
        return vetorValor;
    }

    public float[] CalculaValorProduto(int[] vetorValor, int[][] prodCaract) {

        //valida os tamanhos dos vetores
        if (prodCaract.length <= 0 || vetorValor.length != prodCaract[0].length) {
            throw new VerifyError("Erro em CalculaValorProduto");
        }

        float[] valorProd = new float[prodCaract.length]; //numero de linhas = a n produtos

        int somatorioUn = IntStream.of(vetorValor).sum();

        for (int i = 0; i < prodCaract.length; i++) {
            float soma = 0;
            for (int j = 0; j < vetorValor.length; j++) {
                if (prodCaract[i][j] == 1) {
                    soma += (float) vetorValor[j] / somatorioUn;
                }
            }
            valorProd[i] = soma;
        }

        return valorProd; //vetor com um valor calculado para cada produto;
    }
}

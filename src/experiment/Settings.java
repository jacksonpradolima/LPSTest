/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

/**
 * Class that contains the configuration of LPS Test
 *
 * @author Prado Lima
 */
public class Settings {

    public static final String[] INSTANCES = {
        //"instances/cas",
        //"instances/eshop",
        //"instances/james",
        "instances/weatherstation"
    };

    public static final MetaheuristicType[] ALGORITHMS = {
        MetaheuristicType.NSGAII
        //MetaheuristicType.NSGAIII
    };

    public static final int[] POPULATION_SIZE = {
        50
        //100,
        //200
    };

    public static final int[] GENERATIONS = {
        100
        //300,
        //600
    };

    public static final double[] CROSSOVER_PROBABILITY = {
        //0.8,
        0.9
    };

    public static final double[] MUTATION_PROBABILITY = {
        0.005,
        //0.01
        //0.1
    };

    public static final String[] CROSSOVER_OPERATORS = {
        //"SinglePointCrossover",
        "UniformCrossoverBinary"
    };

    public static final String[] MUTATION_OPERATORS = {
        //"BitFlipMutation",
        "SwapMutationBinary"
    };

    public static final String[] SELECTION_OPERATORS = {
        "BinaryTournament2"
    };

    public static final int EXECUTIONS = 1;
}

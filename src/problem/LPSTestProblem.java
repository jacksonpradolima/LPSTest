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

    private final LPSTestProblemBase problemBase;

    /**
     * Default settings to use in jMetal
     */
    private void defaultJMetalSettings() {
        // JMetal's Settings
        numberOfObjectives_ = 5;
        numberOfConstraints_ = 0;
        numberOfVariables_ = 1;
        length_ = new int[numberOfVariables_];
        length_[0] = problemBase.getNumberOfTestSuite();
        problemName_ = "LPS Test Problem";
        solutionType_ = new BinarySolutionType(this);
    }

    public LPSTestProblem(String filename) {
        problemBase = new LPSTestProblemBase(filename);
        defaultJMetalSettings();
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        //variable = vector positions
        Binary s = (Binary) solution.getDecisionVariables()[0];

        // Minimize the number of test cases (Products)
        solution.setObjective(0, problemBase.getTestScore(s));

        // Minimize the cost of the characteristics
        solution.setObjective(1, problemBase.getCostScore(s));

        // Maximize the mutation score
        solution.setObjective(2, problemBase.getMutantionScore(s) * -1);

        // Maximize the pairwise score
        solution.setObjective(3, problemBase.getPairwiseScore(s) * -1);

        // Maximize the user preference
        solution.setObjective(4, problemBase.getPreferenceScore(s) * -1);
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        Binary s = (Binary) solution.getDecisionVariables()[0];

        // Leastwise a test case must be selected
        if (problemBase.getNumberOfSelectedTestSuite(s) == 0) {
            int random = PseudoRandom.randInt(0, s.getNumberOfBits() - 1);
            s.setIth(random, true);
            evaluate(solution);
        }
    }
}

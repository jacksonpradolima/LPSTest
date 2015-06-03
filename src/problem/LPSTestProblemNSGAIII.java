package problem;

import java.util.BitSet;

import jmetal.encodings.variable.Binary;
import jmetal.util.PseudoRandom;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.JMetalException;

public class LPSTestProblemNSGAIII extends AbstractBinaryProblem implements ConstrainedProblem<BinarySolution> {

    private static final long serialVersionUID = 7488414018042022225L;

    private final LPSTestProblemBase problemBase;

    public LPSTestProblemNSGAIII(String filename) {
        problemBase = new LPSTestProblemBase(filename);

        setNumberOfVariables(1);
        setNumberOfObjectives(5);
        setName("LPS Test Problem - NSGAIII");
    }

    @Override
    public void evaluate(BinarySolution solution) {
        //Binary s = (Binary) solution.getDecisionVariables()[0];
        BitSet bitset = solution.getVariableValue(0);

        Binary s = new Binary(problemBase.getNumberOfTestSuite());
        s.bits_ = bitset;

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
    protected int getBitsPerVariable(int index) {
        if (index != 0) {
            throw new JMetalException("Problem LPSTestProblemNSGAIII has only a variable. Index = " + index);
        }
        return problemBase.getNumberOfTestSuite();
    }

    @Override
    public void evaluateConstraints(BinarySolution solution) {
        BitSet bitset = solution.getVariableValue(0);

        Binary s = new Binary(problemBase.getNumberOfTestSuite());
        s.bits_ = bitset;

        // Leastwise a test case must be selected
        if (problemBase.getNumberOfSelectedTestSuite(s) == 0) {
            int random = PseudoRandom.randInt(0, s.getNumberOfBits() - 1);
            s.setIth(random, true);
            evaluate(solution);
        }
    }
}

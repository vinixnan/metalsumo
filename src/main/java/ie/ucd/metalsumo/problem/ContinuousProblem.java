package ie.ucd.metalsumo.problem;

import ie.ucd.metalsumo.problem.traci.SumoConnectionModel;
import java.util.Arrays;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

/**
 *
 * @author vinicius
 */
public class ContinuousProblem  extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution>{
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    protected SumoConnectionModel model;

    public ContinuousProblem() {
        setNumberOfVariables(5);
        setNumberOfObjectives(3);
        setName("ProblemDefinition");
        setNumberOfConstraints(10);
        // defining the lower and upper limits for each decision variable
        Double[] LOWERLIMIT = {0.3, 0.1, 0.01, 1.0, 1.0};
        Double[] UPPERLIMIT = {0.5, 0.3, 0.6, 1000.0, 1000.0};

        setLowerLimit(Arrays.asList(LOWERLIMIT));
        setUpperLimit(Arrays.asList(UPPERLIMIT));

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
        model=new SumoConnectionModel();
    }
    
    @Override
    public void evaluate(DoubleSolution solution) {
        double[] x = new double[solution.getNumberOfVariables()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i);
        }
        model.run(x);
        //define objectives
        solution.setObjective(0, 0);
        solution.setObjective(1, 0);
        solution.setObjective(2, 0);
    }
    
    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[getNumberOfConstraints()];
        double[] x = new double[solution.getNumberOfVariables()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i);
        }
        //define constraints
        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
            }
        }
        solution.setAttribute("overallConstraintViolationDegree", overallConstraintViolation);
        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}

package application;

import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTest {
    @Test
    public void testEvaluateExpr(){
        // ∧∨¬⊕
        assertFalse(Controller.evaluateExpr("1∧1∧¬1∨¬1"));
        assertTrue(Controller.evaluateExpr("¬¬∨¬0"));
        assertTrue(Controller.evaluateExpr("1∨¬(0∨0)"));
        assertTrue(Controller.evaluateExpr("1∧(1∧(¬(0∨0)))"));
        assertTrue(Controller.evaluateExpr("1∧(1∧(¬(0∨0)))"));
        assertFalse(Controller.evaluateExpr("1∧(1∧(¬(1∨0)))"));


        assertTrue(Controller.evaluateExpr("¬(0∨0)∨(0∧0)"));
        assertFalse(Controller.evaluateExpr("¬(0∨1)∨(0∧1)"));
        assertFalse(Controller.evaluateExpr("¬(1∨0)∨(1∧0)"));
        assertTrue(Controller.evaluateExpr("¬(1∨1)∨(1∧1)"));

        Controller.printTruthTable("a∧¬(y∨z)∨(y∧z)");
        System.out.println();
        Controller.printTruthTable("a∧c∧(((x∧(y∧z))∨(y∧z))∨(¬y⊕z))");
    }
}
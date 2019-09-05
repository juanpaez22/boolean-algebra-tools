package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

public class Controller {
    @FXML
    TextField main_field;

    enum Operation {
        AND, OR, XOR
    }

    public void buttonClickGenerate(){

    }

    public void buttonClickAnd() {
        String text = main_field.getText();
        text += " ∧ ";
        main_field.setText(text);
    }

    public void buttonClickOr() {
        String text = main_field.getText();
        text += " ∨ ";
        main_field.setText(text);
    }

    public void buttonClickNot() {
        String text = main_field.getText();
        text += " ¬ ";
        main_field.setText(text);
    }

    public void buttonClickXor() {
        String text = main_field.getText();
        text += " ⊕ ";
        main_field.setText(text);
    }

    public boolean equationIsValid(String str) {
        char prev = 0;
        Stack<Character> parentheses_stack = new Stack<>();
        for (char c : str.toCharArray()){
            if (c == '('){
                // Push to stack to count open parentheses.
                // Ensure a variable does not precede an open parenthesis.
                if (prev != 0 && !isTwoInputOperator(prev) && prev != '¬') return false;
                parentheses_stack.push(c);
            }
            else if (c == ')'){
                // Use stack to ensure balanced parentheses.
                // Ensure no empty parentheses and no close parenthesis after an operator.
                if (prev == '(' || isTwoInputOperator(prev)) return false;
                if (parentheses_stack.isEmpty()) return false;
                parentheses_stack.pop();
            }
            else if (isTwoInputOperator(c)){
                // Ensure an operator does not come after an open parenthesis or another operator.
                if (prev == '(' || isTwoInputOperator(c)) return false;
            }
            else if (c == '¬'){
                // Ensure only an open parenthesis or operator can come before a not.
                if (prev != '(' && prev != 0 && !isTwoInputOperator(prev)) return false;
            }
            else {
                // Ensure only an open parenthesis, an operator, or a not can come before a variable.
                if (prev != '(' && prev != 0 && !isTwoInputOperator(prev) && prev != '¬') return false;
            }
            prev = c;
        }
        return true;
    }

    public String trimSpaces(String str){
        StringBuilder trimmed = new StringBuilder();
        for (char c : str.toCharArray()){
            if (c != ' ') trimmed.append(c);
        }
        return trimmed.toString();
    }

    /**
     * Returns the value of a boolean expression.
     * @param str boolean expression containing only 1's, 0's, parentheses, and operators. Ex: "1∨(¬1∧0)".
     * @return evaluated expression.
     */
    public static boolean evaluateExpr(String str){
        if (str.length() == 0){
            return false;
        }
        char[] char_arr = str.toCharArray();
        boolean current_val = false;
        Operation next_op = Operation.OR;
        boolean negate_next = false;

        for (int i = 0; i < char_arr.length; i ++){
            char next_char = char_arr[i];
            if (next_char == '1' || next_char == '0'){
                boolean next_val = (next_char == '1');
                current_val = evaluateExpr(current_val, next_val, negate_next, next_op);
                if (negate_next) negate_next = false;
            }
            else if (next_char =='¬'){
                negate_next = true;
            }
            else if (next_char == '∨'){
                next_op = Operation.OR;
            }
            else if (next_char == '∧'){
                next_op = Operation.AND;
            }
            else if (next_char == '⊕'){
                next_op = Operation.XOR;
            }
            else if (next_char == '('){
                int start_idx = i + 1;
                int open_count = 1;
                while (open_count != 0){
                    i++;
                    if (char_arr[i] == '(') open_count ++;
                    else if (char_arr[i] == ')') open_count --;
                }
                int end_idx = i;
                boolean next_val = evaluateExpr(str.substring(start_idx, end_idx));
                current_val = evaluateExpr(current_val, next_val, negate_next, next_op);
                if (negate_next) negate_next = false;
            }
        }
        return current_val;
    }

    public static boolean evaluateExpr(boolean op1, boolean op2, boolean negate, Operation op){
        boolean ret_val = false;
        if (op == Operation.AND) ret_val = evaluateAnd(op1, op2, negate);
        else if (op == Operation.OR) ret_val = evaluateOr(op1, op2, negate);
        else if (op == Operation.XOR) ret_val = evaluateXor(op1, op2, negate);
        return ret_val;
    }

    public static boolean evaluateAnd(boolean op1, boolean op2, boolean negate){
        if (negate) return op1 && !op2;
        return op1 && op2;
    }

    public static boolean evaluateOr(boolean op1, boolean op2, boolean negate){
        if (negate) return op1 || !op2;
        return op1 || op2;
    }

    public static boolean evaluateXor(boolean op1, boolean op2, boolean negate){
        if (negate) return op1 ^ !op2;
        return op1 ^ op2;
    }

    // TEST
    public static void printTruthTable(String str){
        HashSet<Character> variables = new HashSet<>();
        for (char c : str.toCharArray()) {
            if (c != '(' && c != ')' && c != '∧' && c != '∨' && c != '¬' && c != '⊕') {
                variables.add(c);
            }
        }
        printTableRecursive(str, variables, new HashMap<>());
    }

    // TEST
    public static void printTableRecursive(String str, HashSet<Character> variables_rem, HashMap<Character, Integer> value_map){
        if (variables_rem.isEmpty()){
            for (Map.Entry<Character, Integer> entry : value_map.entrySet()){
                System.out.print(entry.getKey() + " = " + entry.getValue() + ", ");
            }
            System.out.print(" output = " + evaluateExpr(str));
            System.out.println();
            return;
        }
        char to_replace = variables_rem.iterator().next();
        variables_rem.remove(to_replace);

        value_map.put(to_replace, 0);
        StringBuilder str_with_0s = new StringBuilder();
        for (char c : str.toCharArray()){
            if (c == to_replace) str_with_0s.append('0');
            else str_with_0s.append(c);
        }
        printTableRecursive(str_with_0s.toString(), variables_rem, value_map);

        value_map.remove(to_replace);
        value_map.put(to_replace, 1);
        StringBuilder str_with_1s = new StringBuilder();
        for (char c : str.toCharArray()){
            if (c == to_replace) str_with_1s.append('1');
            else str_with_1s.append(c);
        }
        printTableRecursive(str_with_1s.toString(), variables_rem, value_map);

        variables_rem.add(to_replace);
        value_map.remove(to_replace);
    }

    public boolean isTwoInputOperator(char c){
        return (c == '∧' || c == '∨' || c == '⊕');
    }

}

package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    Button generate;
    @FXML
    Button op_and;
    @FXML
    Button op_or;
    @FXML
    Button op_not;
    @FXML
    Button op_xor;
    @FXML
    TextField main_field;

    public void printResult(){

    }

    public void addAnd(){
        String text = main_field.getText();
        text += " ∧ ";
        main_field.setText(text);
    }

    public void addOr(){
        String text = main_field.getText();
        text += " ∨ ";
        main_field.setText(text);
    }

    public void addNot(){
        String text = main_field.getText();
        text += " ¬ ";
        main_field.setText(text);
    }

    public void addXor(){
        String text = main_field.getText();
        text += " ⊕ ";
        main_field.setText(text);
    }


}

package com.example.calculator_huggins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private String formula;
    private Stack<String> pastFormulas;
    private TextView display;
    private Button previousForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pastFormulas = new Stack<String>();

        display = findViewById(R.id.display);
        previousForm = findViewById(R.id.buttonPrev);

        formula = display.getText().toString();

        previousForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pastFormulas.empty()){
                    formula = pastFormulas.pop();
                    updateDisplay();
                }
            }
        });
    }

    public void buttonOnCLick (View view){
        Button clickedButton = (Button)view;
        String buttonText = clickedButton.getText().toString();

        switch(buttonText){
            case "CE":
                formula = "";
                updateDisplay();
                break;
            case "C":
                if(!formula.isEmpty()){
                    int lastIndex = 0;

                    if(lastIndex < formula.lastIndexOf("+")) lastIndex = formula.lastIndexOf("+");
                    if(lastIndex < formula.lastIndexOf("-")) lastIndex = formula.lastIndexOf("-");
                    if(lastIndex < formula.lastIndexOf("*")) lastIndex = formula.lastIndexOf("*");
                    if(lastIndex < formula.lastIndexOf("/")) lastIndex = formula.lastIndexOf("/");

                    if(lastIndex == 0) formula = "";
                    if(lastIndex == formula.length()-1) formula = formula.substring(0,formula.length()-1);
                    else if(lastIndex > 0) formula = formula.substring(0,lastIndex+1);


                }
                updateDisplay();
                break;
            case "=":
                if(!formula.isEmpty()) pastFormulas.push(formula);
                double formulaSolve = calculate(formula);
                if(formulaSolve == (long) formulaSolve) formula = String.format("%d",(long)formulaSolve);
                else formula = String.format("%s",formulaSolve);
                updateDisplay();
                break;
            case "+/-":
                int lastIndex = 0;

                if(lastIndex < formula.lastIndexOf("+")) lastIndex = formula.lastIndexOf("+");
                if(lastIndex < formula.lastIndexOf("-")) lastIndex = formula.lastIndexOf("-");
                if(lastIndex < formula.lastIndexOf("*")) lastIndex = formula.lastIndexOf("*");
                if(lastIndex < formula.lastIndexOf("/")) lastIndex = formula.lastIndexOf("/");

                StringBuilder tempForm = new StringBuilder(formula);
                try {
                    if (lastIndex == 0) {
                        if (formula.charAt(0) == '_') formula = formula.substring(1);
                        else formula = "_" + formula;
                    } else {
                        if (formula.charAt(lastIndex + 1) == '_') {
                            tempForm.deleteCharAt(lastIndex + 1);
                            formula = tempForm.toString();
                        } else {
                            tempForm.insert(lastIndex + 1, "_");
                            formula = tempForm.toString();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateDisplay();
                break;
            default:
                formula += buttonText;
                updateDisplay();
                break;
        }
    }

    private double calculate(String formula){
        String tempNum = "";
        String operator = "";
        double solution = 0;
        for(int i=0; i < formula.length(); i++){
            char ch = formula.charAt(i);

            if(Character.isDigit(ch) || ch == '_' || ch == '.') tempNum += ch;
            if((!Character.isDigit(ch) && ch != '_' && ch != '.') || i == formula.length()-1){
                double num = 0;
                try {
                    if (tempNum.charAt(0) == '_') num = -1 * Double.parseDouble(tempNum.substring(1));
                    else num = Double.parseDouble(tempNum);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(operator == "") solution = num;
                else {
                    if(operator.equals("+")) solution += num;
                    if(operator.equals("-")) solution -= num;
                    if(operator.equals("*")) solution *= num;
                    if(operator.equals("/") && solution * num != 0) solution /= num;
                    else if(operator.equals("/")) solution = 0;
                }
                tempNum = "";
                operator = "" + ch;
            }
        }
        return solution;
    }

    private void updateDisplay(){
        String tempFormula = "";
        for(char ch:formula.toCharArray()){
            if(ch == '_') tempFormula += '-';
            else tempFormula += ch;
        }
        display.setText(tempFormula);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(getString(R.string.formulaKey),formula);
        ArrayList<String> pastFormsArray = new ArrayList<>();

        while(!pastFormulas.empty()){
            pastFormsArray.add(pastFormulas.pop());
        }
        outState.putStringArrayList(getString(R.string.pastFormsKey),pastFormsArray);
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     *
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}. This method is called only when recreating
     * an activity; the method isn't invoked if {@link #onStart} is called for
     * any other reason.</p>
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> pastFormsArray = savedInstanceState.getStringArrayList(getString(R.string.pastFormsKey));
        for(int i = pastFormsArray.size()-1; i >= 0; i--){
            pastFormulas.push(pastFormsArray.get(i));
        }
        formula = savedInstanceState.getString(getString(R.string.formulaKey));
        updateDisplay();
    }
}
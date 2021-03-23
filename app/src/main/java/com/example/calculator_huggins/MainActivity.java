package com.example.calculator_huggins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                System.out.println("-------------:" + formula + ":------------------");
                updateDisplay();
                break;
            case "C":
                if(!formula.isEmpty()) formula = formula.substring(0,formula.length()-1);
                System.out.println("-------------:" + formula + ":------------------");
                updateDisplay();
                break;
            case "=":
                System.out.println("-------------" + formula + "------------------");
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
                        System.out.println("--------__:" + formula);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateDisplay();
                break;
            default:
                formula += buttonText;
                System.out.println("-------------" + formula + "------------------");
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

            if(Character.isDigit(ch) || ch == '_') tempNum += ch;
            if((!Character.isDigit(ch) && ch != '_') || i == formula.length()-1){
                int num = 0;
                try {
                    if (tempNum.charAt(0) == '_') num = -1 * Integer.parseInt(tempNum.substring(1));
                    else num = Integer.parseInt(tempNum);
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
}
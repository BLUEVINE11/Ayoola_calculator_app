package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvDisplay;
    double firstNumber = 0, secondNumber = 0, result = 0;
    String operator = "";
    boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        tvDisplay = findViewById(R.id.tvDisplay);

        // Number buttons
        int[] numIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
                R.id.btn8, R.id.btn9};
        for (int id : numIds) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(v -> {
                    String digit = ((Button) v).getText().toString();
                    if (isNewInput) {
                        tvDisplay.setText(digit);
                        isNewInput = false;
                    } else {
                        String currentText = tvDisplay.getText().toString();
                        tvDisplay.setText(String.format("%s%s", currentText, digit));
                    }
                });
            }
        }

        // Decimal
        View btnDot = findViewById(R.id.btnDot);
        if (btnDot != null) {
            btnDot.setOnClickListener(v -> {
                if (!tvDisplay.getText().toString().contains(".")) {
                    String currentText = tvDisplay.getText().toString();
                    tvDisplay.setText(String.format("%s.", currentText));
                    isNewInput = false;
                }
            });
        }

        // Clear
        View btnClear = findViewById(R.id.btnClear);
        if (btnClear != null) {
            btnClear.setOnClickListener(v -> {
                tvDisplay.setText("0");
                firstNumber = secondNumber = result = 0;
                operator = "";
                isNewInput = true;
            });
        }

        // +/-
        View btnPlusMinus = findViewById(R.id.btnPlusMinus);
        if (btnPlusMinus != null) {
            btnPlusMinus.setOnClickListener(v -> {
                try {
                    double val = Double.parseDouble(tvDisplay.getText().toString());
                    tvDisplay.setText(formatResult(-val));
                } catch (NumberFormatException ignored) {}
            });
        }

        // %
        View btnPercent = findViewById(R.id.btnPercent);
        if (btnPercent != null) {
            btnPercent.setOnClickListener(v -> {
                try {
                    double val = Double.parseDouble(tvDisplay.getText().toString());
                    tvDisplay.setText(formatResult(val / 100));
                } catch (NumberFormatException ignored) {}
            });
        }

        // Operators
        View.OnClickListener opListener = v -> {
            try {
                firstNumber = Double.parseDouble(tvDisplay.getText().toString());
                operator = ((Button) v).getText().toString();
                isNewInput = true;
            } catch (NumberFormatException ignored) {}
        };

        int[] opIds = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide};
        for (int id : opIds) {
            View v = findViewById(id);
            if (v != null) v.setOnClickListener(opListener);
        }

        // Equals
        View btnEquals = findViewById(R.id.btnEquals);
        if (btnEquals != null) {
            btnEquals.setOnClickListener(v -> {
                try {
                    secondNumber = Double.parseDouble(tvDisplay.getText().toString());
                    switch (operator) {
                        case "+": result = firstNumber + secondNumber; break;
                        case "-": result = firstNumber - secondNumber; break;
                        case "×": result = firstNumber * secondNumber; break;
                        case "÷":
                            if (secondNumber == 0) {
                                tvDisplay.setText("Error");
                                return;
                            }
                            result = firstNumber / secondNumber;
                            break;
                        case "nPr":
                            result = permutation((int) firstNumber, (int) secondNumber);
                            break;
                        case "nCr":
                            result = combination((int) firstNumber, (int) secondNumber);
                            break;
                        case "pow":
                            result = Math.pow(firstNumber, secondNumber);
                            break;
                        case "EE":
                            result = firstNumber * Math.pow(10, secondNumber);
                            break;
                        default: return;
                    }
                    tvDisplay.setText(formatResult(result));
                    isNewInput = true;
                } catch (NumberFormatException ignored) {}
            });
        }

        // Scientific buttons
        View.OnClickListener sciListener = v -> {
            try {
                double val = Double.parseDouble(tvDisplay.getText().toString());
                double res;
                int id = v.getId();

                if (id == R.id.btnSin)            res = Math.sin(Math.toRadians(val));
                else if (id == R.id.btnCos)       res = Math.cos(Math.toRadians(val));
                else if (id == R.id.btnTan)       res = Math.tan(Math.toRadians(val));
                else if (id == R.id.btnSinh)      res = Math.sinh(val);
                else if (id == R.id.btnCosh)      res = Math.cosh(val);
                else if (id == R.id.btnTanh)      res = Math.tanh(val);
                else if (id == R.id.btnLog)       res = Math.log10(val);
                else if (id == R.id.btnLn)        res = Math.log(val);
                else if (id == R.id.btnSqrt)      res = Math.sqrt(val);
                else if (id == R.id.btnSquare)    res = Math.pow(val, 2);
                else if (id == R.id.btnFactorial) res = (double) factorial((int) val);
                else if (id == R.id.btnPi) {
                    tvDisplay.setText(formatResult(Math.PI));
                    isNewInput = true;
                    return;
                } else return;

                tvDisplay.setText(formatResult(res));
                isNewInput = true;
            } catch (NumberFormatException ignored) {}
        };

        int[] sciIds = {R.id.btnSin, R.id.btnCos, R.id.btnTan, R.id.btnSinh, R.id.btnCosh,
                R.id.btnTanh, R.id.btnLog, R.id.btnLn, R.id.btnSqrt, R.id.btnSquare,
                R.id.btnFactorial, R.id.btnPi};
        for (int id : sciIds) {
            View v = findViewById(id);
            if (v != null) v.setOnClickListener(sciListener);
        }

        // nPr, nCr, Power, EE buttons
        View btnNPR = findViewById(R.id.btnNPR);
        View btnNCR = findViewById(R.id.btnNCR);
        View btnPower = findViewById(R.id.btnPower);
        View btnEE = findViewById(R.id.btnEE);

        if (btnNPR != null) {
            btnNPR.setOnClickListener(v -> {
                firstNumber = Double.parseDouble(tvDisplay.getText().toString());
                operator = "nPr";
                isNewInput = true;
            });
        }
        if (btnNCR != null) {
            btnNCR.setOnClickListener(v -> {
                firstNumber = Double.parseDouble(tvDisplay.getText().toString());
                operator = "nCr";
                isNewInput = true;
            });
        }
        if (btnPower != null) {
            btnPower.setOnClickListener(v -> {
                firstNumber = Double.parseDouble(tvDisplay.getText().toString());
                operator = "pow";
                isNewInput = true;
            });
        }
        if (btnEE != null) {
            btnEE.setOnClickListener(v -> {
                firstNumber = Double.parseDouble(tvDisplay.getText().toString());
                operator = "EE";
                isNewInput = true;
            });
        }

    } // end onCreate

    private String formatResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value))
            return "Error";
        if (value == (long) value)
            return String.valueOf((long) value);
        else
            return String.valueOf(Math.round(value * 1e10) / 1e10);
    }

    private long factorial(int n) {
        if (n < 0) return -1;
        if (n == 0 || n == 1) return 1;
        long res = 1;
        for (int i = 2; i <= n; i++) res *= i;
        return res;
    }

    private double permutation(int n, int r) {
        if (r > n) return 0;
        return (double) factorial(n) / factorial(n - r);
    }

    private double combination(int n, int r) {
        if (r > n) return 0;
        return (double) factorial(n) / (factorial(r) * factorial(n - r));
    }

} // end class
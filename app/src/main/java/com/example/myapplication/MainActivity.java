package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup choicesRadioGroup;
    private Button submitButton;
    private int currentQuestionIndex = 0;

    private String[] questions = {"What is the result of 5 + 7?", "What is the result of 10 - 3?", "What is the result of 5 - 2?", "What is the result of 4 + 6?"};
    private String[][] choices = {{"10", "12", "11", "13"}, {"7", "8", "9", "10"},{"3", "8", "2", "6"},{"7", "11", "15", "10"}};
    private String[] correctAnswers = {"12", "7","3", "10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.question_text_view);
        choicesRadioGroup = findViewById(R.id.choices_radio_group);
        submitButton = findViewById(R.id.submit_button);

        displayQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = choicesRadioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    // No answer selected
                    Toast.makeText(MainActivity.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton selectedRadioButton = findViewById(checkedRadioButtonId);
                    String selectedAnswer = selectedRadioButton.getText().toString();
                    if (selectedAnswer.equals(correctAnswers[currentQuestionIndex])) {
                        // Correct answer
                        Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                        // Play correct animation
                    } else {
                        // Incorrect answer
                        Toast.makeText(MainActivity.this, "Incorrect. The correct answer is " + correctAnswers[currentQuestionIndex] + ".", Toast.LENGTH_SHORT).show();
                        // Play incorrect animation
                    }
                    // Move to the next question
                    currentQuestionIndex++;
                    if (currentQuestionIndex == questions.length) {
                        // No more questions, restart the game
                        currentQuestionIndex = 0;
                    }
                    displayQuestion();
                }
            }
        });

    }

    private void displayQuestion() {
        questionTextView.setText(questions[currentQuestionIndex]);
        for (int i = 0; i < choicesRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) choicesRadioGroup.getChildAt(i);
            radioButton.setText(choices[currentQuestionIndex][i]);
        }
        choicesRadioGroup.clearCheck();
    }
}
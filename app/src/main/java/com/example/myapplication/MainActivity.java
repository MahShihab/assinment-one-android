package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView questionTextView;
    private RecyclerView choicesRecyclerView;
    private Button submitButton;
    private int currentQuestionIndex = 0;

    private String[] questions = {"What is the result of 5 + 7?", "What is the result of 10 - 3?", "What is the result of 5 - 2?", "What is the result of 4 + 6?"};
    private String[][] choices = {{"10", "12", "11", "13"}, {"7", "8", "9", "10"}, {"3", "8", "2", "6"}, {"7", "11", "15", "10"}};
    private String[] correctAnswers = {"12", "7", "3", "10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] correct = new int[1];
        final int[] ans = new int[1];

        questionTextView = findViewById(R.id.question_text_view);
        choicesRecyclerView = findViewById(R.id.choices_recycler_view);
        submitButton = findViewById(R.id.submit_button);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        displayQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedPosition = getCheckedPosition();
                correct[0] = sharedPreferences.getInt("correct", 0);
                ans[0] = sharedPreferences.getInt("ans", 0);

                if (checkedPosition == -1) {
                    // No answer selected
                    Toast.makeText(MainActivity.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                } else {
                    if (correct[0] == ans[0]) {
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

    private int getCheckedPosition() {
        for (int i = 0; i < choicesRecyclerView.getChildCount(); i++) {
            ChoiceAdapter.ChoiceViewHolder viewHolder = (ChoiceAdapter.ChoiceViewHolder) choicesRecyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null && viewHolder.radioButton.isChecked()) {
                return i;
            }
        }
        return -1; // No answer selected
    }



    private int getCheckedRadioButtonId() {
        if (choicesRecyclerView.getLayoutManager() != null) {
            RecyclerView.ViewHolder viewHolder = choicesRecyclerView.findViewHolderForAdapterPosition(currentQuestionIndex);
            if (viewHolder instanceof ChoiceAdapter.ChoiceViewHolder) {
                return ((ChoiceAdapter.ChoiceViewHolder) viewHolder).radioButton.getId();
            }
        }
        return -1;
    }


    private void displayQuestion() {
        questionTextView.setText(questions[currentQuestionIndex]);
        ChoiceAdapter adapter = new ChoiceAdapter(choices[currentQuestionIndex]);
        choicesRecyclerView.setAdapter(adapter);
        choicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ChoiceViewHolder> {

        private String[] choices;
        private int checkedPosition = -1;

        public ChoiceAdapter(String[] choices) {
            this.choices = choices;
        }

        @NonNull
        @Override
        public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice, parent, false);
            return new ChoiceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChoiceViewHolder holder, int position) {
            holder.radioButton.setText(choices[position]);
            holder.radioButton.setChecked(checkedPosition == position);

            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int previousCheckedPosition = checkedPosition;
                    checkedPosition = holder.getAdapterPosition();
                    notifyItemChanged(previousCheckedPosition);
                    notifyItemChanged(checkedPosition);

                    int selectedQuestionIndex = currentQuestionIndex;
                    String selectedAnswer = choices[checkedPosition];
                    String correctAnswer = correctAnswers[selectedQuestionIndex];

                    if (selectedAnswer.equals(correctAnswer)) {
                        // Correct answer
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("correct", Integer.parseInt(selectedAnswer));
                        editor.putInt("ans", Integer.parseInt(correctAnswer));
                        editor.apply();

                        // Play correct animation
                    } else {
                        // Incorrect answer
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("correct", Integer.parseInt(selectedAnswer));
                        editor.putInt("ans", Integer.parseInt(correctAnswer));
                        editor.apply();
                        // Play incorrect animation
                    }
                }
            });
        }



        @Override
        public int getItemCount() {
            return choices.length;
        }

        class ChoiceViewHolder extends RecyclerView.ViewHolder {
            RadioButton radioButton;

            ChoiceViewHolder(View itemView) {
                super(itemView);
                radioButton = itemView.findViewById(R.id.choice_radio_button);
            }
        }
    }
}

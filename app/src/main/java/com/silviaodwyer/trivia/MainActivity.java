package com.silviaodwyer.trivia;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.silviaodwyer.trivia.model.AnswerListAsyncResponse;
import com.silviaodwyer.trivia.model.Question;
import com.silviaodwyer.trivia.model.QuestionBank;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView questionTextView;
  private TextView questionCounterTextView;
  private Button trueButton;
  private Button falseButton;
  private ImageButton nextButton;
  private ImageButton prevButton;
  private final String MESSAGE_ID = "0";
  private int currentScore = 0;
  private int currentQuestionIndex = 0;
  private List<Question> questionList;
  private SharedPreferences.Editor editor;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    nextButton = findViewById(R.id.next);
    prevButton = findViewById(R.id.prev);
    trueButton = findViewById(R.id.trueButton);
    falseButton = findViewById(R.id.falseButton);

    questionCounterTextView = findViewById(R.id.counterText);
    questionTextView = findViewById(R.id.question_textview);

    questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
      @Override
      public void processFinished(ArrayList<Question> questionArrayList) {
        questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
          Log.d("TAG", "Question Array List" + questionArrayList);
      }
    });

    nextButton.setOnClickListener(this);
    prevButton.setOnClickListener(this);
    trueButton.setOnClickListener(this);
    falseButton.setOnClickListener(this);

    SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
    editor = sharedPreferences.edit();

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()){
      case R.id.prev:
        prevQuestion();
        break;

      case R.id.next:
        nextQuestion();
        break;

      case R.id.trueButton:
        checkAnswer(true);
        updateQuestionView();
        break;

      case R.id.falseButton:
        checkAnswer(false);
        updateQuestionView();
        break;
    }
  }

  public void nextQuestion() {
    this.currentQuestionIndex = (currentQuestionIndex + 1);
    Log.d("NEXT QUESTION", "On next question");
    updateQuestionView();
  }

  public void prevQuestion() {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
    }
    updateQuestionView();
  }

  public void updateQuestionView() {
    Question currentQuestion = this.questionList.get(this.currentQuestionIndex);
    this.questionTextView.setText(currentQuestion.getAnswer());
    this.questionCounterTextView.setText("" + this.currentQuestionIndex + " out of" + this.questionList.size());
    updateScore();
  }

  public void updateScore() {
    editor.putInt("score", this.currentScore);
    editor.apply();
  }

  public void checkAnswer(boolean userAnswer) {
    Boolean answer = this.questionList.get(this.currentQuestionIndex).isAnswerTrue();
    String toastId;

    if (answer == userAnswer) {
      fadeView();
      Log.d("TAG", "CORRECT ANSWER");
      toastId = "Correct!";
    } else {
      shakeAnimation();
      Log.d("INCORRECTANS", "checkAnswer: INCORRECT ANSWER");
      toastId = "Wrong!";
    }

    Toast.makeText(MainActivity.this, toastId, Toast.LENGTH_SHORT).show();
  }

  private void shakeAnimation() {
    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);

    final CardView cardView = findViewById(R.id.cardView);
    cardView.setAnimation(shake);

    shake.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        cardView.setCardBackgroundColor(Color.RED);
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        cardView.setCardBackgroundColor(Color.WHITE);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });


  }

  public void fadeView() {
    final CardView cardView1 = findViewById(R.id.cardView);

    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
    alphaAnimation.setDuration(350);
    alphaAnimation.setRepeatCount(1);
    alphaAnimation.setRepeatMode(Animation.REVERSE);

    cardView1.setAnimation(alphaAnimation);

    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        cardView1.setBackgroundColor(Color.GREEN);
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        cardView1.setBackgroundColor(Color.WHITE);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });
  }
}

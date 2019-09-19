package com.silviaodwyer.trivia.model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.silviaodwyer.trivia.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.silviaodwyer.trivia.controller.AppController.TAG;

public class QuestionBank {
  ArrayList<Question> questionArrayList = new ArrayList<>();
  private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";


  public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
      Request.Method.GET,
      url,
      (JSONArray) null,
      new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
          Log.d("JSON", "onResponse: " + response);

          for (int i = 0; i < response.length(); i++) {
            try {
              Log.d("JSON", "onResponse" + response.getJSONArray(i).get(0).toString());

              Question question = new Question();
              String statement = response.getJSONArray(i).get(0).toString();
              Boolean isTrue = response.getJSONArray(i).getBoolean(1);

              question.setAnswer(statement);
              question.setAnswerTrue(isTrue);

              questionArrayList.add(question);
            } catch (JSONException e) {
              e.printStackTrace();
            }

          }
          if (null != callBack) callBack.processFinished(questionArrayList);

        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
          Log.d("JSON ERROR", "there was an error" + error.getMessage());
      }
    }
    );

    AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    return questionArrayList;
  }

}

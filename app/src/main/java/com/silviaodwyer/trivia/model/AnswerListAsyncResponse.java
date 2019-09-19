package com.silviaodwyer.trivia.model;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {

  void processFinished(ArrayList<Question> questionArrayList);

}

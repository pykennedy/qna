package pyk.qna.model.firebase;

import java.util.ArrayList;
import java.util.List;

import pyk.qna.controller.Utility;
import pyk.qna.model.object.Answer;
import pyk.qna.model.object.Question;

public class FirebaseCache {
  private static List<Question> qlist = new ArrayList<>();
  private static List<Answer>   alist = new ArrayList<>();
  
  public static void addToQList(Question question) {
    qlist.remove(question);
    qlist.add(question);
  }
  
  public static void addToAList(Answer answer) {
    alist.remove(answer);
    alist.add(answer);
  }
  
  public static Question getQ(String questionID) {
    for (Question q : qlist) {
      if (Utility.getIDFromObject(q, null).equals(questionID)) {
        return q;
      }
    }
    return null;
  }
  
  public static Answer getA(String answerID) {
    for (Answer a : alist) {
      if (Utility.getIDFromObject(null, a).equals(answerID)) {
        return a;
      }
    }
    return null;
  }
}

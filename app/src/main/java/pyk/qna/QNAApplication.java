package pyk.qna;

import android.app.Application;
import android.content.Context;

public class QNAApplication extends Application {
  private static QNAApplication instance;
  public static Context context;
  
  public QNAApplication() {
    instance = this;
    context = getApplicationContext();
  }
  
  public static QNAApplication getInstance() {
    return instance;
  }
}

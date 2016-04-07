package org.md2k.moodsurfing;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.md2k.datakitapi.datatype.DataTypeJSONObject;
import org.md2k.datakitapi.time.DateTime;

import java.util.ArrayList;

/**
 * Created by monowar on 3/23/16.
 */
public class QuestionAnswer {
    ArrayList<QuestionsJSON> question_answers;
    String status;
    Context context;
    private static QuestionAnswer instance;
    public static QuestionAnswer getInstance(Context context){
        if(instance==null)
            instance=new QuestionAnswer(context);
        return instance;
    }
    QuestionAnswer(Context context){
        this.context=context;
        question_answers=new ArrayList<>();
    }
    public void add(QuestionsJSON questionsJSON){
        question_answers.add(questionsJSON);
        if(status==null)
            status=questionsJSON.status;
        else if(questionsJSON.status.equals(Constants.COMPLETED))
            status=questionsJSON.status;

    }
    void sendData(){
        Gson gson = new Gson();
        JsonObject sample = new JsonParser().parse(gson.toJson(question_answers)).getAsJsonObject();
        DataTypeJSONObject dataTypeJSONObject = new DataTypeJSONObject(DateTime.getDateTime(), sample);
        Intent intent=new Intent();
        intent.setAction("org.md2k.ema_scheduler.response");
        intent.putExtra("TYPE","RESULT");
        intent.putExtra("ANSWER",dataTypeJSONObject);
        intent.putExtra("STATUS",status);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }
    void sendLastResponseTime(long lastResponseTime, String message){
        Intent intent=new Intent();
        intent.setAction("org.md2k.ema_scheduler.response");
        intent.putExtra("TYPE","STATUS_MESSAGE");
        intent.putExtra("TIMESTAMP",lastResponseTime);
        intent.putExtra("MESSAGE",message);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }
    public static void clear(){
        instance=null;
    }

}

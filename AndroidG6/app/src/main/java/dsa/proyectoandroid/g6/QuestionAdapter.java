package dsa.proyectoandroid.g6;

import android.content.Context;

import java.util.List;

import dsa.proyectoandroid.g6.models.Question;
import dsa.proyectoandroid.g6.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class QuestionAdapter {
    private Question q;
    private OnQuestionClickListener listener;
    private Context context;

    public  interface OnQuestionClickListener{void onQuestionClick(Question q);}

    public QuestionAdapter(Question q, OnQuestionClickListener listener,Context context){
        this.q =q;
        this.listener = listener;
        this.context = context;
    }

    private QuestionService questionService;

    public QuestionAdapter(){
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        questionService = retrofit.create(QuestionService.class);
    }

    public void createQuestion(Question question, Callback<Question> callback){
        Call<Question> call = questionService.createQuestion(question);
        call.enqueue(callback); //llama a la API de manera asincrona
    }
}

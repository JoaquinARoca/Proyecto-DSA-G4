package dsa.proyectoandroid.g6;

import java.util.List;

import dsa.proyectoandroid.g6.models.Question;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface QuestionService {
    @POST("dsaApp/question")
    Call<Question> createQuestion(@Body Question question);

    @GET("dsaApp/question")
    Call<List<Question>> getAllQuestions();

}

package dsa.proyectoandroid.g6.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import dsa.proyectoandroid.g6.QuestionAdapter;
import dsa.proyectoandroid.g6.R;
import dsa.proyectoandroid.g6.models.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity {
    private QuestionAdapter questionAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionAdapter = new QuestionAdapter();
    }
    public void dameQClick(View v){
        //Aqui iria el codigo de solicitud de questions, Juan me ha dicho que me olvide de
        //solicitar las questions de un tema.
    }

    public void sendQClick(View v){
        String titulo = ((EditText) findViewById(R.id.et_title)).getText().toString();
        String mensaje = ((EditText) findViewById(R.id.et_message)).getText().toString();
        String remitente = ((EditText) findViewById(R.id.et_sender)).getText().toString();

        if (remitente.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Question nuevaPregunta = new Question(titulo,mensaje,remitente);

        // Usar el método del adaptador para crear un usuario
        questionAdapter.createQuestion(nuevaPregunta, new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if(response.isSuccessful()){
                    Toast.makeText(QuestionActivity.this,"Mensaje enviado correctamente "+response.body().getTitle()+" "+
                                    response.body().getDate()+" "+response.body().getMessage()+" "+response.body().getSender()
                            ,Toast.LENGTH_LONG).show();
                    clearFields();
                }else{
                    String errorMessage = "Error al enviar el mensaje";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ": " + response.errorBody().string();
                        } catch (IOException e) {
                            errorMessage += ": respuesta inesperada";
                        }
                    }
                    Toast.makeText(QuestionActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable throwable) {
                Toast.makeText(QuestionActivity.this,
                        "Error de conexión: " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private  void clearFields(){
        ((EditText) findViewById(R.id.et_sender)).setText("");
        ((EditText) findViewById(R.id.et_message)).setText("");
        ((EditText) findViewById(R.id.et_title)).setText("");
    }
}
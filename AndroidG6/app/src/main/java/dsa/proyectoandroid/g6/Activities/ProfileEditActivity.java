package dsa.proyectoandroid.g6.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dsa.proyectoandroid.g6.R;
import dsa.proyectoandroid.g6.RetrofitClient;
import dsa.proyectoandroid.g6.AdaptersAndServices.UserAdapter;
import dsa.proyectoandroid.g6.AdaptersAndServices.UserService;
import dsa.proyectoandroid.g6.models.SavedPreferences;
import dsa.proyectoandroid.g6.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {
    private int cont = 0;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

    }

    public void changesClick(View v){
        if(cont==0){
            Toast.makeText(ProfileEditActivity.this,
                    "¿Estás seguro de estos cambios? Presiona otra vez si estas de acuerdo",
                    Toast.LENGTH_LONG).show();
            cont++;
        }else {
            User currentUser = SavedPreferences.getInstance().getMy_user();
            String id = currentUser.getId();
            String nombre = ((EditText) findViewById(R.id.usertbx)).getText().toString();
            String pass1 = ((EditText) findViewById(R.id.passwdtbx)).getText().toString();

            User u = new User(id,nombre,pass1);
            UserService service = RetrofitClient.getRetrofitInstance().create(UserService.class);
            Call<User> call = service.updateUser(id,u);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()) {
                        User changedUser = response.body();
                        SavedPreferences.getInstance().setMy_user(changedUser);
                        Toast.makeText(ProfileEditActivity.this, "Usuario Actualizado correctamente", Toast.LENGTH_LONG).show();
                        Intent Actv = new Intent(ProfileEditActivity.this, Dashboard.class);
                        startActivity(Actv);
                        finish();
                    } else if (response.code()==404) {
                        Toast.makeText(ProfileEditActivity.this,"Usuario no encontrado",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(ProfileEditActivity.this,"Error desconocido: " + response.code(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    Toast.makeText(ProfileEditActivity.this,"Error de conexión: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
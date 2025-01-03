package dsa.proyectoandroid.g6.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import dsa.proyectoandroid.g6.R;
import dsa.proyectoandroid.g6.models.SavedPreferences;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Usar un Handler para retrasar la navegación
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtén la instancia del Singleton y verifica el usuario
                SavedPreferences userSingleton = SavedPreferences.getInstance();
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra SplashScreenActivity
            }
        }, 5000);
    }
}
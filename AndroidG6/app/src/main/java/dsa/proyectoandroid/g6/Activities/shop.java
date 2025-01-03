package dsa.proyectoandroid.g6.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dsa.proyectoandroid.g6.AdaptersAndServices.ProductAdapter;
import dsa.proyectoandroid.g6.R;
import dsa.proyectoandroid.g6.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class shop extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;

    private List<Product> productList = new ArrayList<>();

    private final int PROGRESS_DELAY_75 = 5000; // 5 segundos para el 75%
    private final int PROGRESS_DELAY_100 = 3000; // 3 segundos para el 100%

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Inicializar vistas
        recyclerView = findViewById(R.id.ShopObjectsRV);
        progressBar = findViewById(R.id.progressBar);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);
        // Simulación de carga del ProgressBar
        simulateProgressBar();

        // Cargar datos de productos
        loadProducts();
    }

    private void simulateProgressBar() {
        // Simula la carga inicial hasta el 75%
        progressBar.setProgress(0);
        Handler handler = new Handler();

        // Incrementar hasta el 75% y mantener por 5 segundos
        handler.postDelayed(() -> {
            progressBar.setProgress(75);
        }, 2000); // 2 segundos para alcanzar el 75%

        handler.postDelayed(() -> {
            progressBar.setProgress(75); // Mantener en 75% por 5 segundos
        }, 2000 + PROGRESS_DELAY_75);
    }

    private void completeProgress() {
        Handler handler = new Handler();

        // Completar al 100% después de los 5 segundos del 75%
        handler.postDelayed(() -> {
            progressBar.setProgress(100);
        }, PROGRESS_DELAY_75);

        // Ocultar el ProgressBar después de 3 segundos en 100%
        handler.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
        }, PROGRESS_DELAY_75 + PROGRESS_DELAY_100);

    }

    private void loadProducts() {
        // Crear una instancia del adaptador de productos (sin datos iniciales)
        productAdapter = new ProductAdapter(productList, this, product ->
                Toast.makeText(this, "Producto seleccionado: " + product.getNombre(), Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(productAdapter);

        // Llamar a la API para obtener todos los productos
        ProductAdapter productServiceAdapter = new ProductAdapter("https://tu-base-url.com/api");

        productServiceAdapter.getAllProducts(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizar lista de productos y notificar al adaptador
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();

                    // Simular la carga completada del ProgressBar
                    completeProgress();
                    ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    showError("Error al obtener productos.");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                showError("Fallo en la conexión: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(shop.this, message, Toast.LENGTH_SHORT).show();
        progressBar.setProgress(0); // Reiniciar el progreso en caso de error
    }
}
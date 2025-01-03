package dsa.proyectoandroid.g6.AdaptersAndServices;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dsa.proyectoandroid.g6.RetrofitClient;
import dsa.proyectoandroid.g6.models.Product;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ProductService productService; // Servicio para acceder a la API
    private List<Product> productList; // Lista de productos a mostrar
    private OnProductClickListener listener; // Interfaz de clics
    private Context context; // Contexto de la actividad

    // Constructor principal para inicializar ProductService
    public ProductAdapter(String baseUrl) {
        this.productService = RetrofitClient.getRetrofitInstance().create(ProductService.class);
    }

    // Constructor para asignar la lista de productos y el listener
    public ProductAdapter(List<Product> productList, Context context, OnProductClickListener listener) {
        this.productList = productList;
        this.context = context;
        this.listener = listener;
    }

    // Interfaz para manejar clics en los productos
    public interface OnProductClickListener {
        void onProductClick(Product p);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crear un layout dinámico en lugar de inflar un XML
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        // TextView para el nombre del producto
        TextView name = new TextView(parent.getContext());
        name.setTextSize(18);
        name.setTextColor(Color.BLACK);
        layout.addView(name);

        // TextView para el precio del producto
        TextView price = new TextView(parent.getContext());
        price.setTextSize(14);
        price.setTextColor(Color.GRAY);
        layout.addView(price);

        // Devolver el ViewHolder con las vistas dinámicas
        return new ProductViewHolder(layout, name, price);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Obtener el producto en la posición actual
        Product product = productList.get(position);

        // Asignar los valores a los TextViews
        holder.name.setText("Producto: "+product.getNombre());
        holder.price.setText("Precio: "+String.valueOf(product.getPrecio()));

        // Configurar clics en el ítem
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    // ViewHolder para mantener las referencias a las vistas
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;

        public ProductViewHolder(View itemView, TextView name, TextView price) {
            super(itemView);
            this.name = name;
            this.price = price;
        }
    }

    // Métodos para interactuar con la API usando ProductService
    public void getProductById(String id, Callback<Product> callback) {
        Call<Product> call = productService.getProductById(id);
        call.enqueue(callback);
    }

    public void createProduct(Product product, Callback<Product> callback) {
        Call<Product> call = productService.createProduct(product);
        call.enqueue(callback);
    }

    public void getProductPrice(String id, Callback<Double> callback) {
        Call<Double> call = productService.getProductPrice(id);
        call.enqueue(callback);
    }

    public void getAllProducts(Callback<List<Product>> callback) {
        Call<List<Product>> call = productService.getAllProducts();
        call.enqueue(callback);
    }
}

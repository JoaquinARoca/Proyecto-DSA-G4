package dsa.proyectoandroid.g6.AdaptersAndServices;
import android.content.Context;

import java.util.List;

import dsa.proyectoandroid.g6.RetrofitClient;
import dsa.proyectoandroid.g6.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class UserAdapter {
    private User u;
    private OnUserClickListener listener;
    private Context context;

    public interface OnUserClickListener{void onUserClick(User u);}

    public UserAdapter(User u,OnUserClickListener listener, Context context ){
        this.u=u;
        this.listener = listener;
        this.context = context;
    }

    private UserService userService;

    public UserAdapter() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        userService = retrofit.create(UserService.class);
    }

    public void getUsers(Callback<List<User>> callback) {
        Call<List<User>> call = userService.getUsers();
        call.enqueue(callback);
    }

    public void getUserByName(String name, Callback<User> callback) {
        Call<User> call = userService.getUserByName(name);
        call.enqueue(callback);
    }

    public void createUser(User user, Callback<User> callback) {
        Call<User> call = userService.createUser(user);
        call.enqueue(callback);  // Llama a la API de manera asíncrona
    }

    public void login(User credentials, Callback<User> callback) {
        Call<User> call = userService.login(credentials);
        call.enqueue(callback);
    }

    public void updateUser(String id,User user, Callback<User> callback){
        Call<User> call = userService.updateUser(id,user);
        call.enqueue(callback);
    }
}

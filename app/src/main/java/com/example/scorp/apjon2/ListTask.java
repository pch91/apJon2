package com.example.scorp.apjon2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.scorp.apjon2.DAO.Tasklistapi;
import com.example.scorp.apjon2.DAO.ReturnApi;
import com.example.scorp.apjon2.item_view_holder.TaskListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.xwray.groupie.GroupAdapter;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListTask extends BaseActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String token;
    public GroupAdapter adapter =  new GroupAdapter();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    public void populateViewListTask(List<com.example.scorp.apjon2.DAO.Task> lT,GroupAdapter ga){
        for (com.example.scorp.apjon2.DAO.Task T: lT) {
            ga.add(new TaskListItem(T));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Lista de atividades");




        firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    token = task.getResult().getToken();

                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                    httpClient.addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder().addHeader("Authorization", token).build();
                            return chain.proceed(request);
                        }
                    });

                   Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://infnet-devjava-assessment-api.herokuapp.com/")
                            .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
                            .build();

                    Tasklistapi taskapi = retrofit.create(Tasklistapi.class);
                    Call<ReturnApi> cltask = taskapi.loaddescription(token);

                    cltask.enqueue(new Callback<ReturnApi>() {
                        @Override
                        public void onResponse(Call<ReturnApi> call, retrofit2.Response<ReturnApi> response) {
                            if(response.isSuccessful()){
                                ReturnApi tl =  response.body();
                                RecyclerView PessoaView = findViewById(R.id.taskView);
                                PessoaView.setLayoutManager(new GridLayoutManager(ListTask.this, 1));
                                PessoaView.setAdapter(adapter);
                                populateViewListTask(tl.getTasks(),adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<ReturnApi> call, Throwable t) {
                            int a = 4;
                        }
                    });

                }
            }
        });

    }
}

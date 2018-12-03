package com.example.scorp.apjon2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scorp.apjon2.DAO.User;
import com.example.scorp.apjon2.DAO.UserDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class CadastroUsuario extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Id correspondente ao botão Up/Home da actionbar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = new User();
                u.setId(firebaseAuth.getCurrentUser().getUid());
                u.setCPF( ((TextView)findViewById(R.id.cadcpf)).getText().toString() ); ;
                u.setCell( ((TextView)findViewById(R.id.cadcell)).getText().toString() ) ;
                UserDao dao =  new UserDao();
                try {
                    dao.add(u,getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(CadastroUsuario.this, ListTask.class);
                startActivity(intent);
            }

        });
    }
}

package com.example.scorp.apjon2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scorp.apjon2.DAO.FireBaseCalback;
import com.example.scorp.apjon2.DAO.User;
import com.example.scorp.apjon2.DAO.UserDao;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView t2;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            updateUi(currentUser);
        } else {
            // No user is signed in
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        loginButton = findViewById(R.id.login_button);

        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //t2 = (TextView) findViewById(R.id.textView);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email"));


        findViewById(R.id.btnCad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroUsuario.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.LoginEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(((TextView) findViewById(R.id.editText)).getText().toString(), ((TextView) findViewById(R.id.editText2)).getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    updateUi(firebaseUser);
                                } else {
                                    firebaseAuth.createUserWithEmailAndPassword(((TextView) findViewById(R.id.cademail)).getText().toString(), ((TextView) findViewById(R.id.cadsenha)).getText().toString())
                                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "foi", Toast.LENGTH_LONG);

                                                    firebaseAuth.signInWithCredential(EmailAuthProvider.getCredential(((TextView) findViewById(R.id.cademail)).getText().toString(), ((TextView) findViewById(R.id.cadsenha)).getText().toString()))
                                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    firebaseUser = firebaseAuth.getCurrentUser();
                                                                    updateUi(firebaseUser);
                                                                }
                                                            });

                                                }else{
                                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                                                }
                                            }
                                        });
                                }
                            };
                        });
            }
        });

    }

    public void buttonlogin(View view){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
                Toast.makeText(getApplicationContext(),"entrou", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"cancelou", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"error - " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUi(user);
                }else{
                   Toast.makeText(getApplicationContext(),"Não pode se registar no facebook. ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void handleEMAIL(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUi(user);
                }else{
                    Toast.makeText(getApplicationContext(),"Não pode se registar no facebook. ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUi(FirebaseUser user) {

        final UserDao dao = new UserDao();
        try {
            dao.getObject(User.class, "", user.getUid(), new FireBaseCalback() {
                @Override
                public <T> void onCalback(List<T> list) {
                    List<User> luser =  (List<User>) list;
                    if(luser.size() > 0 ) {
                        showProgressDialog();
                        Intent intent = new Intent(MainActivity.this, ListTask.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainActivity.this, CadastroUsuario.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

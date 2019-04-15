package edu.css.cis3334_unit12_firebase_2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 * @author Dustin Kowal, Malik Alwan
 */

public class MainActivity extends AppCompatActivity {

    private TextView textViewStatus;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoogleLogin;
    private Button buttonCreateLogin;
    private Button buttonSignOut;
    private Button buttonStartChat;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoogleLogin = (Button) findViewById(R.id.buttonGoogleLogin);
        buttonCreateLogin = (Button) findViewById(R.id.buttonCreateLogin);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonStartChat = findViewById(R.id.buttonStartChat);

        //Writes message to database
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        buttonCreateLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                googleSignIn();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();
            }
        });

        buttonStartChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);

            }
        });


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("CIS3344", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("CIS3344", "Failed to read value.", error.toException());
            }
        });

    }


    private void createAccount(String email, String password) {

        Log.d("CIS3334", "createAccount:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            textViewStatus.setText("Status: an account has been successfully created.");
                        } else {
                            // If sign in fails, display a message to the user.
                            textViewStatus.setText("Status:Failed to create a account.");
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    /**
     * Checks to make sure the users password and email match the correct list stored in the FireBase authentication list.
     * @param email the email which the user has entered.
     * @param password the  password the user has entered.
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            //updateUI(null);
                        }


                    }
                });
    }
    /**
     * Signs out the user.
     */
    private void signOut () {

        mAuth.signOut();
        textViewStatus.setText("Signed out successfully");

    }
    /**
     * Has the ability to use a Google account to sign in
     */
    private void googleSignIn() {

        textViewStatus.setText("Status: Log in has failed");

    }





}

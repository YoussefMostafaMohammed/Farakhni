package com.example.farakhni.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.farakhni.AppScreen;
import com.example.farakhni.R;
import com.example.farakhni.firebase.signup.SignUpContract;
import com.example.farakhni.firebase.signup.SignUpPresenter;
import com.example.farakhni.login.LoginScreen;
import com.google.firebase.auth.FirebaseUser;

public class SignupScreen extends AppCompatActivity implements SignUpContract.View{

    TextView editFullName;
    TextView editSignupEmail;
    TextView editSignupPassword;
    TextView editConfirmPassword;
    private SignUpContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_screen);
        // Initialize buttons
        Button txtLogin = findViewById(R.id.txtLogin);
        Button btnSignup = findViewById(R.id.btnSignup);
        editFullName=findViewById(R.id.editFullName);
        editSignupEmail=findViewById(R.id.editSignupEmail);
        editSignupPassword=findViewById(R.id.editSignupPassword);
        editConfirmPassword=findViewById(R.id.editConfirmPassword);
        presenter = new SignUpPresenter(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName=editFullName.getText().toString().trim();
                String email = editSignupEmail.getText().toString().trim();
                String password   = editSignupPassword.getText().toString().trim();
                String Confirmpassword   = editConfirmPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty() || Confirmpassword.isEmpty()) {
                    Toast.makeText(SignupScreen.this, "Error: All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (!password.equals(Confirmpassword)) {Toast.makeText(SignupScreen.this, "Error: Confirm password mismatch with password.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    presenter.doSignUp(email, password,fullName);
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onSignUpSuccess(FirebaseUser user) {
        Intent intent = new Intent(SignupScreen.this, AppScreen.class);
        startActivity(intent);
    }

    @Override
    public void onSignUpFailure(String errorMessage) {
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

    }
}
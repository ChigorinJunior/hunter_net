package com.example.gron.hunternet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;

//import jp.wasabeef.glide.transformations.BlurTransformation;
//import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import android.text.TextUtils;

import static com.example.gron.hunternet.R.color.emptyfields;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private TextView labelWrongPassword;
    private EditText textPassword;
    private EditText textEmail;
    private Button buttonUseVkOkFb;
    private Button buttonShowPassword;
    private Button buttonForgotPassword;
    private Button buttonEnter;
    private Button buttonRegistration;
    private Button buttonAutarisation;
    private ImageButton imageButtonVk;
    private ImageButton imageButtonOk;
    private ImageButton imageButtonFb;
    private ImageButton imageButtonBackArrow;
    private ImageView imageUseVkOkFb;
    private ImageView imageShadow;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonShowPassword = (Button) findViewById(R.id.buttonShowPassword);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textEmail = (EditText) findViewById(R.id.textEmail);
        buttonShowPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textPassword.setTransformationMethod(null);
                buttonShowPassword.setVisibility(View.INVISIBLE);
            }
        });

        buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.forgot,
                        Toast.LENGTH_SHORT).show();
            }
        });

        labelWrongPassword = (TextView) findViewById(R.id.labelWrongPassword);
        buttonEnter = (Button) findViewById(R.id.buttonEnter);
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn(textEmail.getText().toString(), textPassword.getText().toString());
            }
        });

        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createAccount(textEmail.getText().toString(), textPassword.getText().toString());
            }
        });

        buttonAutarisation = (Button) findViewById(R.id.buttonAutarisation);
        buttonAutarisation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();
            }
        });
        imageButtonBackArrow = (ImageButton) findViewById(R.id.imageButtonBackArrow);
        imageButtonBackArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();
            }
        });

        buttonUseVkOkFb = (Button) findViewById(R.id.buttonUseVkOkFb);
        buttonUseVkOkFb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonUseVkOkFb.setVisibility(View.INVISIBLE);
                imageUseVkOkFb = (ImageView) findViewById(R.id.imageUseVkOkFb);
                imageUseVkOkFb.setVisibility(View.VISIBLE);
                imageShadow = (ImageView) findViewById(R.id.imageShadow);
                imageShadow.setVisibility(View.VISIBLE);

                imageButtonVk = (ImageButton) findViewById(R.id.imageButtonVk);
                imageButtonVk.setVisibility(View.VISIBLE);
                imageButtonOk = (ImageButton) findViewById(R.id.imageButtonOk);
                imageButtonOk.setVisibility(View.VISIBLE);
                imageButtonFb = (ImageButton) findViewById(R.id.imageButtonFb);
                imageButtonFb.setVisibility(View.VISIBLE);

                imageShadow.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        buttonUseVkOkFb.setVisibility(View.VISIBLE);
                        imageUseVkOkFb.setVisibility(View.INVISIBLE);
                        imageShadow.setVisibility(View.INVISIBLE);
                        imageButtonVk.setVisibility(View.INVISIBLE);
                        imageButtonOk.setVisibility(View.INVISIBLE);
                        imageButtonFb.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

                updateUI(null);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
       /*Glide.with(this).load(R.drawable.)
               asBitmap().apply()
               .bitmapTransform(new BlurTransformation(25))
                .into((ImageView) findViewById(R.id.imageUseVkOkFb));*/

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, R.string.auth_successfull,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, R.string.sign_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.sign_successfull,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(mAuth.getCurrentUser());

                        }
                    }
                });
    }

    private void signOut() {
        if (mAuth.getCurrentUser()!=null) {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, R.string.signout,
                    Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = textEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textEmail.setError(getString(R.string.error));
            valid = false;
        } else {
            textEmail.setError(null);
        }

        String password = textPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            textPassword.setError(getString(R.string.error));
            valid = false;
        } else {
            textPassword.setError(null);
        }
        changeColorofButton(valid);
        return valid;
    }

    private void changeColorofButton(boolean valid) {
        if (!valid) {
            buttonEnter.setText(R.string.emptyfields);
            buttonEnter.setBackgroundColor(getResources().getColor(R.color.emptyfields));
        } else {
            buttonEnter.setText(R.string.enter);
            buttonEnter.setBackgroundColor(getResources().getColor(R.color.myColorGreenRect));
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.buttonEnter).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.buttonEnter).setVisibility(View.VISIBLE);
        }
    }
}

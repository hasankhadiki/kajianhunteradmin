package tehhutan.app.kajianhunteradmin.app;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tehhutan.app.kajianhunteradmin.R;
import tehhutan.app.kajianhunteradmin.model.User;

public class SignUp extends AppCompatActivity {


    MaterialEditText editNrp, editEmail, editNama, editPassword;
    Button btnSignUp;
    DatabaseReference table_user;
    boolean UserTelahAda = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editNrp = (MaterialEditText)findViewById(R.id.et_nrp);
        editEmail = (MaterialEditText)findViewById(R.id.et_email);
        editNama = (MaterialEditText)findViewById(R.id.et_nama);
        editPassword = (MaterialEditText)findViewById(R.id.et_password);
        btnSignUp = (Button)findViewById(R.id.btn_signup);

        editEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(validateEmail(editEmail.getText().toString())){
                    editEmail.setError("Email is valid");
                }else{
                    editEmail.setError("Invalid email format");
                }
                return false;
            }
        });



        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFieldsForEmptyValues();

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Tunggu sebentar..");
                mDialog.show();

                table_user.child("Admin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    //Cek apakah NRP sudah ada didalam database
                        if(dataSnapshot.child(editNrp.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "No telepon telah terdaftar!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            cekUserBiasa(mDialog);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void cekUserBiasa(final ProgressDialog mDialog) {
        table_user.child("Regular").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cek apakah NRP sudah ada didalam database
                if(dataSnapshot.child(editNrp.getText().toString()).exists()){
                    mDialog.dismiss();
                    Toast.makeText(SignUp.this, "No telepon telah terdaftar!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mDialog.dismiss();
                    User user = new User(editEmail.getText().toString(), editNama.getText().toString(), editPassword.getText().toString(), null, editNrp.getText().toString());
                    table_user.child("Admin").child(editNrp.getText().toString()).setValue(user);
                    Toast.makeText(SignUp.this, "Pendaftaran akun baru berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean validateEmail(String string){
        if (TextUtils.isEmpty(string)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
        }
    }


    private  void checkFieldsForEmptyValues(){
        String email = editEmail.getText().toString();
        String nama = editNama.getText().toString();
        String nrp = editNrp.getText().toString();
        String password = editPassword.getText().toString();

        if (email.length() > 0 && nama.length() > 0 && nrp.length() > 0 && password.length() > 0) {
            btnSignUp.setEnabled(true);
        } else {
            btnSignUp.setEnabled(false);
        }

    }

}

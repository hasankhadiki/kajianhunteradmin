package tehhutan.app.kajianhunteradmin.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import tehhutan.app.kajianhunteradmin.Booking;
import tehhutan.app.kajianhunteradmin.R;
import tehhutan.app.kajianhunteradmin.model.User;

public class SignIn extends AppCompatActivity {

    MaterialEditText editNrp, editPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editNrp = (MaterialEditText)findViewById(R.id.et_nrp);
        editPassword = (MaterialEditText)findViewById(R.id.et_password);
        btnSignIn = (Button)findViewById(R.id.btn_signin);

        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Tunggu sebentar..");
                mDialog.show();

                table_user.child("Admin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                      //Check account existence in db
                      if(dataSnapshot.child(editNrp.getText().toString()).exists()) {


                          //Get user informastion
                          mDialog.dismiss();
                          User user = dataSnapshot.child(editNrp.getText().toString()).getValue(User.class);
                          if (user.getPassword().equals(editPassword.getText().toString())) {
//                              Toast.makeText(SignIn.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                              Intent bookingIntent = new Intent(SignIn.this, Booking.class);

                              startActivity(bookingIntent);
                              finish();
                          } else {
                              Toast.makeText(SignIn.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                          }
                      }
                      else
                      {
                          mDialog.dismiss();
                          Toast.makeText(SignIn.this, "Data pengguna tidak ada di Database", Toast.LENGTH_SHORT).show();
                      }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}

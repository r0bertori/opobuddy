package dam.tfc.opobuddy.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dam.tfc.opobuddy.databinding.ActivityRegistroBinding;
import dam.tfc.opobuddy.models.User;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbRef = FirebaseDatabase.getInstance().getReference("usuarios");
        auth = FirebaseAuth.getInstance();

        // 1. Comprobar que el correo sea válido
        // 2. Comprobar que el correo no esté usado ya
        // 3. Comprobar que las contraseñas coincidan
        // 4. Comprobar que el username no esté cogido

        binding.btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.etUsername.getText().toString().trim();
                String correo = binding.etCorreo.getText().toString().trim();
                String pwd = binding.etContrasenia.getText().toString().trim();
                String repetirPwd = binding.etRepetirContrasenia.toString().trim();

                if (correo.contains("@") && correo.contains(".")) {

                    dbRef.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                binding.tvMensajeError.setText("El correo ya está en uso");
                            } else {
                                if (pwd.equals(repetirPwd)) {

                                    dbRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                binding.tvMensajeError.setText("El nombre de usuario ya está cogido");
                                            } else {
                                                auth.createUserWithEmailAndPassword(correo, pwd)
                                                        .addOnCompleteListener(task -> {
                                                            if (task.isSuccessful()) {
                                                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                                                if (firebaseUser != null) {
                                                                    guardarUsuarioEnDatabase(username, correo, pwd);
                                                                }
                                                            } else {

                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                } else {
                                    binding.tvMensajeError.setText("Las contraseñas no coinciden");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    binding.tvMensajeError.setText("El correo no es válido");
                }

            }
        });

    }

    private void guardarUsuarioEnDatabase(String username, String correo, String pwd) {
        User user = new User(username, correo, pwd);
        dbRef.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "Registro realizado con éxito", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {

                }
            }
        });
    }
}
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dam.tfc.opobuddy.databinding.ActivityRecuperarContraseniaBinding;

public class RecuperarContraseniaActivity extends AppCompatActivity {
    private ActivityRecuperarContraseniaBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecuperarContraseniaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("usuarios");

        binding.ibVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecuperarContraseniaActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        binding.btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = binding.etCorreo.getText().toString().trim();
                
                if (correo.isEmpty()) {
                    Toast.makeText(RecuperarContraseniaActivity.this, "Introduce un correo", Toast.LENGTH_SHORT).show();
                } else {
                    existeCorreoBBDD(correo);
                }
                
            }
        });

    }

    private void existeCorreoBBDD(String correo) {
        usersRef.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.tvMensajeError.setVisibility(View.GONE);
                    enviarCorreo(correo);
                } else {
                    binding.tvMensajeError.setText("El correo no está registrado");
                    binding.tvMensajeError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecuperarContraseniaActivity.this, "Error en la BBDD", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarCorreo(String correo) {
        auth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RecuperarContraseniaActivity.this, "Correo enviado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecuperarContraseniaActivity.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
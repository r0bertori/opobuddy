package dam.tfc.opobuddy.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dam.tfc.opobuddy.R;
import dam.tfc.opobuddy.databinding.ActivityElegirUsernameBinding;
import dam.tfc.opobuddy.databinding.ActivityRecuperarContraseniaBinding;

public class ElegirUsernameActivity extends AppCompatActivity {

    private ActivityElegirUsernameBinding binding;

    // Esta clase es para que si se inicia sesión con google/meta por primera vez,
    // el usuario tenga username y luego se cree en la bbdd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityElegirUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ibVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ElegirUsernameActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        binding.btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ir a la página de inicio
            }
        });

    }
}
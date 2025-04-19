package dam.tfc.opobuddy.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dam.tfc.opobuddy.R;
import dam.tfc.opobuddy.ui.model.Amigo;
import dam.tfc.opobuddy.ui.model.Grupo;

public class FragmentSocial extends Fragment {

    private ImageView fotoPerfil;
    private TextView estadoUsuario;
    private ProgressBar barraProgreso;
    private ImageView botonAmigos, botonGrupos;
    private RecyclerView recyclerSocial;
    private SocialAdapter adapter;
    private List<Object> listaSocial;
    private boolean mostrandoAmigos = true;

    private DatabaseReference userRef;
    private String userId = "user1"; // Aquí pon tu lógica para obtener el ID del usuario

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        estadoUsuario = view.findViewById(R.id.estadoUsuario);
        barraProgreso = view.findViewById(R.id.barraProgreso);
        botonAmigos = view.findViewById(R.id.botonAmigos);
        botonGrupos = view.findViewById(R.id.botonGrupos);
        recyclerSocial = view.findViewById(R.id.recyclerSocial);

        recyclerSocial.setLayoutManager(new LinearLayoutManager(getContext()));
        listaSocial = new ArrayList<>();
        adapter = new SocialAdapter(listaSocial, mostrandoAmigos);
        recyclerSocial.setAdapter(adapter);

        userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId);

        cargarPerfil();
        cargarAmigos(); // Mostrar amigos por defecto

        botonAmigos.setOnClickListener(v -> {
            mostrandoAmigos = true;
            adapter = new SocialAdapter(listaSocial, mostrandoAmigos);
            recyclerSocial.setAdapter(adapter);
            cargarAmigos();
        });

        botonGrupos.setOnClickListener(v -> {
            mostrandoAmigos = false;
            adapter = new SocialAdapter(listaSocial, mostrandoAmigos);
            recyclerSocial.setAdapter(adapter);
            cargarGrupos();
        });

        return view;
    }

    private void cargarPerfil() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                String urlFoto = snapshot.child("foto_perfil").getValue(String.class);

                estadoUsuario.setText(estado != null ? estado : "Sin estado");

                if (urlFoto != null && !urlFoto.isEmpty()) {
                    Picasso.get()
                            .load(urlFoto)
                            .placeholder(R.drawable.perfil_por_defecto)
                            .error(R.drawable.perfil_por_defecto)
                            .into(fotoPerfil);
                } else {
                    fotoPerfil.setImageResource(R.drawable.perfil_por_defecto);
                }

                barraProgreso.setProgress(45); // Puedes cargar progreso real desde Firebase
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarAmigos() {
        userRef.child("amigos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaSocial.clear();
                for (DataSnapshot amigoSnapshot : snapshot.getChildren()) {
                    String amigoId = amigoSnapshot.getKey();
                    FirebaseDatabase.getInstance().getReference("Usuarios").child(amigoId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot amigoData) {
                                    String nombre = amigoId; // O reemplaza por amigoData.child("nombre").getValue(String.class);
                                    String estado = amigoData.child("estado").getValue(String.class);
                                    String foto = amigoData.child("foto_perfil").getValue(String.class);
                                    listaSocial.add(new Amigo(nombre, estado, foto));
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void cargarGrupos() {
        listaSocial.clear();
        listaSocial.add(new Grupo("Grupo 1", "Hace 2 horas")); // Sustituye por datos reales de Firebase si tienes
        listaSocial.add(new Grupo("Grupo 2", "Ayer"));
        adapter.notifyDataSetChanged();
    }
}

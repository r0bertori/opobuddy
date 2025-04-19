package dam.tfc.opobuddy.ui.social;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dam.tfc.opobuddy.R;
import dam.tfc.opobuddy.models.Amigo;
import dam.tfc.opobuddy.models.Grupo;

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private boolean esAmigo;

    public SocialAdapter(List<Object> items, boolean esAmigo) {
        this.items = items;
        this.esAmigo = esAmigo;
    }

    @Override
    public int getItemViewType(int position) {
        return esAmigo ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigos, parent, false);
            return new AmigoViewHolder(vista);
        } else {
            View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grupos, parent, false);
            return new GrupoViewHolder(vista);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (esAmigo) {
            Amigo a = (Amigo) items.get(position);
            AmigoViewHolder h = (AmigoViewHolder) holder;
            h.nombre.setText(a.nombre);
            h.estado.setText(a.estado);
            if (a.foto != null && !a.foto.isEmpty()) {
                Picasso.get()
                        .load(a.foto)
                        .placeholder(R.drawable.perfil_por_defecto)
                        .error(R.drawable.perfil_por_defecto)
                        .into(h.foto);
            } else {
                h.foto.setImageResource(R.drawable.perfil_por_defecto);
            }
        } else {
            Grupo g = (Grupo) items.get(position);
            GrupoViewHolder h = (GrupoViewHolder) holder;
            h.nombreGrupo.setText(g.nombre);
            h.ultimaActividad.setText("Última actividad: " + g.ultimaActividad);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class AmigoViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nombre, estado;
        ImageButton btnDesafio;

        AmigoViewHolder(View v) {
            super(v);
            foto = v.findViewById(R.id.fotoPerfil);
            nombre = v.findViewById(R.id.nombreAmigo);
            estado = v.findViewById(R.id.estadoAmigo);
            btnDesafio = v.findViewById(R.id.btnDesafio);
        }
    }

    static class GrupoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreGrupo, ultimaActividad;

        GrupoViewHolder(View v) {
            super(v);
            nombreGrupo = v.findViewById(R.id.nombreGrupo);
            ultimaActividad = v.findViewById(R.id.ultimaActividad);
        }
    }
}

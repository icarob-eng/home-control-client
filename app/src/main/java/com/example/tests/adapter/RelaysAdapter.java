package com.example.tests.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tests.R;
import com.example.tests.model.RelayModel;

import java.util.List;

public class RelaysAdapter extends RecyclerView.Adapter<RelaysAdapter.ViewHolderRelay>{

    private List<RelayModel> listaRelay;

    public RelaysAdapter(List<RelayModel> listaRelay) {
        this.listaRelay = listaRelay;
    }
    @NonNull
    @Override
    public ViewHolderRelay onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relay_item, parent, false);
        return new ViewHolderRelay(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRelay holder, int position) {
        holder.nome.setText(listaRelay.get(position).getNome());
        holder.id.setText(String.valueOf(listaRelay.get(position).getId()));

        holder.button.setOnClickListener(v -> {
            new Thread( () -> listaRelay.get(position).turnRelay(position + 1)).start();
        });
    }

    @Override
    public int getItemCount() {
        return listaRelay.size();
    }

    public class ViewHolderRelay extends RecyclerView.ViewHolder {

        TextView nome, id;
        ImageButton button;

        public ViewHolderRelay(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.relay_nome);
            id = itemView.findViewById(R.id.relay_id);
            button = itemView.findViewById(R.id.relay_button);

        }
    }
}

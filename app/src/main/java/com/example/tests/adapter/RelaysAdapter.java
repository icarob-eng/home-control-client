package com.example.tests.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tests.R;
import com.example.tests.dao.RelayDAO;
import com.example.tests.model.RelayModel;

import java.util.List;

public class RelaysAdapter extends RecyclerView.Adapter<RelaysAdapter.ViewHolderRelay>{

    private final List<RelayModel> listaRelay;
    private RelayDAO relayDAO;

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
        holder.id.setText(String.valueOf(position + 1));

        holder.btnConn.setOnClickListener(v -> new Thread(() -> listaRelay.get(position).turnRelay(position + 1)).start());

        holder.btnEdit.setOnClickListener(v -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setTitle("Atualizar item");
            EditText editText = new EditText(v.getContext());
            editText.setText(holder.nome.getText().toString());
            alert.setView(editText);

            relayDAO = new RelayDAO(v.getContext());

            alert.setPositiveButton("Confirmar", (dialogInterface, i) -> {

                String nome = editText.getText().toString().trim();

               if (!nome.isEmpty()) {

                   RelayModel relay = listaRelay.get(position);
                   relay.setNome(nome);
                   relayDAO.atualizar(relay);
                   listaRelay.set(position, relay);
                   notifyItemChanged(position);

               }
            });

            alert.setNegativeButton("Cancelar", null);
            alert.create();
            alert.show();

        });

        holder.btnDelete.setOnClickListener(v -> {

           AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
           alert.setTitle("Excluir item");
           alert.setMessage("Você deseja excluir o item?");
           relayDAO = new RelayDAO(v.getContext());

           alert.setPositiveButton("Sim", (dialogInterface, i) -> {

               RelayModel relayModel = listaRelay.get(position);
               relayDAO.deletar(relayModel);
               listaRelay.remove(position);
               notifyItemRemoved(position);
               notifyDataSetChanged();

           });

           alert.setNegativeButton("Não", null);
           alert.create();
           alert.show();

        });
    }

    @Override
    public int getItemCount() {
        return listaRelay.size();
    }

    public static class ViewHolderRelay extends RecyclerView.ViewHolder {

        TextView id, nome;
        ImageButton btnConn, btnEdit, btnDelete;

        public ViewHolderRelay(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.relay_nome);
            id = itemView.findViewById(R.id.relay_id);
            btnConn = itemView.findViewById(R.id.relay_button);
            btnEdit = itemView.findViewById(R.id.btn_editrelay);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}

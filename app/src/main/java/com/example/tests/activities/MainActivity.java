package com.example.tests.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tests.R;
import com.example.tests.SocketClient;
import com.example.tests.adapter.RelaysAdapter;
import com.example.tests.dao.RelayDAO;
import com.example.tests.model.RelayModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText fieldIP, fieldPort;
    private Button btnReset, btnPing, btnLED;
    private ImageButton btnNw, btnClear, btnAdd;
    private TextView txtOutput;
    private RecyclerView recyclerRelays;
    private RelaysAdapter relaysAdapter;
    private List<RelayModel> listaRelay = new ArrayList<>();
    private RelayDAO relayDAO;
    private static SocketClient socketClient; // static para ser recuperável

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relayDAO = new RelayDAO(this);
        listaRelay = relayDAO.listar();
        relaysAdapter = new RelaysAdapter(listaRelay);

        fieldIP = findViewById(R.id.edit_ip);
        fieldPort = findViewById(R.id.edit_port);

        txtOutput = findViewById(R.id.txt_output);
        txtOutput.setMovementMethod(new ScrollingMovementMethod());  // configura scrolling

        btnReset = findViewById(R.id.btn_reset);
        btnPing = findViewById(R.id.btn_ping);
        btnLED = findViewById(R.id.btn_LED);
        btnClear = findViewById(R.id.btn_clear);
        btnNw = findViewById(R.id.btn_nw);
        btnAdd = findViewById(R.id.btn_addrelay);

        btnClear.setOnClickListener(v -> clearTerminal());

        btnAdd.setOnClickListener(v -> addRelay());

        setupRecycler();
        setupSocket();
    }

    @Override
    protected void onResume(){
        super.onResume();
        new Thread(socketClient::testConn).start();
    }

    private void setupRecycler() {
        recyclerRelays = findViewById(R.id.recycler_relays);
        recyclerRelays.setAdapter(relaysAdapter);
        recyclerRelays.setHasFixedSize(true);
    }

    private void setupSocket(){
        // inicia uma instância do SocketClient passando esta classe como argumento para poder
        // acessar o getter do ID
        socketClient = new SocketClient(this);

        // configuração dos listeners: métodos do socket client para as ações de rede
        btnReset.setOnClickListener(v -> new Thread(socketClient::actionReset).start());
        btnPing.setOnClickListener(v -> new Thread(socketClient::actionPing).start());
        btnLED.setOnClickListener(v -> new Thread(socketClient::actionLED).start());

        btnNw.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, NetworkSetup.class))
        );
    }

    public void print(String text){
        // pequeno wrapper para adiconar linhas na output
        // precisa ser na Thread de UI para não lançar erro de threads
        runOnUiThread(() -> txtOutput.append("\n > " + text));
        // todo: adcionar horários nos logs
    }

    public void print(String text, String tag){
        // wrapper do print com logs na categoria debug
        this.print(text);
        Log.d(tag, text);
    }

    public String getIP(){
        // getter do IP
        return fieldIP.getText().toString();
    }

    public int getPort(){
        // getter da porta
        return Integer.parseInt(fieldPort.getText().toString());
    }

    public void clearTerminal(){
        runOnUiThread(() -> txtOutput.setText(" > "));
    }

    public static SocketClient getSocketClient(){ return socketClient; }

    public void addRelay() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Adicionar item");
        EditText editText = new EditText(this);
        alert.setView(editText);

        alert.setPositiveButton("Confirmar", (dialogInterface, i) -> {

            String nome = editText.getText().toString().trim();

            if (!nome.isEmpty()) {

                RelayModel relay = new RelayModel();
                relay.setNome(nome);
                relayDAO.salvar(relay);
                listaRelay.add(relay);
                relaysAdapter.notifyItemInserted(listaRelay.size() - 1);

            }
        });

        alert.create();
        alert.show();
    }
}
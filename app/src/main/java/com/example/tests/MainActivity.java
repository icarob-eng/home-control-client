package com.example.tests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText fieldIP, fieldPort;
    private Button btnClear, btnReset, btnPing, btnLED, btnRelay1, btnRelay2, btnNw;
    private TextView txtOutput;
    private static SocketClient socketClient; // static para ser recuperável

    public static SocketClient getSocketClient(){ return socketClient; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fieldIP = findViewById(R.id.fieldIP);
        fieldPort = findViewById(R.id.fieldPort);

        txtOutput = findViewById(R.id.txtOutput);
        txtOutput.setMovementMethod(new ScrollingMovementMethod());  // configura scrolling

        btnReset = findViewById(R.id.btnReset);
        btnPing = findViewById(R.id.btnPing);
        btnLED = findViewById(R.id.btnLED);
        btnRelay1 = findViewById(R.id.btnRelay1);
        btnRelay2 = findViewById(R.id.btnRelay2);
        btnClear = findViewById(R.id.btnClear);
        btnNw = findViewById(R.id.btnNw);

        btnClear.setOnClickListener(v -> clearTerminal());

        setupSocket();
    }

    @Override
    protected void onResume(){
        super.onResume();
        new Thread(socketClient::testConn).start();
    }

    private void setupSocket(){
        // inicia uma instância do SocketClient passando esta classe como argumento para poder
        // acessar o getter do ID
        socketClient = new SocketClient(this);

        // configuração dos listeners: métodos do socket client para as ações de rede
        btnReset.setOnClickListener(v -> new Thread(socketClient::actionReset).start());
        btnPing.setOnClickListener(v -> new Thread(socketClient::actionPing).start());
        btnLED.setOnClickListener(v -> new Thread(socketClient::actionLED).start());
        btnRelay1.setOnClickListener(v -> new Thread(() -> socketClient.actionRelay(1)).start());
        btnRelay2.setOnClickListener(v -> new Thread(() -> socketClient.actionRelay(2)).start());

        btnNw.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this,NetworkSetup.class))
        );
    }

    public void print(String text){
        // pequeno wrapper para adiconar linhas na output
        // precisa ser na Thread de UI para não lançar erro de threads
        runOnUiThread(() -> txtOutput.append("\n>" + text));
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
        runOnUiThread(() -> txtOutput.setText(""));
    }
}
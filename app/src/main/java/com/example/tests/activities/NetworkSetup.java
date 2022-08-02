package com.example.tests.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.tests.R;
import com.example.tests.SocketClient;
import com.example.tests.activities.MainActivity;

public class NetworkSetup extends AppCompatActivity {
    private Button btnAddNw, btnRmNw, btnBack;
    private EditText fieldSSID, fieldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nw_setup);

        // recupera o socket
        SocketClient socketClient = MainActivity.getSocketClient();

        fieldSSID = findViewById(R.id.fieldSSID);
        fieldPassword = findViewById(R.id.fieldPassword);

        btnAddNw = findViewById(R.id.btnAddNw);
        btnRmNw = findViewById(R.id.btnRmNw);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // ação de adicionar rede com SSID e senha
        btnAddNw.setOnClickListener(v -> new Thread(() -> {
            String SSID = fieldSSID.getText().toString();
            String password = fieldPassword.getText().toString();
            if (! SSID.equals(""))
                socketClient.actionAddNw(SSID, password);
        }).start());

        // ação de remover rede pelo SSID
        btnRmNw.setOnClickListener(v -> new Thread(() -> {
            String SSID = fieldSSID.getText().toString();
            if (! SSID.equals(""))
                socketClient.actionRmNw(SSID);
        }).start());
    }
}
package com.example.physlab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Changeable {

    Button buttonStart, buttonStop;
    EditText editH0, editG;
    int h0;
    float g;
    FragmentContainerView fragmentContainerView;

    boolean paramsCheck() {
        if (editH0.getText().toString().isBlank() || editG.getText().toString().isBlank()) {
            Toast.makeText(getApplicationContext(), R.string.toast_mes_1, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (Integer.parseInt(editH0.getText().toString()) > 0
                    && Float.parseFloat(editG.getText().toString()) > 0){
                h0 = Integer.parseInt(editH0.getText().toString());
                g = Float.parseFloat(editG.getText().toString());
                return true;
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_mes_1, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        editH0 = findViewById(R.id.editH0);
        editG = findViewById(R.id.editG);

        editG.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new CustomDialogFragment().show(getSupportFragmentManager(), "select");
                return true;
            }
        });

        fragmentContainerView = findViewById(R.id.fragmentContainerView);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paramsCheck()) {
                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    ((DrawView) fragmentContainerView.getChildAt(0)).startMove(h0, g);
                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                ((DrawView) fragmentContainerView.getChildAt(0)).stopMove();
            }
        });
    }

    @Override
    public void changeG(String g) {
        editG.setText(g);
    }
}
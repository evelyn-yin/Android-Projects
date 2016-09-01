package com.jiuzhang.guojing.yo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.text_anim);

        final EditText nameEdit = (EditText) findViewById(R.id.name);

        final Button button1 = (Button) findViewById(R.id.btn_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.startAnimation(animation);
                Toast.makeText(MainActivity.this, button1.getText() + " " + nameEdit.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        final Button button2 = (Button) findViewById(R.id.btn_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.startAnimation(animation);
                Toast.makeText(MainActivity.this, button2.getText() + " " + nameEdit.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        final Button button3 = (Button) findViewById(R.id.btn_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3.startAnimation(animation);
                Toast.makeText(MainActivity.this, button3.getText() + " " + nameEdit.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        final Button button4 = (Button) findViewById(R.id.btn_4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button4.startAnimation(animation);
                Toast.makeText(MainActivity.this, button4.getText() + " " + nameEdit.getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

package com.example.digitrecogniser;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.fingerpaintview.FingerPaintView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView pred;
    TextView proba;
    TextView timecost;
    FingerPaintView paint;
    Button btn_detect;
    Button btn_clear;

//object of Classifier class
    private Classifier mClassifier;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            mClassifier = new Classifier(this);
        }
        catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        pred = findViewById(R.id.pred);
        proba = findViewById(R.id.probab);
        timecost = findViewById(R.id.timecost);
        paint = findViewById(R.id.paint);
        btn_detect = findViewById(R.id.btn_detect);
        btn_clear = findViewById(R.id.btn_clear);



        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Creating a bitmap file & exporting it taking widht & height from classifier class
                Bitmap bitmap = paint.exportToBitmap(Classifier.IMG_WIDTH,Classifier.IMG_HEIGHT);

                Result res = mClassifier.classify(bitmap);
                pred.setText("Probability: "+res.getProbability()+"");
                proba.setText("Prediction: "+res.getNumber()+"");
                timecost.setText("TimeCost: "+res.getTimeCost()+"");
            }
        });
    }
}
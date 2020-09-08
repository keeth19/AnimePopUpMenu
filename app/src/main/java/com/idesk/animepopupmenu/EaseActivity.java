package com.idesk.animepopupmenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.PoupupMenuButton;

public class EaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease);

        initBmb(R.id.bmb1);
        initBmb(R.id.bmb2);
        initBmb(R.id.bmb3);
        initBmb(R.id.bmb4);
        initBmb(R.id.bmb5);
        initBmb(R.id.bmb6);
        initBmb(R.id.bmb7);
        initBmb(R.id.bmb8);
        initBmb(R.id.bmb9);
    }

    private PoupupMenuButton initBmb(int res) {
        PoupupMenuButton bmb = findViewById(res);
        assert bmb != null;
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());
        return bmb;
    }
}

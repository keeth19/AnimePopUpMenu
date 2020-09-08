package com.idesk.animepopupmenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.PoupupMenuButton;

public class FadeViewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fade_views);

        PoupupMenuButton bmb1 = findViewById(R.id.bmb1);
        for (int i = 0; i < bmb1.getPiecePlaceEnum().pieceNumber(); i++)
            bmb1.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

        PoupupMenuButton bmb2 = findViewById(R.id.bmb2);
        for (int i = 0; i < bmb2.getPiecePlaceEnum().pieceNumber(); i++)
            bmb2.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());
    }
}

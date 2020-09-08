package com.idesk.animepopupmenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.PoupupMenuButton;

public class SquareAndPieceCornerRadiusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_and_piece_corner_radius);

        PoupupMenuButton bmb1 = findViewById(R.id.bmb1);
        for (int i = 0; i < bmb1.getPiecePlaceEnum().pieceNumber(); i++)
            bmb1.addBuilder(BuilderManager.getSquareSimpleCircleButtonBuilder());

        PoupupMenuButton bmb2 = findViewById(R.id.bmb2);
        for (int i = 0; i < bmb2.getPiecePlaceEnum().pieceNumber(); i++)
            bmb2.addBuilder(BuilderManager.getSquareTextInsideCircleButtonBuilder());

        PoupupMenuButton bmb3 = findViewById(R.id.bmb3);
        for (int i = 0; i < bmb3.getPiecePlaceEnum().pieceNumber(); i++)
            bmb3.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilderWithDifferentPieceColor());

        PoupupMenuButton bmb4 = findViewById(R.id.bmb4);
        for (int i = 0; i < bmb4.getPiecePlaceEnum().pieceNumber(); i++)
            bmb4.addBuilder(BuilderManager.getPieceCornerRadiusHamButtonBuilder());
    }
}

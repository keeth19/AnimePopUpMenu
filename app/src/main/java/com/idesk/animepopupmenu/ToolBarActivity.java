package com.idesk.animepopupmenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.idesk.anime_popup_menu.ButtonEnum;
import com.idesk.anime_popup_menu.PoupupMenuButton;


public class ToolBarActivity extends AppCompatActivity {

    private PoupupMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        bmb = findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());
    }
}

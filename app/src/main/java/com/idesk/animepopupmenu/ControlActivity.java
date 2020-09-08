package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.PoupupMenuButton;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {

    private PoupupMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        bmb = findViewById(R.id.bmb);
        assert bmb != null;
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

        findViewById(R.id.boom).setOnClickListener(this);
        findViewById(R.id.reboom).setOnClickListener(this);
        findViewById(R.id.boom_immediately).setOnClickListener(this);
        findViewById(R.id.reboom_immediately).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boom:
                bmb.PopUp();
                break;
            case R.id.reboom:
                bmb.rePopUp();
                break;
            case R.id.boom_immediately:
                bmb.popUpImmediately();
                break;
            case R.id.reboom_immediately:
                bmb.rePopUpImmediately();
                break;
        }
    }
}

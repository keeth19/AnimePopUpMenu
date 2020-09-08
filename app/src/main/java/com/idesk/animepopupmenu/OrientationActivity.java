package com.idesk.animepopupmenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.ButtonEnum;
import com.idesk.anime_popup_menu.Piece.PiecePlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceEnum;
import com.idesk.anime_popup_menu.PoupupMenuButton;

public class OrientationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);

        PoupupMenuButton bmb = findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

        // android:configChanges="keyboardHidden|orientation|screenSize" in AndroidManifest.xml
        bmb.setOrientationAdaptable(true);
    }
}

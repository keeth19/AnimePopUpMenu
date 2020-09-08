package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.ButtonEnum;
import com.idesk.anime_popup_menu.OnMenuListener;
import com.idesk.anime_popup_menu.OnMenuListenerAdapter;
import com.idesk.anime_popup_menu.Piece.PiecePlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.MenuButton;
import com.idesk.anime_popup_menu.PopUpButtons.OnBMClickListener;
import com.idesk.anime_popup_menu.PopUpButtons.SimpleCircleButton;
import com.idesk.anime_popup_menu.PoupupMenuButton;

public class ListenerActivity extends AppCompatActivity {

    private TextView textViewForAnimation;
    private TextView textViewForButton;
    private PoupupMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener);

        textViewForButton = findViewById(R.id.text_for_button);
        textViewForAnimation = findViewById(R.id.text_for_animation);

        bmb = findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_6_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_6_3);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) addBuilder();

        // Use OnBoomListenerAdapter to listen part of methods
        bmb.setOnMenuListener(new OnMenuListenerAdapter() {
            @Override
            public void onPopUpWillShow() {
                super.onPopUpWillShow();
                // logic here
            }
        });

        // Use OnBoomListener to listen all methods
        bmb.setOnMenuListener(new OnMenuListener() {
            @Override
            public void onClicked(int index, MenuButton boomButton) {
                // If you have implement listeners for boom-buttons in builders,
                // then you shouldn't add any listener here for duplicate callbacks.
            }

            @Override
            public void onBackgroundClick() {
                textViewForAnimation.setText("Click background!!!");
            }

            @Override
            public void onPopUpWillHide() {
                Log.d("BMB", "onBoomWillHide: " + bmb.isPopUpd() + " " + bmb.isPopUpd());
                textViewForAnimation.setText("Will RE-BOOM!!!");
            }

            @Override
            public void onPopUpDidHide() {
                Log.d("BMB", "onBoomDidHide: " + bmb.isPopUpd() + " " + bmb.isPopUpd());
                textViewForAnimation.setText("Did RE-BOOM!!!");
            }

            @Override
            public void onPopUpWillShow() {
                Log.d("BMB", "onBoomWillShow: " + bmb.isPopUpd() + " " + bmb.isPopUpd());
                textViewForAnimation.setText("Will BOOM!!!");
            }

            @Override
            public void onPopUpDidShow() {
                Log.d("BMB", "onBoomDidShow: " + bmb.isPopUpd() + " " + bmb.isPopUpd());
                textViewForAnimation.setText("Did BOOM!!!");
            }
        });
    }

    private void addBuilder() {
        bmb.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(BuilderManager.getImageResource())
                .listener(new OnBMClickListener() {
                    @Override
                    public void onPopUpButtonClick(int index) {
                        textViewForButton.setText("No." + index + " boom-button is clicked!");
                    }
                }));
    }
}

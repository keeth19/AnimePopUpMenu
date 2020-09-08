package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.idesk.anime_popup_menu.ButtonEnum;
import com.idesk.anime_popup_menu.Piece.PiecePlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceEnum;
import com.idesk.anime_popup_menu.PoupupMenuButton;

public class ActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText("ActionBar");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);

      /*  PoupupMenuButton leftBmb =  actionBar.findViewById(R.id.action_bar_left_bmb);
        PoupupMenuButton rightBmb = actionBar.findViewById(R.id.action_bar_right_bmb);*/

       /* leftBmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        leftBmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);
        leftBmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        for (int i = 0; i < leftBmb.getPiecePlaceEnum().pieceNumber(); i++)
            leftBmb.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilderWithDifferentPieceColor());

        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++)
            rightBmb.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());*/
    }
}

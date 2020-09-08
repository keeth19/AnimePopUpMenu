package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.PoupupMenuButton;

public class ThreeDAnimationActivity extends AppCompatActivity {

    private TextView durationTextView;

    private PoupupMenuButton bmb1;
    private PoupupMenuButton bmb2;
    private PoupupMenuButton bmb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_d_animation);

        bmb1 = findViewById(R.id.bmb1);
        for (int i = 0; i < bmb1.getPiecePlaceEnum().pieceNumber(); i++)
            bmb1.addBuilder(BuilderManager.getSquareSimpleCircleButtonBuilder());

        bmb2 = findViewById(R.id.bmb2);
        for (int i = 0; i < bmb2.getPiecePlaceEnum().pieceNumber(); i++)
            bmb2.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());

        bmb3 = findViewById(R.id.bmb3);
        for (int i = 0; i < bmb3.getPiecePlaceEnum().pieceNumber(); i++)
            bmb3.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilder());

        Switch threeDAnimationSwitch = findViewById(R.id.three_d_animation_switch);
        threeDAnimationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bmb1.setUse3DTransformAnimation(isChecked);
                bmb2.setUse3DTransformAnimation(isChecked);
                bmb3.setUse3DTransformAnimation(isChecked);
            }
        });
        threeDAnimationSwitch.setChecked(true);

        SeekBar durationSeekBar = findViewById(R.id.duration_seek);
        durationSeekBar.setMax(3000);
        durationSeekBar.setProgress(300);
        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                durationTextView.setText("Show/Hide duration = " + seekBar.getProgress() + " ms");
                bmb1.setDuration(progress);
                bmb2.setDuration(progress);
                bmb3.setDuration(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        durationTextView = findViewById(R.id.duration_text);
        durationTextView.setText("Show/Hide duration = " + durationSeekBar.getProgress() + " ms");
    }
}

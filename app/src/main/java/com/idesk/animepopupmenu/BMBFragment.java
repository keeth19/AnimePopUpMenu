package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.idesk.anime_popup_menu.PoupupMenuButton;

public class BMBFragment extends Fragment {

    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_bmb, container, false);

        TextView text = fragment.findViewById(R.id.text);
        text.setText(String.valueOf(position));

        PoupupMenuButton bmb1 = fragment.findViewById(R.id.bmb1);
        for (int i = 0; i < bmb1.getPiecePlaceEnum().pieceNumber(); i++)
            bmb1.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

        PoupupMenuButton bmb2 = fragment.findViewById(R.id.bmb2);
        for (int i = 0; i < bmb2.getPiecePlaceEnum().pieceNumber(); i++)
            bmb2.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

        return fragment;
    }

    public Fragment position(int position) {
        this.position = position;
        return this;
    }

}

package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.ButtonEnum;
import com.idesk.anime_popup_menu.Piece.PiecePlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceEnum;
import com.idesk.anime_popup_menu.PoupupMenuButton;

import java.util.ArrayList;

public class TextInsideCircleButtonActivity extends AppCompatActivity {

    private PoupupMenuButton bmb;
    private ArrayList<Pair> piecesAndButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_inside_circle_button);

        bmb = findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_1);
        bmb.addBuilder(BuilderManager.getTextInsideCircleButtonBuilder());

        ListView listView = findViewById(R.id.list_view);
        assert listView != null;
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,
                BuilderManager.getCircleButtonData(piecesAndButtons)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bmb.setPiecePlaceEnum((PiecePlaceEnum) piecesAndButtons.get(position).first);
                bmb.setButtonPlaceEnum((ButtonPlaceEnum) piecesAndButtons.get(position).second);
                bmb.clearBuilders();
                for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
                    bmb.addBuilder(BuilderManager.getTextInsideCircleButtonBuilder());
            }
        });
    }
}

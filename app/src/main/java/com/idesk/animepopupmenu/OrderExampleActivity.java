package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.Animation.OrderEnum;
import com.idesk.anime_popup_menu.ButtonEnum;
import com.idesk.anime_popup_menu.Piece.PiecePlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceEnum;
import com.idesk.anime_popup_menu.PoupupMenuButton;

import java.util.ArrayList;
import java.util.List;

public class OrderExampleActivity extends AppCompatActivity {

    private PoupupMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_example);

        bmb = findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

        ListView listView = findViewById(R.id.list_view);
        assert listView != null;
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bmb.setOrderEnum(OrderEnum.values()[position]);
            }
        });
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < OrderEnum.values().length - 1; i++) data.add(OrderEnum.values()[i] + "");
        return data;
    }
}

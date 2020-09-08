package com.idesk.animepopupmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.idesk.anime_popup_menu.PoupupMenuButton;


public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ListView listView = findViewById(R.id.list_view);
        assert listView != null;
        listView.setAdapter(new MyAdapter());
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoupupMenuButton bmb = view.findViewById(R.id.bmb1);
                bmb.PopUp();
            }
        });
    }

    static class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 1000;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);

                viewHolder = new ViewHolder();
                viewHolder.text = convertView.findViewById(R.id.text);
                viewHolder.bmb1 = convertView.findViewById(R.id.bmb1);
                viewHolder.bmb2 = convertView.findViewById(R.id.bmb2);
                viewHolder.bmb3 = convertView.findViewById(R.id.bmb3);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.text.setText("No. " + position);

            viewHolder.bmb1.clearBuilders();
            for (int i = 0; i < viewHolder.bmb1.getPiecePlaceEnum().pieceNumber(); i++)
                viewHolder.bmb1.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

            viewHolder.bmb2.clearBuilders();
            for (int i = 0; i < viewHolder.bmb2.getPiecePlaceEnum().pieceNumber(); i++)
                viewHolder.bmb2.addBuilder(BuilderManager.getHamButtonBuilder());

            viewHolder.bmb3.clearBuilders();
            for (int i = 0; i < viewHolder.bmb3.getButtonPlaceEnum().buttonNumber(); i++)
                viewHolder.bmb3.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

            return convertView;
        }

        class ViewHolder {
            TextView text;
            PoupupMenuButton bmb1;
            PoupupMenuButton bmb2;
            PoupupMenuButton bmb3;
        }
    }
}

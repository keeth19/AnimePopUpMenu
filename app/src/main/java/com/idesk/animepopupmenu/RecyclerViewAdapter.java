package com.idesk.animepopupmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.idesk.anime_popup_menu.PoupupMenuButton;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText("No. " + position);

        holder.bmb1.clearBuilders();
        for (int i = 0; i < holder.bmb1.getPiecePlaceEnum().pieceNumber(); i++)
            holder.bmb1.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());

        holder.bmb2.clearBuilders();
        for (int i = 0; i < holder.bmb2.getPiecePlaceEnum().pieceNumber(); i++)
            holder.bmb2.addBuilder(BuilderManager.getHamButtonBuilder());

        holder.bmb3.clearBuilders();
        for (int i = 0; i < holder.bmb3.getButtonPlaceEnum().buttonNumber(); i++)
            holder.bmb3.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        PoupupMenuButton bmb1;
        PoupupMenuButton bmb2;
        PoupupMenuButton bmb3;

        ViewHolder(View view) {
            super(view);
            this.text = view.findViewById(R.id.text);
            this.bmb1 = view.findViewById(R.id.bmb1);
            this.bmb2 = view.findViewById(R.id.bmb2);
            this.bmb3 = view.findViewById(R.id.bmb3);
        }
    }
}

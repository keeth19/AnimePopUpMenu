package com.idesk.anime_popup_menu;

import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.idesk.anime_popup_menu.Animation.AnimationManager;

@SuppressLint("ViewConstructor")
class BackgroundView extends FrameLayout {

    private int dimColor;

    protected BackgroundView(Context context, final PoupupMenuButton bmb) {
        super(context);

        dimColor = bmb.getDimColor();

        ViewGroup rootView = bmb.getParentView();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                rootView.getWidth(),
                rootView.getHeight());
        setLayoutParams(params);
        setBackgroundColor(Color.TRANSPARENT);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bmb.onBackgroundClicked();
            }
        });
        setMotionEventSplittingEnabled(false);
        rootView.addView(this);
    }

    protected void reLayout(final PoupupMenuButton bmb) {
        ViewGroup rootView = bmb.getParentView();
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.width = rootView.getWidth();
        params.height = rootView.getHeight();
        setLayoutParams(params);
    }

    protected void dim(long duration, AnimatorListenerAdapter completeListener) {
        setVisibility(VISIBLE);
        AnimationManager.animate(
                this, "backgroundColor", 0, duration, new ArgbEvaluator(), completeListener,
                Color.TRANSPARENT, dimColor);
    }

    protected void light(long duration, AnimatorListenerAdapter completeListener) {
        AnimationManager.animate(
                this, "backgroundColor", 0, duration, new ArgbEvaluator(), completeListener,
                dimColor, Color.TRANSPARENT);
    }
}

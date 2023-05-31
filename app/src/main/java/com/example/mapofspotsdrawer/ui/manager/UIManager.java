package com.example.mapofspotsdrawer.ui.manager;

import android.view.View;
import android.widget.ImageButton;

import com.example.mapofspotsdrawer.R;

public class UIManager {
    public static void hideListMapImageButton(ImageButton imageButton) {
        imageButton.setVisibility(View.GONE);
    }

    public static void showListMapImageButton(ImageButton imageButton) {
        imageButton.setImageResource(R.drawable.icon_show_list);
        imageButton.setVisibility(View.VISIBLE);
    }
}

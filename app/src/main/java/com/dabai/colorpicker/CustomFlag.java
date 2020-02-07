package com.dabai.colorpicker;

import android.content.Context;
import android.widget.TextView;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.flag.FlagView;

public class CustomFlag extends FlagView {

    private TextView textView;



    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);

    }

    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {
        textView.setText("#" + colorEnvelope.getHexCode());

    }
}
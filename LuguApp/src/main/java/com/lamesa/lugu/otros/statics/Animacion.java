package com.lamesa.lugu.otros.statics;


import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lamesa.lugu.R;

public class Animacion {


    public static Animation alpha_in(Context mContext) {
        Animation animacion_alpha_in = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_out);
        return animacion_alpha_in;
    }

    public static Animation alpha_out(Context mContext) {
        Animation animacion_alpha_out = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_out);
        return animacion_alpha_out;
    }

    public static Animation anim_alpha_in(Context mContext) {
        Animation animacion_alpha_in = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_in);
        return animacion_alpha_in;
    }

    public static Animation anim_alpha_out(Context mContext) {
        Animation animacion_alpha_in = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_out);
        return animacion_alpha_in;
    }

    public static Animation anim_slide_bottom_in(Context mContext) {
        Animation animacion_alpha_in = AnimationUtils.loadAnimation(mContext, R.anim.anim_slide_bottom_in);
        return animacion_alpha_in;
    }

    public static Animation anim_slide_bottom_out(Context mContext) {
        Animation animacion_alpha_in = AnimationUtils.loadAnimation(mContext, R.anim.anim_slide_bottom_out);
        return animacion_alpha_in;
    }

    public static Animation enter_ios_anim(Context mContext) {
        Animation enter_ios_anim = AnimationUtils.loadAnimation(mContext, R.anim.enter_ios_anim);
        return enter_ios_anim;
    }

    public static Animation exit_ios_anim(Context mContext) {
        Animation exit_ios_anim = AnimationUtils.loadAnimation(mContext, R.anim.exit_ios_anim);
        return exit_ios_anim;
    }

    public static Animation bounce(Context mContext) {
        Animation exit_ios_anim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        return exit_ios_anim;
    }

    public static Animation fade_in_real(Context mContext) {
        Animation exit_ios_anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_real);
        return exit_ios_anim;
    }

    public static Animation fading_out_real(Context mContext) {
        Animation exit_ios_anim = AnimationUtils.loadAnimation(mContext, R.anim.fading_out_real);
        return exit_ios_anim;
    }


    public static Animation scale_up(Context mContext) {
        Animation exit_ios_anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
        return exit_ios_anim;
    }

    public static Animation scale_down(Context mContext) {
        Animation exit_ios_anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_down);
        return exit_ios_anim;
    }

}
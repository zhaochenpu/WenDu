package com.nightfeed.wendu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nightfeed.wendu.R;
import com.nightfeed.wendu.activity.BrowsePictureActivity;
import com.nightfeed.wendu.activity.ImageWordActivity;
import com.nightfeed.wendu.view.flowingdrawer.MenuFragment;


public class MainMenuFragment extends MenuFragment {

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.main_fragment_menu, container,false);
        initView();
        return  setupReveal(view) ;
    }


    public  void  initView(){

        view.findViewById(R.id.duhua).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ImageWordActivity.class));
            }
        });

        view.findViewById(R.id.shangtu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BrowsePictureActivity.class));
            }
        });
    }
}

package com.example.instantmeasure;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Measure extends Fragment {





    public Measure() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Intent i =  new Intent(getActivity(),cam.class);
        startActivity(i);



        View inflate = inflater.inflate(R.layout.fragment_measure, container, false);

        ViewGroup grp =(ViewGroup) inflater.inflate(R.layout.fragment_account,null);

     TextView op = grp.findViewById(R.id.textView2);

        return inflate;
    }

}

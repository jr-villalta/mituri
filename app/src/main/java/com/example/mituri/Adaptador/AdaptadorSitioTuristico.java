package com.example.mituri.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mituri.Clases.SitioTuristico;
import com.example.mituri.R;

import java.util.ArrayList;

public class AdaptadorSitioTuristico  extends BaseAdapter {


    public ArrayList<SitioTuristico> datos;
    public Context context;

    TextView TxtNombreItem, TxtDescripcionItem;

    public AdaptadorSitioTuristico(ArrayList<SitioTuristico> datos, Context context) {
        this.datos = datos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int i) {
        return datos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SitioTuristico Sitio = (SitioTuristico) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_sitio_turistico,null);

        TxtNombreItem = view.findViewById(R.id.TxtNombreItem);
        TxtDescripcionItem = view.findViewById(R.id.TxtDescripcionItem);
        ImageView img = view.findViewById(R.id.ImgItem);

        TxtNombreItem.setText(Sitio.getNombre());
        TxtDescripcionItem.setText(Sitio.getDescripcion());
        Glide.with(context).load(Sitio.getFoto()).into(img);

        return view;
    }
}

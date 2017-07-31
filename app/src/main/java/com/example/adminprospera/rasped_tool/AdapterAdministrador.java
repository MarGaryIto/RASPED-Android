package com.example.adminprospera.rasped_tool;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterAdministrador extends BaseAdapter {
    private Context context;
    private ArrayList<ModelAdministrador> arrayList;

    public AdapterAdministrador(Context context, ArrayList<ModelAdministrador> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_category,null);
        }

        TextView tv_it_nombre = (TextView) convertView.findViewById(R.id.tv_it_nombre);
        TextView tv_it_fecha = (TextView) convertView.findViewById(R.id.tv_it_fecha);
        TextView tv_it_cupo = (TextView) convertView.findViewById(R.id.tv_it_cupo);
        TextView tv_it_hora = (TextView) convertView.findViewById(R.id.tv_it_hora);

        tv_it_nombre.setText(arrayList.get(position).getNombrePersonal());
        tv_it_fecha.setText(arrayList.get(position).getFecha());
        tv_it_cupo.setText(arrayList.get(position).getCupo());
        tv_it_hora.setText(arrayList.get(position).getHora());

        return convertView;
    }
}

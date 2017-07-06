package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FragAdmPersonal extends Fragment {

    ListView lv_ad_personal;

    public FragAdmPersonal() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.frag_ad_personal, container, false);
    }

    //para llenar este fragment se usa el metodo onActivityCreated
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        lv_ad_personal = (ListView) getActivity().findViewById(R.id.lv_ad_personal);

        Context context = this.getActivity();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);

        int dimensionPersonal = sp_datosPersonal.getInt(getString(R.string.sp_dimensionArrayPersonal_key),1);

        String[] elementos = new String[dimensionPersonal];

        for (int i=0;dimensionPersonal>i;i++){
            String personal = sp_datosPersonal.getString(getString(R.string.sp_nombrePersonal_key)+i,"null");
            elementos[i] = personal;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, elementos);
        lv_ad_personal.setAdapter(adapter);
        lv_ad_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

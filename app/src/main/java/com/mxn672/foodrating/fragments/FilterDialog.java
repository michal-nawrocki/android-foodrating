package com.mxn672.foodrating.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.FilterType;
import com.mxn672.foodrating.data.QueryDistance;
import com.mxn672.foodrating.data.QueryType;
import com.mxn672.foodrating.fragments.interfaces.FilterDialogListener;

public class FilterDialog extends DialogFragment{
    private View view;
    private QueryType searchBy = QueryType.NAME;
    private QueryDistance maxDistance = QueryDistance.THREE_MILES;
    private FilterType filterBy = FilterType.DISTANCE;

    // Save preferences using this
    SharedPreferences.Editor editor;
    // Load preferences using this
    SharedPreferences prefs;


    public FilterDialog() {

    }

    // Use this instance of the interface to deliver action events
    FilterDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (FilterDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filter, null);
        editor = getActivity().getPreferences(0).edit();
        prefs = getActivity().getPreferences(0);

        // Spinner SearchBy setup
        Spinner fSearchBy = view.findViewById(R.id.filter_typeSpinner);
        ArrayAdapter<CharSequence> adapter_fSearchBy = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_searchBy, android.R.layout.simple_spinner_item);
        adapter_fSearchBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fSearchBy.setAdapter(adapter_fSearchBy);
        fSearchBy.setSelection(prefs.getInt("searchBy_selected", 0));
        fSearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store the preference
                editor.putInt("searchBy_selected", fSearchBy.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding QueryType
                switch (result){
                    case "Name":
                        searchBy = QueryType.NAME;
                        break;
                    case "Street":
                        searchBy = QueryType.STREET;
                        break;
                    case "City":
                        searchBy = QueryType.CITY;
                        break;
                    case "Post Code":
                        searchBy = QueryType.POSTCODE;
                        break;
                }
                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Spinner MaxDistance setup
        Spinner fMaxDistance = view.findViewById(R.id.filter_distanceSpinner);
        ArrayAdapter<CharSequence> adapter_fMaxDistance = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_maxDistance, android.R.layout.simple_spinner_item);
        adapter_fMaxDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fMaxDistance.setAdapter(adapter_fMaxDistance);
        fMaxDistance.setSelection(prefs.getInt("maxDistance_selected", 0));
        fMaxDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("maxDistance_selected", fMaxDistance.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding QueryDistance
                switch (result){
                    case "1 mi.":
                        maxDistance = QueryDistance.ONE_MILE;
                        break;
                    case "2 mi.":
                        maxDistance = QueryDistance.TWO_MILES;
                        break;
                    case "3 mi.":
                        maxDistance = QueryDistance.THREE_MILES;
                        break;
                    case "5 mi.":
                        maxDistance = QueryDistance.FIVE_MILES;
                        break;
                    case "10 mi.":
                        maxDistance = QueryDistance.TEN_MILES;
                        break;
                    case "No limit":
                        maxDistance = QueryDistance.NO_LIMIT;
                        break;
                }

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Spinner FilterBy setup
        Spinner fFilterBy = view.findViewById(R.id.filter_filterSpinner);
        ArrayAdapter<CharSequence> adapter_fFilterBy = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_filterBy, android.R.layout.simple_spinner_item);
        adapter_fFilterBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fFilterBy.setAdapter(adapter_fFilterBy);
        fFilterBy.setSelection(prefs.getInt("filterBy_selected", 3));
        fFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("filterBy_selected", fFilterBy.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding QueryDistance
                switch (result){
                    case "Business Type":
                        filterBy = FilterType.TYPE;
                        break;
                    case "Date Of Inspection":
                        filterBy = FilterType.DATE;
                        break;
                    case "Local Authority":
                        filterBy = FilterType.AUTHORITY;
                        break;
                    case "Distance":
                        filterBy = FilterType.DISTANCE;
                        break;
                    case "Rating":
                        filterBy = FilterType.RATING;
                        break;
                    case "Region":
                        filterBy = FilterType.REGION;
                        break;
                }

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        builder.setView(view);
        builder.setPositiveButton("Apply",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success

                listener.onDialogPositiveClick(searchBy, maxDistance, filterBy);
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return builder.create();
    }
}

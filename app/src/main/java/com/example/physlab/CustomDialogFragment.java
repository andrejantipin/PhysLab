package com.example.physlab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    ListView list;
    Changeable changeable;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        changeable = (Changeable) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        list = view.findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice,
                getResources().getStringArray(R.array.planets)));
        list.setItemChecked(0, true);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeable.changeG(getResources().getStringArray(R.array.g)
                                [list.getCheckedItemPosition()]);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}

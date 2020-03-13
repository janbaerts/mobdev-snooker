package com.janbaerts.android.snookerscoreboard.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;

import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;

public class SelectPictureDialogFragment extends DialogFragment {

    private SelectedPictureListener selectedPictureListener;
    FavouriteBall selectedPicture;

    public SelectPictureDialogFragment() {
        // Required empty public constructor
    }

    public interface SelectedPictureListener {
        void onPictureTapped(int selectedColor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selectedPictureListener = (SelectedPictureListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException("Context doesn't implement SelectedPictureListener.");
        }

    }

    // TODO: Nice to have: customized list using ListAdapter.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose color...")
            .setItems(R.array.ball_colors, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int color) {
                    Log.d("JAN", "In onClick van onCreateDialog().");
                    Log.d("JAN", "You clicked item number " + color + ".");
                    selectedPictureListener.onPictureTapped(color);
                }
            });

        return builder.create();
    }

}

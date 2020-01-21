package com.janbaerts.android.snookerscoreboard.viewmodels;

import androidx.lifecycle.ViewModel;

import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;

public class CreateNewPlayerViewModel extends ViewModel {

    private FavouriteBall favouriteBall;
    private String[] fields;

    public CreateNewPlayerViewModel() {
        fields = new String[5];
        setFavouriteBall(FavouriteBall.REDBALL);
    }

    public FavouriteBall getFavouriteBall() {
        return favouriteBall;
    }

    public void setFavouriteBall(FavouriteBall favouriteBall) {
        this.favouriteBall = favouriteBall;
    }

    public String[] getFields() {
        return fields;
    }

    public void setField(int i, String s) {
        fields[i] = s;
    }

}

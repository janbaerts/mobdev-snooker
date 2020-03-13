package com.janbaerts.android.snookerscoreboard.recyclerviews;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class SearchPlayerRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Player> playerList;

    public static class SearchPlayerRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        ImageView favouriteBallImageView;
        TextView playerNameTextView;
        TextView userNameTextView;
        SearchPlayerRecyclerViewViewHolder(@Nonnull ConstraintLayout constraintLayout) {
            super(constraintLayout);
            favouriteBallImageView = constraintLayout.findViewById(R.id.favouriteBallImageView);
            playerNameTextView = constraintLayout.findViewById(R.id.playerNameTextView);
            userNameTextView = constraintLayout.findViewById(R.id.userNameTextView);
        }
    }

    public SearchPlayerRecyclerViewAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_player_list_item_view, parent, false);
        SearchPlayerRecyclerViewViewHolder svh = new SearchPlayerRecyclerViewViewHolder(constraintLayout);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchPlayerRecyclerViewViewHolder viewHolder = (SearchPlayerRecyclerViewViewHolder)holder;
        Player displayedPlayer = playerList.get(position);

        viewHolder.favouriteBallImageView.setImageDrawable(holder.itemView.getResources().
                getDrawable(displayedPlayer.getFavouriteBall().getResource(), null));
        viewHolder.playerNameTextView.setText(displayedPlayer.getFullName());
        viewHolder.userNameTextView.setText(displayedPlayer.getEmail());
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public Player getPlayerFromPlayerList(String playerId) {
        for (Player p : playerList) {
            if (p.getEmail().equalsIgnoreCase(playerId))
                return p;
        }
        return null;
    }

}

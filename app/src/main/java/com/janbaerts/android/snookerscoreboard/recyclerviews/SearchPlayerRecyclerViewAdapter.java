package com.janbaerts.android.snookerscoreboard.recyclerviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.List;

public class SearchPlayerRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Player> playerList;

    public static class SearchPlayerRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public SearchPlayerRecyclerViewViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }

    public SearchPlayerRecyclerViewAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_player_list_item_view, parent, false);
        SearchPlayerRecyclerViewViewHolder svh = new SearchPlayerRecyclerViewViewHolder(tv);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SearchPlayerRecyclerViewViewHolder)holder).textView.setText(playerList.toArray()[position].toString());
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

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//    }
}

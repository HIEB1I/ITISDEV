package com.mobdeve.sustainabite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private Context context;
    private List<SearchResult> searchResults;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String name);
    }

    public SearchResultsAdapter(Context context, List<SearchResult> searchResults, OnItemClickListener listener) {
        this.context = context;
        this.searchResults = searchResults;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResult searchResult = searchResults.get(position);

        holder.nameTextView.setText(searchResult.getName());
        holder.secondaryTextView.setText(searchResult.getSecondaryInfo());

        // Make name clickable
        holder.nameTextView.setOnClickListener(v -> listener.onItemClick(searchResult.getName()));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, secondaryTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            secondaryTextView = itemView.findViewById(R.id.secondaryTextView);
        }
    }
}

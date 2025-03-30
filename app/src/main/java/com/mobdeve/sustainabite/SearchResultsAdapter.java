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
        void onItemClick(String item);
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
        holder.titleTextView.setText(searchResult.getTitle());

        // Clear any previous views to prevent duplication
        holder.resultsLayout.removeAllViews();

        // Add individual results dynamically
        for (String result : searchResult.getResults()) {
            TextView resultTextView = new TextView(context);
            resultTextView.setText(result);
            resultTextView.setTextSize(14);
            resultTextView.setPadding(10, 5, 10, 5);
            resultTextView.setBackgroundResource(android.R.drawable.list_selector_background);
            resultTextView.setClickable(true);

            // Handle clicks on each item
            resultTextView.setOnClickListener(v -> listener.onItemClick(result));

            // Add TextView dynamically to the layout
            holder.resultsLayout.addView(resultTextView);
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        LinearLayout resultsLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            resultsLayout = itemView.findViewById(R.id.resultsLayout);
        }
    }
}

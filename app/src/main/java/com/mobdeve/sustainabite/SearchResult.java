package com.mobdeve.sustainabite;

import java.util.List;

public class SearchResult {
    private String title;
    private List<String> results;

    public SearchResult(String title, List<String> results) {
        this.title = title;
        this.results = results;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getResults() {
        return results;
    }
}


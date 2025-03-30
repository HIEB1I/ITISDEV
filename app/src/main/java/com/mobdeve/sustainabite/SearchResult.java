package com.mobdeve.sustainabite;

import java.util.List;

public class SearchResult {
    private String name;
    private String secondaryInfo;

    public SearchResult(String name, String secondaryInfo) {
        this.name = name;
        this.secondaryInfo = secondaryInfo;
    }

    public String getName() {
        return name;
    }

    public String getSecondaryInfo() {
        return secondaryInfo;
    }
}


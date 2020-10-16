package com.billBook.apiServices;

import java.io.Serializable;
import java.util.List;

public class ApiResponse implements Serializable {

    private int resultCount;
    private List<Object> results;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<Object> getResults() {
        return results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

}
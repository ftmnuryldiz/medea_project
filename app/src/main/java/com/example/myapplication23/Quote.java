package com.example.myapplication23;

public class Quote {
    String quote;

String quoteID;

    public String getQuoteID() {
        return quoteID;
    }
public Quote(){

}
    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }

    public Quote(String quote) {
        this.quote = quote;

    }


    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}

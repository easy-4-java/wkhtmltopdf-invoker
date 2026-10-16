package com.github.jhonnymertz.Calibre.wrapper.params;

public enum Symbol {

    separator(" "), 
    
    param("");

    private final String symbol;

    Symbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

}

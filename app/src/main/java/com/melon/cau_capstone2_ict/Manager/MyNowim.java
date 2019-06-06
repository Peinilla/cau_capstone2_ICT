package com.melon.cau_capstone2_ict.Manager;

public class MyNowim {
    private String word;
    private int count;

    public MyNowim(String s, int i){
        word = s;
        count = i;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public String getWord() {
        return word;
    }
}

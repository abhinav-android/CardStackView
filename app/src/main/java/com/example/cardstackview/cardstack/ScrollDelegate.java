package com.example.cardstackview.cardstack;

public interface ScrollDelegate {
    void scrollViewTo(int x, int y);
    void setViewScrollY(int y);
    void setViewScrollX(int x);
    int getViewScrollY();
    int getViewScrollX();
}

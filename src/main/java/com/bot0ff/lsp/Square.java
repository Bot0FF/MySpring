package com.bot0ff.lsp;

public class Square extends Rectangle {

    void setSize(int size) {
        this.width = size;
        this.height = size;
    }

    @Override
    public void setWidth(int width) {
        this.setSize(width);
    }

    @Override
    public void setHeight(int height) {
        this.setSize(height);
    }
}

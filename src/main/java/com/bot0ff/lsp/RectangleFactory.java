package com.bot0ff.lsp;

public class RectangleFactory implements AbstractRectangleFactory{

    @Override
    public Rectangle get() {
        return new Rectangle();
    }
}

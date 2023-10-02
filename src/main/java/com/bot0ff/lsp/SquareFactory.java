package com.bot0ff.lsp;

public class SquareFactory implements AbstractRectangleFactory{

    @Override
    public Rectangle get() {
        return new Square();
    }
}

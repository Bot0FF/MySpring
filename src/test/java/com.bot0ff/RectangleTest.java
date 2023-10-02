package com.bot0ff;

import com.bot0ff.lsp.AbstractRectangleFactory;
import com.bot0ff.lsp.Rectangle;
import com.bot0ff.lsp.Square;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class RectangleTest {

    @Test
    void test() {
        Rectangle rectangle = ServiceLoader.load(AbstractRectangleFactory.class)
                        .findFirst().orElseThrow().get();
        rectangle.setHeight(10);
        rectangle.setWidth(3);

        assertInstanceOf(Square.class, rectangle);
        assertEquals(30, rectangle.getArea());
    }
}

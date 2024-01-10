package io.nuvolo.juice.infrastructure.text;

public interface TextStorage {
    void setText(String text, Point position, BoxDimensions dimensions);

    String getText(Point position, BoxDimensions dimensions);
}

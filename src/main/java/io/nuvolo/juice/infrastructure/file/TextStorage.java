package io.nuvolo.juice.infrastructure.file;

import java.util.ArrayList;
import java.util.Collections;

public class TextStorage {
    private final BoxDimensions dimensions;
    private final ArrayList<Character> text;

    public TextStorage(BoxDimensions dimensions) {
        this.dimensions = dimensions;
        this.text = new ArrayList<>(dimensions.size());
        this.text.addAll(Collections.nCopies(dimensions.size(), '\0'));
    }

    public void setText(String text, Point position, BoxDimensions dimensions) {
        final Point endPosition = new Point(position.x() + dimensions.width(), position.y() + dimensions.height());
        if (text.length() < dimensions.size()) {
            text = text + "\0".repeat(dimensions.size() - text.length());
        }
        setText(text, position, endPosition);
    }

    private void setText(String text, Point startPosition, Point endPosition) {
        for (int i = startPosition.y(); i < endPosition.y(); ++i) {
            final int startIndex = indexOf(new Point(startPosition.x(), i));
            final int endIndex = indexOf(new Point(endPosition.x(), i));
            setText(text, startIndex, endIndex);
            text = text.substring(Math.min(dimensions.width(), text.length()));
        }
    }

    private void setText(String text, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            this.text.set(i, text.charAt(i - startIndex));
        }
    }

    private int indexOf(Point position) {
        return dimensions.width() * position.y() + position.x();
    }

    public String getText(Point position, BoxDimensions dimensions) {
        final Point endPosition = new Point(position.x() + dimensions.width(), position.y() + dimensions.height());
        return getText(position, endPosition).trim();
    }

    private String getText(Point startPosition, Point endPosition) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startPosition.y(); i < endPosition.y(); ++i) {
            final int startIndex = indexOf(new Point(startPosition.x(), i));
            final int endIndex = indexOf(new Point(endPosition.x(), i));
            builder.append(getText(startIndex, endIndex));
        }
        return builder.toString();
    }

    private String getText(int startIndex, int endIndex) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            builder.append(text.get(i));
        }
        return builder.toString();
    }
}

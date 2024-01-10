package io.nuvolo.juice.infrastructure.text;

public record BoxDimensions(int width, int height) {
    public BoxDimensions {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
    }

    int size() {
        return width * height;
    }
}

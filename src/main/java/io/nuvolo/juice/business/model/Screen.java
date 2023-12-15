package io.nuvolo.juice.business.model;

public interface Screen extends Container {
    ScreenName getName();
    void performAction(ActionName name);
}

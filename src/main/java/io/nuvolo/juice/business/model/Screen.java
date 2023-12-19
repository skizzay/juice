package io.nuvolo.juice.business.model;

public interface Screen extends Container {
    ScreenName getScreenName();
    void performAction(ActionName name);
}

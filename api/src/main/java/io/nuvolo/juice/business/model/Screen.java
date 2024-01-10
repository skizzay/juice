package io.nuvolo.juice.business.model;

import java.util.Map;

public interface Screen extends Container {
    ScreenName getScreenName();
    void performAction(UserInterface userInterface, ActionName name);

    void rememberState();
    Map<FieldName, String> getLastState();
}

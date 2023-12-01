package io.nuvolo.juice.business.model;

import java.util.Collection;

public interface Screen extends Container {
    ScreenName getName();
    void navigateTo(ScreenName name);
    void performAction(ActionName name);
    Collection<ScreenName> getDirectlyNavigableScreens();
}

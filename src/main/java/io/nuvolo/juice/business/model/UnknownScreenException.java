package io.nuvolo.juice.business.model;

public class UnknownScreenException extends RuntimeException {
    private final ScreenName screenName;

    public UnknownScreenException(ScreenName screenName) {
        this.screenName = screenName;
    }

    public UnknownScreenException(String message, ScreenName screenName) {
        super(message);
        this.screenName = screenName;
    }

    public UnknownScreenException(String message, Throwable cause, ScreenName screenName) {
        super(message, cause);
        this.screenName = screenName;
    }

    public UnknownScreenException(Throwable cause, ScreenName screenName) {
        super(cause);
        this.screenName = screenName;
    }

    public UnknownScreenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ScreenName screenName) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.screenName = screenName;
    }

    public ScreenName getScreenName() {
        return screenName;
    }
}

package chris.constant;

public class Global {

    public static boolean isDev() {
        return true;
    }

    public static final boolean ASSISTANT_DEBUG = isDev();

    public static final String HTTP_USER_AGENT = "CHRIS/1.0.0";

    public static final String SPLIT=";";
}

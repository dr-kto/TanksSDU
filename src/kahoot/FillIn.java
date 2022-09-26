package kahoot;

public class FillIn extends Question {
    private String typedAns;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getTypedAns() {
        return typedAns;
    }

    public void setTypedAns(String typedAns) {
        this.typedAns = typedAns;
    }

    public String toString() {
        return getDescription().replace("{blank}", "____________");
    }
}


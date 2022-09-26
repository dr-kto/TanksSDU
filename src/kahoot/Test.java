package kahoot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Test extends Question {
    private ArrayList<String> labels = new ArrayList<>();
    private String[] options;
    private int selectedInd;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private int numOfOptions;

    public Test() {
        labels.add("A");
        labels.add("B");
        labels.add("C");
        selectedInd = -1;
    }

    public void setSelectedInd(int selectedInd) {
        this.selectedInd = selectedInd;
    }

    public int getSelectedInd() {
        return selectedInd;
    }

    public String getLabels(int optionIndex) {
        return labels.get(optionIndex);
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setOptions(String[] options) {
        this.options = options;

        ArrayList<String> temp = new ArrayList<>(Arrays.asList(options));


        Collections.shuffle(temp);

        for (int i = 0; i < options.length; i++) {
            options[i] = temp.get(i);
        }
    }

    public String getOptionAt(int optionIndex) {
        return options[optionIndex];
    }

    @Override
    public String toString() {
        return "____________________________________________________" + "\n"
                + getDescription() + "\n"
                + getLabels(0) + ") " + getOptionAt(0) + "\n"
                + getLabels(1) + ") " + getOptionAt(1) + "\n"
                + getLabels(2) + ") " + getOptionAt(2) + "\n"
                + getLabels(3) + ") " + getOptionAt(3) + "\n"
                + "---------------------------";
    }
}


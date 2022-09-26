package kahoot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Quiz {
    private static final List<Question> questions = new ArrayList<>();
    private static String name;

    public Quiz() {
    }

    public List<Question> loadFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner fileInput = new Scanner(file);
            if (file.length() == 0) {
                throw new InvalidQuizFormatException("No line found!");
            } else {
                while (fileInput.hasNextLine()) {
                    String description = fileInput.nextLine();
                    if (description.contains("{blank}")) {
                        FillIn fillIn = new FillIn();
                        fillIn.setDescription(description);
                        fillIn.setAnswer(fileInput.nextLine());
                        questions.add(fillIn);
                    } else {
                        Test test = new Test();
                        test.setDescription(description);
                        String[] options = new String[4];
                        for (int i = 0; i < options.length; i++) {
                            options[i] = fileInput.nextLine();
                        }
                        test.setAnswer(options[0]);
                        test.setOptions(options);
                        questions.add(test);
                    }
                    fileInput.nextLine();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Such a file does not exist!");
            System.exit(1);
        } catch (InvalidQuizFormatException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void loadFromFile1(String name) throws InvalidQuizFormatException, StringIndexOutOfBoundsException {
        int score = 0;
        String[] optionsTemp = new String[3];
        String fileName = name + ".txt";
        Scanner scan = new Scanner(System.in);
        try {
            File file = new File(fileName);
            Scanner fileScan = new Scanner(file);
            if (file.length() == 0) {
                throw new InvalidQuizFormatException("No line found!");
            } else {
                while (fileScan.hasNextLine()) {
                    String line1 = fileScan.nextLine();
                    if (line1.contains("{blank}")) {
                        FillIn fillIn = new FillIn();
                        fillIn.setDescription(line1);
                        fillIn.setAnswer(fileScan.nextLine());
                        questions.add(fillIn);
                        System.out.println(fillIn);
                        System.out.print("Type your answer: ");
                        while (true) {
                            String userInput = scan.nextLine();
                            if (Pattern.matches("[a-zA-Z]+", userInput)) {
                                if (userInput.equalsIgnoreCase(fillIn.getAnswer())) {
                                    System.out.println("Correct!");
                                    score++;
                                } else {
                                    System.out.println("Incorrect!");
                                }
                                break;
                            } else {
                                System.out.print("Type only A-Z or a-z: ");
                            }
                        }
                    } else {
                        Test test = new Test();
                        test.setDescription(line1);
                        for (int i = 0; i < optionsTemp.length; i++) {
                            optionsTemp[i] = fileScan.nextLine();
                        }
                        test.setAnswer(optionsTemp[0]);
                        test.setOptions(optionsTemp);
                        questions.add(test);
                        System.out.println(test);
                        System.out.print("Enter the correct answer: ");
                        while (true) {
                            try {
                                char userAnswer = scan.nextLine().charAt(0);
                                if (Character.isAlphabetic(userAnswer)) {
                                    if (userAnswer == 'A' || userAnswer == 'B' || userAnswer == 'C') {
                                        int optionIndex = userAnswer - 'A';
                                        if (test.getOptionAt(optionIndex).equals(test.getAnswer())) {
                                            System.out.println("Correct!");
                                            score++;
                                        } else {
                                            System.out.println("Incorrect!");
                                        }
                                        break;
                                    } else {
                                        System.out.print("Invalid choice! Try again (Ex: A, B, ...): ");
                                    }
                                } else {
                                    System.out.print("Invalid choice! Try again (Ex: A, B, ...): ");
                                }
                            } catch (StringIndexOutOfBoundsException e) {
                                System.out.print("Type your answer: ");
                            }
                        }
                    }
                    if (fileScan.hasNextLine()) {
                        fileScan.nextLine();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Such a file does not exist!");
            System.exit(1);
        }
        System.out.println("____________________________________________________");
        System.out.println("Correct answers: " + score + "/" + questions.size() + " " + "(" + (float) ((score * 100) / questions.size()) + "%)");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Quiz.name = name.replace(".txt", "");
    }

    public void addQuestion() {
    }
}

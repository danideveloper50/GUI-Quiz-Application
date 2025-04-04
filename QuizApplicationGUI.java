import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

abstract class Question {
    private String questionText;

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public abstract boolean checkAnswer(String answer);

    public abstract void display(JPanel panel, ActionListener listener);
}

class MultipleChoiceQuestion extends Question {
    private String[] options;
    private int correctOption;

    public MultipleChoiceQuestion(String questionText, String[] options, int correctOption) {
        super(questionText);
        this.options = options;
        this.correctOption = correctOption;
    }

    @Override
    public boolean checkAnswer(String answer) {
        return Integer.parseInt(answer) == correctOption;
    }

    @Override
    public void display(JPanel panel, ActionListener listener) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel questionLabel = new JLabel(getQuestionText());
        panel.add(questionLabel);

        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            JRadioButton optionButton = new JRadioButton((i + 1) + ". " + options[i]);
            optionButton.setActionCommand(String.valueOf(i + 1));
            group.add(optionButton);
            panel.add(optionButton);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (group.getSelection() != null) {
                listener.actionPerformed(new ActionEvent(group.getSelection(), ActionEvent.ACTION_PERFORMED, group.getSelection().getActionCommand()));
            } else {
                JOptionPane.showMessageDialog(panel, "Please select an option.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(submitButton);
        panel.revalidate();
        panel.repaint();
    }
}

class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(String questionText, boolean correctAnswer) {
        super(questionText);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean checkAnswer(String answer) {
        return Boolean.parseBoolean(answer) == correctAnswer;
    }

    @Override
    public void display(JPanel panel, ActionListener listener) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel questionLabel = new JLabel(getQuestionText());
        panel.add(questionLabel);

        ButtonGroup group = new ButtonGroup();
        JRadioButton trueButton = new JRadioButton("True");
        trueButton.setActionCommand("true");
        JRadioButton falseButton = new JRadioButton("False");
        falseButton.setActionCommand("false");

        group.add(trueButton);
        group.add(falseButton);

        panel.add(trueButton);
        panel.add(falseButton);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (group.getSelection() != null) {
                listener.actionPerformed(new ActionEvent(group.getSelection(), ActionEvent.ACTION_PERFORMED, group.getSelection().getActionCommand()));
            } else {
                JOptionPane.showMessageDialog(panel, "Please select an option.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(submitButton);
        panel.revalidate();
        panel.repaint();
    }
}

public class QuizApplicationGUI {
    private ArrayList<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    public QuizApplicationGUI() {
        questions = new ArrayList<>();
        questions.add(new MultipleChoiceQuestion("What is the capital of France?",
                new String[]{"Berlin", "Paris", "Rome", "Madrid"}, 2));
        questions.add(new TrueFalseQuestion("The Earth is flat.", false));
        questions.add(new MultipleChoiceQuestion("Which planet is known as the Red Planet?",
                new String[]{"Earth", "Mars", "Jupiter", "Venus"}, 2));
        questions.add(new TrueFalseQuestion("The Great Wall of China is visible from space.", false));
        questions.add(new MultipleChoiceQuestion("What is the largest mammal in the world?",
                new String[]{"Elephant", "Blue Whale", "Giraffe", "Shark"}, 2));
        questions.add(new TrueFalseQuestion("Light travels faster than sound.", true));
        questions.add(new MultipleChoiceQuestion("What is the chemical symbol for water?",
                new String[]{"H2O", "O2", "CO2", "HO"}, 1));
        questions.add(new TrueFalseQuestion("Mount Everest is the tallest mountain in the world.", true));
        questions.add(new MultipleChoiceQuestion("Who wrote 'Romeo and Juliet'?",
                new String[]{"Charles Dickens", "William Shakespeare", "J.K. Rowling", "Mark Twain"}, 2));
        questions.add(new TrueFalseQuestion("The Pacific Ocean is larger than the Atlantic Ocean.", true));

        JFrame frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        frame.add(panel);

        displayQuestion(panel);

        frame.setVisible(true);
    }

    private void displayQuestion(JPanel panel) {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            question.display(panel, e -> handleAnswer(e.getActionCommand(), panel));
        } else {
            showResults(panel);
        }
    }

    private void handleAnswer(String answer, JPanel panel) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        if (currentQuestion.checkAnswer(answer)) {
            score++;
            JOptionPane.showMessageDialog(panel, "Correct!", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel, "Incorrect!", "Result", JOptionPane.INFORMATION_MESSAGE);
        }
        currentQuestionIndex++;
        displayQuestion(panel);
    }

    private void showResults(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new GridLayout(2, 1));

        JLabel resultLabel = new JLabel("Quiz Completed! Your score: " + score + "/" + questions.size(), JLabel.CENTER);
        panel.add(resultLabel);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> System.exit(0));
        panel.add(closeButton);

        panel.revalidate();
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizApplicationGUI::new);
    }
}

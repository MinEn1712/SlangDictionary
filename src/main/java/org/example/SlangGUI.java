package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//This class is used to create pop up frame for each function.
class PopUpFrame extends JFrame implements ActionListener{
    Popup popup;
    String input;
    List<String> output = new ArrayList<>();
    final String[] frameTitles = {"Search by word", "Search by definition", "Add a slang", "Edit a slang", "Delete a slang",
            "Random slang generator", "History", "Reset", "Slang quiz", "Definition quiz"};
    public static JButton button = new JButton();
    public static JTextArea outputArea = new JTextArea(10, 20);
    PopUpFrame(int userChoice, SlangDictionary slangDict){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel inputPanel = new JPanel();
        JScrollPane scroller = new JScrollPane(outputArea);
        PopupFactory popupFactory = new PopupFactory();

        popup = popupFactory.getPopup(frame, panel, 180, 100);

        frame.setTitle(frameTitles[userChoice]);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        switch (userChoice){
            case 0, 1:{
                JTextField inputField = new JTextField(20);

                frame.setSize(300, 200);

                button = new JButton("Search");
                button.addActionListener(this);
                button.setActionCommand("Search");

                outputArea.setEditable(false);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                panel.add(scroller);
                inputPanel.add(inputField);
                inputPanel.add(button);
                panel.add(inputPanel);

                PopUpFrame.button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        input = inputField.getText();
                        inputField.setText("");
                        outputArea.setText("");

                        output = userChoice == 0 ? slangDict.searchByWord(input.toUpperCase(), 1) : slangDict.searchByDefinition(input, 1);

                        if(output == null || output.size() == 0){
                            outputArea.append("Not found");
                        }
                        else{
                            for (String str : output){
                                outputArea.append(str);
                                outputArea.append("\n");
                            }
                        }
                    }
                });

                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.setVisible(true);
                break;
            }
            case 2, 3, 4:{
                frame.setSize(400, 70);
                inputPanel.setLayout(new FlowLayout());

                JTextField inputField = new JTextField(20);
                JLabel labelInputWord = new JLabel("Enter a word");

                button = new JButton("OK");
                button.addActionListener(this);
                button.setActionCommand("OK");

                inputPanel.add(labelInputWord);
                inputPanel.add(inputField);
                inputPanel.add(button);

                PopUpFrame.button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        input = inputField.getText();
                        inputField.setText("");
                        output = slangDict.searchByWord(input.toUpperCase(), 0);

                        if(input.isBlank()){
                            JOptionPane.showMessageDialog(frame,
                                    "Please enter a word!",
                                    frameTitles[userChoice],
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else if(output == null || output.size() == 0){
                            if(userChoice == 2){
                                PopUpFrame defPopUp = new PopUpFrame(userChoice, 0, input, slangDict, -1);
                                frame.setVisible(false);
                            }
                            else{
                                JOptionPane.showMessageDialog(frame,
                                        "Word not found!",
                                        frameTitles[userChoice],
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else{
                            if(userChoice == 2){
                                String[] options = { "Overwrite", "Duplicate" };
                                int option = JOptionPane.showOptionDialog(frame, "Word already exists. Do you want to overwrite or duplicate the word?", "Add a slang",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                                if(option == 0){
                                    PopUpFrame defPopUp = new PopUpFrame(userChoice, 1, input, slangDict, -1);
                                    frame.setVisible(false);
                                }
                                else if (option == 1){
                                    PopUpFrame defPopUp = new PopUpFrame(userChoice, 2, input, slangDict, -1);
                                    frame.setVisible(false);
                                }
                            }
                            else if(userChoice == 3){
                                String[] options = new String[output.size()];
                                output.toArray(options);

                                int option = JOptionPane.showOptionDialog(frame, "Which definition do you want to edit?", "Edit a slang",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                                PopUpFrame defPopUp = new PopUpFrame(userChoice, 3, input, slangDict, option);
                                frame.setVisible(false);
                            }
                            else{
                                int option = JOptionPane.showConfirmDialog(frame, "Do you want to delete this word?", "Delete a slang", JOptionPane.YES_NO_OPTION);
                                if(option == 0){
                                    slangDict.deleteSlang(input);
                                    JOptionPane.showMessageDialog(frame,
                                            "Word is deleted.",
                                            frameTitles[userChoice],
                                            JOptionPane.INFORMATION_MESSAGE);
                                    frame.setVisible(false);
                                }
                                else if (option == 1){
                                    frame.setVisible(false);
                                }
                            }
                        }
                    }
                });

                panel.add(inputPanel);
                frame.add(panel);
                frame.setVisible(true);
                break;
            }
            case 5:{
                frame.setSize(300, 200);

                String random = "";
                String word = slangDict.randomSlang();
                List<String> defs = new ArrayList<>(slangDict.searchByWord(word, 0));

                random += word + ": ";
                if(defs.size() == 1){
                    random += defs.get(0) + ".";
                }
                else{
                    for(int i = 0; i < defs.size(); i++){
                        int index = i + 1;
                        if(i != defs.size() - 1){
                            random += index + ". " + defs.get(i) + "; ";
                        }
                        else{
                            random += index + ". " + defs.get(i) + ".";
                        }
                    }
                }

                JOptionPane.showMessageDialog(frame,
                        random,
                        "Random slang generator",
                        JOptionPane.PLAIN_MESSAGE);

                break;
            }
            case 6:{
                frame.setSize(300, 200);

                outputArea.setEditable(false);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                output = slangDict.getHistory();
                for (String str : output){
                    outputArea.append(str);
                    outputArea.append("\n");
                }

                panel.add(scroller);
                frame.add(panel);
                frame.setVisible(true);
                break;
            }
            case 7:{
                slangDict.resetSlangData();
                JOptionPane.showMessageDialog(frame,
                        "Slang dictionary has been reset.",
                        "Reset",
                        JOptionPane.INFORMATION_MESSAGE);

                break;
            }
            case 8, 9:{
                output = slangDict.slangQuiz();
                Random rand = new Random();
                String[] quizQuestion;
                String[] quizChoice;
                String[] questionType = {"What is the definition of ", "Which word has the meaning "};

                if(userChoice == 8){
                    quizQuestion = output.toArray(new String[0]);
                    quizChoice = new String[output.size()];

                    for(int i = 0; i < output.size(); i++){
                        List<String> defs = new ArrayList<>(slangDict.searchByWord(quizQuestion[i], 0));
                        if(defs.size() > 1){
                            int randomDef = rand.nextInt(defs.size());
                            quizChoice[i] = defs.get(randomDef);
                            continue;
                        }
                        quizChoice[i] = defs.get(0);
                    }
                }
                else{
                    quizQuestion = new String[output.size()];
                    quizChoice = output.toArray(new String[0]);

                    for(int i = 0; i < output.size(); i++){
                        List<String> defs = new ArrayList<>(slangDict.searchByWord(quizChoice[i], 0));
                        if(defs.size() > 1){
                            int randomDef = rand.nextInt(defs.size());
                            quizQuestion[i] = defs.get(randomDef);
                            continue;
                        }
                        quizQuestion[i] = defs.get(0);
                    }
                }

                int randomWord = rand.nextInt(quizQuestion.length);
                String question = questionType[userChoice - 8] + "\"" + quizQuestion[randomWord] + "\"?";

                int option = JOptionPane.showOptionDialog(frame, question, frameTitles[userChoice],
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, quizChoice, quizChoice[0]);

                if(option == randomWord){
                    JOptionPane.showMessageDialog(frame,
                            "Correct :)",
                            frameTitles[userChoice],
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(frame,
                            "Incorrect :( The answer is \"" + quizChoice[randomWord] + "\"",
                            frameTitles[userChoice],
                            JOptionPane.INFORMATION_MESSAGE);
                }

                break;
            }
            default:{
                System.out.println("Error");
                break;
            }
        }

        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                outputArea.setText("");
            }
        });
    }

    PopUpFrame(int userChoice, int modifyType, String word, SlangDictionary slangDict, int editDefIndex){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel inputPanel = new JPanel();
        JTextField inputField = new JTextField(20);
        PopupFactory popupFactory = new PopupFactory();

        popup = popupFactory.getPopup(frame, panel, 180, 100);

        frame.setTitle(frameTitles[userChoice]);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 70);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        inputPanel.setLayout(new FlowLayout());

        JLabel labelInputWord = new JLabel("Enter a definition");
        inputPanel.add(labelInputWord);

        button = new JButton("OK");
        button.addActionListener(this);
        button.setActionCommand("OK");

        inputPanel.add(inputField);
        inputPanel.add(button);

        PopUpFrame.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = inputField.getText();
                inputField.setText("");
                String[] modifyMessage = {"New slang is added!", "The slang is overwritten!", "Another definition is added!", "The slang is editted!"};

                if (modifyType == 0 || modifyType == 1){
                    List<String> def = new ArrayList<>();
                    def.add(input);
                    slangDict.addSlang(word, def, modifyType);
                }
                else if (modifyType == 2){
                    List<String> def = new ArrayList<>(slangDict.searchByWord(word.toUpperCase(), 0));
                    def.add(input);
                    slangDict.addSlang(word, def, modifyType);
                }
                else if (modifyType == 3){
                    List<String> def = new ArrayList<>(slangDict.searchByWord(word.toUpperCase(), 0));
                    slangDict.editSlang(word, input, def, editDefIndex);
                }

                JOptionPane.showMessageDialog(frame,
                        modifyMessage[modifyType],
                        frameTitles[userChoice],
                        JOptionPane.INFORMATION_MESSAGE);

                frame.setVisible(false);
            }
        });

        panel.add(inputPanel);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        popup.show();
    }
}

class ActionHandle implements ActionListener{
    public SlangDictionary slangDict = new SlangDictionary();
    public void actionPerformed(ActionEvent ae){
        String action = ae.getActionCommand();
        slangDict.getSlangData();
        PopUpFrame frame = new PopUpFrame(Integer.parseInt(action), slangDict);
    }
}
public class SlangGUI extends JPanel{
    public SlangGUI(){
        setLayout(new BorderLayout());
        String[] buttonNames = {"Search by word", "Search by definition", "Add a slang", "Edit a slang", "Delete a slang",
                                "Generate a random slang", "History", "Reset", "Slang quiz", "Definition quiz"};

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 600));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0,30)));

        JLabel label = new JLabel();
        label.setText("Slang Dictionary");
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setFont(new Font("Serif", Font.PLAIN, 30));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0,20)));

        for (int i = 0; i < 10; i++){
            JButton button = new JButton(buttonNames[i]);
            ActionHandle action = new ActionHandle();

            button.setActionCommand(Integer.toString(i));
            button.setMaximumSize(new Dimension(200,30));
            button.setAlignmentX(JButton.CENTER_ALIGNMENT);
            button.addActionListener(action);

            panel.add(button);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
        }

        add(panel, BorderLayout.CENTER);
    }

    public void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Slang Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new SlangGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

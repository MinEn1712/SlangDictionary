package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

class PopUpFrame extends JFrame implements ActionListener{
    Popup popup;
    String input;
    List<String> output = new ArrayList<>();
    public static JButton button = new JButton();
    public static JTextArea outputArea = new JTextArea(10, 20);
    PopUpFrame(int userChoice, SlangDictionary slangDict){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel inputPanel = new JPanel();
        JScrollPane scroller = new JScrollPane(outputArea);
        PopupFactory popupFactory = new PopupFactory();

        popup = popupFactory.getPopup(frame, panel, 180, 100);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        switch (userChoice){
            case 0, 1:{
                if(userChoice == 0){
                    frame.setTitle("Search by word");
                }
                else if(userChoice == 1){
                    frame.setTitle("Search by definition");
                }

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

                        if(userChoice == 0){
                            output = slangDict.searchByWord(input.toUpperCase(), 1);
                        }
                        else if(userChoice == 1){
                            output = slangDict.searchByDefinition(input, 1);
                        }

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
                frame.show();
                break;
            }
            case 2:{
                frame.setTitle("Add a slang");
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
                                    "Add a slang",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else if(output == null || output.size() == 0){
                            PopUpFrame defPopUp = new PopUpFrame(userChoice, 0, input, slangDict, -1);
                            frame.hide();
                        }
                        else{
                            String[] options = { "Overwrite", "Duplicate" };
                            int option = JOptionPane.showOptionDialog(frame, "Word already exists. Do you want to overwrite or duplicate the word?", "Add a slang",
                                    JOptionPane.WHEN_FOCUSED, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                            if(option == 0){
                                PopUpFrame defPopUp = new PopUpFrame(userChoice, 1, input, slangDict, -1);
                                frame.hide();
                            }
                            else if (option == 1){
                                PopUpFrame defPopUp = new PopUpFrame(userChoice, 2, input, slangDict, -1);
                                frame.hide();
                            }
                        }
                    }
                });

                panel.add(inputPanel);
                frame.add(panel);
                frame.show();
                break;
            }
            case 3:{
                frame.setTitle("Edit a slang");
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
                                    "Edit a slang",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else if(output == null || output.size() == 0){
                            JOptionPane.showMessageDialog(frame,
                                    "Word not found!",
                                    "Edit a slang",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            String[] options = new String[output.size()];
                            output.toArray(options);

                            int option = JOptionPane.showOptionDialog(frame, "Which definition do you want to edit?", "Edit a slang",
                                    JOptionPane.WHEN_FOCUSED, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                            PopUpFrame defPopUp = new PopUpFrame(userChoice, 3, input, slangDict, option);
                            frame.hide();
                        }
                    }
                });

                panel.add(inputPanel);
                frame.add(panel);
                frame.show();
                break;
            }
            case 4:{
                frame.setTitle("Delete a slang");
                frame.setSize(400, 70);

                JTextField inputField = new JTextField(20);
                JLabel labelInputWord = new JLabel("Enter a word");

                button = new JButton("OK");
                button.addActionListener(this);
                button.setActionCommand("OK");

                inputPanel.add(labelInputWord);
                inputPanel.add(inputField);
                inputPanel.add(button);
                panel.add(inputPanel);

                PopUpFrame.button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        input = inputField.getText();
                        inputField.setText("");
                        output = slangDict.searchByWord(input.toUpperCase(), 0);

                        if(input.isBlank()){
                            JOptionPane.showMessageDialog(frame,
                                    "Please enter a word!",
                                    "Edit a slang",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else if(output == null || output.size() == 0){
                            JOptionPane.showMessageDialog(frame,
                                    "Word not found!",
                                    "Delete a slang",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            int option = JOptionPane.showConfirmDialog(frame, "Do you want to delete this word?", "Delete a slang", JOptionPane.YES_NO_OPTION);
                            if(option == 0){
                                slangDict.deleteSlang(input);
                                JOptionPane.showMessageDialog(frame,
                                        "Word is deleted.",
                                        "Delete a slang",
                                        JOptionPane.INFORMATION_MESSAGE);
                                frame.hide();
                            }
                            else if (option == 1){
                                frame.hide();
                            }
                        }
                    }
                });

                frame.add(panel);
                frame.show();
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
                frame.setTitle("History");
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
                frame.show();
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
            case 8:{
                output = slangDict.slangQuiz();
                Random rand = new Random();
                String[] quizQuestion = output.toArray(new String[0]);
                String[] quizChoice = new String[output.size()];

                for(int i = 0; i < output.size(); i++){
                    List<String> defs = new ArrayList<>(slangDict.searchByWord(quizQuestion[i], 0));
                    if(defs.size() > 1){
                        int randomDef = rand.nextInt(defs.size());
                        quizChoice[i] = defs.get(randomDef);
                        continue;
                    }
                    quizChoice[i] = defs.get(0);
                }

                int randomWord = rand.nextInt(quizQuestion.length);
                String question = "What is the definition of " + quizQuestion[randomWord];

                int option = JOptionPane.showOptionDialog(frame, question, "Slang quiz",
                        JOptionPane.WHEN_FOCUSED, JOptionPane.QUESTION_MESSAGE, null, quizChoice, quizChoice[0]);

                if(option == randomWord){
                    JOptionPane.showMessageDialog(frame,
                            "Correct :)",
                            "Slang quiz",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(frame,
                            "Incorrect :(",
                            "Slang quiz",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                break;
            }
            default:{
                System.out.println("Error");
                break;
            }
        }

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

        if(userChoice == 2){
            frame.setTitle("Add a slang");
        }
        else if (userChoice == 3){
            frame.setTitle("Edit a slang");
        }
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
                String[] choiceTitle = {"Add a slang", "Edit a slang"};
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
                        choiceTitle[userChoice - 2],
                        JOptionPane.INFORMATION_MESSAGE);

                frame.hide();
            }
        });

        panel.add(inputPanel);
        frame.add(panel);
        frame.show();
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

        String buttonNames[] = {"Search by word", "Search by definition", "Add a slang", "Edit a slang", "Delete a slang",
                                "Generate a random slang", "History", "Reset", "Slang quiz", "Definition quiz"};
        for (int i = 0; i < 10; i++){
            JButton button = new JButton(buttonNames[i]);
            ActionHandle action = new ActionHandle();

            button.setActionCommand(Integer.toString(i));
            button.setMaximumSize(new Dimension(200,30));
            button.setAlignmentX(JButton.CENTER_ALIGNMENT);

            panel.add(button);
            panel.add(Box.createRigidArea(new Dimension(0,10)));

            button.addActionListener(action);
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
        frame.setVisible(true);
    }
}

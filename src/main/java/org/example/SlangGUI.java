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

    //This constructor is used to manipulate user choices in the main menu.
    //Where userChoice is what the user selects in the main menu, slangDict is a SlangDictionary object.
    PopUpFrame(int userChoice, SlangDictionary slangDict){
        //Components
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel inputPanel = new JPanel();
        JScrollPane scroller = new JScrollPane(outputArea);
        PopupFactory popupFactory = new PopupFactory();

        popup = popupFactory.getPopup(frame, panel, 180, 100);

        frame.setTitle(frameTitles[userChoice]);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //Manipulate user choices.
        switch (userChoice){
            //SEARCH CHOICES
                //0: Search by word.
                //1: Search by definition.
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

                //"Search" button action listener.
                PopUpFrame.button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Get the input from JTextField inputField.
                        input = inputField.getText();
                        inputField.setText("");
                        outputArea.setText("");

                        //Get the output from slangDict.searchByWord(...) or slangDict.searchByDefinition(...).
                        output = userChoice == 0 ? slangDict.searchByWord(input.toUpperCase(), 1) : slangDict.searchByDefinition(input, 1);

                        //If the output is empty, print "Not found" to outputArea, else print output to outputArea.
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
            //MODIFICATION CHOICES
                //2: Add a slang.
                //3: Edit a slang.
                //4: Delete a slang.
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

                //"OK" button action listener.
                PopUpFrame.button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Get the input from JTextField inputField.
                        input = inputField.getText();
                        inputField.setText("");
                        output = slangDict.searchByWord(input.toUpperCase(), 0);

                        //If the input is blank, show a message dialog.
                        if(input.isBlank()){
                            JOptionPane.showMessageDialog(frame,
                                    "Please enter a word!",
                                    frameTitles[userChoice],
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        //If the output is empty:
                            //If userChoice is 2 - "Add a slang", create a PopUpFrame object and call the constructor which is used for definition adding.
                            //Else, show a message dialog.
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
                            //If userChoice is 2 - "Add a slang", show an option dialog with 2 options "Overwrite" and "Duplicate".
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
                            //If userChoice is 3 - "Edit a slang", show an option dialog with several options which are the definitions of the word.
                            else if(userChoice == 3){
                                String[] options = new String[output.size()];
                                output.toArray(options);

                                int option = JOptionPane.showOptionDialog(frame, "Which definition do you want to edit?", "Edit a slang",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                                PopUpFrame defPopUp = new PopUpFrame(userChoice, 3, input, slangDict, option);
                                frame.setVisible(false);
                            }
                            //If userChoice is 4 - "Delete a slang", show a confirm dialog with 2 options "Yes" and "No".
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
            //5: Random slang generator.
            case 5:{
                frame.setSize(300, 200);

                //Get a random slang and get the definitions.
                String random = "";
                String word = slangDict.randomSlang();
                List<String> defs = new ArrayList<>(slangDict.searchByWord(word, 0));

                //Format the text for display purpose.
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

                //Show a message dialog with a random slang word and its definitions.
                JOptionPane.showMessageDialog(frame,
                        random,
                        "Random slang generator",
                        JOptionPane.PLAIN_MESSAGE);

                break;
            }
            //6: Show history.
            case 6:{
                frame.setSize(300, 200);

                outputArea.setEditable(false);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                //Get the history and print it to the outputArea.
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
            //7: Reset the dictionary.
            case 7:{
                //Reset the dictionary method.
                slangDict.resetSlangData();
                JOptionPane.showMessageDialog(frame,
                        "Slang dictionary has been reset.",
                        "Reset",
                        JOptionPane.INFORMATION_MESSAGE);

                break;
            }
            //SLANG QUIZ
                //8: Word quiz.
                //9: Definition quiz.
            case 8, 9:{
                //Get slang quizzes.
                output = slangDict.slangQuiz();
                Random rand = new Random();
                String[] quizQuestion;
                String[] quizChoice;
                String[] questionType = {"What is the definition of ", "Which word has the meaning "};

                if(userChoice == 8){
                    quizQuestion = output.toArray(new String[0]);
                    quizChoice = new String[output.size()];

                    //Get the definition of each word.
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

                    //Get the definition of each word.
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

                //Get a random slang/definition for the question.
                int randomWord = rand.nextInt(quizQuestion.length);
                String question = questionType[userChoice - 8] + "\"" + quizQuestion[randomWord] + "\"?";

                //Show option dialog with a question and 4 choices.
                int option = JOptionPane.showOptionDialog(frame, question, frameTitles[userChoice],
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, quizChoice, quizChoice[0]);

                //Check the answer and show message dialog.
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

    //This constructor is used to adding new definition for the modification choices (add, edit, delete).
    //userChoice is what the user selects in the main menu.
    //modifyType: 0 - Add a new slang, 1 - Slang overwrite, 2 - Slang duplicate (add another definition), 3 - Edit a slang.
    //word: the word which its definition is manipulated.
    //slangDict is a SlangDictionary object.
    //editDefIndex: the index of the definition in the definition list of the word (only for Edit a slang function).
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

        //"OK" button action listener.
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

//This class is used for manipulating user choices in the main menu.
class ActionHandle implements ActionListener{
    public SlangDictionary slangDict = new SlangDictionary();
    public void actionPerformed(ActionEvent ae){
        String action = ae.getActionCommand();
        slangDict.getSlangData();
        PopUpFrame frame = new PopUpFrame(Integer.parseInt(action), slangDict);
    }
}

//This class is to create the GUI for the main menu.
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

        //Create each button for each buttonNames.
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

    //Show GUI.
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

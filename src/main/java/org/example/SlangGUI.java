package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

class PopUpFrame extends JFrame implements ActionListener{
    Popup popup;
    String input;
    List<String> output = new ArrayList<>();
    public static JButton button = new JButton();
    public static JTextArea outputArea = new JTextArea(10, 20);
    PopUpFrame(int userChoice, SlangDictionary slangDict){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
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

                JPanel inputPanel = new JPanel();
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
                            output = slangDict.searchByWord(input.toUpperCase());
                        }
                        else if(userChoice == 1){
                            output = slangDict.searchByDefinition(input);
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
                break;
            }
            case 7:{
                slangDict.resetSlangData();
                frame.setSize(400, 70);

                JLabel label = new JLabel("Slang dictionary has been reset.");
                label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                label.setFont(new Font("Verdana", Font.BOLD, 16));
                panel.add(label);

                frame.add(panel);
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

        switch (Integer.parseInt(action)){
            case 0:{
                PopUpFrame frame = new PopUpFrame(0, slangDict);
                break;
            }
            case 1:{
                PopUpFrame frame = new PopUpFrame(1, slangDict);
                break;
            }
            case 2:{
                System.out.println("Add a slang");
                break;
            }
            case 3:{
                System.out.println("Edit a slang");
                break;
            }
            case 4:{
                System.out.println("Delete a slang");
                break;
            }
            case 5:{
                System.out.println("Generate a random slang");
                break;
            }
            case 6:{
                PopUpFrame frame = new PopUpFrame(6, slangDict);
                break;
            }
            case 7:{
                PopUpFrame frame = new PopUpFrame(7, slangDict);
                break;
            }
            case 8:{
                System.out.println("Slang quiz");
                break;
            }
            case 9:{
                System.out.println("Definition quiz");
                break;
            }
            default:{
                System.out.println("Error");
                break;
            }
        }
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

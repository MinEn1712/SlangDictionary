package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ActionHandle implements ActionListener{
    public void actionPerformed(ActionEvent ae){
        String action = ae.getActionCommand();

        switch (Integer.parseInt(action)){
            case 0:{
                System.out.println("Search by word");
                break;
            }
            case 1:{
                System.out.println("Search by definition");
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
                System.out.println("History");
                break;
            }
            case 7:{
                System.out.println("Reset");
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0,30)));

        JLabel label = new JLabel();
        label.setText("Slang Dictionary");
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setFont(new Font("Serif", Font.PLAIN, 30));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0,20)));

        String button_name[] = {"Search by word", "Search by definition", "Add a slang", "Edit a slang", "Delete a slang",
                                "Generate a random slang", "History", "Reset", "Slang quiz", "Definition quiz"};
        for (int i = 0; i < 10; i++){
            JButton button = new JButton(button_name[i]);
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

import logic.PageReplacementAlgorithm;
import logic.SetPageList;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


public class MyFrame extends JFrame implements ActionListener {
    JButton button;
    String line;

    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        button = new JButton("Select File");
        button.addActionListener(this);
        this.add(button);
        this.setSize(new Dimension(1280, 720));
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(".")); //sets current directory
            int response = fileChooser.showOpenDialog(null); //select file to open
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    line = br.readLine();
                    SetPageList setPageList = new SetPageList(line);
                    setPageList.setPageRW();
                    PageReplacementAlgorithm pageReplacementAlgorithm = new PageReplacementAlgorithm(6,10);
                    pageReplacementAlgorithm.notRecentlyUsed();
                } catch (IOException exception) {
                    System.out.println("Arquivo não foi encontrado!");
                }
            }
        }
    }
} 
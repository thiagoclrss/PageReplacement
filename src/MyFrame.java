import logic.PageReplacementAlgorithm;
import logic.SetPageList;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


public class MyFrame extends JFrame implements ActionListener {
    JButton button;
    String line;
    JTextField bitsTextField;
    JTextField framesTextField;
    JRadioButton fifoRadioButton;
    JRadioButton secondChanceRadioButton;
    JRadioButton nurRadioButton;
    JRadioButton mruRadioButton;

    int framesTextValue;
    int bitsTextValue;


    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());

        JLabel bitsLabel = new JLabel("Zerar Bit R:");
        bitsTextField = new JTextField(10);
        this.add(bitsLabel);
        this.add(bitsTextField);

        JLabel framesLabel = new JLabel("Quantidade de frames:");
        framesTextField = new JTextField(10);
        this.add(framesLabel);
        this.add(framesTextField);

        button = new JButton("Select File");
        button.addActionListener(this);
        this.add(button);

        // Adiciona os Radio Buttons com os rótulos
        fifoRadioButton = new JRadioButton("Fifo");
        secondChanceRadioButton = new JRadioButton("Second Chance");
        nurRadioButton = new JRadioButton("NUR");
        mruRadioButton = new JRadioButton("MRU");

        // Agrupa os Radio Buttons para garantir que apenas um deles seja selecionado por vez
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(fifoRadioButton);
        buttonGroup.add(secondChanceRadioButton);
        buttonGroup.add(nurRadioButton);
        buttonGroup.add(mruRadioButton);

        fifoRadioButton.addActionListener(this);
        secondChanceRadioButton.addActionListener(this);
        nurRadioButton.addActionListener(this);
        mruRadioButton.addActionListener(this);

        this.add(fifoRadioButton);
        this.add(secondChanceRadioButton);
        this.add(nurRadioButton);
        this.add(mruRadioButton);

        Log log = new Log();
        log.setBounds(288, 300, 690, 230);
        OutputStream outputStream = new OutputStream(log.getLogTextArea());
        System.setOut(new PrintStream(outputStream));
        add(log);

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
                    setPageList.setPage();
                } catch (IOException exception) {
                    System.out.println("Arquivo não foi encontrado!");
                }
            }
        }
        if (e.getSource() == fifoRadioButton) {
            framesTextValue = Integer.parseInt(framesTextField.getText());
            PageReplacementAlgorithm pageReplacementAlgorithm = new PageReplacementAlgorithm(framesTextValue);
            pageReplacementAlgorithm.fifo();
        }
        if (e.getSource() == secondChanceRadioButton) {
            framesTextValue = Integer.parseInt(framesTextField.getText());
            bitsTextValue = Integer.parseInt(bitsTextField.getText());
            PageReplacementAlgorithm pageReplacementAlgorithm = new PageReplacementAlgorithm(framesTextValue, bitsTextValue);
            pageReplacementAlgorithm.secondChance();
        }
        if (e.getSource() == nurRadioButton) {
            framesTextValue = Integer.parseInt(framesTextField.getText());
            bitsTextValue = Integer.parseInt(bitsTextField.getText());
            PageReplacementAlgorithm pageReplacementAlgorithm = new PageReplacementAlgorithm(framesTextValue, bitsTextValue);
            pageReplacementAlgorithm.notRecentlyUsed();
        }
        if (e.getSource() == mruRadioButton) {
            framesTextValue = Integer.parseInt(framesTextField.getText());
            PageReplacementAlgorithm pageReplacementAlgorithm = new PageReplacementAlgorithm(framesTextValue);
            pageReplacementAlgorithm.leastRecentlyUsed();
        }
    }
}
import javax.swing.*;
import java.awt.*;

public class Log extends JPanel {
    private JTextArea logTextArea;

    public Log() {
        setBackground(Color.BLACK);
        logTextArea = new JTextArea(13, 60);
        logTextArea.setEditable(false);
        logTextArea.setBackground(Color.BLACK);
        logTextArea.setForeground(Color.WHITE);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane);
    }

    public JTextArea getLogTextArea() {
        return logTextArea;
    }
}
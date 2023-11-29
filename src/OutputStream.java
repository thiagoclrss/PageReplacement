import java.io.IOException;
import javax.swing.JTextArea;

public class OutputStream extends java.io.OutputStream {
    private JTextArea textArea;

    public OutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
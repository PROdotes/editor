package editor;


import editor.main.EditorPanel;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("");
        window.setTitle("editor test");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(450 + 17 + 3 * 256, 3 * 256 + 40));
        //window.setResizable(false);

        EditorPanel editorPanel = new EditorPanel();
        window.add(editorPanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        editorPanel.startThread();
    }

}
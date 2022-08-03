package editor;


import javax.swing.*;

import editor.main.EditorPanel;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("");
        window.setTitle("editor test");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        EditorPanel editorPanel = new EditorPanel();
        window.add(editorPanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        editorPanel.startThread();
    }
}
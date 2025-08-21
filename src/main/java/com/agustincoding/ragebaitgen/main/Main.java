package com.agustincoding.ragebaitgen.main;

import com.agustincoding.ragebaitgen.controller.PostGeneratorController;
import com.agustincoding.ragebaitgen.view.PostGeneratorView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {

                PostGeneratorView view = new PostGeneratorView();


                new PostGeneratorController(view);

                view.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Error loading application: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}

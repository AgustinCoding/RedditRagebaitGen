package com.agustincoding.ragebaitgen.view;

import com.agustincoding.ragebaitgen.model.Post.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Enhanced GUI view for the Reddit Ragebait Post Generator
 * Provides a modern and user-friendly interface for generating ragebait posts
 */
public class PostGeneratorView extends JFrame {

    // Input components
    private JTextField subredditNameField;
    private JTextArea subredditDescriptionArea;
    private JTextArea limitationsArea;
    private JTextField topicField;

    // Generated content display
    private JTextArea generatedTitleArea;
    private JTextArea generatedContentArea;

    // Action buttons
    private JButton generatePostButton;
    private JButton clearFormButton;
    private JButton copyTitleButton;
    private JButton copyContentButton;

    // Status components
    private JLabel statusLabel;
    private JProgressBar progressBar;

    // Colors
    private final Color PRIMARY_COLOR = new Color(255, 69, 0); // Reddit orange
    private final Color SECONDARY_COLOR = new Color(30, 30, 30); // Dark gray
    private final Color ACCENT_COLOR = new Color(74, 119, 184); // Blue accent
    private final Color BACKGROUND_COLOR = new Color(240, 240, 242); // Light gray background
    private final Color CARD_COLOR = Color.WHITE;

    public PostGeneratorView() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDefaultValues();
        applyModernStyling();
    }

    private void initializeComponents() {
        // Input components
        subredditNameField = createStyledTextField(30);
        subredditDescriptionArea = createStyledTextArea(3, 50);
        limitationsArea = createStyledTextArea(3, 50);
        topicField = createStyledTextField(50);

        // Generated content display
        generatedTitleArea = createOutputTextArea(2, 50);
        generatedContentArea = createOutputTextArea(12, 50);

        // Action buttons
        generatePostButton = createPrimaryButton("Generate Ragebait Post", 200, 40);
        clearFormButton = createSecondaryButton("Clear All", 120, 35);
        copyTitleButton = createTertiaryButton("Copy Title", 100, 30);
        copyTitleButton.setEnabled(false);
        copyContentButton = createTertiaryButton("Copy Content", 120, 30);
        copyContentButton.setEnabled(false);

        // Status components
        statusLabel = new JLabel("Ready to generate posts");
        statusLabel.setForeground(new Color(0, 128, 0));
        progressBar = new JProgressBar();
        progressBar.setVisible(false);

        // Customize progress bar
        progressBar.setForeground(PRIMARY_COLOR);
        progressBar.setBackground(new Color(220, 220, 220));
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    super.paintComponent(g2);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        field.setOpaque(false);
        field.setBackground(new Color(248, 248, 248));
        field.setBorder(new RoundedBorder(15, new Color(200, 200, 200)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JTextArea createStyledTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    super.paintComponent(g2);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setOpaque(false);
        area.setBackground(new Color(248, 248, 248));
        area.setBorder(new RoundedBorder(15, new Color(200, 200, 200)));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return area;
    }

    private JTextArea createOutputTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setOpaque(false);
        area.setBackground(new Color(248, 248, 248));
        area.setBorder(new RoundedBorder(10, new Color(180, 180, 180)));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return area;
    }

    private JButton createPrimaryButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PRIMARY_COLOR.brighter());
                } else {
                    g2.setColor(PRIMARY_COLOR);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(width, height));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        return button;
    }

    private JButton createSecondaryButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(220, 220, 220));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(245, 245, 245));
                } else {
                    g2.setColor(Color.WHITE);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(SECONDARY_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(width, height));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(SECONDARY_COLOR);
        return button;
    }

    private JButton createTertiaryButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(230, 230, 230));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 240, 240));
                } else {
                    g2.setColor(new Color(248, 248, 248));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(ACCENT_COLOR);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(width, height));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(ACCENT_COLOR);
        return button;
    }

    private void setupLayout() {
        setTitle("Reddit Ragebait Post Generator - Modern Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Input section
        JPanel inputPanel = createCardPanel("Input Configuration");
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.insets = new Insets(8, 8, 8, 8);
        inputGbc.anchor = GridBagConstraints.WEST;

        // Subreddit name
        inputGbc.gridx = 0; inputGbc.gridy = 0;
        inputPanel.add(createLabel("Subreddit Name:"), inputGbc);
        inputGbc.gridx = 1; inputGbc.fill = GridBagConstraints.HORIZONTAL; inputGbc.weightx = 1.0;
        inputPanel.add(subredditNameField, inputGbc);

        // Subreddit description
        inputGbc.gridx = 0; inputGbc.gridy = 1; inputGbc.fill = GridBagConstraints.NONE; inputGbc.weightx = 0;
        inputPanel.add(createLabel("Subreddit Description:"), inputGbc);
        inputGbc.gridx = 1; inputGbc.fill = GridBagConstraints.BOTH; inputGbc.weightx = 1.0; inputGbc.weighty = 0.3;
        inputPanel.add(new JScrollPane(subredditDescriptionArea), inputGbc);

        // Subreddit limitations
        inputGbc.gridx = 0; inputGbc.gridy = 2; inputGbc.fill = GridBagConstraints.NONE; inputGbc.weightx = 0; inputGbc.weighty = 0;
        inputPanel.add(createLabel("Subreddit Limitations:"), inputGbc);
        inputGbc.gridx = 1; inputGbc.fill = GridBagConstraints.BOTH; inputGbc.weightx = 1.0; inputGbc.weighty = 0.3;
        inputPanel.add(new JScrollPane(limitationsArea), inputGbc);

        // Topic (optional)
        inputGbc.gridx = 0; inputGbc.gridy = 3; inputGbc.fill = GridBagConstraints.NONE; inputGbc.weightx = 0; inputGbc.weighty = 0;
        inputPanel.add(createLabel("Topic (Optional):"), inputGbc);
        inputGbc.gridx = 1; inputGbc.fill = GridBagConstraints.HORIZONTAL; inputGbc.weightx = 1.0;
        inputPanel.add(topicField, inputGbc);

        // Add input panel to main panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.4;
        mainPanel.add(inputPanel, gbc);

        // Generated content section
        JPanel outputPanel = createCardPanel("Generated Content");
        GridBagConstraints outputGbc = new GridBagConstraints();
        outputGbc.insets = new Insets(8, 8, 8, 8);
        outputGbc.anchor = GridBagConstraints.WEST;

        // Generated title
        outputGbc.gridx = 0; outputGbc.gridy = 0;
        outputPanel.add(createLabel("Generated Title:"), outputGbc);
        outputGbc.gridx = 1; outputGbc.anchor = GridBagConstraints.EAST;
        outputPanel.add(copyTitleButton, outputGbc);

        outputGbc.gridx = 0; outputGbc.gridy = 1; outputGbc.gridwidth = 2; outputGbc.fill = GridBagConstraints.BOTH;
        outputGbc.weightx = 1.0; outputGbc.weighty = 0.2; outputGbc.anchor = GridBagConstraints.WEST;
        outputPanel.add(new JScrollPane(generatedTitleArea), outputGbc);

        // Generated content
        outputGbc.gridx = 0; outputGbc.gridy = 2; outputGbc.gridwidth = 1; outputGbc.fill = GridBagConstraints.NONE;
        outputGbc.weightx = 0; outputGbc.weighty = 0;
        outputPanel.add(createLabel("Generated Content:"), outputGbc);
        outputGbc.gridx = 1; outputGbc.anchor = GridBagConstraints.EAST;
        outputPanel.add(copyContentButton, outputGbc);

        outputGbc.gridx = 0; outputGbc.gridy = 3; outputGbc.gridwidth = 2; outputGbc.fill = GridBagConstraints.BOTH;
        outputGbc.weightx = 1.0; outputGbc.weighty = 0.8; outputGbc.anchor = GridBagConstraints.WEST;
        outputPanel.add(new JScrollPane(generatedContentArea), outputGbc);

        // Add output panel to main panel
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.6;
        mainPanel.add(outputPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Bottom panel with buttons and status
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(750, 850));
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
        ));
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(SECONDARY_COLOR);
        return label;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(generatePostButton);
        buttonPanel.add(clearFormButton);

        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createHorizontalStrut(20));
        statusPanel.add(progressBar);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private void applyModernStyling() {
        // Set application-wide font
        Font segoeUI = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Button.font", segoeUI);
        UIManager.put("Label.font", segoeUI);
        UIManager.put("TextField.font", segoeUI);
        UIManager.put("TextArea.font", segoeUI);
        UIManager.put("TitledBorder.font", new Font("Segoe UI", Font.BOLD, 14));
    }

    private void setupEventHandlers() {
        // Button handlers (will be implemented in controller)
        generatePostButton.addActionListener(e -> onGeneratePost());
        clearFormButton.addActionListener(e -> onClearForm());
        copyTitleButton.addActionListener(e -> onCopyTitle());
        copyContentButton.addActionListener(e -> onCopyContent());
    }

    private void setDefaultValues() {
        // Set some helpful examples as placeholders
        subredditNameField.setText("AmItheAsshole");
        subredditDescriptionArea.setText("A subreddit for people to ask if they were the asshole in a particular situation. Users share personal conflicts and ask the community to judge their actions.");
        limitationsArea.setText("Posts are limited to 3,000 characters. No posts about relationships, ending friendships, violence, food tampering, DUIs, or property damage.");
        topicField.setText("");

        statusLabel.setText("Enter subreddit information and click Generate");
    }

    // Event handler methods (to be implemented by controller)
    private void onGeneratePost() {
        // TODO: Implement in controller
        updateStatus("Generating post... Please wait", Color.BLUE);
        showProgress(true);
    }

    private void onClearForm() {
        clearAllFields();
        updateStatus("Form cleared", new Color(0, 128, 0));
    }

    private void onCopyTitle() {
        if (!generatedTitleArea.getText().trim().isEmpty()) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new java.awt.datatransfer.StringSelection(generatedTitleArea.getText()), null);
            updateStatus("Title copied to clipboard", new Color(0, 128, 0));
        }
    }

    private void onCopyContent() {
        if (!generatedContentArea.getText().trim().isEmpty()) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new java.awt.datatransfer.StringSelection(generatedContentArea.getText()), null);
            updateStatus("Content copied to clipboard", new Color(0, 128, 0));
        }
    }

    private void clearAllFields() {
        subredditNameField.setText("");
        subredditDescriptionArea.setText("");
        limitationsArea.setText("");
        topicField.setText("");
        generatedTitleArea.setText("");
        generatedContentArea.setText("");

        copyTitleButton.setEnabled(false);
        copyContentButton.setEnabled(false);

        setDefaultValues();
    }

    // Utility methods for controller interaction
    public void updateStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public void showProgress(boolean show) {
        progressBar.setVisible(show);
        generatePostButton.setEnabled(!show);
        if (show) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
        }
    }

    public void setGeneratedContent(String title, String content) {
        generatedTitleArea.setText(title);
        generatedContentArea.setText(content);
        copyTitleButton.setEnabled(true);
        copyContentButton.setEnabled(true);
        showProgress(false);
        updateStatus("Post generated successfully!", new Color(0, 128, 0));
    }

    public void showError(String errorMessage) {
        updateStatus("Error: " + errorMessage, Color.RED);
        showProgress(false);
    }

    // Getters for controller access
    public String getSubredditName() {
        return subredditNameField.getText().trim();
    }

    public String getSubredditDescription() {
        return subredditDescriptionArea.getText().trim();
    }

    public String getLimitations() {
        return limitationsArea.getText().trim();
    }

    public String getTopic() {
        return topicField.getText().trim();
    }

    public String getGeneratedTitle() {
        return generatedTitleArea.getText();
    }

    public String getGeneratedContent() {
        return generatedContentArea.getText();
    }

    // Public hooks for controller
    public void addGeneratePostListener(ActionListener listener) {
        generatePostButton.addActionListener(listener);
    }

    public void addClearListener(ActionListener listener) {
        clearFormButton.addActionListener(listener);
    }

    // Custom border class for rounded corners
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = radius + 1;
            insets.top = insets.bottom = radius + 1;
            return insets;
        }
    }
}
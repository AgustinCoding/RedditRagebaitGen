package com.agustincoding.ragebaitgen.controller;

import com.agustincoding.ragebaitgen.gemini.GeminiService;
import com.agustincoding.ragebaitgen.model.Post;
import com.agustincoding.ragebaitgen.view.PostGeneratorView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDK 21 Compatible Controller for the PostGeneratorView
 * Handles ragebait post generation using Gemini AI with subreddit limitations support
 */
public class PostGeneratorController {

    private final PostGeneratorView view;
    private Post currentPost;

    // File management constants
    private static final String OUTPUT_DIRECTORY = "generated_posts";
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public PostGeneratorController(PostGeneratorView view) {
        this.view = view;
        initializeController();
        createOutputDirectory();
    }

    private void initializeController() {
        setupEventHandlers();
        view.updateStatus("Controller initialized - Ready to generate posts", Color.GREEN);
    }

    private void setupEventHandlers() {
        // Wire view buttons to controller methods using method references (JDK 21 style)
        view.addGeneratePostListener(e -> handleGeneratePost());
        view.addClearListener(e -> handleClearForm());
    }

    /**
     * Handles the Generate Post button action using SwingWorker for async processing
     */
    public void handleGeneratePost() {
        // Validate input first
        if (!validateInput()) {
            return;
        }

        SwingWorker<Post, Void> worker = new SwingWorker<>() {
            @Override
            protected Post doInBackground() throws Exception {
                view.updateStatus("Generating ragebait post...", Color.BLUE);
                view.showProgress(true);

                return generateRagebaitPost();
            }

            @Override
            protected void done() {
                view.showProgress(false);
                try {
                    currentPost = get();
                    if (currentPost != null) {
                        // Display the generated content in the view
                        view.setGeneratedContent(currentPost.getTitle(), currentPost.getContent());

                        // Save to file
                        String filename = savePostToFile(currentPost);
                        view.updateStatus("Post generated successfully! Saved to: " + filename, Color.GREEN);
                    } else {
                        view.showError("Failed to generate post. Please try again.");
                    }
                } catch (Exception e) {
                    view.showError("Error generating post: " + e.getMessage());
                    System.err.println("Error in post generation: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    /**
     * Handles the Clear Form button action
     */
    public void handleClearForm() {
        currentPost = null;
        view.updateStatus("Form cleared", Color.GREEN);
    }

    /**
     * Validates user input before generation - JDK 21 enhanced validation
     */
    private boolean validateInput() {
        String subredditName = view.getSubredditName();
        String subredditDescription = view.getSubredditDescription();

        if (subredditName.isBlank()) {
            view.showError("Please enter a subreddit name");
            return false;
        }

        if (subredditDescription.isBlank()) {
            view.showError("Please enter a subreddit description");
            return false;
        }

        // Additional validation for subreddit name format
        if (!subredditName.matches("^[a-zA-Z0-9_]+$")) {
            view.showError("Subreddit name can only contain letters, numbers, and underscores");
            return false;
        }

        return true;
    }

    /**
     * Generates a ragebait post using Gemini AI with enhanced error handling
     */
    private Post generateRagebaitPost() {
        try {
            String prompt = buildRagebaitPrompt();
            String aiResponse = GeminiService.getResponseTo(prompt);

            if (aiResponse == null || aiResponse.isBlank()) {
                throw new RuntimeException("Empty response from AI service");
            }

            return parseAIResponse(aiResponse);

        } catch (Exception e) {
            System.err.println("Error in generateRagebaitPost: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the enhanced prompt for Gemini AI including limitations - JDK 21 text blocks
     */
    private String buildRagebaitPrompt() {
        String subredditName = view.getSubredditName();
        String subredditDescription = view.getSubredditDescription();
        String limitations = view.getLimitations();
        String topic = view.getTopic();

        // Using JDK 21 enhanced text blocks and string formatting
        String basePrompt = """
            🔥 **REDDIT RAGEBAIT POST GENERATOR** 🔥
            
            **YOUR MISSION:** Generate a highly engaging ragebait post for Reddit that will 
            maximize emotional response and drive comments and engagement.
            
            **TARGET SUBREDDIT:** r/%s
            **SUBREDDIT CONTEXT:** %s
            """.formatted(subredditName, subredditDescription);

        StringBuilder prompt = new StringBuilder(basePrompt);

        // Add limitations if provided
        if (!limitations.isBlank()) {
            prompt.append("\n**CRITICAL SUBREDDIT RESTRICTIONS:** ").append(limitations);
            prompt.append("\n**COMPLIANCE REQUIREMENT:** The generated post MUST strictly follow these limitations to avoid being removed or banned.\n");
        }

        // Add topic if provided
        if (!topic.isBlank()) {
            prompt.append("\n**SPECIFIC TOPIC TO FOCUS ON:** ").append(topic).append("\n");
        }

        // Enhanced psychological triggers section
        String psychologicalSection = """
            
            **PSYCHOLOGICAL TRIGGERS TO ACTIVATE:**
            🎯 MORAL OUTRAGE - Present a clear injustice that violates basic fairness
            🎯 SOCIAL PROOF - Make readers feel they're on the 'right' side
            🎯 SUPERIORITY COMPLEX - Include obviously wrong behavior for readers to judge
            🎯 PERSONAL INVESTMENT - Use highly relatable situations that readers can identify with
            🎯 CONFIRMATION BIAS - Align with common frustrations and widely-held beliefs
            🎯 MISSING CONTEXT - Leave strategic gaps for reader assumptions and speculation
            
            **REQUIREMENTS:**
            • Create a compelling, clickable title (under 300 characters)
            • Write engaging content (300-800 words) that feels completely authentic
            • Include realistic details, specific dialogue, and believable scenarios
            • Build to a moral dilemma that seems obvious but includes doubt
            • Use paragraph breaks for easy mobile reading
            • End with a question that invites judgment, opinions, and discussion
            
            **WRITING STYLE:**
            • Casual, authentic Reddit voice with natural imperfections
            • First-person perspective with emotional investment
            • Include specific details that make the story believable
            • Use quotation marks for realistic dialogue
            • Show, don't tell - let readers draw their own conclusions
            • Create multiple comment-worthy discussion points
            """;

        prompt.append(psychologicalSection);

        // Enhanced output format requirements
        String formatSection = """
            
            **OUTPUT FORMAT (VERY IMPORTANT):**
            Please format your response EXACTLY like this:
            
            TITLE: [Your engaging title here]
            
            CONTENT:
            [Your complete post content here]
            
            **QUALITY CHECKLIST:**
            ✅ Follows all subreddit restrictions
            ✅ Contains clear moral conflict
            ✅ Includes specific, believable details
            ✅ Ends with engagement-driving question
            ✅ Uses authentic Reddit voice
            ✅ Creates multiple discussion points
            
            **GENERATE THE PERFECT RAGEBAIT POST NOW!**
            """;

        prompt.append(formatSection);

        return prompt.toString();
    }

    /**
     * Parses the AI response into a Post object with enhanced error handling
     */
    private Post parseAIResponse(String aiResponse) {
        try {
            // Extract title using improved regex
            String title = extractField(aiResponse, "TITLE:");
            if (title == null || title.isBlank()) {
                // Enhanced fallback: look for lines that could be titles
                title = findPotentialTitle(aiResponse);
            }

            // Extract content with better parsing
            String content = extractContent(aiResponse);
            if (content == null || content.isBlank()) {
                // Fallback: clean the response and use it as content
                content = cleanResponseAsContent(aiResponse);
            }

            // Create Post object using builder pattern
            Post.Builder builder = new Post.Builder()
                    .title(title != null ? title.trim() : "Generated Ragebait Post")
                    .content(content.trim())
                    .subreddit(view.getSubredditName())
                    .subredditDescription(view.getSubredditDescription())
                    .intensity(Post.PostIntensity.MODERATE); // Default intensity

            // Add optional fields if present
            if (!view.getLimitations().isBlank()) {
                builder.limitations(view.getLimitations());
            }

            if (!view.getTopic().isBlank()) {
                builder.topic(view.getTopic());
            }

            return builder.build();

        } catch (Exception e) {
            System.err.println("Error parsing AI response: " + e.getMessage());
            e.printStackTrace();
            // Create fallback post with error handling
            return createFallbackPost(aiResponse);
        }
    }

    /**
     * Enhanced field extraction with better regex patterns
     */
    private String extractField(String response, String fieldName) {
        // Try multiple patterns for more reliable extraction
        Pattern[] patterns = {
                Pattern.compile(fieldName + "\\s*([^\n]+)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("\\*\\*" + fieldName + "\\*\\*\\s*([^\n]+)", Pattern.CASE_INSENSITIVE),
                Pattern.compile(fieldName.replace(":", "") + ":\\s*([^\n]+)", Pattern.CASE_INSENSITIVE)
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }
        return null;
    }

    /**
     * Enhanced content extraction with multiple fallback patterns
     */
    private String extractContent(String response) {
        // Primary pattern: everything after "CONTENT:" until end or next section
        Pattern[] patterns = {
                Pattern.compile("CONTENT:\\s*\n(.*?)(?:\n\n\\*\\*|$)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
                Pattern.compile("CONTENT:\\s*\n(.*)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
                Pattern.compile("\\*\\*CONTENT:\\*\\*\\s*\n(.*?)(?:\n\n\\*\\*|$)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                String content = matcher.group(1).trim();
                if (!content.isBlank()) {
                    return content;
                }
            }
        }

        return null;
    }

    /**
     * JDK 21 enhanced method to find potential titles in unstructured text
     */
    private String findPotentialTitle(String response) {
        String[] lines = response.split("\n");

        // Look for lines that could be titles (reasonable length, not too short)
        for (String line : lines) {
            String cleaned = line.trim();
            if (cleaned.length() > 10 && cleaned.length() < 300 &&
                    !cleaned.toLowerCase().contains("content") &&
                    !cleaned.startsWith("**") &&
                    !cleaned.startsWith("🔥")) {
                return cleaned;
            }
        }

        return "Generated Ragebait Post";
    }

    /**
     * Clean the AI response to use as content when parsing fails
     */
    private String cleanResponseAsContent(String response) {
        return response
                .replaceAll("\\*\\*[^*]+\\*\\*", "") // Remove markdown headers
                .replaceAll("🔥|✅", "") // Remove emojis
                .replaceAll("TITLE:.*?\n", "") // Remove title lines
                .replaceAll("CONTENT:\\s*\n", "") // Remove content headers
                .trim();
    }

    /**
     * Creates a fallback post if parsing fails completely
     */
    private Post createFallbackPost(String aiResponse) {
        return new Post.Builder()
                .title("Generated Ragebait Post - " + view.getSubredditName())
                .content(cleanResponseAsContent(aiResponse))
                .subreddit(view.getSubredditName())
                .subredditDescription(view.getSubredditDescription())
                .limitations(view.getLimitations().isBlank() ? null : view.getLimitations())
                .intensity(Post.PostIntensity.MODERATE)
                .build();
    }

    /**
     * Saves the post to a file with enhanced formatting - JDK 21 improvements
     */
    private String savePostToFile(Post post) throws IOException {
        String timestamp = LocalDateTime.now().format(FILE_TIMESTAMP);
        String filename = "ragebait_r_%s_%s.txt".formatted(post.getSubreddit(), timestamp);

        Path outputPath = Path.of(OUTPUT_DIRECTORY, filename);
        String fileContent = formatPostForFile(post);

        Files.writeString(outputPath, fileContent, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return filename;
    }

    /**
     * Enhanced file formatting with JDK 21 text blocks and string formatting
     */
    private String formatPostForFile(Post post) {
        String header = """
            ================================================================================
            REDDIT RAGEBAIT POST GENERATED
            Generated at: %s
            ================================================================================
            """.formatted(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        StringBuilder content = new StringBuilder(header);

        // Post Information
        content.append("\nSUBREDDIT: r/").append(post.getSubreddit()).append("\n");
        content.append("SUBREDDIT DESCRIPTION: ").append(post.getSubredditDescription()).append("\n");

        if (post.hasLimitations()) {
            content.append("SUBREDDIT LIMITATIONS: ").append(post.getLimitations()).append("\n");
        }

        if (post.hasTopicSpecified()) {
            content.append("TOPIC FOCUS: ").append(post.getTopic()).append("\n");
        }

        content.append("INTENSITY LEVEL: ").append(post.getIntensity().name())
                .append(" - ").append(post.getIntensity().getDescription()).append("\n\n");

        // Post Content with enhanced formatting
        String postContent = """
            TITLE:
            --------------------------------------------------
            %s
            --------------------------------------------------
            
            CONTENT:
            --------------------------------------------------
            %s
            --------------------------------------------------
            """.formatted(post.getTitle(), post.getContent());

        content.append(postContent);

        // Metadata section
        String metadata = """
            
            METADATA:
            ==============================
            Title Length: %d characters
            Content Length: %d characters
            Word Count: %d words
            Valid for Submission: %s
            """.formatted(
                post.getTitle() != null ? post.getTitle().length() : 0,
                post.getContentLength(),
                post.getWordCount(),
                post.isValidForSubmission() ? "YES" : "NO"
        );

        content.append(metadata);

        // Footer
        String footer = """
            
            
            ================================================================================
            END OF GENERATED POST
            ================================================================================
            """;

        content.append(footer);

        return content.toString();
    }

    /**
     * Ensure the output directory exists; if it cannot be created, notify the view
     */
    private void createOutputDirectory() {
        try {
            Path dir = Path.of(OUTPUT_DIRECTORY);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            System.err.println("Failed to create output directory: " + e.getMessage());
            view.showError("Could not create output directory: " + e.getMessage());
        }
    }
}

package com.agustincoding.ragebaitgen.gemini;

import com.agustincoding.ragebaitgen.model.Post;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PostGenerator {

    private static final Random random = new Random();

    /**
     * Generates a complete Post object with all metadata and content
     * @param subredditName The name of the subreddit
     * @param subredditDescription Description of the subreddit
     * @param limitations Subreddit rules and restrictions
     * @param topic Optional topic for AI generation
     * @return A complete Post object ready for submission
     */
    public static Post getPost(String subredditName, String subredditDescription,
                               String limitations, String topic) {

        // Generate the raw content
        String prompt = buildRagepostPrompt(subredditName, subredditDescription, limitations, topic);
        String rawResponse = GeminiService.getResponseTo(prompt);

        // Parse the response and build the Post object
        return parseResponseToPost(rawResponse, subredditName, subredditDescription, limitations, topic);
    }

    /**
     * Generates a random complete Post object with default values
     */
    public static Post getPost() {
        return getPost("AmItheAsshole",
                "A subreddit for people to ask if they were the asshole in a particular situation",
                "Posts are limited to 3,000 characters. No posts about relationships, ending friendships, violence, etc.",
                "");
    }

    private static String buildRagepostPrompt(String subredditName, String subredditDescription,
                                              String limitations, String topic) {
        StringBuilder sb = new StringBuilder();

        sb.append("**REDDIT RAGEBAIT POST GENERATOR**\n\n");
        sb.append("**YOUR MISSION:** Create a highly engaging ragebait post for Reddit\n\n");

        sb.append("**TARGET SUBREDDIT:** r/").append(subredditName).append("\n");
        sb.append("**SUBREDDIT DESCRIPTION:** ").append(subredditDescription).append("\n");

        if (limitations != null && !limitations.trim().isEmpty()) {
            sb.append("**SUBREDDIT RESTRICTIONS:** ").append(limitations).append("\n");
            sb.append("**IMPORTANT:** The post MUST follow these restrictions!\n");
        }

        if (topic != null && !topic.trim().isEmpty()) {
            sb.append("**TOPIC FOCUS:** ").append(topic).append("\n");
        }

        sb.append("\n**PSYCHOLOGICAL TRIGGERS TO ACTIVATE:**\n");
        sb.append("MORAL OUTRAGE - Present a clear injustice\n");
        sb.append("SOCIAL PROOF - Make readers feel part of the 'right' side\n");
        sb.append("SUPERIORITY COMPLEX - Include obviously wrong antagonist behavior\n");
        sb.append("PERSONAL INVESTMENT - Use relatable situations\n");
        sb.append("CONFIRMATION BIAS - Align with common frustrations\n\n");

        sb.append("**REQUIREMENTS:**\n");
        sb.append("Create a compelling title (under 300 characters)\n");
        sb.append("Write engaging content (300-800 words)\n");
        sb.append("Include realistic details and believable scenarios\n");
        sb.append("Build to a moral dilemma that seems obvious\n");
        sb.append("Use paragraph breaks for easy reading\n");
        sb.append("End with a question that invites discussion\n\n");

        sb.append("**OUTPUT FORMAT:**\n");
        sb.append("TITLE: [Your engaging title here]\n\n");
        sb.append("CONTENT:\n");
        sb.append("[Your complete post content here]\n\n");

        sb.append("**GENERATE THE POST NOW**");

        return sb.toString();
    }

    private static Post parseResponseToPost(String rawResponse, String subredditName,
                                            String subredditDescription, String limitations, String topic) {
        try {
            // Extract title
            String title = extractField(rawResponse, "TITLE:");
            if (title == null) {
                title = "Generated Ragebait Post";
            }

            // Extract content
            String content = extractContent(rawResponse);
            if (content == null) {
                content = rawResponse; // Fallback to entire response
            }

            // Build the Post object
            return new Post.Builder()
                    .title(title)
                    .content(content)
                    .subreddit(subredditName)
                    .subredditDescription(subredditDescription)
                    .limitations(limitations)
                    .topic(topic)
                    .intensity(Post.PostIntensity.MODERATE) // Default intensity
                    .build();

        } catch (Exception e) {
            // Fallback: create a basic post if parsing fails
            return new Post.Builder()
                    .title("Generated Ragebait Post")
                    .content(rawResponse)
                    .subreddit(subredditName)
                    .subredditDescription(subredditDescription)
                    .limitations(limitations)
                    .topic(topic)
                    .intensity(Post.PostIntensity.MODERATE)
                    .build();
        }
    }

    private static String extractField(String response, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + "\\s*([^\n]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static String extractContent(String response) {
        Pattern pattern = Pattern.compile("CONTENT:\\s*\n(.*?)(?=$)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}
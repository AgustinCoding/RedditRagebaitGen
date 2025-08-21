package com.agustincoding.ragebaitgen.model;

import java.util.Objects;

/**
 * Simplified Reddit Post model for ragebait generation
 * Contains only the essential fields needed for post creation
 */
public class Post {

    // Essential post fields
    private String title;
    private String content;
    private String subreddit;
    private String subredditDescription;
    private String limitations; // Subreddit rules and restrictions
    private String topic; // Optional topic for AI generation

    // Generation metadata
    private PostIntensity intensity;
    private String generationPrompt; // The prompt used to generate this post

    // Enums for simplified configuration
    // Currently not usable on GUI but will when it's improved
    public enum PostIntensity {
        MILD("Subtle ragebait with light controversy"),
        MODERATE("Moderate controversy to drive engagement"),
        HIGH("Strong emotional triggers for maximum engagement");

        private final String description;

        PostIntensity(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // Constructors
    public Post() {
        this.intensity = PostIntensity.MODERATE; // Default to moderate
    }

    public Post(String title, String content, String subreddit) {
        this();
        this.title = title;
        this.content = content;
        this.subreddit = subreddit;
    }

    // Builder pattern for easier post creation
    public static class Builder {
        private Post post;

        public Builder() {
            this.post = new Post();
        }

        public Builder title(String title) {
            post.title = title;
            return this;
        }

        public Builder content(String content) {
            post.content = content;
            return this;
        }

        public Builder subreddit(String subreddit) {
            post.subreddit = subreddit;
            return this;
        }

        public Builder subredditDescription(String description) {
            post.subredditDescription = description;
            return this;
        }

        public Builder limitations(String limitations) {
            post.limitations = limitations;
            return this;
        }

        public Builder topic(String topic) {
            post.topic = topic;
            return this;
        }

        public Builder intensity(PostIntensity intensity) {
            post.intensity = intensity;
            return this;
        }

        public Builder generationPrompt(String prompt) {
            post.generationPrompt = prompt;
            return this;
        }

        public Post build() {
            // Basic validation
            if (post.title == null || post.title.trim().isEmpty()) {
                throw new IllegalStateException("Post title cannot be null or empty");
            }

            if (post.subreddit == null || post.subreddit.trim().isEmpty()) {
                throw new IllegalStateException("Subreddit cannot be null or empty");
            }

            if (post.content == null || post.content.trim().isEmpty()) {
                throw new IllegalStateException("Post content cannot be null or empty");
            }

            return post;
        }
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getSubredditDescription() {
        return subredditDescription;
    }

    public void setSubredditDescription(String subredditDescription) {
        this.subredditDescription = subredditDescription;
    }

    public String getLimitations() {
        return limitations;
    }

    public void setLimitations(String limitations) {
        this.limitations = limitations;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public PostIntensity getIntensity() {
        return intensity;
    }

    public void setIntensity(PostIntensity intensity) {
        this.intensity = intensity;
    }

    public String getGenerationPrompt() {
        return generationPrompt;
    }

    public void setGenerationPrompt(String generationPrompt) {
        this.generationPrompt = generationPrompt;
    }

    // Helper methods
    public int getContentLength() {
        return content != null ? content.length() : 0;
    }

    public int getWordCount() {
        return content != null ? content.split("\\s+").length : 0;
    }

    public boolean isValidForSubmission() {
        return title != null && !title.trim().isEmpty() &&
                content != null && !content.trim().isEmpty() &&
                subreddit != null && !subreddit.trim().isEmpty();
    }

    public boolean hasTopicSpecified() {
        return topic != null && !topic.trim().isEmpty();
    }

    public boolean hasLimitations() {
        return limitations != null && !limitations.trim().isEmpty();
    }

    // Format post for display
    public String getFormattedPost() {
        StringBuilder sb = new StringBuilder();
        sb.append("**Title:** ").append(title).append("\n\n");
        sb.append("**Subreddit:** r/").append(subreddit).append("\n\n");
        sb.append("**Content:**\n").append(content);

        if (hasTopicSpecified()) {
            sb.append("\n\n**Topic Focus:** ").append(topic);
        }

        if (hasLimitations()) {
            sb.append("\n\n**Limitations Applied:** ").append(limitations);
        }

        sb.append("\n\n**Intensity:** ").append(intensity.getDescription());

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Post{title='%s', subreddit='r/%s', intensity=%s, wordCount=%d}",
                title != null ? title.substring(0, Math.min(50, title.length())) + "..." : "null",
                subreddit,
                intensity,
                getWordCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(title, post.title) &&
                Objects.equals(content, post.content) &&
                Objects.equals(subreddit, post.subreddit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, subreddit);
    }
}
package org.example;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import okhttp3.*;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;

public class Main extends ListenerAdapter {
    // Please Set Environment Variables.
    // Need Help? take a look at the README
    private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN"); // Discord Bot Token
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY"); // OpenAI API Key
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private static final int MAX_HISTORY = 5; // Chatting Log Save
    private final Map<String, List<JsonObject>> conversationHistory = new HashMap<>();

    public static void main(String[] args) throws LoginException {
        JDABuilder.createDefault(BOT_TOKEN)
                .addEventListeners(new Main())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.listening(""))
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        String userId = event.getAuthor().getId();
        String userName = event.getAuthor().getAsMention(); // User Mention

        if (message.getAuthor().isBot()) return;

        if (content.startsWith("..")) return;

        if (message.getChannelId().equals("")) { // ChatGPT Channel
            event.getChannel().sendTyping().queue();
            new Thread(() -> {
                String response = getChatGPTResponse(userId, content);

                if (response == null || response.isEmpty()) {
                    event.getChannel().sendMessage(userName + " ⚠ ChatGPT Retry Please.").queue();
                    return;
                }

                if (response.length() > 2000) {
                    event.getChannel().sendMessage(userName + " ⚠ Too Long. please shorten it.").queue();
                    return;
                }

                event.getMessage().reply(response).queue();
            }).start();
        }
    }

    private String getChatGPTResponse(String userId, String query) {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                JsonObject json = new JsonObject();
                json.addProperty("model", "gpt-4o");
                json.addProperty("max_tokens", 2500);

                List<JsonObject> history = conversationHistory.getOrDefault(userId, new ArrayList<>());
                JsonArray messagesArray = new JsonArray();

                JsonObject systemMessage = new JsonObject();
                systemMessage.addProperty("role", "system");
                systemMessage.addProperty("content",
                        "you are friendly assistant. use many markdowns and emojis.");
                messagesArray.add(systemMessage);

                for (JsonObject pastMessage : history) {
                    messagesArray.add(pastMessage);
                }

                JsonObject userMessage = new JsonObject();
                userMessage.addProperty("role", "user");
                userMessage.addProperty("content", query);
                messagesArray.add(userMessage);
                json.add("messages", messagesArray);

                RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url("https://api.openai.com/v1/chat/completions")
                        .header("Authorization", "Bearer " + OPENAI_API_KEY)
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        return "⚠ ChatGPT Error (Code: " + response.code() + ")";
                    }

                    String responseBody = response.body().string();
                    System.out.println("📜 ChatGPT: " + responseBody);

                    JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonArray choices = responseJson.getAsJsonArray("choices");

                    if (choices.size() > 0) {
                        JsonObject firstChoice = choices.get(0).getAsJsonObject();
                        String responseText = firstChoice.getAsJsonObject("message").get("content").getAsString().trim();

                        String actualModel = responseJson.get("model").getAsString(); // Model information

                        // Chatting Log
                        history.add(userMessage);
                        JsonObject assistantMessage = new JsonObject();
                        assistantMessage.addProperty("role", "assistant");
                        assistantMessage.addProperty("content", responseText);
                        history.add(assistantMessage);

                        if (history.size() > MAX_HISTORY) {
                            history.remove(0);
                        }

                        conversationHistory.put(userId, history);
                        return responseText;
                    }
                    return "⚠ ChatGPT has can't understand it.";
                }
            } catch (IOException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    return "⚠ Request Failed: " + e.getMessage();
                }
            }
        }
        return "⚠ Error. But i don't know... Sorry.";
    }
}

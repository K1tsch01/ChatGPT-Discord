# ChatGPT Discord
A Java-powered ChatGPT bot for Discord. 🚀

## Requirements
- **Java 17 (Oracle OpenJDK 17.0.14)**
- **Gradle (Kotlin DSL)**

---

## Setup Instructions

### 1. Set Environment Variables
Before running the bot, set your **OpenAI API Key** and **Discord Bot Token** as environment variables.

#### Windows (CMD with Admin Privileges)
```sh
set DISCORD_BOT_TOKEN=your_discord_token_here
set OPENAI_API_KEY=your_openai_api_key_here
```

#### Mac/Linux (Terminal)
```sh
export DISCORD_BOT_TOKEN=your_discord_token_here
export OPENAI_API_KEY=your_openai_api_key_here
```

### 2. Configure the Bot
Open the source code and navigate to **line 222, 223**. Enter the **channel ID** where ChatGPT will be used.

### 3. Run the Bot
Now, your bot is ready to run! 🎉

---

## Commands
The bot supports various commands for interaction and configuration:

| Command | Description |
|---------|-------------|
| `..c` | Check the current system message. |
| `..edit <set/add> <내용>` | Add or modify the system message. |
| `..reset` | Reset the system message to default. |
| `..memory` | Set the maximum conversation history ChatGPT can remember. |
| `.. <메시지>` | Send a message that ChatGPT cannot read. |
| `..help` | Show command descriptions and usage. |

---

## Notes
- Editing system messages allows you to customize how ChatGPT behaves in your server.
- Be sure to properly set environment variables before running the bot.
- If you encounter any issues, check the bot logs for debugging.

Enjoy chatting with your AI-powered Discord bot! 🤖🎙️


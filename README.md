# ChatGPT Discord  
A Java-powered ChatGPT bot for Discord.  

## Description  
This bot enables users to interact with ChatGPT within Discord.  

---  
While using this bot, you may encounter various errors. One of the most common error codes is **429**. If you receive this error, it is likely because your OpenAI API credits have been fully consumed. If you wish to continue using the bot, please recharge your API balance.  

## Requirements  
- **Java 17 (Oracle OpenJDK 17.0.14)**  
- **Gradle (Kotlin DSL)**  

## Setup Instructions  

1. Set your **OpenAI API Key** and **Discord Bot Token** as environment variables.  
2. Open the source code and go to **line 222, 223**, then enter the **channel ID** where ChatGPT will be used.
3. You're all set! 🚀  

---  

## Setting Environment Variables  

### Windows (CMD with Admin Privileges)  
```sh  
set DISCORD_BOT_TOKEN=your_discord_token_here  
set OPENAI_API_KEY=your_openai_api_key_here  
```

### Mac/Linux (Terminal)  
```sh  
export DISCORD_BOT_TOKEN=your_discord_token_here  
export OPENAI_API_KEY=your_openai_api_key_here  
```

## Commands  
- **`..c`** → Check the current system message  
- **`..edit <set/add> <content>`** → Add or modify the system message  
- **`..reset`** → Reset the system message  
- **`..memory`** → Set the maximum conversation history ChatGPT can remember  
- **`.. <message>`** → Message that ChatGPT cannot read  
- **`..help`** → Show command descriptions and list  

Now, your bot is ready to run! 🎉


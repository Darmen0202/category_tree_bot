package com.example.category_tree.bot;


import com.example.category_tree.command.BotCommandsHandler;
import com.example.category_tree.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CategoryBot extends TelegramLongPollingBot {

    private final BotCommandsHandler botCommandsHandler;
    private final CategoryRepository categoryRepository;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Autowired
    public CategoryBot(BotCommandsHandler botCommandsHandler, CategoryRepository categoryRepository) {
        this.botCommandsHandler = botCommandsHandler;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        botCommandsHandler.handleCommand(update, this);
    }

}

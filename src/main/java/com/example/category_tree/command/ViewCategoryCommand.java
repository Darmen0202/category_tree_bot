//package com.example.category_tree.command;
//
//import com.example.category_tree.entity.Category;
//import com.example.category_tree.repository.CategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.bots.AbsSender;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class ViewCategoryCommand implements Command {
//
//
//    private final CategoryRepository categoryRepository;
//    private final AbsSender bot;
//
//    @Autowired
//    public ViewCategoryCommand(CategoryRepository categoryRepository, AbsSender bot) {
//        this.categoryRepository = categoryRepository;
//        this.bot = bot; // инжектируем бота через конструктор
//    }
//
//    @Override
//    public void execute(Update update) {
//        List<Category> categories = categoryRepository.findAll();
//        String messageText = categories.stream()
//                .map(category -> category.getId() + ": " + category.getName())
//                .collect(Collectors.joining("\n"));
//
//        SendMessage message = new SendMessage();
//        message.setChatId(update.getMessage().getChatId().toString());
//        message.setText(messageText.isEmpty() ? "Нет доступных категорий." : messageText);
//
//        try {
//            bot.execute(message); // используем ссылку на бота для отправки сообщения
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}

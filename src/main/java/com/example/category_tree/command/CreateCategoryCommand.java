//package com.example.category_tree.command;
//
//import com.example.category_tree.entity.Category;
//import com.example.category_tree.repository.CategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.bots.AbsSender;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//@Component
//public class CreateCategoryCommand implements Command {
//
//    private final CategoryRepository categoryRepository;
//    private final AbsSender bot;
//
//    @Autowired
//    public CreateCategoryCommand(CategoryRepository categoryRepository, AbsSender bot) {
//        this.categoryRepository = categoryRepository;
//        this.bot = bot; // инжектируем бота через конструктор
//    }
//
//    @Override
//    public void execute(Update update) {
//        // Логика для создания категории
//        // Предположим, что текст сообщения содержит имя категории
//        String categoryName = update.getMessage().getText();
//        Category category = new Category();
//        category.setName(categoryName);
//        categoryRepository.save(category);
//        // Отправить сообщение об успешном создании категории
//        SendMessage message = new SendMessage();
//        message.setChatId(update.getMessage().getChatId().toString());
//        message.setText("Категория '" + categoryName + "' успешно создана.");
//
//        try {
//            bot.execute(message); // Правильно вызываем метод execute для отправки сообщения
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
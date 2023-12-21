//package com.example.category_tree.command;
//
//import com.example.category_tree.repository.CategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.bots.AbsSender;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//
//@Component
//public class DeleteCategoryCommand implements Command {
//
//
//    private final CategoryRepository categoryRepository;
//    private final AbsSender bot;
//
//    @Autowired
//    public DeleteCategoryCommand(CategoryRepository categoryRepository, AbsSender bot) {
//        this.categoryRepository = categoryRepository;
//        this.bot = bot; // инжектируем бота через конструктор
//    }
//
//    @Override
//    public void execute(Update update) {
//        String messageText = update.getMessage().getText();
//        Long categoryId;
//        try {
//            // Предполагаем, что id категории следует после команды, например "/delete 123"
//            categoryId = Long.parseLong(messageText.split(" ")[1]);
//            categoryRepository.deleteById(categoryId);
//
//            // Здесь должна быть логика отправки подтверждения об удалении
//            SendMessage confirmationMessage = new SendMessage();
//            confirmationMessage.setChatId(update.getMessage().getChatId().toString());
//            confirmationMessage.setText("Категория с ID " + categoryId + " была удалена.");
//            bot.execute(confirmationMessage);
//
//        } catch (NumberFormatException e) {
//            // Отправить сообщение об ошибке, если id неверный
//            SendMessage errorMessage = new SendMessage();
//            errorMessage.setChatId(update.getMessage().getChatId().toString());
//            errorMessage.setText("Ошибка: неверный формат ID категории.");
//            try {
//                bot.execute(errorMessage);
//            } catch (TelegramApiException e1) {
//                e1.printStackTrace();
//            }
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//

//package com.example.category_tree.command;
//
//import com.example.category_tree.service.CategoryService;
//import com.example.category_tree.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.bots.AbsSender;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//@Component
//public class BotCommandsHandler {
//    private final UserService userService;
//    private final CategoryService categoryService;
//
//    @Autowired
//    public BotCommandsHandler(UserService userService, CategoryService categoryService) {
//        this.userService = userService;
//        this.categoryService = categoryService;
//    }
//
//    public void handleCommand(Update update, AbsSender absSender) {
//        Long userId = update.getMessage().getFrom().getId();
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText().trim();
//            long chatId = update.getMessage().getChatId();
//            SendMessage response = new SendMessage();
//            response.setChatId(String.valueOf(chatId));
//
//            try {
//                if (messageText.startsWith("/addElement")) {
//                    String[] parts = messageText.split("\\s+");
//                    if (parts.length == 2) {
//                        String name = parts[1];
//                        categoryService.addCategory(name, null);
//                        response.setText("Категория '" + name + "' добавлена как корневая.");
//                    } else if (parts.length == 3) {
//                        String parentName = parts[1];
//                        String childName = parts[2];
//                        categoryService.addCategory(childName, parentName);
//                        response.setText("Категория '" + childName + "' добавлена к родителю '" + parentName + "'.");
//                    } else {
//                        response.setText("Неправильный формат команды. Используйте: /addElement <родитель> <элемент>");
//                    }
//                } else if (messageText.startsWith("/removeElement")) {
//                    String[] parts = messageText.split("\\s+");
//                    if (parts.length == 2) {
//                        String name = parts[1];
//                        categoryService.removeCategory(name);
//                        response.setText("Категория '" + name + "' и все дочерние элементы удалены.");
//                    } else {
//                        response.setText("Неправильный формат команды. Используйте: /removeElement <элемент>");
//                    }
//                } else if (messageText.startsWith("/viewTree")) {
//                    String tree = categoryService.viewTree();
//                    response.setText(tree.isEmpty() ? "Дерево категорий пусто." : tree);
//                } else if (messageText.equals("/help")) {
//                    response.setText(buildHelpMessage());
//                } else {
//                    response.setText("Неизвестная команда.");
//                }
//                absSender.execute(response);
//            } catch (IllegalArgumentException e) {
//                response.setText(e.getMessage());
//                try {
//                    absSender.execute(response);
//                } catch (TelegramApiException ex) {
//                    ex.printStackTrace();
//                }
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String buildHelpMessage() {
//        return "Доступные команды:\n" +
//                "/addElement <название> - добавить корневой элемент\n" +
//                "/addElement <родитель> <дочерний> - добавить дочерний элемент\n" +
//                "/removeElement <название> - удалить элемент и его подэлементы\n" +
//                "/viewTree - показать дерево категорий\n" +
//                "/help - показать эту справку";
//    }
//
//
//}

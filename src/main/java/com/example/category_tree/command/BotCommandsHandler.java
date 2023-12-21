package com.example.category_tree.command;

import com.example.category_tree.entity.Role;
import com.example.category_tree.service.CategoryService;
import com.example.category_tree.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotCommandsHandler {

    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public BotCommandsHandler(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public void handleCommand(Update update, AbsSender absSender) {
        Long userId = update.getMessage().getFrom().getId(); // Telegram ID пользователя
        boolean isAdmin = userService.isUserAdmin(userId); // Проверяем, является ли пользователь администратором

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId().toString());

            try {
                if (messageText.startsWith("/addElement")) {
                    if (isAdmin) {
                        handleAddElementCommand(messageText, response);
                    } else {
                        response.setText("У вас нет прав для добавления категории.");
                    }
                } else if (messageText.startsWith("/removeElement")) {
                    if (isAdmin) {
                        handleRemoveElementCommand(messageText, response);
                    } else {
                        response.setText("У вас нет прав для удаления категории.");
                    }
                } else if (messageText.startsWith("/viewTree")) {
                    handleViewTreeCommand(response);
                } else if (messageText.equals("/help")) {
                    response.setText(buildHelpMessage());
                } else if (isAdmin && messageText.startsWith("/setRole")) {
                    handleSetRoleCommand(messageText, absSender, update);
                } else {
                    response.setText("Неизвестная команда.");
                }
                absSender.execute(response);
            } catch (IllegalArgumentException e) {
                response.setText(e.getMessage());
                try {
                    absSender.execute(response);
                } catch (TelegramApiException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSetRoleCommand(String messageText, AbsSender absSender, Update update) {
        // предполагаем, что команда имеет формат "/setRole <telegramId> <role>"
        String[] parts = messageText.split("\\s+");
        if (parts.length == 3) {
            try {
                Long telegramId = Long.parseLong(parts[1]);
                String roleName = parts[2].toUpperCase();
                Role role = Role.valueOf(roleName); // Это бросит IllegalArgumentException, если roleName недопустим
                userService.changeUserRole(telegramId, role);
                sendTextMessage(update.getMessage().getChatId(), "Роль пользователя обновлена на " + roleName, absSender);
            } catch (IllegalArgumentException e) {
                sendTextMessage(update.getMessage().getChatId(), "Неправильное имя роли: " + parts[2], absSender);
            }
        } else {
            sendTextMessage(update.getMessage().getChatId(), "Неправильный формат команды. Используйте: /setRole <telegramId> <role>", absSender);
        }
    }

    private void sendTextMessage(Long chatId, String text, AbsSender absSender) {
        SendMessage message = new SendMessage(chatId.toString(), text);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleAddElementCommand(String messageText, SendMessage response) {
        String[] parts = messageText.split("\\s+");
        if (parts.length == 2) {
            String name = parts[1];
            categoryService.addCategory(name, null);
            response.setText("Категория '" + name + "' добавлена как корневая.");
        } else if (parts.length == 3) {
            String parentName = parts[1];
            String childName = parts[2];
            categoryService.addCategory(childName, parentName);
            response.setText("Категория '" + childName + "' добавлена к родителю '" + parentName + "'.");
        } else {
            response.setText("Неправильный формат команды. Используйте: /addElement <родитель> <элемент>");
        }
    }

    private void handleRemoveElementCommand(String messageText, SendMessage response) {
        String[] parts = messageText.split("\\s+");
        if (parts.length == 2) {
            String name = parts[1];
            categoryService.removeCategory(name);
            response.setText("Категория '" + name + "' и все дочерние элементы удалены.");
        } else {
            response.setText("Неправильный формат команды. Используйте: /removeElement <элемент>");
        }
    }

    private void handleViewTreeCommand(SendMessage response) {
        String tree = categoryService.viewTree();
        response.setText(tree.isEmpty() ? "Дерево категорий пусто." : tree);
    }

    private String buildHelpMessage() {
        return "Доступные команды:\n" +
                "/addElement <название> - добавить корневой элемент\n" +
                "/addElement <родитель> <дочерний> - добавить дочерний элемент\n" +
                "/removeElement <название> - удалить элемент и его подэлементы\n" +
                "/viewTree - показать дерево категорий\n" +
                "/help - показать эту справку";
    }
}


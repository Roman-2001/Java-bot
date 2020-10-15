import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Scanner;

public class Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(new Main());

//        System.out.println("Привет!");
//        String h = new ChatBot().help;
//        System.out.println(h);
//        ChatBot chat = new ChatBot();
//        String id = "";
//        while (true) {
//            Scanner a = new Scanner(System.in);
//            String message = a.next();
//            if (message.equals("играть")) {
//                System.out.println("Введите числовой индентификатор");
//                id = a.next();
//            }
//            System.out.println(chat.getMessage(message, id));
//        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        ChatBot chat = new ChatBot();
        String message = update.getMessage().getText();
        String res = chat.getMessage(message, update.getMessage().getChatId().toString());
        sendMsg(update.getMessage().getChatId().toString(), res);
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
    }

    @Override
    public String getBotUsername() {
        return System.getenv("UserBotName1");
    }

    @Override
    public String getBotToken() {
        return System.getenv("Token1");
    }
}
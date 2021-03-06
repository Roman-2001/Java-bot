import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.HashMap;
import java.util.Map;


public class Main extends TelegramLongPollingBot {
    private ChatBot chatBot = new ChatBot();
    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new Main());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String res = null;
        String id = update.getMessage().getChatId().toString();
        res = chatBot.getMessage(message, id);
        sendMsg(id, res);
        Player player = chatBot.players.get(id);
        if (player.lastProgramMessage == Player.LastProgramMessage.WAITBATTLE) {
            res = String.format("Хотите сразиться в битве с %s?\n/yes:%s\n/no", id, id);
            for (String playerID : chatBot.players.keySet()) {
                if (!(playerID.equals(id)))
                    sendMsg(playerID, res);
            }
        }



        if (!player.opponentID.equals("")) {
            Player opponent = chatBot.players.get(player.opponentID);
            if (opponent.opponentID.equals("")) {
                res = chatBot.getMessage("go:" + id, player.opponentID);
                sendMsg(player.opponentID, res);
            }
            if (!(player.fight || opponent.fight)) {
                sendMsg(player.opponentID, chatBot.findWinner(opponent));
                finishBattle(player);
                finishBattle(opponent);
            }
            if (Math.abs(player.pointForBattle - opponent.pointForBattle) >= 5 ||
                    (!opponent.fight && (player.pointForBattle > opponent.pointForBattle)) ||
                    (!player.fight && player.pointForBattle < opponent.pointForBattle))
            {
                sendMsg(opponent.opponentID, chatBot.findWinner(player));
                sendMsg(player.opponentID, chatBot.findWinner(opponent));
                finishBattle(player);
                finishBattle(opponent);
            }
        }
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void finishBattle(Player player){
        player.opponentID = "";
        player.lastProgramMessage = null;
        player.fight = false;
        player.pointForBattle = 0;
        player.health = 3;
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
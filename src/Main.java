import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args) {
        //Initialize Api Context
        ApiContextInitializer.init();
        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

  /*      try {
            botsApi.registerBot(new PhotoBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("PhotoBot successfully started!");
*/
        try {
            botsApi.registerBot(new EmojiTestBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        /*try {
            botsApi.registerBot(new BotApi20());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/
        System.out.println("EmojiTestBot successfully started!");
    }
}

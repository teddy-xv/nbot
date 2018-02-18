import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import static java.lang.Math.toIntExact;

public class EmojiTestBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        //String botUserName = "nightoutaddisbot";
        //String token = "481925653:AAFMhKPxaui8ZA-CMkpK4MLweHm-p3DwPw8";
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/start")) {
                // Set variables
                String user_first_name = update.getMessage().getChat().getFirstName();
                String user_last_name = update.getMessage().getChat().getLastName();
                String user_username = update.getMessage().getChat().getUserName();
                long user_id = update.getMessage().getChat().getId();
                String message_text = update.getMessage().getText();
                long chat_id = update.getMessage().getChatId();

                String answer = EmojiParser.parseToUnicode("Welcome to Night Out Addis Bot :smile:\n\n Few things you can do :robot:");
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(answer);
                log(user_first_name, user_last_name, Long.toString(user_id), message_text, answer);

                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                try {
                    sendMessage(message);
                    check(user_first_name, user_last_name, toIntExact(user_id), user_username);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }




            }


        }
    }


    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "nightoutaddisbot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "481925653:AAFMhKPxaui8ZA-CMkpK4MLweHm-p3DwPw8";
    }

    private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }


    private String check(String first_name, String last_name, int user_id, String username) {
        // Set loggers
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_nightoutbot");
        MongoCollection<Document> collection = database.getCollection("users");
        long found = collection.count(Document.parse("{id : " + Integer.toString(user_id) + "}"));
        if (found == 0) {
            Document doc = new Document("first_name", first_name)
                    .append("last_name", last_name)
                    .append("id", user_id)
                    .append("username", username);
            collection.insertOne(doc);
            mongoClient.close();
            System.out.println("User not exists in database. Written.");
            return "no_exists";
        } else {
            System.out.println("User exists in database.");
            mongoClient.close();
            return "exists";
        }


    }

}


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Main {
    // 1. PUT YOUR TOKEN HERE
    private static final String TOKEN = "8547551173:AAGE2JdFxo3HCVaXXE3jzcyQSaioc5fjteM";
    private static final String BASE_URL = "https://api.telegram.org/bot" + TOKEN;

    public static void main(String[] args) {
        int lastUpdateId = 0;
        System.out.println("Bot is running... Send me 'Jigar' or any word!");

        while (true) {
            try {
                // Fetch updates
                String response = request(BASE_URL + "/getUpdates?offset=" + (lastUpdateId + 1) + "&timeout=10");

                if (response.contains("\"text\":\"")) {
                    // Extract update_id
                    int idIdx = response.lastIndexOf("\"update_id\":") + 12;
                    lastUpdateId = Integer.parseInt(response.substring(idIdx, response.indexOf(",", idIdx)));

                    // Extract Chat ID
                    int chatIdx = response.lastIndexOf("\"chat\":{\"id\":") + 13;
                    String chatId = response.substring(chatIdx, response.indexOf(",", chatIdx));

                    // Extract EXACT User Message
                    int textIdx = response.lastIndexOf("\"text\":\"") + 8;
                    String userMessage = response.substring(textIdx, response.indexOf("\"", textIdx));

                    // LOGIC: Just send back exactly what was received
                    System.out.println("Received: " + userMessage);
                    sendReply(chatId, userMessage); 
                }
                
                Thread.sleep(500); // Faster response time
            } catch (Exception e) {
                // Silently handle errors to keep the bot running
            }
        }
    }

    private static void sendReply(String chatId, String text) throws Exception {
        // URLEncoder.encode is CRITICAL so 'Jigar' or 'Hello World' works
        String encodedText = URLEncoder.encode(text, "UTF-8");
        request(BASE_URL + "/sendMessage?chat_id=" + chatId + "&text=" + encodedText);
    }

    private static String request(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder res = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) res.append(line);
        in.close();
        return res.toString();
    }
}

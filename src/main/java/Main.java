import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Main {

    public static void main(String[] args) throws IOException {

        JSONObject Json = new JSONObject();
        Json.put("firstName", "");
        Json.put("lastName", "");
        Json.put("email", "");
        Json.put("challengeId", "");
        Json.put("challengeAnswer", "");
        Json.put("phone", "");
        Json.put("cv", encodeFileToBase64Binary(""));
        Json.put("messageToGepardec", "");


        URL url = new URL("https://weckdengeparden-57-services.cloud.itandtel.at/challenge/1/answer");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "text/plain");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = Json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

    }

    /**
     * Read the file and encode it to a base64 string
     * @param fileName The file which we want to read and encode
     * @return a basic base64 string
     * @throws IOException
     */
    private static String encodeFileToBase64Binary(String fileName) throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.getEncoder().encode(bytes);
        String encodedString = new String(encoded, StandardCharsets.UTF_8);

        return encodedString;
    }

    /**
     * Load a file and store it in a bytearray
     * @param file the file which we want to read
     * @return a byte array
     * @throws IOException
     */
    private static byte[] loadFile(File file) throws IOException {
        InputStream inputFileStream = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            System.out.println("File is to large.");
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length &&
                (numRead = inputFileStream.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException(String.format("File '%s' could not be read completely", file.getName()));
        }

        inputFileStream.close();
        return bytes;
    }
}

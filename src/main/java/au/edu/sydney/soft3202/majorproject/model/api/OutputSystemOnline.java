package au.edu.sydney.soft3202.majorproject.model.api;

import au.edu.sydney.soft3202.majorproject.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OutputSystemOnline {
    /**
     * Launch the POST request to the output API
     * @param pasteString
     * @param outputKey
     * @return response string
     */
    public String postToOutputAPI(String pasteString, String outputKey){
//curl -X POST -d 'api_dev_key=UZ5Tsp_Ex8PC9AZ6EZbqClVbu8GEZyW6' -d 'api_paste_code=test' -d
// 'api_option=paste' "https://pastebin.com/api/api_post.php"
        try{
//            String key = "api_dev_key=UZ5Tsp_Ex8PC9AZ6EZbqClVbu8GEZyW6";
            String key = "api_dev_key=" + outputKey;
            String input = "api_paste_code=" + pasteString;
            String user = "api_user_key=" + Main.outputKey;
            //curl -X POST -d 'api_dev_key=UZ5Tsp_Ex8PC9AZ6EZbqClVbu8GEZyW6' -d
            // 'api_paste_code=test' -d 'api_option=paste' "https://pastebin.com/api/api_post.php"
            System.out.println(pasteString);
            String[] commands = new String[]{"curl", "-X", "POST",
                    "https://pastebin.com/api/api_post.php","-d", key, "-d", input,
                    "-d", "api_option=paste"};
            Process process = Runtime.getRuntime().exec(commands);
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println(response);
            return response.toString();

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

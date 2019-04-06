import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.*;

public class HttpRequest1 {
    public final String url = "http://www.notexponential.com/aip2pgaming/api/index.php";
    public static final String KEY = "089c4da0750c3dcfefb3";
    public static final String USERID = "696";
    public String teamId = "1133";
    public String gameId = "";

    public String doGet(String httpUrl) {
        InputStream is = null;
        HttpURLConnection connection = null;
        BufferedReader br = null;
        String results = "";
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-api-key", KEY);
            connection.setRequestProperty("userid", USERID);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                results = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        return results;
    }

    public String doPost(String httpUrl, String parameter) {
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        HttpURLConnection connection = null;
        String results = "";
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("x-api-key", KEY);
            connection.setRequestProperty("userid", USERID);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            os = connection.getOutputStream();
            os.write(parameter.getBytes());
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                results = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        return results;
    }

    public void create_game(String teamId2, int boardsize, int target){
        String str = doPost(url, "type=game&teamId1="+teamId+"&teamId2="+teamId2+"&gameType=TTT&boardSize="+boardsize+"&target="+target);
        Map<String, Object> map = jsonToMap(str);
        gameId = (String) map.get("gameId");
        System.out.println("gameId:"+gameId);
    }

    public void make_move(Point p){
        String str = doPost(url, "type=move&gameId="+gameId+"&teamId="+teamId+"&move="+p.y+","+p.x);
        System.out.println("makemove:"+str);
    }

    public String get_moves(int count){
        return doGet(url+"?type=moves&gameId="+gameId+"&count="+count);
    }

    public List<Map<String, Object>> jsonToList(String jsonStr){
        List<Map<String, Object>> resultList = new ArrayList<>();
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        for(int j = 0; j < jsonArray.size(); j++){
            String jm = jsonArray.getString(j);
            if(jm.indexOf("{")==0){
                Map<String, Object> m = jsonToMap(jm);
                resultList.add(m);
            }
        }
        return resultList;
    }

    public Map<String, Object> jsonToMap(String jsonStr){
        Map<String, Object> resultMap = new HashMap<>();
        if(!jsonStr.isEmpty()){
            JSONObject json = JSONObject.fromObject(jsonStr);
            Iterator it = json.keys();
            while(it.hasNext()){
                String key = (String) it.next();
                String value = json.getString(key);
                if(value.indexOf("{")==0){
                    resultMap.put(key.trim(),jsonToMap(value));
                }else if(value.indexOf("[")==0){
                    resultMap.put(key.trim(),jsonToList(value));
                }else{
                    resultMap.put(key.trim(),value.trim());
                }
            }
        }
        return resultMap;
    }
}
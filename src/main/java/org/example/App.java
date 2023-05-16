package org.example;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        List<String> list1 = new ArrayList<>(Arrays.asList("0x649D1d6320c1C35b16c09C14EA1cDa13803726fc",
                "0x642Ef8E2D004F0360408EDEd2a40aEdaee38f44C",
                "0x24Ce1453918AB2B2d1EFA616a6c6c9C363ABAe2F",
                "0x7057C11703852AB8F59418f10A48077fF92059C8"));
        while (true) {
            sendRequests(list1);
            System.out.println("sleep for 5 min");
            Thread.sleep(300000);
        }
    }

    public static void sendRequests(List<String> adressList){
        try {
            for (String str : adressList){
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://swaprum.finance/server/claim-free?address=%s".formatted(str)))
                        .GET()
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(str + ' ' + response.body());
                checkUpBalance(str);
                Thread.sleep(1500);
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void checkUpBalance(String str){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://swaprum.finance/server/user?address=%s".formatted(str)))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject object = new JSONObject(response.body());
            Long parsed = Long.parseLong((String) object.get("freeClaimBalance"));
            System.out.println("balance: " + parsed);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

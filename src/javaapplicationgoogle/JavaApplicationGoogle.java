/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplicationgoogle;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author celso
 */
public class JavaApplicationGoogle {

    /**
     * @param args the command line arguments
     *
     * para informacoes de parametros
     * https://developers.google.com/image-search/v1/jsondevguide?csw=1#basic_query
     */
    public JavaApplicationGoogle(String termo) throws IOException {

        //String area = " eletrônica";
        String atributos = "";
        String nome_arquivo = termo;
        termo = termo.replace(" ", "%20");
        termo = "&q=" + termo;
        int max = 15;
        int termos_salvos = 0;
        for (int h = 0; h < max; h = h + 5) {
            int aux = h;
            URL url = null;
            try {
                url = new URL("http://ajax.googleapis.com/ajax/services/search/images?rsz=5&as_file&v=1.0" + atributos + termo);
            } catch (MalformedURLException ex) {
                Logger.getLogger(JavaApplicationGoogle.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("--------------------------" + url);
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Referer", "images.google.com.br");
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (java.io.IOException eio) {
                max++;
                continue;
            }
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject json = new JSONObject(builder.toString());
            JSONArray results = json.getJSONObject("responseData").getJSONArray("results"); // pegando os resultados

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i); // passando cada resultado para um JSONObject
                String url_txt = result.getString("url").toString().replace("%2520", "%20");
                URL aux_url = new URL(url_txt); // pegando o campo url de cada resultado

                System.out.println(aux_url);
                InputStream is = null;
                try {

                    URLConnection con = aux_url.openConnection();
                    con.setConnectTimeout(1500);
                    con.setReadTimeout(8000);
                    is = con.getInputStream();

                    //is = aux_url.openStream(); //pegando foto
                } catch (FileNotFoundException efnf) {
                    max++;
                    continue;
                } catch (IOException eio) {
                    max++;
                    continue;
                }

                //salvando foto
                File arq = new File("pics/" + nome_arquivo);
                arq.mkdir();

                String extension = "." + url_txt.charAt((url_txt.length() - 3)) + url_txt.charAt((url_txt.length() - 2)) + url_txt.charAt((url_txt.length() - 1));

                String nome = "pics/" + nome_arquivo + "/" + nome_arquivo + h + i + extension;
                OutputStream os = null;
                try {
                    os = new FileOutputStream(nome);
                } catch (Exception e) {
                    continue;
                }
                System.out.println(nome + "\n");
                byte[] b = new byte[2048];
                int length;
                try{
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                }catch(java.net.SocketTimeoutException est){
                    continue;
                }
                catch(java.io.IOException eio){
                    continue;
                }
                is.close();
                os.close();
                if(termos_salvos>11)
                    return;
                termos_salvos++;
                aux++;
            }

            atributos = "&start=" + aux;
        }
        
    }
}

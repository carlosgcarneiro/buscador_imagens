/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplicationgoogle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.AbstractList;
import java.util.List;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author caca
 */
public class PegarImagens {

    public static void main(String[] args) throws FileNotFoundException, IOException, JDOMException {

        //String fileName = "pt_BR - Copiaaaa.dic";
        String fileName = "sgn46.spml";
        Vector<String> results = lerXML(fileName);
        int cont = 0;
        for (String result : results) {
            if (cont > 1657) {
                new JavaApplicationGoogle(result);
                System.out.println(result + "\n" + "termos processados: " + cont);
            }
            cont++;
        }

    }

    public static String trocaAcentuacao(String acentuada) {

        char[] acentuados = new char[]{'ç', 'á', 'à', 'ã', 'â', 'ä', 'é', 'è', 'ê', 'ë', 'í', 'ì', 'î', 'ï', 'ó', 'ò', 'õ', 'ô', 'ö', 'ú', 'ù', 'û', 'ü'};

        char[] naoAcentuados = new char[]{'c', 'a', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'i', 'o', 'o', 'o', 'o', 'o', 'u', 'u', 'u', 'u'};

        for (int i = 0; i < acentuados.length; i++) {
            acentuada = acentuada.replace(acentuados[i], naoAcentuados[i]);
            acentuada = acentuada.replace(Character.toUpperCase(acentuados[i]), Character.toUpperCase(naoAcentuados[i]));
        }
        return acentuada;
    }

    public static void lerDic(String fileName) throws FileNotFoundException, IOException {
        InputStream is;
        String s;
        try {
            is = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        int cont = 0;
        do {
            s = trocaAcentuacao(br.readLine());

            cont++;
            new JavaApplicationGoogle(s);
        } while (cont != 3);

        System.out.println(cont);

    }

    public static Vector<String> lerXML(String file) throws JDOMException, IOException {
        Vector<String> termos_to_search = new Vector<String>();

        FileReader file_reader = new FileReader(file);
        //Criamos um objeto SAXBuilder
        // para ler o arquivo
        SAXBuilder sb = new SAXBuilder();
        //Criamos um objeto Document que
        // recebe o conteúdo do arquivo
        Document doc = sb.build(file_reader);
        //Criamos um objeto Element que
        // recebe as tags do XML
        Element elements = doc.getRootElement();

        List<Element> lista = elements.getChildren();
        String excessao = "Libras Escrita (www.librasescrita.com.br)/ Glossário CEFET-MG (Vera Lúcia de Souza e Lima)";
        String excessao1 = "Vera Lúcia de Souza e Lima (CEFET-MG); Libras Escrita (www.librasescrita.com.br)";
        String excessao2 = "Glossário CEFET-MG (Vera Lúcia de Souza e Lima); Libras Escrita (www.librasescrita.com.br)";

        List<Element> termos = null;
        int cont = 0;
        String result;

        for (Element element : lista) {
            if (cont > 1) {
                try {
                    termos = element.getChildren();
                    String src = termos.get((termos.size() - 1)).getValue();
                    result = termos.get(1).getValue();
                    if (src != excessao || src != excessao1 || src != excessao2) {
                        termos_to_search.add(result);
                    }

                } catch (IndexOutOfBoundsException e) {
                }
            }

            cont++;
        }
        return termos_to_search;
    }

}

package com.monkey.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午4:19
 **/
public class JsoupTest {

    public static void main(String []  args){
        try {
          Document document =  Jsoup.connect("https://www.ibm.com/developerworks/cn/java/j-lo-jsouphtml/")
                  .get();
          Elements  elements = document.select(".ibm-h2");
          for (Element element: elements){
              System.out.println(element.text());
          }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

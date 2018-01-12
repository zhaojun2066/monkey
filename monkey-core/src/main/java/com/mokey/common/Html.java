package com.mokey.common;

import com.mokey.parse.Parse;
import org.jsoup.nodes.Document;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 下午3:40
 *
 * jsoup docuemt and some function for xpath and selector
 **/
public class Html extends Document{

  //  private Document document;// jsoup

 //   private Parse parse;

    public Html(String baseUri) {
        super(baseUri);
    }


    public static Html me(String baseUri){
        Html html = new Html(baseUri);
        return html;

    }


}

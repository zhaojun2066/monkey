package com.mokey.analyzer;

import com.mokey.common.Page;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;

/**
 * @User: jufeng
 * @Date: 18-1-10
 * @Time: 下午10:44
 **/
public class DemoAnalyzer implements Analyzer{
    @Override
    public void analyzer(Page page, Map<String,String> customerParams) {
        System.out.println(page.getDocument());
        Elements elements = null;

        elements = page.getDocument().select("body > div.column_banner > div.column_ba_con > h3 > a");

        for (Element e: elements){
            System.out.println("e-> "+e.text());
        }
    }
}

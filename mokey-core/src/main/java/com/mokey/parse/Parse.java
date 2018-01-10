package com.mokey.parse;

import java.util.List;

/**
 * @User: jufeng
 * @Date: 18-1-10
 * @Time: 下午5:12
 **/
public interface Parse {


    Parse links();

    Parse select(String select);
    Parse css(String cssSelect);

    Parse selectAttr(String select,String attr);
    Parse cssAttr(String css,String attr);

    Parse first();

    List<String > all();

    String one();






}

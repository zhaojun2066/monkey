package com.mokey.analyzer;

import com.mokey.common.Page;

import java.util.Map;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 下午9:03
 **/
public interface Analyzer {

    /**
     * analyzer Page
     * @param page
     */
    void analyzer(Page page, Map<String,String> customerParams);
}

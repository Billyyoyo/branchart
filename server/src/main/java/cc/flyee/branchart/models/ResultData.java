package cc.flyee.branchart.models;

import java.util.HashMap;
import java.util.Map;

public class ResultData {

    private Map<String, Object> data;

    private ResultData() {
        data = new HashMap<>();
    }

    public static ResultData build() {
        return new ResultData();
    }

    public ResultData put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

}

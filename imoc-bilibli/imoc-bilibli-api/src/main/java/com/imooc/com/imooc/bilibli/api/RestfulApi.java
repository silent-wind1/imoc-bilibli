package com.imooc.com.imooc.bilibli.api;

import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RestfulApi {
    private final Map<Integer, Map<String, Object>> dataMap;

    public RestfulApi() {
        dataMap = new HashMap<>();
        for (int i = 1; i < 3; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", i);
            data.put("name", "name" + i);
            dataMap.put(i, data);
        }
    }

    @GetMapping("/obj/{id}")
    public Map<String, Object> getData(@PathVariable Integer id) {
        return dataMap.get(id);
    }

    @PostMapping("/obj")
    public String postData(@RequestBody Map<String, Object> data) {
            Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);
            Arrays.sort(idArray);
            int nextID = idArray[idArray.length - 1] + 1;
            dataMap.put(nextID, data);
        return "post success";
    }

    @PutMapping("/obj")
    public String putData(@RequestBody Map<String, Object> data)  {
        Integer id = Integer.valueOf(String.valueOf(data.get("id")));
        Map<String, Object> map = dataMap.get(id);
        if (map == null) {
            Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);
            Arrays.sort(idArray);
            int nextID = idArray[idArray.length - 1] + 1;
            dataMap.put(nextID, data);
        }else {
            dataMap.put(id, data);
        }
        return "put success";
    }

    @DeleteMapping("/obj/{id}")
    public String deleteData(@PathVariable Integer id) {
        dataMap.remove(id);
        return "delete success";
    }
}

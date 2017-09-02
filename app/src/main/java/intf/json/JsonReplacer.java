package intf.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class JsonReplacer {
    public Map mapValue = new TreeMap();

    public Map getMapValue() {
        return mapValue;
    }

    public JsonReplacer(Map mapValue) {
        this.mapValue = mapValue;
    }

    public void setMapValue(Map mapValue) {
        this.mapValue = mapValue;
    }

    public List<Object> fromJsonArray(JSONArray arrayObject) {
        List<Object> arrayvalues = null;
        try {
            arrayvalues = new ArrayList<Object>();
            JSONArray array = arrayObject;
            for (int i = 0; i < array.length(); i++) {
                String item = array.get(i).toString();

                arrayvalues.add(extractJsonRightValue(item,""));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrayvalues;

    }

    public List<Object> fromJsonArray(String content) {
        try {
            return fromJsonArray(new JSONArray(content));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String fromJsonObject(String jsonObjectValue) {
        try {
            return fromJsonObject(new JSONObject(jsonObjectValue), "");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public String fromJsonObject(String jsonObjectValue, String fromkey) {
        try {
            return fromJsonObject(new JSONObject(jsonObjectValue), fromkey);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Map map = new TreeMap();

    public String fromJsonObject(JSONObject jsonObjectValue, String fromkey) {
        // TODO Auto-generated method stub
        List<Object> arrayvalues = null;
        Map<String, String> map = null;
        try {
            arrayvalues = new ArrayList<Object>();
            JSONObject jsonobject = jsonObjectValue;
            map = new HashMap<String, String>();
            String name;
            String strvalue;
            Object extractedValue;
            for (int i = 0; i < jsonobject.length(); i++) {
                name = jsonobject.names().getString(i);
                strvalue = jsonobject.getString(name);
                    String nkey = (fromkey==null||fromkey.equals("")?"":fromkey+"-") + name.replaceAll(" ","");
                extractedValue = extractJsonRightValue(strvalue, nkey);
//                if (extractedValue.equals("*")) {
                    if (mapValue.containsKey(nkey)) {
                    String nV = (String) mapValue.get(nkey);
                        if(nV!=null&&!nV.equals("")){
                        extractedValue = nV;

                        }
                    }
//                }
                map.put(name, extractedValue.toString());
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return JsonToMapUtils.mapToJson(map);
    }

    public Object extractJsonRightValue(String value, String fromkey) {
        Object jsonvalue = null;
        if (value.startsWith("[")) {
            jsonvalue = fromJsonArray(value);
        } else if (value.startsWith("{")) {
            jsonvalue = fromJsonObject(value, fromkey);
        } else {
//            if (value.toString().equals("*")) {
                if (fromkey != null && mapValue.containsKey(fromkey)) {
                    jsonvalue = mapValue.get(fromkey);
                } else {
                    jsonvalue = value;
                }
//            } else {
//                jsonvalue = value;
//            }
        }
        return jsonvalue;
    }

    public Object findJsonNode(String exactname, JSONArray rootObject) {
        if (rootObject instanceof JSONArray) {
            JSONArray jsonobject = (JSONArray) rootObject;
            for (int i = 0; i < jsonobject.length(); i++) {
                try {
                    return findJsonNode(exactname, jsonobject.getString(i));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return rootObject;
    }

    public Object findJsonNode(String exactname, JSONObject rootObject) {
        try {
            String name;
            String strvalue;
            if (rootObject instanceof JSONObject) {
                JSONObject jsonobject = (JSONObject) rootObject;
                for (int i = 0; i < jsonobject.length(); i++) {
                    name = jsonobject.names().getString(i);
                    strvalue = jsonobject.getString(name);
                    if (name.equalsIgnoreCase(exactname)) {
                        return strvalue;
                    } else {
                        Object node = findJsonNode(exactname, strvalue);
                        if (node != null && !node.toString().equals(strvalue)) {
                            return node.toString();
                        }
                    }

                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Object findJsonNode(String exactname, String strvalue) {
        try {
            if (strvalue.startsWith("[")) {

                return findJsonNode(exactname, new JSONArray(strvalue));

            } else if (strvalue.startsWith("{")) {
                return findJsonNode(exactname, new JSONObject(strvalue));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strvalue;
    }

    public Object findJsonLink(String nodelink, String rootObject) {
        try {
            return findJsonLink(nodelink, new JSONObject(rootObject));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                return findJsonLink(nodelink, new JSONArray(rootObject).get(0));
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                return "";
            }

        }
    }

    public Object findJsonLink(String nodelink, Object rootObject) {
        if (rootObject instanceof JSONObject) {
            return findJsonLink(nodelink, (JSONObject) rootObject);
        } else if (rootObject instanceof JSONArray) {
            return findJsonLink(nodelink, (JSONArray) rootObject);
        }
        return rootObject;
    }

    public Object findJsonLink(String nodelink, JSONArray rootObject) {
        Object node = "";
        for (int i = 0; i < rootObject.length(); i++) {
            try {
                node = findJsonLink(nodelink, rootObject.get(i));
                if (node.equals("")) {
                    continue;
                } else {
                    return node;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return rootObject;

    }

    public Object findJsonLink(String nodelink, JSONObject rootObject) {
        Object jsonobject = null;
        if (nodelink.equals("")) {
            return rootObject;
        }
        try {
            String[] nodes = nodelink.split("-");
            String data = nodes[0];

            jsonobject = getNode(rootObject, data);
            if (jsonobject == null || jsonobject.equals("")) {
                return jsonobject;
            }
            for (int j = 1; j < nodes.length; j++) {

                if (jsonobject instanceof JSONObject) {
                    jsonobject = getNode((JSONObject) jsonobject, nodes[j]);
                } else if (jsonobject instanceof JSONArray) {
                    jsonobject = ((JSONArray) jsonobject).get(0);
                } else {
                    return jsonobject;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonobject;
    }

    private Object getNode(JSONObject rootObject, String data)
            throws JSONException {
        Object jsonobject;
        String[] ns = data.split(":");
        String node = ns[0];
        String index = "";

        if (!rootObject.has(node)) {
            return "";
        }
        jsonobject = rootObject.get(node);
        if (jsonobject == null) {
            return "";
        }
        if (ns.length > 1) {
            index = ns[1];
            Integer i = Integer.parseInt(index);
            if (((JSONArray) jsonobject).length() + 1 >= i) {
                jsonobject = ((JSONArray) jsonobject).get(Integer
                        .parseInt(index));
            }
        }
        return jsonobject;
    }
}

import java.io.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public abstract class FileVariables <T>{

    HashMap<String, T> map;
    HashMap<String, T[]> arrayMap;

    String saveFile;
    final String seperator = ":", arraySeperator = "|", valueId = "V", arrayId = "A";

    public FileVariables(String saveFile)
    {
        map = new HashMap<>();
        arrayMap = new HashMap<>();

        this.saveFile = saveFile;

        File create = new File(saveFile);
        try {
            create.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        load();
    }

    public abstract T parseType(String toParse);
    public abstract T[] createArray(int s);

    private String encodeArray(T[] array)
    {
        String s = "";

        for (T t: array)
        {
            s += t.toString() + arraySeperator;
        }

        return s;
    }

    private T[] parseArray(String toParse)
    {
        String[] unparsed = toParse.split(String.format("[%s]", arraySeperator));
        T[] parsed = createArray(unparsed.length);

        for (int i=0;i<unparsed.length;i++)
        {
            parsed[i] = parseType(unparsed[i]);
        }

        return parsed;
    }

    private void load()
    {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(String.format("[%s]", seperator), 3);

                    switch (split[1])
                    {
                        case valueId: map.put(split[0], parseType(split[2])); break;
                        case arrayId: arrayMap.put(split[0], parseArray(split[2])); break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save()
    {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(saveFile));

            for (Map.Entry<String, T> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String identifier = valueId;

                pw.println(key + seperator + identifier + seperator + value);
            }

            for (Map.Entry<String, T[]> entry: arrayMap.entrySet())
            {
                String key = entry.getKey();
                String encodedValue = encodeArray(entry.getValue());
                String identifier = arrayId;

                pw.println(key + seperator + identifier + seperator + encodedValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.close();
    }

    public T getValue(String key)
    {
        return map.get(key);
    }

    public void putValue(String key, T value)
    {
        map.put(key, value);
    }

    public void putArray(String key, T[] array)
    {
        if (array == null)
        {
            arrayMap.remove(key);
        }
        else {
            arrayMap.put(key, array);
        }
    }

    public void putArrayValue(String key, T value, int index)
    {
        arrayMap.get(key)[index] = value;
    }

    public T[] getArray(String key)
    {
        return arrayMap.get(key);
    }

    public T getArrayValue(String key, int index)
    {
        return arrayMap.get(key)[index];
    }

    public boolean containsKey(String key)
    {
        return map.containsKey(key) || arrayMap.containsKey(key);
    }
}

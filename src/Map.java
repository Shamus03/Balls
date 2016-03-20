public class Map<K extends Comparable, V>
{
    private ArrayList<MapEntry> map;

    public Map()
    {
        map = new ArrayList<MapEntry>();
    }

    public boolean containsKey(K check)
    {
        boolean found = false;
        for (int i = 0; !found && i < map.size(); i++)
        {
            if (map.get(i).getKey().equals(check))
                found = true;
        }
        return found;
    }

    public V get(K key)
    {
        boolean found = false;
        MapEntry e = null;
        for (int i = 0; !found && i < map.size(); i++)
        {
            e = map.get(i);
            if (e.getKey().equals(key))
                found = true;
        }
        if (found)
        {
            return e.getValue();
        }
        else
        {
            return null;
        }
    }

    public void put(K key, V value)
    {
        if (!containsKey(key))
        {
            MapEntry entry = new MapEntry(key, value);
            map.add(entry);
        }
        else
        {
            boolean found = false;
            MapEntry e;
            for (int i = 0; !found && i < map.size(); i++)
            {
                e = map.get(i);
                if (e.getKey().equals(key))
                {
                    found = true;
                    e.setValue(value);
                }
            }
        }
    }

    private class MapEntry
    {
        private K key;
        private V value;

        public MapEntry(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        public K getKey()
        {
            return key;
        }

        public V getValue()
        {
            return value;
        }

        public void setKey(K key)
        {
            this.key = key;
        }
        
        public void setValue(V value)
        {
            this.value = value;
        }
    }
}

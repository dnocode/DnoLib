import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dino on 25/03/15.
 */
public class Keep{


    private HashMap<String, ArrayList<Object>> mObjectsHolder=new HashMap<>();

    private static Keep INSTANCE = new Keep();

    public static Keep alive() { return INSTANCE;  }


    public  boolean it(String key,Object obj) {

        ArrayList<Object> list= mObjectsHolder.containsKey(key)?mObjectsHolder.get(key):
                                mObjectsHolder.put(key,new ArrayList());
        return list.add(obj);

    }

    public  <E extends ArrayList> E read(String key) {

        return  mObjectsHolder.get(key)!=null?(E)mObjectsHolder.get(key):null;

    }

    public void unload(String ... keys){

        if(keys.length>0){
            for(String key:keys)mObjectsHolder.remove(key);
            return;
        }
        mObjectsHolder.clear();
    }

}

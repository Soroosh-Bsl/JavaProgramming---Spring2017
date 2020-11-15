import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lenovo on 28/05/2017.
 */

public class Hammam <T extends Comparable>{

    private ArrayList<T> items = new ArrayList<T>();
    private ArrayList<T> backups = new ArrayList<T>();
    private ArrayList<T> workWith = new ArrayList<T>();

    public void add(T item){
        if (item != null){
            if (items.size() != 0){
                items.add(item);
            }
            else{
                items.add(item);
                return;
            }
        }
    }

    public T getMin() throws IllegalStateException{
       if (items.size() > 0){
           T temp = items.get(0);
           for (int i = 0; i < items.size(); i++){
               if (items.get(i).compareTo(temp) < 0){
                   temp = items.get(i);
               }
           }
           backups.add(temp);
           items.remove(temp);
           return temp;
       }
       else {
           throw new IllegalStateException();
       }
    }

    public T getLast(boolean remove) throws IllegalStateException{
        if (items.size() > 0){
            if (remove){
                T temp = items.get(items.size() - 1);
                backups.add(temp);
                items.remove(temp);
                return temp;
            }
            return items.get(items.size() - 1);
        }
        else {
            throw new IllegalStateException();
        }
    }

    public T getFirst(boolean remove) throws IllegalStateException{
        if (items.size() > 0){
            if (remove){
                T temp = items.get(0);
                backups.add(temp);
                items.remove(temp);
                return temp;
            }
            return items.get(0);
        }
        else {
            throw new IllegalStateException();
        }
    }

    public Comparable[] getLess(T element, boolean remove){

//        Class classWanted = null;
//        try {
//            classWanted = Class.forName(element.getClass().getName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        if (items.size() > 0){
            workWith.clear();
            for (int i = 0; i < items.size(); i++){
                if (items.get(i).compareTo(element) < 0){
                    workWith.add(items.get(i));
                    if (remove){
                        backups.add(items.get(i));
                        items.remove(items.get(i));
                    }
                }
            }

            Comparable[] temp = null;
//            T[] temp = (T[]) Array.newInstance(classWanted, workWith.size());
            try{
                temp = new Comparable[workWith.size()];
                workWith.toArray(temp);
            }catch (Exception e){

            }
            return temp;
        }

        return null;
    }

    public Comparable[] getRecentlyRemoved(int n){
        if (backups.size() == 0){
            return null;
        }
        else{
//            Class classWanted = Class.forName(backups.get(0).getClass().getName());
//            T[] results = (T[]) Array.newInstance(classWanted, n);
            Comparable[] results;
            if (backups.size() >= n){
                results = new Comparable[n];
                for (int i = n - 1; i >= 0; i--){
                    results[i] = backups.get(i);
                }
            }
            else {
                results = new Comparable[backups.size()];
                for (int i = backups.size() - 1; i >= 0; i--){
                    results[i] = backups.get(i);
                }

            }
            return results;

        }
    }
}

import java.util.*;

public class AStar {

    public static Node[] map = new Node[42];
    public static List<Integer> closedList = new ArrayList();
    public static List<Integer> openList = new ArrayList();
    public static int matrixSize = 6;
    public static int endNode = 34;
    public static int startNode = 13;

    public static void main(String... args) {
        for (int i = 0; i < map.length; i++) {
            map[i] = new Node();
        }
        setLocked();

        start();
    }

    public static void setLocked() {
        map[15].locked = true;
        map[25].locked = true;
        map[27].locked = true;
        map[33].locked = true;
    }

    public static void start() {
        int count = 0;
        while (!endNodeFound(findSmallestFValue()) && count < 20) {
            printMap();
            checkNode(findSmallestFValue());
            count++;
        }      	
        System.out.println(openList);
        System.out.println(closedList);
        printMap();
    }

    public static boolean endNodeFound(int node) {
        List<Integer> tmpList = new ArrayList();
        tmpList.addAll(findCorners(node));
        tmpList.addAll(findOrthogonals(node));
        return tmpList.contains(endNode);
    }

    public static int findSmallestFValue() {

        if (openList.isEmpty()) {
            return startNode;
        }
        int min = openList.get(0);
        for (Integer i : openList) {
            if (closedList.contains(i))
                continue;
            if (map[i].f < map[min].f) {
                min = i;
            }
        }
        return min;
    }

    public static void checkNode(int node) {
        closedList.add(node);
        List<Integer> tmpList = new ArrayList();
        tmpList.addAll(findCorners(node));
        tmpList.addAll(findOrthogonals(node));
        for (Integer i : new ArrayList<Integer>(tmpList)) {

            if (map[i].locked) {
                tmpList.remove(i);
            }
            if (closedList.contains(i)) {
                continue;
            }
            int g = populateG(i, node);

            if (openList.contains(i)) {
                if (map[i].g > g) {
                    map[i].parent = node;
                }
            } else {
                map[i].parent = node;
                map[i].h = populateH(i);
                map[i].g = g;
                map[i].f = map[i].g + map[i].h;
            }
        }
        openList.addAll(tmpList);
    }

    public static int populateG(int i, int p) {
        if (p % matrixSize == i % matrixSize || p + 1 == i || p - 1 == i) {
            return map[p].g + 10;
        } else {
            return map[p].g + 14;
        }
    }

    public static int populateH(int i) {
        int r1 = (endNode) % matrixSize;
        int r2 = (i) % matrixSize;
        int a = r1 > r2 ? r1 - r2 : r2 - r1;

        int d1 = (endNode) / matrixSize;
        int d2 = (i) / matrixSize;
        int b = d1 > d2 ? d1 - d2 : d2 - d1;
        return a + b;
    }

    public static List<Integer> findCorners(int i) {
        List<Integer> list = new ArrayList();
        if (i - (matrixSize + 1) >= 0) {
            list.add(i - matrixSize + 1);
        }
        if (i - (matrixSize - 1) >= 0) {
            list.add(i - matrixSize - 1);
        }
        if (i + (matrixSize + 1) < map.length) {
            list.add(i + matrixSize + 1);
        }
        if (i + (matrixSize - 1) < map.length) {
            list.add(i + matrixSize - 1);
        }
        list.removeAll(openList);
        return list;
    }

    public static List<Integer> findOrthogonals(int i) {
        List<Integer> list = new ArrayList();
        if (i - (matrixSize) >= 0) {
            list.add(i - matrixSize);
        }
        if (i - 1 >= 0) {
            list.add(i - 1);
        }
        if (i + 1 < map.length) {
            list.add(i + 1);
        }
        if (i + (matrixSize) < map.length) {
            list.add(i + matrixSize);
        }
        list.removeAll(openList);
        return list;
    }

    public static void printFinalMap() {

    }
  
    public static void printMap() {
    	String l1  ="", l2 = "", l3 ="";
          
    	for (int i = 0; i < map.length; i++) {
        	
            String strI = String.format("%2s",i);
            String strH = String.format("%7s",map[i].h == 0?"": map[i].h);
            String strG = String.format("%7s",map[i].g == 0?"": map[i].g);
            String strF = String.format("%2s",map[i].f == 0?"": map[i].f);
          
            if (map[i].locked){
              	l1 += "║XXXXXXXXX║";
              	l2 += "║XXXXXXXXX║";
              	l3 += "║XXXXXXXXX║";
            }else{
            l1 += ("║" + strI + strH + "║");
            l3 += ("║" + strF + strG + "║");         
            
            if (i == startNode || i == endNode){
            	l2 += ("║    @    ║");
            }else if (closedList.contains(i)){
            	l2 += ("║    0    ║");
            }else 
                l2 += ("║         ║");
            }
          	if (i % (matrixSize) == matrixSize-1) {
               	printLines(l1, l2, l3);
				l1 = "";
                l2 = "";
                l3 = "";
            }
      	}
      	System.out.println("---------------------------------------------------------------------------");
         
    }
  
  	public static void printLines(String l1, String l2, String l3){
       	
        String top = "";
        String mid = "";
        String bot = "";
      	for(int i = 0 ; i < matrixSize; i++){
      		top  += "╔═════════╗";
          	mid  += "║         ║";
          	bot  += "╚═════════╝";
        }
        System.out.println(top);
    	System.out.println(l1);
      	System.out.println(mid);      
      	System.out.println(l2);
      	System.out.println(mid);
      	System.out.println(l3);
     	System.out.println(bot);
    }  
}

import java.util.*;

public class AStar {

    public static Node[] map = new Node[42];
    public static List<Integer> closedList = new ArrayList();
    public static List<Integer> openList = new ArrayList();
    public static int matrixSize = 6;
    public static int startNode = 0;
    public static int endNode = 34;

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
        map[21].locked = true;
        map[22].locked = true;
        map[27].locked = true;
        map[33].locked = true;
    }

    public static void start() {
        int count = 0;
      	int smallest = findSmallestFValue();
        while (!endNodeFound(smallest) && count < 200) {
            //printMap(false);
            checkNode(smallest);
            count++;
          	smallest = findSmallestFValue();
        }      	
        printMap(true);
    }

    public static boolean endNodeFound(int node) {
        List<Integer> tmpList = new ArrayList();
        tmpList.addAll(findCorners(node));
        tmpList.addAll(findOrthogonals(node));
      	if(tmpList.contains(endNode)){
        	map[endNode].parent = node;
          	return true;
        }
        return false;
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
      	openList.removeAll(List.of(node));
        List<Integer> tmpList = new ArrayList();
        tmpList.addAll(findCorners(node));
        tmpList.addAll(findOrthogonals(node));
      
        for (Integer i : new ArrayList<Integer>(tmpList)) {
 			if (map[i].locked) {
                tmpList.removeAll(List.of(i));
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
      	tmpList.removeAll(closedList);
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
        int r1 = (endNode+1) % matrixSize;
        int r2 = (i+1) % matrixSize;
        int a = r1 > r2 ? r1 - r2 : r2 - r1;

        int d1 = (endNode+1) / matrixSize;
        int d2 = (i+1) / matrixSize;
        int b = d1 > d2 ? d1 - d2 : d2 - d1;
        return a + b;
    }

    public static List<Integer> findCorners(int i) {
      	int l = (i/matrixSize)+1;
      	int c = (i%matrixSize)+1;
      
        List<Integer> list = new ArrayList();
        if (l > 1 && c > 1) {
            list.add((i - matrixSize) - 1);
        }
      	if (l > 1 && c < matrixSize) {
            list.add((i - matrixSize) + 1);
        }
      	if (l < map.length/matrixSize && c > 1) {
            list.add((i + matrixSize) - 1);
        }
      	if (l < map.length/matrixSize && c < matrixSize) {
            list.add((i + matrixSize) + 1);
        }
      	if(list.contains(-1)){
          System.out.println(i + " diagonal");
        }
        list.removeAll(openList);
        return list;
    }

    public static List<Integer> findOrthogonals(int i) {
      	int l = (i/matrixSize)+1;
      	int c = (i%matrixSize)+1;
      	
        List<Integer> list = new ArrayList();
        if (c > 1) {
            list.add(i - 1);
        }
       	if (c < matrixSize) {
            list.add(i + 1);
        }
        if (l > 1) {
            list.add(i - matrixSize);
        }
        if (l < map.length/matrixSize) {
            list.add(i + matrixSize);
        }
       
      	if(list.contains(-1)){
          System.out.println(i + " ortogonal");
        }
        list.removeAll(openList);
        return list;
    }
  
    public static void printMap(boolean finalMap) {
    	String l1  ="", l2 = "", l3 ="";
        List<Integer> list = new ArrayList();
        if(finalMap){        
          list.add(endNode);
          int a = endNode;
          int count = 0;
          while (!list.contains(startNode) && count <100){  
            System.out.print(a + " -> ");
            list.add(map[a].parent);
            a = map[a].parent;
            count++;
          }
          System.out.println();
        }
          
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
              if(finalMap){
                l1 += ("║         ║");
                l3 += ("║         ║");         

                if (list.contains(i)){
                    l2 += ("║    0    ║");            
                }else {
                    l2 += ("║         ║");
                }

              }else{
                l1 += ("║" + strI + strH + "║");
                l3 += ("║" + strF + strG + "║");         

                if (i == startNode || i == endNode){
                    l2 += ("║    @    ║");
                }else if (closedList.contains(i)){
                    l2 += ("║    0    ║");
                }else {
                    l2 += ("║         ║");
                }
                
              }
             
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

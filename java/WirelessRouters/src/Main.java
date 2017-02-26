import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public int N;
    public int M;
    public int[] s;
    public Room[] roomArray;
    public LinkedList<Integer> usedRoomList;
    static class Room{
    	int roomPoints = 0;
    	ArrayList<Integer> adjecentRooms = null;
    	int maxPoints = 0;
    	boolean setFlag = false;
    }
    public void generateroomMap(int[]doors) {
    	for(int i = 0; i < doors.length; i++) {
    		if(roomArray[doors[i] - 1].adjecentRooms == null) {
    			roomArray[doors[i] - 1].adjecentRooms = new ArrayList<Integer>();
    			roomArray[doors[i] - 1].adjecentRooms.add(doors[i]);
    			roomArray[doors[i] - 1].maxPoints = s[doors[i] - 1];
    		}   		
    		roomArray[doors[i] - 1].adjecentRooms.add(doors[doors.length - i - 1]);
    		roomArray[doors[i] - 1].maxPoints += s[doors[doors.length - i - 1] - 1];
    	}
    }
    
    public int getMaximumPoints(){
    	int MaxPoints = 0;
    	int i = 0;
    	while((M > 0) && (i < N)) {
    		MaxPoints += getMaxPointsRoom();
    		M--;
    		i++;
    	}
    	return MaxPoints;
    }
    
    public int getMaxPointsRoom() {
    	int maxIndex = -1;
    	for(int i = 0; i < roomArray.length; i++) {
    		if(!roomArray[i].setFlag) {
    			if((maxIndex == -1) || (roomArray[i].maxPoints > roomArray[maxIndex].maxPoints)) {
    				maxIndex = i;
    			}
    		}
    	}
    	if(roomArray[maxIndex].setFlag) {
    		return 0;
    	}
    	roomArray[maxIndex].setFlag = true;
    	for(int i = 0; i < roomArray[maxIndex].adjecentRooms.size(); i++) {
    		int adjecentRoom = roomArray[maxIndex].adjecentRooms.get(i);
    		if(roomArray[adjecentRoom - 1].setFlag) {
    			continue;
    		}
    		for(int j = 0; j < roomArray[adjecentRoom - 1].adjecentRooms.size(); j++) {
    			int adjecentRoom1 = roomArray[adjecentRoom - 1].adjecentRooms.get(j);
    			if(roomArray[adjecentRoom1 - 1].setFlag || (adjecentRoom1 == adjecentRoom) ) {
    				if(usedRoomList.contains(adjecentRoom1)) {
    					continue;
    				}
    				roomArray[adjecentRoom - 1].maxPoints -= s[adjecentRoom1 - 1];
    			}
    			else{
    				if(usedRoomList.contains(adjecentRoom)) {
    					continue;
    				}
    				roomArray[adjecentRoom1 - 1].maxPoints -= s[adjecentRoom - 1];
    			}
    		}
    		usedRoomList.add(adjecentRoom);
    	}
    	usedRoomList.add(maxIndex + 1);
    	
    	return roomArray[maxIndex].maxPoints;
    }
    
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Main wirelessRouters = new Main();
    	
    	Scanner scanner = new Scanner(System.in);
    	wirelessRouters.N = scanner.nextInt();
    	wirelessRouters.M = scanner.nextInt();
        wirelessRouters.s = new int[wirelessRouters.N];
        int i = 0;
        while (i < wirelessRouters.N) {
        	wirelessRouters.s[i] = scanner.nextInt();
        	i++;
        }
        wirelessRouters.roomArray = new Room[wirelessRouters.N];
        for(i = 0; i < wirelessRouters.N; i++) {
        	wirelessRouters.roomArray[i] = new Room();
        }
        wirelessRouters.usedRoomList = new LinkedList<Integer>();
        i = 0;
        while (i < wirelessRouters.N - 1) {
        	int[] inputArray = new int[2];
        	inputArray[0] = scanner.nextInt();
        	inputArray[1] = scanner.nextInt();
        	wirelessRouters.generateroomMap(inputArray);
        	i++;
        }
        System.out.println(wirelessRouters.getMaximumPoints());
    }
}
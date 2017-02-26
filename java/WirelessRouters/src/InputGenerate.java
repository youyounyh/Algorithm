import java.util.ArrayList;
import java.util.Random;

public class InputGenerate{
	static class Room{
		ArrayList<Integer> adjecentRooms = new ArrayList<Integer>();
		boolean checkFlag = false;
	}
	static Room[]rooms;
	static ArrayList<Integer> RoomNoDoor = new ArrayList<Integer>();
	public static void main(String[] args) {
       Random rand = new Random(System.currentTimeMillis());
//       int N = 2 + rand.nextInt(999);
       int N = 1000;
//       int M = 1 + rand.nextInt(100);
       int M = 100;
       System.out.println(N + " " + M);
       
       for(int i = 0; i < N; i++) {
    	   System.out.print((1 + rand.nextInt(10)) + " ");
       }
       System.out.println();
       
       rooms = new Room[N];
       for(int i = 0; i < N; i++) {
    	   rooms[i] = new Room();
    	   RoomNoDoor.add(i + 1);
       }
//       System.out.println("RoomNoDoor : " + RoomNoDoor); 
       checkDoor(1, rand);
//       for(int j = 0; j < N; j++) {
//		   System.out.println("room : " + (j + 1));
//   		   System.out.println(rooms[j].adjecentRooms);
//	   }
       
       for(int i = 0; i < N; i++) {
    	   ArrayList<Integer> adjecentRooms = rooms[i].adjecentRooms;
    	   for(int j = 0; j < adjecentRooms.size(); j++) {
    		   if(adjecentRooms.get(j) > i + 1) {
    			   System.out.println((i + 1) + " " + adjecentRooms.get(j));
    		   }
    	   }
       }
    }
	
	static void checkDoor(int roomId, Random rand) {
		if(rooms[roomId - 1].checkFlag || (RoomNoDoor.size() == 0)) {
			return;
		}
		rooms[roomId - 1].checkFlag = true;
//		System.out.println("roomId : " + roomId);
//		System.out.println("RoomNoDoor : " + RoomNoDoor); 
		ArrayList<Integer> adjecentRooms = rooms[roomId - 1].adjecentRooms;
		if(adjecentRooms.size() < 3) {
			if(RoomNoDoor.contains(roomId)) {
				RoomNoDoor.remove(RoomNoDoor.indexOf(roomId));
			}
			int generateSum = 0;
			if(RoomNoDoor.size() > 0) {
				generateSum = 1 + rand.nextInt(3 - adjecentRooms.size());
			}
//			System.out.println("generateSum : " + generateSum);
			for(int i = 0; i < generateSum; i++) {
				if(RoomNoDoor.size() == 0) {
					break;
				} 
				int index = rand.nextInt(RoomNoDoor.size());
				int door = RoomNoDoor.get(index);
				RoomNoDoor.remove(RoomNoDoor.indexOf(door));
				rooms[roomId - 1].adjecentRooms.add(door);
				rooms[door - 1].adjecentRooms.add(roomId);
			}
			for(int i = 0; i < adjecentRooms.size(); i++) {
				checkDoor(adjecentRooms.get(i), rand);
			}
		}
	}
}
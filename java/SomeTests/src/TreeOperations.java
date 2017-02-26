import java.util.ArrayList;
import java.util.Scanner;

public class TreeOperations {
	private static final int CMD_ADD = 1;
	private static final int CMD_MOVE = 2;
	private static final int CMD_INFECT = 3;
	private static final int CMD_RECOVER = 4;
	private static final int CMD_REMOVE = 5;
	
	private static Scanner sc;
	private static UserSolution userSolution = new UserSolution();
	
	private static int run() {
		int score = 0;
		int N = Integer.parseInt(sc.next());
		
		for(int i = 0; i < N; i++) {
			int cmd = Integer.parseInt(sc.next());
			int ret = 0;
			
			switch(cmd) {
				case CMD_ADD: {
					int id = Integer.parseInt(sc.next());
					int pid = Integer.parseInt(sc.next());
					int fileSize = Integer.parseInt(sc.next());
					ret = userSolution.add(id, pid, fileSize);
					break;
				}
				case CMD_MOVE: {
					int id = Integer.parseInt(sc.next());
					int pid = Integer.parseInt(sc.next());
					ret = userSolution.move(id, pid);
					break;
				}
				case CMD_INFECT: {
					int id = Integer.parseInt(sc.next());
					ret = userSolution.infect(id);
					break;
				}
				case CMD_RECOVER: {
					int id = Integer.parseInt(sc.next());
					ret = userSolution.recover(id);
					break;
				}
				case CMD_REMOVE: {
					int id = Integer.parseInt(sc.next());
					ret = userSolution.remove(id);
					break;
				}
			}
			
			int checkSum = Integer.parseInt(sc.next());
			if(ret == checkSum) {
				score++;
			}
		}
		return score;
	}
	
	public static void main(String arg[]) throws Exception {
		sc = new Scanner(System.in);
		
		int totalScore = 0;
		
		int T = sc.nextInt();
		for(int t = 1; t < T; t++) {
			userSolution.init();
			int score = run();
			System.out.println("#" + t + " " + score);
			totalScore += score;
		}
		sc.close();
		System.out.println("Total Score : " + totalScore);
	}
}

class UserSolution {
	private Node root = null;
	
	public UserSolution() {}
	
	public void init() {
		root = new Node();
		root.id = 10000;
	}
	
	public int add(int id, int pid, int fileSize) {
		resultNode = null;
		Node pNode = findNode(root, pid);
		if((pNode != null) && (!pNode.fileFlag)) {
			Node cNode = new Node();
			cNode.id = id;
			cNode.totalSize_original = fileSize;
			cNode.totalSize_current = fileSize;
			if(fileSize > 0) {
				cNode.fileFlag = true;
				cNode.totalFileNum = 1;
			}
			cNode.parentNode = pNode;
			pNode.childList.add(cNode);
			parentsStatusChange(pNode, fileSize, fileSize, cNode.totalFileNum);
			return getTotalSize(pNode);
		}
		return -1;
	}
	
	public int move(int id, int pid) {
		resultNode = null;
		Node cNode = findNode(root, id);
		resultNode = null;
		Node pNode = findNode(root, pid);
		if((cNode != null) && (pNode != null)) {
			cNode.parentNode.childList.remove(cNode);
			parentsStatusChange(cNode.parentNode, -cNode.totalSize_original, -cNode.totalSize_current, -cNode.totalFileNum);
			cNode.parentNode = pNode;
			pNode.childList.add(cNode);
			parentsStatusChange(pNode, cNode.totalSize_original, cNode.totalSize_current, cNode.totalFileNum);
			return getTotalSize(pNode);
		}
		return -1;
	}
	
	public int infect(int id) {
		resultNode = null;
		Node cNode = findNode(root, id);
		if(cNode != null) {
			if (root.totalFileNum > 0) {
				int diff = getTotalSize(root) / root.totalFileNum;
				iterateInfectNode(cNode, diff);
			}
			return getTotalSize(cNode);
		}
		return -1;
	}
	
	public int recover(int id) {
		resultNode = null;
		Node cNode = findNode(root, id);
		if(cNode != null) {
			if(cNode.infectedFlag) {
				iterateRecoverNode(cNode);
			}
			return getTotalSize(cNode);
		}
		return -1;
	}
	
	public int remove(int id) {
		resultNode = null;
		Node cNode = findNode(root, id);
		if(cNode != null) {
			int removeSize = getTotalSize(cNode);
			if(cNode.parentNode == null) {
				root.childList.clear();
				root.totalFileNum = 0;
				root.infectedFlag = false;
				root.totalSize_current = 0;
				root.totalSize_original = 0;
			}
			else {
				cNode.parentNode.childList.remove(cNode);
				parentsStatusChange(cNode.parentNode, -cNode.totalSize_original, -cNode.totalSize_current, -cNode.totalFileNum);
			}
			return removeSize;
		}
		return -1;
	}
	
	private Node resultNode = null;
	private Node findNode(Node node, int id) {
		if(node.id == id) {
			if(resultNode == null) {
				resultNode = node;
			}
			return resultNode;
		}
		else {
			for(int i = 0; i < node.childList.size(); i++) {
				if(findNode(node.childList.get(i), id) != null) {
					if(resultNode == null) {
						resultNode =  node.childList.get(i);
					}
					return resultNode;
				}
			}
		}
		return null;
	}
	
	private void iterateInfectNode(Node node, int diff) {
		if(node != null) {
			node.infectedFlag = true;
			if(node.fileFlag) {
				node.totalSize_current += diff;
				parentsStatusChange(node.parentNode, 0, diff, 0);
				return;
			}
			else if(node.totalFileNum == 0) {
				node.infectedFlag = false;
				return;
			}
			for(int i = 0; i < node.childList.size(); i++) {
				iterateInfectNode(node.childList.get(i), diff);
			}
		}
	}
	
	private void iterateRecoverNode(Node node) {
		if(node != null) {
			node.infectedFlag = false;
			if(node.fileFlag) {
				int diff = node.totalSize_original - node.totalSize_current;
				node.totalSize_current = node.totalSize_original;
				parentsStatusChange(node.parentNode, 0, diff, 0);
				return;
			}
			for(int i = 0; i < node.childList.size(); i++) {
				iterateRecoverNode(node.childList.get(i));
			}
		}
	}
	
	private void parentsStatusChange(Node node, int sizeOriginalDiff, int sizeCurrentDiff, int fileNumDiff) {
		if(node != null) {
			node.totalSize_current += sizeCurrentDiff;
			node.totalSize_original += sizeOriginalDiff;
			if(node.totalSize_current != node.totalSize_original) {
				node.infectedFlag = true;
			}
			node.totalFileNum += fileNumDiff;
		
			parentsStatusChange(node.parentNode, sizeOriginalDiff, sizeCurrentDiff, fileNumDiff);
		}
	}
	
	private int getTotalSize(Node node) {
		return node.totalSize_current;
	}
	
	class Node {
		private int id = -1;
		private Node parentNode = null;
		private boolean fileFlag = false;
		private boolean infectedFlag = false;
		private int totalSize_original = 0;
		private int totalSize_current = 0;
		private ArrayList<Node>childList = new ArrayList<Node>();
		private int totalFileNum = 0;
	}
}
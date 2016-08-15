import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.PriorityQueue;

public class Ignatius {
	private static class Node implements Comparable<Node>{
		private int x;
		private int y;
		private int timeConsume;
		private int timeBefore;
		private List<Node> conNode = new ArrayList<>();

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getTimeConsume() {
			return timeConsume;
		}

		public int getTimeBefore() {
			return timeBefore;
		}

		public void setTimeBefore(int timeBefore) {
			this.timeBefore = timeBefore;
		}

		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		
		public Node(int x, int y, int timeConsume) {
			super();
			this.x = x;
			this.y = y;
			this.timeConsume = timeConsume;
			this.timeBefore = Integer.MAX_VALUE;
		}
		
		public void addNode(Node node){
			conNode.add(node);
		}

		@Override
		public int compareTo(Node o) {
			if (this.timeBefore < o.getTimeBefore()){
				return -1;
			}
			else if (this.timeBefore > o.getTimeBefore()){
				return 1;
			}
			else{
				if (this.x < o.getX()){
					return -1;
				}
				else if (this.x > o.getX()){
					return 1;
				}
				else{
					if (this.y < o.getY()){
						return -1;
					}
					else if (this.y > o.getY()){
						return 1;
					}
					else{
						return 0;
					}
				}
			}
		}

	}
	
	public static void main(String[] args) throws IOException {
		long start =System.currentTimeMillis();
		long end;
		if (args.length != 1){
			System.out.println("wrong arguments");
			return;
		}
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(args[0]));
			String lineNum;
			while((lineNum = br.readLine()) != null){
				String[] size = lineNum.split("\\s+");
				int row = Integer.parseInt(size[0]);
				int col = Integer.parseInt(size[1]);
				Node[][] nodeArray = new Node[row][col];
				int[][] timeArray = new int[row][col];
				HashMap<Node, List<Node>> pathMap = new HashMap<>();
				Set<Node> nodeArrived = new HashSet<Node>();
				TreeSet<Node> nodeArriving = new TreeSet<Node>();
				Set<Node> nodeNoArrive = new HashSet<Node>();
				String line;
				for(int i = 0; i < row; ++i){
					if((line = br.readLine()) == null)
						return;
					for(int j = 0; j < col; ++j){
						if(line.charAt(j) == '.'){
							nodeArray[i][j] = new Node(i, j, 0);
						}
						else if (line.charAt(j) == 'X') {
							nodeArray[i][j] = null;
						}
						else if (line.charAt(j) >= '0' && line.charAt(j) <= '9'){
							nodeArray[i][j] = new Node(i, j, line.charAt(j) - '0');
						}
						else{
							return;
						}
						if(nodeArray[i][j] == null)
							continue;
						nodeNoArrive.add(nodeArray[i][j]);
						timeArray[i][j] = Integer.MAX_VALUE;
						if(i > 0){
							if(nodeArray[i - 1][j] != null){
								nodeArray[i][j].addNode(nodeArray[i - 1][j]);
								nodeArray[i - 1][j].addNode(nodeArray[i][j]);
							}
						}
						if(j > 0){
							if(nodeArray[i][j - 1] != null){
								nodeArray[i][j].addNode(nodeArray[i][j - 1]);
								nodeArray[i][j - 1].addNode(nodeArray[i][j]);
							}
						}
					}
				}
				Node nextNode = nodeArray[0][0];
				nextNode.setTimeBefore(0);
				timeArray[0][0] = 0;
				nodeArriving.add(nextNode);
				nodeNoArrive.remove(nextNode);
				List<Node> tempPath = new ArrayList<>();
				tempPath.add(nextNode);
				pathMap.put(nextNode, tempPath);
				while(!nodeArriving.isEmpty()){
					nodeArriving.remove(nextNode);
					nodeArrived.add(nextNode);
					for(Node node: nextNode.conNode){
						if (nodeNoArrive.contains(node)){
							nodeNoArrive.remove(node);
							nodeArriving.add(node);
						}
						if (node.getTimeConsume() + nextNode.getTimeBefore() < node.getTimeBefore()){
							if(nodeArriving.contains(node)){
								nodeArriving.remove(node);
							}
							node.setTimeBefore(node.getTimeConsume() + nextNode.getTimeBefore());
							nodeArriving.add(node);
							List<Node> newPath = new ArrayList<Node>();
							newPath.addAll(tempPath);
							newPath.add(node);
							pathMap.put(node, newPath);
						}
					}
					if (nodeArriving.isEmpty())
						continue;
					nextNode = nodeArriving.first();
					tempPath = pathMap.get(nextNode);
				}

				if(nodeArray[row - 1][col - 1].getTimeBefore() == Integer.MAX_VALUE){
					System.out.println("God please help our poor hero.");
				}
				else{
					System.out.println("It takes " + String.valueOf(nodeArray[row - 1][col - 1].getTimeBefore()) +" seconds to reach the target position, let me show you the way.");
					printPath(pathMap.get(nodeArray[row - 1][col - 1]));
					System.out.println("Finish");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(br != null)
			br.close();
		}
		end =System.currentTimeMillis();
		System.out.println(end - start);
	}
	
	public static void printPath(List<Node> tempPath){
		for(Node n: tempPath){
			System.out.print('(' + String.valueOf(n.getX()) + ',' + String.valueOf(n.getY()) + ')');
		}
		System.out.println();
	}
}

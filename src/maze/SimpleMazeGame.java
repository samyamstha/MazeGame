/*
 * SimpleMazeGame.java
 * Copyright (c) 2008, Drexel University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Drexel University nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DREXEL UNIVERSITY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DREXEL UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package maze;

import maze.ui.MazeViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * 
 * @author Sunny
 * @version 1.0
 * @since 1.0
 */
public class SimpleMazeGame
{
	/**
	 * Creates a small maze.
	 */
	public static Maze createMaze() throws FileNotFoundException {
		
		Maze maze = new Maze();


			Room room0 = new Room(0);
			Room room1 = new Room(1);
			Door door0 = new Door(room0, room1);
			maze.addRoom(room0);
			maze.addRoom(room1);
			room0.setSide(Direction.North, new Wall());
			room0.setSide(Direction.East, door0);
			room0.setSide(Direction.South, new Wall());
			room0.setSide(Direction.West, new Wall());
			room1.setSide(Direction.North, new Wall());
			room1.setSide(Direction.East, new Wall());
			room1.setSide(Direction.South, new Wall());
			room1.setSide(Direction.West, door0);

			maze.setCurrentRoom(0);


		//System.out.println("The maze does not have any rooms yet!");
		return maze;
		

	}

	public static Maze loadMaze(final String path) throws FileNotFoundException {
		Maze maze = new Maze();

		HashMap<String, String> roomMap = null;
		HashMap<String, String> doorMap = null;
		System.out.println("Please load a maze from the file!");

		ArrayList<HashMap> roomList = new ArrayList<>();
		ArrayList<HashMap> doorList = new ArrayList<>();
		File file = new File(path);
		Scanner sc = new Scanner(file);
		ArrayList<String> mazeInfo = new ArrayList<>();
		ArrayList<Room> roomObjs = new ArrayList<>();
		ArrayList<Door> doorObjs = new ArrayList<>();

		while (sc.hasNextLine()){
			mazeInfo.add(sc.nextLine());
		}

		/*for (int i = 0; i < mazeInfo.size(); i++){
			System.out.println("Printing from mazeInfo");
			System.out.println("Index :" + i + " value: " + mazeInfo.get(i));
		}*/

		for (String info: mazeInfo) {

			String [] element = info.split(" ");

			if(element[0].equals("room")){
				roomMap = new HashMap<>();
				roomMap.put("number", element[1]);
				roomMap.put("north", element[2]);
				roomMap.put("south", element[3]);
				roomMap.put("east", element[4]);
				roomMap.put("west", element[5]);
				roomList.add(roomMap);
			}else{
				doorMap = new HashMap<>();
				doorMap.put("name", element[1]);
				doorMap.put("room1", element[2]);
				doorMap.put("room2", element[3]);
				doorMap.put("openOrClose", element[4]);
				doorList.add(doorMap);
			}
		}


		System.out.println();

		for (HashMap<String,String> rmap : roomList) {
			System.out.println(rmap.get("number"));
			int roomNum = Integer.parseInt(rmap.get("number"));
			Room room = new Room(roomNum);
			roomObjs.add(room);
			maze.addRoom(room);
		}

		for (HashMap<String,String> dmap : doorList) {
			//System.out.println(dmap.get("name"));
			String doorName = dmap.get("name");
			int room1 = Integer.parseInt(dmap.get("room1"));
			int room2 = Integer.parseInt(dmap.get("room2"));

			Door door = new Door(roomObjs.get(room1), roomObjs.get(room2));
			if (dmap.get("openOrClose").equals("close")){
				door.setOpen(false);
			}else{
				door.setOpen(true);
			}
			doorObjs.add(door);

		}

		System.out.println("is wall? : " +roomList.get(0).get("south").toString() );
		for(int i = 0; i < roomObjs.size(); i++){

				switch (roomList.get(i).get("north").toString()) {

					case "wall":
						roomObjs.get(i).setSide(Direction.North, new Wall());
						System.out.println("Set New Wall from first switch ");
						break;
					case "d0":
						roomObjs.get(i).setSide(Direction.North, doorObjs.get(0));
						System.out.println("Set New d0 from first switch");

						break;
					case "d1":
						roomObjs.get(i).setSide(Direction.North, doorObjs.get(1));
						System.out.println("Set New d1 from first switch");
						break;
					default:
						System.out.println("Default for north");
				}

				switch (roomList.get(i).get("south").toString()) {

					case "wall":
						roomObjs.get(i).setSide(Direction.South, new Wall());
						System.out.println("Set New Wall from second switch");

						break;
					case "d0":
						roomObjs.get(i).setSide(Direction.South, doorObjs.get(0));
						System.out.println("Set New d0  from second switch");

						break;
					case "d1":
						roomObjs.get(i).setSide(Direction.South, doorObjs.get(1));
						System.out.println("Set New d1 from second switch");

						break;
					default:
						System.out.println("Default for south");
				}
				switch (roomList.get(i).get("east").toString()) {

					case "wall":
						roomObjs.get(i).setSide(Direction.East, new Wall());
						System.out.println("Set New Wall  from third switch");

						break;
					case "d0":
						roomObjs.get(i).setSide(Direction.East, doorObjs.get(0));
						System.out.println("Set New d0  from third switch");

						break;
					case "d1":
						roomObjs.get(i).setSide(Direction.East, doorObjs.get(1));
						System.out.println("Set New d1 from third switch");
						break;
					default:
						System.out.println("Default for east");
				}

				switch (roomList.get(i).get("west").toString()) {
					case "wall":
						roomObjs.get(i).setSide(Direction.West, new Wall());
						System.out.println("Set New Wall  from fourth switch");
						break;
					case "d0":
						roomObjs.get(i).setSide(Direction.West, doorObjs.get(0));
						System.out.println("Set New d0 from fourth switch ");

						break;
					case "d1":
						roomObjs.get(i).setSide(Direction.West, doorObjs.get(1));
						System.out.println("Set New d1 from fourth switch");
						break;
					default:
						System.out.println("Default for west");
				}
			}

		System.out.println("\n");
		for (Room ro: roomObjs) {
			System.out.println(ro.getNumber());
			System.out.println();
			System.out.println(ro.getSide(Direction.North));
			System.out.println(ro.getSide(Direction.South));
			System.out.println(ro.getSide(Direction.East));
			System.out.println(ro.getSide(Direction.West));

		};



		maze.setCurrentRoom(0);



/*
		for (int i = 0; i < roomList.size(); i++){
			System.out.println("Index :" + i + " value: " + roomList.get(i));
		}*/
/*
		for (String key: roomMap.keySet()) {
			System.out.println("key is "+ key + "  value is " + roomMap.get(key));

		}*/

/*
		System.out.println();
		for (int i = 0; i < doorList.size(); i++){
			System.out.println("Index :" + i + " value: " + doorList.get(i));
		}*/

		return maze;
	}


	public static void main(String[] args) throws FileNotFoundException {

		Maze maze = null;
	    String filePath;
	    if (args.length == 1 ){
	    	filePath = args[0];
			System.out.println("The argument passed is " + filePath);
			maze = loadMaze(filePath);
		}else{
	    	maze = createMaze();
		}

		MazeViewer viewer = new MazeViewer(maze);
	    viewer.run();
	}
}

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Sunny
 * @version 1.0
 * @since 1.0
 */
public class SimpleMazeGame {
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

        System.out.println("The maze does not have any rooms yet!");
        return maze;


    }

    public static Maze loadMaze(final String path) throws FileNotFoundException {
        Maze maze = new Maze();


        HashMap<String, String> roomMap = null;
        HashMap<String, String> doorMap = null;
        System.out.println("Please load a maze from the file!");

        ArrayList<HashMap> roomList = new ArrayList<>();
        ArrayList<HashMap> doorList = new ArrayList<>();

        //open the file and start reading
        File file = new File(path);
        Scanner sc = new Scanner(file);


        ArrayList<String> mazeInfo = new ArrayList<>(); //ArrayList to add the lines of the file
        ArrayList<Room> roomObjs = new ArrayList<>();  //ArrayList to add the Room objects
        ArrayList<Door> doorObjs = new ArrayList<>();   //ArrayList to add the Door objects

        //Run till the file has line in it
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            //Only add the line if it is not blank
            if (line.length() > 0){
                mazeInfo.add(line);
            }

        }

        //Split the line and add the attributes of room and doors to respective maps
        for (String info : mazeInfo) {

            String[] element = info.split(" ");

            if (element[0].equals("room")) {
                roomMap = new HashMap<>();
                roomMap.put("number", element[1]);
                roomMap.put("north", element[2]);
                roomMap.put("south", element[3]);
                roomMap.put("east", element[4]);
                roomMap.put("west", element[5]);
                roomList.add(roomMap);
            } else {
                doorMap = new HashMap<>();
                doorMap.put("name", element[1]);
                doorMap.put("room1", element[2]);
                doorMap.put("room2", element[3]);
                doorMap.put("openOrClose", element[4]);
                doorList.add(doorMap);
            }
        }


        System.out.println();

        //Instantiate Room objects and add them to the maze, and also to the ArrayList of the Room objects
        for (HashMap<String, String> rmap : roomList) {
            //  System.out.println(rmap.get("number"));
            int roomNum = Integer.parseInt(rmap.get("number"));
            Room room = new Room(roomNum);
            roomObjs.add(room);
            maze.addRoom(room);
        }

        //Instantiate Door objects and add them to the ArrayList of Door objects
        for (HashMap<String, String> dmap : doorList) {
            String doorName = dmap.get("name");
            int room1 = Integer.parseInt(dmap.get("room1"));
            int room2 = Integer.parseInt(dmap.get("room2"));

            Door door = new Door(roomObjs.get(room1), roomObjs.get(room2));
            if (dmap.get("openOrClose").equals("close")) {
                door.setOpen(false);
            } else {
                door.setOpen(true);
            }
            doorObjs.add(door);

        }

        //Iterate through the rooms and add set the sides
        for (int i = 0; i < roomObjs.size(); i++) {

            SimpleMazeGame.decideCase(roomList, i, doorObjs, roomObjs, "north", Direction.North);
            SimpleMazeGame.decideCase(roomList, i, doorObjs, roomObjs, "south", Direction.South);
            SimpleMazeGame.decideCase(roomList, i, doorObjs, roomObjs, "east", Direction.East);
            SimpleMazeGame.decideCase(roomList, i, doorObjs, roomObjs, "west", Direction.West);
        }

        maze.setCurrentRoom(0);

        return maze;
    }

    //Function that decides what to set on the sides of the rooms
    public static void decideCase(ArrayList<HashMap> roomList, int i, ArrayList<Door> doorObjs, ArrayList<Room> roomObjs, String direction, Direction dir) {

        String lookUp = roomList.get(i).get(direction).toString();

        //Add wall
        if (lookUp.equals("wall")){
            roomObjs.get(i).setSide(dir, new Wall());

            //Add respective door
        }else if(lookUp.charAt(0) == 'd'){
            int doorNum = (Character.getNumericValue(lookUp.charAt(1)));
            roomObjs.get(i).setSide(dir, doorObjs.get(doorNum));

            //Add respective room
        }else {
            int roomNum = Integer.parseInt(lookUp);
            roomObjs.get(i).setSide(dir, roomObjs.get(roomNum));

        }

    }


    public static void main(String[] args) throws FileNotFoundException {

        Maze maze = null;
        String filePath;
        //check if the argument has been passed to the program
        //call loadMaze() if argument that is passed is correct to the maze type
        //else call createMaze()
        if (args.length == 1) {
            filePath = args[0];
            System.out.println("The argument passed is " + filePath);
            try {
                maze = loadMaze(filePath);
            } catch (FileNotFoundException e) {
                System.out.println("Invalid File");
                System.out.println("Exiting the program");
                System.exit(0);
            }
        } else {
            maze = createMaze();
        }

        MazeViewer viewer = new MazeViewer(maze);
        viewer.run();
    }
}

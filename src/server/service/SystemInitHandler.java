package server.service;

import server.dataAccess.Controller;
import server.domain.models.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class SystemInitHandler {

        public void initSystem(String fileName){
            File initFile = new File(fileName);
            String s = initFile.getAbsolutePath();
            try{
                Scanner scn = new Scanner(initFile);
                while(scn.hasNextLine()){
                    executeCommand(scn.nextLine());
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

        private void executeCommand(String command) throws ParseException {
            String[] splitedCommand =  command.split(", ");
            if (splitedCommand[0].equals("addStudent")) { //[String studentID, String name, int points]
                Student s = new Student(splitedCommand[1], splitedCommand[2], Integer.valueOf(splitedCommand[3]));
                Controller.create(s);
            }
            else if (splitedCommand[0].equals("addSlot")) {//[String courseID, int groupID, int slotID, int day, int hour, int duration, int capacity]
                Slot s = new Slot(splitedCommand[1], Integer.valueOf(splitedCommand[2]), Integer.valueOf(splitedCommand[3]), Integer.valueOf(splitedCommand[4]), Integer.valueOf(splitedCommand[5]), Integer.valueOf(splitedCommand[6]), Integer.valueOf(splitedCommand[7]));
                Controller.create(s);
            }
            else if (splitedCommand[0].equals("addSlotDate")) {//[int slotID, Date date, int slotDateID]
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SlotDates sd = new SlotDates(Integer.valueOf(splitedCommand[1]), df.parse(splitedCommand[2]), Integer.valueOf(splitedCommand[3]));
                Controller.create(sd);
            }
            else if (splitedCommand[0].equals("addBid")){//[int slotID, String studentID, int percentage]
                Bid b = new Bid(Integer.valueOf(splitedCommand[1]), splitedCommand[2], Integer.valueOf(splitedCommand[3]));
                Controller.create(b);
            }
            else if (splitedCommand[0].equals("addLecture")){//[String studentID, int slotID]
                Lecture s = new Lecture(splitedCommand[1], Integer.valueOf(splitedCommand[2]));
                Controller.create(s);
            }
            else if (splitedCommand[0].equals("addAssignings")){//[int slotDateID, String studentID, Date date]
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Assignings a = new Assignings(Integer.valueOf(splitedCommand[1]), Controller.getStudentByID(splitedCommand[2]),df.parse(splitedCommand[2]));
                Controller.create(a);
            }
            else{
                System.out.println("Illegal command: "+splitedCommand[0]);
                throw new IllegalArgumentException();
            }
        }
    }



/** @author Muhammet Fatih Ulu
 *  @brief A Java program for simulating discrete events.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.PriorityQueue;
import java.math.BigDecimal;

// Create person class

class Person {
	public int id;
	public double skill;
	public int massageCount;
	public boolean activeState;
	public ArrayList<Double> waitingTimeTrainingQu = new ArrayList<Double>();
	public ArrayList<Double> waitingTimeTherapyQu = new ArrayList<Double>();
	public ArrayList<Double> waitingTimeMassageQu = new ArrayList<Double>();
	public ArrayList<Double> trainingTime = new ArrayList<Double>();
	public ArrayList<Double> therapyTime = new ArrayList<Double>();
	public ArrayList<Double> massageTime = new ArrayList<Double>();
	
		
	public Person(int id, double skill, int massageCount, boolean activeState) {
		this.id = id;
		this.skill = skill;
		this.massageCount = massageCount;
		this.activeState = activeState;
	}
}

// Create physiotherapist's class

class Physiotherapist {
	public int id;
	public BigDecimal serviceTime;
	public BigDecimal time;
	public boolean available;
	
	public Physiotherapist(int id, BigDecimal serviceTime, BigDecimal time, boolean available) {
		this.id = id;
		this.serviceTime = serviceTime;
		this.time = time;
		this.available = available;
	}	
}

// Create trainer's class

class Trainer {
	public int id;
	public BigDecimal time;
	public boolean available;
	
	public Trainer(int id, BigDecimal time, boolean available){
		this.id = id;
		this.time = time;
		this.available = available;
	}
}

// Create masseur's class

class Masseur {
	public int id;
	public BigDecimal time;;
	public boolean available;;
	
	public Masseur(int id, BigDecimal time, boolean available){
		this.id = id;
		this.time = time;
		this.available = available;
	}
}


// Create activity class (for queue) and its comparator

class ActivityLog {
	public String arrivalType;
	public int id;
	public BigDecimal requestTime;
	public BigDecimal duration;

	public ActivityLog(String arrivalType, int id, BigDecimal requestTime, BigDecimal duration){
		this.arrivalType = arrivalType;
		this.id = id;
		this.requestTime = requestTime;
		this.duration = duration;
	}
	
	public String toString() {
		return this.arrivalType + " " + this.id + " " + this.requestTime + " " + this.duration;
	}
}

class ActivityLogComparator implements Comparator<ActivityLog> {
	public int compare(ActivityLog act1, ActivityLog act2) {
		if (act1.requestTime.equals(act2.requestTime)) {
			BigDecimal outIndicator = new BigDecimal(-1.2345);
			if (act1.duration.equals(outIndicator) && act2.duration.equals(outIndicator))
				return act1.id > act2.id ? 1 : -1;
			else if (act1.duration.equals(outIndicator) || act2.duration.equals(outIndicator))
				return act1.duration.compareTo(act2.duration) > 0 ? 1 : -1;
			else
			    return act1.id > act2.id ? 1 : -1;
		}
		else
			return act1.requestTime.compareTo(act2.requestTime) > 0 ? 1 : -1;
}
}

// Create training class (for queue) and its comparator

class Training {
	public int id;
	public BigDecimal requestTime;
	public BigDecimal duration;

	public Training(int id, BigDecimal requestTime, BigDecimal duration){
		this.id = id;
		this.requestTime = requestTime;
		this.duration = duration;
	}

	public String toString() {
		return this.id + " " + this.requestTime + " " + this.duration;
	}
}

class TrainingComparator implements Comparator<Training> { 
	public int compare(Training trainee1, Training trainee2) {
		if (trainee1.requestTime.equals(trainee2.requestTime))
			return trainee1.id > trainee2.id ? 1 : -1;
		else
			return trainee1.requestTime.compareTo(trainee2.requestTime) > 0 ? 1 : -1;
	}
}

// Create physiotherapy class (for queue) and its comparator

class Physiotherapy {
	public int id;
	public BigDecimal requestTime;
	public BigDecimal trainingDuration;

	public Physiotherapy(int id, BigDecimal requestTime, BigDecimal trainingDuration){
		this.id = id;
		this.requestTime = requestTime;
		this.trainingDuration = trainingDuration;
	}
	
	public String toString() {
		return this.id + " " + this.requestTime + " " + this.trainingDuration;
	}
}

class PhysiotherapyComparator implements Comparator<Physiotherapy> {
	public int compare(Physiotherapy p1, Physiotherapy p2) {
		if (p1.trainingDuration.equals(p2.trainingDuration))
			if (p1.requestTime.equals(p2.requestTime))
				return p1.id > p2.id ? 1 : -1;
			else
				return p1.requestTime.compareTo(p2.requestTime) > 0 ? 1 : -1;
		else
			return p1.trainingDuration.compareTo(p2.trainingDuration) < 0 ? 1 : -1;
		}
}


// Create massage class (for queue) and its comparator

class Massage {
	public int id;
	public BigDecimal requestedTime;
	public BigDecimal duration;
	public double skill;

	public Massage(int id, BigDecimal requestedTime, BigDecimal duration, double skill){
		this.id = id;
		this.requestedTime = requestedTime;
		this.duration = duration;
		this.skill = skill;
	}
	
	public String toString() {
		return this.id + " " + this.requestedTime + " " + this.duration + " " + this.skill;
	}
}

class MassageComparator implements Comparator<Massage> {
	public int compare(Massage p1, Massage p2) {
			if (p1.skill == p2.skill)	
				if (p1.requestedTime.equals(p2.requestedTime))
					return p1.id > p2.id ? 1 : -1;
				else
					return p1.requestedTime.compareTo(p2.requestedTime) > 0 ? 1 : -1;	
			else
				return p1.skill < p2.skill ? 1 : -1;
		}
}


public class project2main {
	public static void main(String[] args) throws IOException {

		// Read all lines in input
		
		ArrayList<String> allLines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line = reader.readLine();
			while (line != null) {
				allLines.add(line);
				line = reader.readLine();
			}				
			reader.close();
		} catch (FileNotFoundException err) {
			System.out.println("An error occured while reading the file.");
			err.printStackTrace();
		}

		// Initialization part
		
		PriorityQueue<ActivityLog> actsQueued = new PriorityQueue<ActivityLog>(1, new ActivityLogComparator()); // for activities
		PriorityQueue<Training> traineeQueue = new PriorityQueue<Training>(1, new TrainingComparator());
		PriorityQueue<Massage> massagedQueue = new PriorityQueue<Massage>(1, new MassageComparator());
		PriorityQueue<Physiotherapy> therapyQueue = new PriorityQueue<Physiotherapy>(1, new PhysiotherapyComparator());
		
		// In case 4th massage is requested
		int invalidAttempt = 0;
		// In case requests are made when in active state
		int canceledAttempt = 0;
		
		// Initialize max length of queues
		int maxLengthTraineeQu = 0;
		int maxLengthTherapyQu = 0;
		int maxLengthMassagedQu = 0;
		
		// Time calculations are made by using BigDecimal class for more accuracy
		BigDecimal timeGlobal = new BigDecimal(0.0); // instantiation of global time 
		BigDecimal timeDifference = new BigDecimal(0.0); // find time difference between activities
		BigDecimal outIndicator = new BigDecimal(-1.2345); // this is used for leaving acts to check if sb is in queue
		BigDecimal partialTime = new BigDecimal(10E-9); // due to inconsistencies of double values in calculations
		BigDecimal zeroB = new BigDecimal(0.0); // double 0.0 time in BigDecimal class is needed
				
		
		// Create array of objects
		
		// Store info about people
		int peopleNum = Integer.parseInt(allLines.get(0)); // get how many people are there from the first line
		Person[] people = new Person[peopleNum]; // create an array of Person objects with the length of people
		for (int i = 0; i < peopleNum; i++) {
			String[] wordsArr = allLines.get(i + 1).split(" ");
			int personID = Integer.parseInt(wordsArr[0]);
			double skill = Double.parseDouble(wordsArr[1]);
			people[i] = new Person(personID, skill, 0, false);
		}
		
		// Store info about activities
		int activityNum = Integer.parseInt(allLines.get(peopleNum + 1)); // get how many activities are there
		for (int i = 0; i < activityNum; i++) { // create an activity object and add to queue and and loop 
			String[] wordsArr = allLines.get(peopleNum + i + 2).split(" ");
			String arrivalType = wordsArr[0];
			int id = Integer.parseInt(wordsArr[1]);
			BigDecimal requestTime = new BigDecimal(Double.parseDouble(wordsArr[2]));
			BigDecimal requestedDur = new BigDecimal(Double.parseDouble(wordsArr[3]));
			ActivityLog activity = new ActivityLog(arrivalType, id, requestTime, requestedDur);
			actsQueued.add(activity);			
		}
		
		// Store info about physiotherapists and their therapy time
		String[] wordsArr = allLines.get(peopleNum + activityNum + 2).split(" "); // obtain line regarding physiotherapists
		int physiotherapistNum = Integer.parseInt(wordsArr[0]); // obtain physiotherapis number 
		Physiotherapist[] physiotherapists = new Physiotherapist[physiotherapistNum]; // create array of physioth. objects
		for (int i = 0; i < physiotherapistNum; i++) { // create physiotherapist object and loop
			BigDecimal serviceTime = new BigDecimal(Double.parseDouble(wordsArr[i+1])); 
			physiotherapists[i] = new Physiotherapist(i, serviceTime, zeroB, true);
		}
				
		
		// Store info about number of trainers and masseurs
		String[] wordsArr2 = allLines.get(peopleNum + activityNum + 3).split(" "); // get line about trainers and masseurs
		int trainerNum = Integer.parseInt(wordsArr2[0]);
		int masseurNum = Integer.parseInt(wordsArr2[1]);
		Trainer[] trainers = new Trainer[trainerNum]; // create trainer objects
		Masseur[] masseurs = new Masseur[masseurNum]; // create masseur objects
		for (int i = 0; i < trainerNum; i++) { // to store info about time for trainers
			trainers[i] = new Trainer(i, zeroB, true);
		}
		for (int i = 0; i < masseurNum; i++) { // to store info about time for masseurs
			masseurs[i] = new Masseur(i, zeroB, true);
		}
			
		while (!actsQueued.isEmpty()) {
			
			// Check for greatest lengths of queues in each activity
			maxLengthTraineeQu = (traineeQueue.size() > maxLengthTraineeQu) ? traineeQueue.size() : maxLengthTraineeQu;
			maxLengthTherapyQu = (therapyQueue.size() > maxLengthTherapyQu) ? therapyQueue.size() : maxLengthTherapyQu;
			maxLengthMassagedQu = (massagedQueue.size() > maxLengthMassagedQu) ? massagedQueue.size() : maxLengthMassagedQu;
			
			// Stringify the head of queue
			String polled = actsQueued.poll().toString();
			String[] wordsArr3 = polled.split(" ");
			
			// Read the data of head of activities queue
			String arrivalType = wordsArr3[0]; 
			int id = Integer.parseInt(wordsArr3[1]);
			double requestTime = Double.parseDouble(wordsArr3[2]);
			double requestedDur = Double.parseDouble(wordsArr3[3]);
			BigDecimal requestTimeB = new BigDecimal(requestTime); 
			BigDecimal requestedDurB = new BigDecimal(requestedDur);
		
			timeDifference = requestTimeB.subtract(timeGlobal); // find time difference between two activities
			timeGlobal = requestTimeB; // update global time to time that last event occurred
			
			// Loop # of trainers time to find any of them is available and update time for working trainers
			for (int i = 0; i < trainerNum; i++) {  
				// partial time is added due to inconsistencies of double values while calculations
				if (trainers[i].time.compareTo(timeDifference.add(partialTime)) <= 0) {
					trainers[i].time = zeroB;
					trainers[i].available = true;
				}
				else {
					trainers[i].time = trainers[i].time.subtract(timeDifference);
					trainers[i].available = false;
				}
			}
			
			// Same goes for masseurs
			for (int i = 0; i < masseurNum; i++) { 
				if (masseurs[i].time.compareTo(timeDifference.add(partialTime)) <= 0) {
					masseurs[i].time = zeroB;
					masseurs[i].available = true;
				}
				else {
					masseurs[i].time = masseurs[i].time.subtract(timeDifference);
					masseurs[i].available = false;
				}
			}
			
			// Same goes for physiotherapists
			for (int i = 0; i < physiotherapistNum; i++) {
				if (physiotherapists[i].time.compareTo(timeDifference.add(partialTime)) <= 0) {
					physiotherapists[i].time = zeroB;
					physiotherapists[i].available = true;
				}
				else {
					physiotherapists[i].time = physiotherapists[i].time.subtract(timeDifference);
					physiotherapists[i].available = false;
				}
			}

			// Check for invalid attempts and if so, go next
			if ((requestedDur != -1.2345) && arrivalType.equals("m") && people[id].massageCount == 3) {
				invalidAttempt++;
				continue;
			}
			
			// If a person's act is finished, then state that person is not active
			if (requestedDur == -1.2345)
				people[id].activeState = false; 
			
			
			// Check for canceled attempts and if so, go next
			if (people[id].activeState == true) {
				canceledAttempt++;
				continue;
			}
			
			if (requestedDur == -1.2345) { 
				
				/**
				 *  some person just finished physiotherapy, massage or training activity
				 *	then check if there is a person in the waiting queue of related activity
				 */
				 
				if (arrivalType.equals("m") && massagedQueue.size() > 0) {

					// Read the data of head of massage queue
					String polledMQueue = massagedQueue.poll().toString();
					String[] wordsArr4 = polledMQueue.split(" ");

					int idQ = Integer.parseInt(wordsArr4[0]); // id of person polled from the queue
					double requestTimeQ = Double.parseDouble(wordsArr4[1]); // request time of the person
					double requestedDurQ = Double.parseDouble(wordsArr4[2]); // requested duration
					BigDecimal requestTimeQB = new BigDecimal(requestTimeQ);
					BigDecimal requestedDurQB = new BigDecimal(requestedDurQ);

					// Find the available masseur's index and upgrade masseur's info
					int i;
					for (i = 0; i < masseurNum; i++) {
						if (masseurs[i].available == true) {
							masseurs[i].available = false;
							masseurs[i].time = requestedDurQB;
							break;
						}						
					}
															
					// Upgrade person info
					people[idQ].activeState = true;
					people[idQ].massageTime.add(requestedDurQ);
					people[idQ].waitingTimeMassageQu.add(timeGlobal.subtract(requestTimeQB).doubleValue());	 
					people[idQ].massageCount++; 
											
					// Leaving activity
					ActivityLog activity = new ActivityLog(arrivalType, idQ, timeGlobal.add(requestedDurQB), outIndicator); 
					actsQueued.add(activity);

					
				} else if (arrivalType.equals("p") && therapyQueue.size() > 0) {

					// Read the data of head of physiotherapy queue
					String polledPQueue = therapyQueue.poll().toString();
					String[] wordsArr4 = polledPQueue.split(" ");
					
					int idQ = Integer.parseInt(wordsArr4[0]); // id of person polled from the queue
					double requestTimeQ = Double.parseDouble(wordsArr4[1]); // request time of the person
					BigDecimal requestTimeQB = new BigDecimal(requestTimeQ);

					// Find the available physiotherapist's index and upgrade physiotherapist info		
					int i;
					for (i = 0; i < physiotherapistNum; i++) {
						if (physiotherapists[i].available == true) {
							physiotherapists[i].available = false;
							physiotherapists[i].time = physiotherapists[i].serviceTime;
							break;
						}
					}
					
					// Upgrade person info
					people[idQ].activeState = true; 
					people[idQ].therapyTime.add(physiotherapists[i].serviceTime.doubleValue());
					people[idQ].waitingTimeTherapyQu.add(timeGlobal.subtract(requestTimeQB).doubleValue());
					
					// New leaving activity
					ActivityLog activity = new ActivityLog(arrivalType, idQ, 
							timeGlobal.add(physiotherapists[i].serviceTime), outIndicator);
					actsQueued.add(activity);
					
				} else if (arrivalType.equals("t") && traineeQueue.size() > 0) {

					// Read the data of head of training queue
					String polledTQueue = traineeQueue.poll().toString();
					String[] wordsArr4 = polledTQueue.split(" ");

					int idQ = Integer.parseInt(wordsArr4[0]); // id of the person polled from the queue
					double requestTimeQ = Double.parseDouble(wordsArr4[1]); // request time
					double requestedDurQ = Double.parseDouble(wordsArr4[2]); // requested duration
					BigDecimal requestTimeQB = new BigDecimal(requestTimeQ);
					BigDecimal requestedDurQB = new BigDecimal(requestedDurQ);

					// Find the available trainer's index and upgrade trainer info		
					int i;
					for (i = 0; i < trainerNum; i++) {
						if (trainers[i].available == true) {
							trainers[i].available = false;
							trainers[i].time = requestedDurQB;
							break;
						}						
					}
					
					// Upgrade person info
					people[idQ].activeState = true;
					people[idQ].trainingTime.add(requestedDurQ);
					people[idQ].waitingTimeTrainingQu.add(timeGlobal.subtract(requestTimeQB).doubleValue());

					// New activity for physiotherapy
					ActivityLog activity = new ActivityLog("p", idQ, timeGlobal.add(requestedDurQB), requestedDurQB);
					actsQueued.add(activity);
				
					// New leaving activity 
					ActivityLog activity2 = new ActivityLog(arrivalType, idQ, timeGlobal.add(requestedDurQB), outIndicator);
					actsQueued.add(activity2);
					
				}
			} else { 
				
				double leavingTime = requestTime + requestedDur; // use leaving time if available slot is found
				BigDecimal leavingTimeB = new BigDecimal(leavingTime);
				
				if (arrivalType.equals("t")) {
					
					// Check for available slot among trainers if so update info
					boolean availability = false;
					int i;
					for (i = 0; i < (trainerNum); i++) {
						if (trainers[i].available == true) {
							trainers[i].available = false;
							trainers[i].time = requestedDurB;
							availability = true;
							break;
						}
					}					
					
					if (availability) {
										
						// Upgrade person info
						people[id].activeState = true; 
						people[id].trainingTime.add(requestedDur);
											
						// New activity for physiotherapy
						ActivityLog activity = new ActivityLog("p", id, leavingTimeB, requestedDurB);
						actsQueued.add(activity);
						
						// New leaving activity
						ActivityLog activity2 = new ActivityLog(arrivalType, id, leavingTimeB, outIndicator);
						actsQueued.add(activity2);
						
											
					} else { // add to queue
						Training trainee = new Training(id, requestTimeB, requestedDurB);
						traineeQueue.add(trainee);		
						people[id].activeState = true; 
					}
				} else if (arrivalType.equals("m")) {

					// Check for available slot among masseurs if so update info
					boolean availability = false;
					int i;
					for (i = 0; i < masseurNum; i++) {
						if (masseurs[i].available == true) {
							masseurs[i].available = false;
							masseurs[i].time = requestedDurB;
							availability = true;
							break;
						}						
					}
					
					if (availability) {
												
						// Update person info
						people[id].activeState = true; 
						people[id].massageTime.add(requestedDur);	 
						people[id].massageCount++; 
												
						// New leaving act
						ActivityLog activity = new ActivityLog(arrivalType, id, leavingTimeB, outIndicator);
						actsQueued.add(activity);							
											
					} else { // Add to queue
						double skill = people[id].skill;
						Massage massaged = new Massage(id, requestTimeB, requestedDurB, skill);
						massagedQueue.add(massaged);	
						people[id].activeState = true; 
					}
				} else if (arrivalType.equals("p")) {

					// Check for available slot among physiotherapists if so update info
					boolean availability = false;
					int i;
					for (i = 0; i < physiotherapistNum; i++) {
						if (physiotherapists[i].available == true) {
							physiotherapists[i].available = false;
							physiotherapists[i].time = physiotherapists[i].serviceTime;
							availability = true;
							break;
						}						
					}
					
					if (availability) {
						
						// Update person info
						people[id].activeState = true; 
						people[id].therapyTime.add(physiotherapists[i].serviceTime.doubleValue());	
						
						// New leaving act
						ActivityLog activity = new ActivityLog(arrivalType, id, 
								requestTimeB.add(physiotherapists[i].serviceTime), outIndicator); 
						actsQueued.add(activity);	
						
					} else { // Add to queue		
						Physiotherapy phystherapied = new Physiotherapy(id, requestTimeB, requestedDurB);
						therapyQueue.add(phystherapied);
						people[id].activeState = true; 											
					}
				} else {
					System.out.println("Error: The first character is not valid: " + wordsArr3[0]);
					System.exit(0);
				}
			}
		}
		
		// Calculations
		
		ArrayList<String> answers = new ArrayList<String>(); // 
		
		// Q1
		String Q1 = String.valueOf(maxLengthTraineeQu);
		answers.add(Q1);
		// Q2
		String Q2 = String.valueOf(maxLengthTherapyQu);
		answers.add(Q2);
		// Q3
		String Q3 = String.valueOf(maxLengthMassagedQu);
		answers.add(Q3);

		
		// Q7
		double totalTrainingTime = 0;
		double totalTrainingAction = 0;
		for (int i = 0; i < peopleNum; i++) {
			int size = people[i].trainingTime.size();
			totalTrainingAction += size;
			for (int j = 0; j < size; j++) {
				totalTrainingTime += people[i].trainingTime.get(j);
			}
		}
		double avgTraining = totalTrainingTime / totalTrainingAction;
		double Q7D = avgTraining;
		String Q7 = String.format("%.3f", Q7D);

		// Q4
		double totalWaitingTimeTrainingQu = 0;
		for (int i = 0; i < peopleNum; i++) {
			int size = people[i].waitingTimeTrainingQu.size();
			for (int j = 0; j < size; j++) {
				totalWaitingTimeTrainingQu += people[i].waitingTimeTrainingQu.get(j);
			}
		}
		double avgWaitingTimeTrainingQu = totalWaitingTimeTrainingQu / totalTrainingAction;
		double Q4D = avgWaitingTimeTrainingQu;
		String Q4 = String.format("%.3f", Q4D);
		answers.add(Q4);
		
		// Q8
		double totalTherapyTime = 0;
		double totalTherapyAction = 0;
		for (int i = 0; i < peopleNum; i++) {
			int size = people[i].therapyTime.size();
			totalTherapyAction += size;
			for (int j = 0; j < size; j++) {
				totalTherapyTime += people[i].therapyTime.get(j);
			}
		}
		double avgTherapy = totalTherapyTime / totalTherapyAction;
		double Q8D = avgTherapy;
		String Q8 = String.format("%.3f", Q8D);
		
		// Q5
		double totalWaitingTimeTherapyQu = 0;
		for (int i = 0; i < peopleNum; i++) {
			int size = people[i].waitingTimeTherapyQu.size();
			for (int j = 0; j < size; j++) {
				totalWaitingTimeTherapyQu += people[i].waitingTimeTherapyQu.get(j);
			}
		}
		double avgWaitingTimeTherapyQu = totalWaitingTimeTherapyQu / totalTherapyAction;
		double Q5D = avgWaitingTimeTherapyQu;
		String Q5 = String.format("%.3f", Q5D);
		answers.add(Q5);
		
		// Q9
		double totalMassageTime = 0;
		double totalMassageAction = 0;
		for (int i = 0; i < peopleNum; i++) {
			int size = people[i].massageTime.size();
			totalMassageAction += size;
			for (int j = 0; j < size; j++) {
				totalMassageTime += people[i].massageTime.get(j);
			}
		}
		double avgMassage = totalMassageTime / totalMassageAction;	
		double Q9D = avgMassage;
		String Q9 = String.format("%.3f", Q9D);
	
		// Q6
		double totalWaitingTimeMassageQu = 0;
		for (int i = 0; i < peopleNum; i++) {
			int size = people[i].waitingTimeMassageQu.size();
			for (int j = 0; j < size; j++) {
				totalWaitingTimeMassageQu += people[i].waitingTimeMassageQu.get(j);
			}
		}
		double avgWaitingTimeMassageQu = totalWaitingTimeMassageQu / totalMassageAction; // qu
		double Q6D = avgWaitingTimeMassageQu;
		String Q6 = String.format("%.3f", Q6D);
		answers.add(Q6);
		answers.add(Q7);
		answers.add(Q8);
		answers.add(Q9);
		
		
		// Q10 = Q4 + Q5 + Q7 + Q8
		double avgTurnaround = avgWaitingTimeTrainingQu + avgWaitingTimeTherapyQu + avgTraining + avgTherapy;
		double Q10D = avgTurnaround;
		String Q10 = String.format("%.3f", Q10D);
		answers.add(Q10);
		
		// Q11
		ArrayList<Double> totTherapyWaitingTime = new ArrayList<Double>();
		for (int i = 0; i < peopleNum; i++) {
		    double therapyQuWait = 0.0;
			int size = people[i].waitingTimeTherapyQu.size();
			for (int j = 0; j < size; j++) {
				therapyQuWait += people[i].waitingTimeTherapyQu.get(j);
			}
			totTherapyWaitingTime.add(therapyQuWait);
		}
		double maxTherapyQuWait = totTherapyWaitingTime.get(0);
		int maxTherapyQuWaiter = 0;
		for (int i = 1; i < peopleNum; i++) {
			if (maxTherapyQuWait < totTherapyWaitingTime.get(i)) {
				maxTherapyQuWait = totTherapyWaitingTime.get(i);
				maxTherapyQuWaiter = i;		
			}
		}
		
		String maxTherapyQuWaitStr = String.format("%.3f", maxTherapyQuWait);
	    String Q11 = maxTherapyQuWaiter + " " + maxTherapyQuWaitStr;
	    answers.add(Q11);
		
		// Q12
	    ArrayList<Double> totMassageWaitingTime = new ArrayList<Double>();
		for (int i = 0; i < peopleNum; i++) {
		    if (people[i].massageCount != 3) {
				totMassageWaitingTime.add(Double.POSITIVE_INFINITY);
				continue;
			}
		    double massageQuWait = 0.0;
			int size = people[i].waitingTimeMassageQu.size();
			for (int j = 0; j < size; j++) {
				massageQuWait += people[i].waitingTimeMassageQu.get(j);
			}
			totMassageWaitingTime.add(massageQuWait);
		}
		double minMassageQuWait = totMassageWaitingTime.get(0);
		int minMassageQuWaiter = 0;
		for (int i = 1; i < peopleNum; i++) {
			if (minMassageQuWait > totMassageWaitingTime.get(i)) {
				minMassageQuWait = totMassageWaitingTime.get(i);
				minMassageQuWaiter = i;		
			}
		}
		
		String minMassageQuWStr = String.format("%.3f", minMassageQuWait);
		String Q12 = minMassageQuWaiter + " " + minMassageQuWStr;
		answers.add(Q12);
		
		// Q13
		String Q13 = String.valueOf(invalidAttempt);
		answers.add(Q13);
		// Q14
		String Q14 = String.valueOf(canceledAttempt);
		answers.add(Q14);
		// Q15
		double Q15D = timeGlobal.doubleValue();
		String Q15 = String.format("%.3f", Q15D);
		
		
		// Create output file & check if that name exists
		String outputPath = args[1];
		File output = new File(outputPath);
		try {					
			if (output.createNewFile())
				System.out.println("File is created.");
			else {
				System.out.println("File already exists.");
				return;
			}
			FileWriter writer = new FileWriter(outputPath);

			for (String str : answers) {
				writer.write(str);
				writer.write("\n");			
			}
			writer.write(Q15);			
			writer.close();
		} catch (Exception err) {
			System.out.println("An error occured while writing to the file.");
			err.getStackTrace();
		}			
	}
}

package TDNextLocal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import TDNextLocal.ParserAPILocal;
import TDNextLocal.TDNextLogicAPILocal.CommandType;

public class TDNextLocal {
	
	private ArrayList<TaskLocal> _listTask = new ArrayList<TaskLocal>();
	private String _lastCommand = new String();
	private ArrayList<TaskLocal> _tempTask;
	private static Logger _logger = Logger.getLogger("Logic");

	public TDNextLocal() {
	}
	
	public ArrayList<TaskLocal> executeCommand(String input) {
		CommandType command = ParserAPILocal.parseCommand(input);
		
		switch (command) {
			case ADD :  
				addTask(input);
				return _listTask;
				
			case DELETE : 
				deleteTask(input);
				return _listTask;
			
			case SEARCH :
				ArrayList<TaskLocal> output = searchTask(input);
				return output;
			
			case EDIT :
				editTask(input);
				return _listTask;
				
			case CLEAR :
				clearAll();
				return _listTask;
			
			case DONE :
				markTaskAsDone(input);
				return _listTask;
				
			case SORT_DEFAULT :
				sortDefault();
				return _listTask;
			
			case SORT_BY_NAME :
				sortName();
				return _listTask;
				
			case SORT_BY_DEADLINE :
				sortDeadline();
				return _listTask;
				
			case UNDO:
				undo();
				return _listTask;
				
			case EXIT :
				exitProgram();
				return _listTask;
			
			default :
				return _listTask;
		}
	}
	
	public ArrayList<TaskLocal> startProgram() {
		ArrayList<String> allFileInfo = new ArrayList<String>();
		try {
			allFileInfo = StorageAPILocal.getFromFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < allFileInfo.size(); i++) {
			ArrayList<String> information = ParserAPILocal.parseInformation(allFileInfo.get(i));
			TaskLocal currTask = new TaskLocal(information);
			if(!currTask.isDone()) {
				_listTask.add(currTask);
			}
		}
		sortDefault();
		_logger.log(Level.INFO, "Program started");
		
		return _listTask;
	}
	
	private void undo(){ 
		System.out.println(_lastCommand);
		executeCommand(_lastCommand);
	}

	private void undoMarkAsDone() {
		int index = ParserAPILocal.parseIndex(_lastCommand);
		TaskLocal currTask = _listTask.get(index);
		String oldDesc = currTask.getDescription();
		currTask.markAsUndone();
		String newDesc = currTask.getDescription();
		try {
			StorageAPILocal.editToFile(oldDesc, newDesc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void editTask(String input) {
		int index = ParserAPILocal.parseIndex(input);
		String oldDesc = _listTask.get(index).getDescription();
		_listTask.remove(index);
		ArrayList<String> information = ParserAPILocal.parseInformation(input);
		TaskLocal newTask = new TaskLocal(information);
		_listTask.add(newTask);
		try {
			StorageAPILocal.editToFile(newTask.getDescription(), oldDesc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sortDefault();
		_lastCommand = new String();
		_lastCommand = _lastCommand + "EDIT " + _listTask.indexOf(newTask) +
						" " + oldDesc;
	}

	private void clearAll(){
		_tempTask = new ArrayList<TaskLocal>(_listTask);
		_listTask.clear();
		try {
			StorageAPILocal.clearFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void markTaskAsDone(String input) {
		int index = ParserAPILocal.parseIndex(input);
		TaskLocal currTask = _listTask.remove(index);;
		String oldDesc = currTask.toString();
		System.out.println(oldDesc);
		currTask.markAsDone();
		String newDesc = currTask.toString();
		System.out.println(newDesc);
		try {
			StorageAPILocal.editToFile(newDesc, oldDesc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	private void exitProgram() {
		// TODO Auto-generated method stub
		
	}

	private void addTask(String input) {
		ArrayList<String> information = ParserAPILocal.parseInformation(input);
		TaskLocal newTask = new TaskLocal(information);
		try {
			StorageAPILocal.writeToFile(newTask.toString());
		} catch (IOException e) {
		}
		_listTask.add(newTask);
		sortDefault();
		int index = _listTask.indexOf(newTask) + 1;
		_lastCommand = new String();
		_lastCommand = _lastCommand + "DELETE " + index;
		_logger.log(Level.INFO, "Task added");
		
	}
	
	private void deleteTask(String input) {
		int index = ParserAPILocal.parseIndex(input);
		TaskLocal deletedTask = _listTask.remove(index);
		try {
			StorageAPILocal.deleteFromFile(deletedTask.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_lastCommand = new String();
		_lastCommand = _lastCommand + "ADD " + deletedTask.toString();
		System.out.println(_lastCommand);
		_logger.log(Level.INFO, "Task deleted");
		
	}
	
	private ArrayList<TaskLocal> searchTask(String input) {
		ArrayList<String> information = ParserAPILocal.parseInformation(input);
		String name = information.get(0);
		ArrayList<TaskLocal> output = new ArrayList<TaskLocal>();
		
		for(int i = 0; i < _listTask.size(); i++) {
			TaskLocal currTask = _listTask.get(i);
			if(currTask.getDescription().contains(name)) {
				output.add(currTask);
			}
		}
		
		return output;
	}
	
	private void sortDefault() {
		Collections.sort(_listTask, new PriorityComparator());
		_logger.log(Level.INFO, "Default sorted");
	}
	
	private void sortName() {
		Collections.sort(_listTask, new NameComparator());
		_logger.log(Level.INFO, "Sorted by name");
	}

	private void sortDeadline() {
		Collections.sort(_listTask, new DateComparator());
		_logger.log(Level.INFO, "Sorted by deadline");
		
	}
}

package TDNextLocal;

import java.util.ArrayList;

import TDNextLocal.TDNextLogicAPILocal.CommandType;

public class ParserAPILocal {
	
	public static CommandType parseCommand(String input) {
		input = input.toUpperCase();
		
		if(input.contains("ADD")) {
			return CommandType.ADD;
		} else if (input.contains("EDIT")) {
			return CommandType.EDIT;
		} else if (input.contains("DELETE")) {
			return CommandType.DELETE;
		} else if (input.contains("CLEAR")) {
			return CommandType.CLEAR;
		} else if (input.contains("SORT")) {
			if (input.contains("BY NAME")) {
				return CommandType.SORT_BY_NAME;
			} else if (input.contains("BY DEADLINE")) {
				return CommandType.SORT_BY_DEADLINE;
			} else {
				return CommandType.SORT_DEFAULT;
			}
		} else if (input.contains("SEARCH")) {
			return CommandType.SEARCH;
		} else if (input.contains("DONE")) {
			return CommandType.DONE;
		} else if (input.contains("UNDO")) {
			return CommandType.UNDO;
		} else if (input.contains("EXIT")) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}
	
	public static ArrayList<String> parseInformation(String input) {
		String firstWord = getFirstWord(input).toUpperCase();
		if(firstWord.equals("ADD") || firstWord.equals("EDIT") || firstWord.equals("SEARCH"))
		input = removeFirstWord(input);
		
		ArrayList<String> listInfo = new ArrayList<String>();
		if((input.split(" ", 2)[0].matches("[0-9]"))) {			
			input = input.split(" ", 2)[1];
		}
		listInfo.add(input);
		if(input.contains("IMPORTANT")) {
			listInfo.add("IMPORTANT");
		} else {
			listInfo.add("");
		}
		if((input.contains("BY")) || (input.contains("ON"))) {
			input = input.replace("ON", "BY");
			String date = input.split("BY ", 2)[1].split(" ", 2)[0];
			listInfo.add(parseDate(date));
		} else {
			listInfo.add("");
		}
		if(input.contains("(x)")) {
			listInfo.add("done");
		} else {
			listInfo.add("");
		}
		
		return listInfo;
	}
	
	public static int parseIndex(String input) {
		String[] splitInput = input.split(" ");
		int index = -1;
		
		for(int i = 0; i < splitInput.length; i++) {
			try {
				System.out.println(splitInput[i]);
				index = Integer.parseInt(splitInput[i]);
			} catch (NumberFormatException e) {
			}
		}
		
		return index - 1;
	}
	
	private static String parseDate(String dateIn) {
		return dateIn;
	}
	
	private static String removeFirstWord(String input) {
		return input.substring(input.indexOf(" ") + 1);
	}
	
	private static String getFirstWord(String input) {
		return input.substring(0, input.indexOf(" "));
	}
}

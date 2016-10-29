import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage; 

public class Project9_4 extends Application {
	public static File file1 = new File("project9_4_1.txt");
	public static File file2 = new File("project9_4_2.txt");
	public static File file3 = new File("project9_4_3.txt");
	public static File file4 = new File("project9_4_4.txt");
	public static File file5 = new File("project9_4_5.txt");
	private Text word = new Text("Enter word(s): ");
	private TextField tfWord = new TextField();
	private Button button = new Button("Search");
	private TextArea ta = new TextArea();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// read file and store the word to a string array
		String[] wordArr1 = read(file1);
		String[] wordArr2 = read(file2);
		String[] wordArr3 = read(file3);
		String[] wordArr4 = read(file4);
		String[] wordArr5 = read(file5);
		
		// array list to store the word and its frequency
		ArrayList <Freq> wordFreq1 = new ArrayList<Freq>();
		ArrayList <Freq> wordFreq2 = new ArrayList<Freq>();
		ArrayList <Freq> wordFreq3 = new ArrayList<Freq>();
		ArrayList <Freq> wordFreq4 = new ArrayList<Freq>();
		ArrayList <Freq> wordFreq5 = new ArrayList<Freq>();
		
		// compute the frequency
		int n1 = freq(wordArr1, wordFreq1);
		int n2 = freq(wordArr2, wordFreq2);
		int n3 = freq(wordArr3, wordFreq3);
		int n4 = freq(wordArr4, wordFreq4);
		int n5 = freq(wordArr5, wordFreq5);
		
		// create a trie to store the word
		Trie trie = new Trie();
		
		// insert the word to the trie
		wordInsert(trie, wordFreq1, n1, file1);
		wordInsert(trie, wordFreq2, n2, file2);
		wordInsert(trie, wordFreq3, n3, file3);
		wordInsert(trie, wordFreq4, n4, file4);
		wordInsert(trie, wordFreq5, n5, file5);
		
		// create a grid pane
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.add(word, 0, 0);
		pane.add(tfWord, 1, 0);
		pane.add(button, 2, 0);
		pane.add(ta, 1, 2);
		ColumnConstraints column1 = new ColumnConstraints(90);
		ColumnConstraints column2 = new ColumnConstraints(400);
		ColumnConstraints column3 = new ColumnConstraints(90);
		pane.getColumnConstraints().addAll(column1, column2, column3);
		pane.setVgap(20);
		pane.setHalignment(word, HPos.CENTER);
		pane.setHalignment(button, HPos.CENTER);
		ta.setEditable(false);
		
		// search
		button.setOnAction(e -> searching(trie));
		
		// create a scene
		Scene scene = new Scene(pane, 600, 300);
		primaryStage.setTitle("Searching Engine");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void searching(Trie trie) {
		// clear the text area
		ta.setText("");
		
		// get the word(s)
		String searchingWord = tfWord.getText().trim();
		String searchingWordLowerCase = searchingWord.toLowerCase();
		String[] searchingWordArr = searchingWordLowerCase.toString().split(" ");
		int wordNum = searchingWordArr.length + 2;
		
		// create an array to store the frequency of each word
		int[][] result = new int[5][wordNum];
		for (int i = 0; i < 5; i++) {
			result[i][0] = i + 1;
			for (int j = 1; j < wordNum; j++) {
				result[i][j] = 0;
			}
		}
					
		// check whether the word(s) is in these file
		check(trie, searchingWordArr, result);
					
		// add these frequency as the relative number
		for (int i = 0; i < 5; i++) {
			int sum = 0;
			for (int j = 2; j < wordNum; j++) {
				sum += result[i][j];
			}
			result[i][1] = sum;
		}
					
		// sort by relative number
		bubbleSort(result, 5, wordNum);
					
		// display
		for (int i = 0; i < 5; i++) {
			ta.appendText("File: " + result[i][0] + ".txt" + '\n');
			ta.appendText("     has " + result[i][1] + " relative words." + '\n');
			for (int j = 2; j < wordNum; j++) {
				ta.appendText("     " + searchingWordArr[j - 2] + ": " + result[i][j]);
			}
			ta.appendText("" + '\n');
		}
	}
	
	public static void bubbleSort(int[][] result, int fileNum, int wordNum) {
		int temp;
		for (int i = 0; i < fileNum - 1; i++) {
			for (int j = i + 1; j < fileNum; j++) {
				if (result[i][1] < result[j][1]) {
					for (int k = 0; k < wordNum; k++) {
						temp = result[i][k];
						result[i][k] = result[j][k];
						result[j][k] = temp;
					}
				}
			}
		}
	}  
	
	public static void check(Trie trie, String[] Arr, int[][] result) {
		for (int i = 0; i < Arr.length; i++) {
			trie.checkWord(Arr[i], result, i);
		}
	}
	
	public static void wordInsert(Trie trie, ArrayList <Freq> wordFreq, int n, File file) {
		for (int i = 0; i < n; i++) {
			// insert the word, its frequency and its filename
			trie.insert(wordFreq.get(i).getWord(), wordFreq.get(i).getFreq(), file.getName());
		}
	}
	
	public static String[] read(File file) throws Exception {
		Scanner file_input = new Scanner(file);	
		StringBuilder stringBuilder = new StringBuilder();      // create a string builder
		while (file_input.hasNext()) {
			String str = file_input.nextLine();                 // read
			String only_ab = str.replaceAll("[^a-zA-Z]", " ");  // keep the alphabet character
			String lowerCase = only_ab.toLowerCase();           // change into lower case
			stringBuilder.append(lowerCase);                    // store in the string builder
			stringBuilder.append(" ");
		}
		String wordStr = delOthers(stringBuilder.toString());   // delete some other word
		String[] wordArr = wordStr.split(" ");                  // store the word to a string array
		return wordArr;
	}
	
	public static int freq(String[] wordArr, ArrayList <Freq> wordFreq) {
		Map <String, Integer> map = new HashMap <String, Integer> ();     // create a hash map to store the word and frequency
		int number = 0;                                                   // count how many different words in a file
		for (int i = 0; i < wordArr.length; i++) {
			Integer count = map.get(wordArr[i]);
			if(count == null) {
				map.put(wordArr[i], 1);
				number++;
			}
			else {
				map.put(wordArr[i], ++count);
			}
		}
		
		for(Map.Entry <String, Integer> entry : map.entrySet()) {
			Freq freq = new Freq(entry.getKey(), entry.getValue());       // create a "freq" to store the word and frequency
			wordFreq.add(freq);
		}
		
		return number;
	}
	
	public static String delOthers(String wordStr) {
		String art = " a | an | the ";
		String prep = " in | on | with | by | for | at | about | under | as | of | into | within | throughout | inside | outside | without | of | to | from | up | out ";
		String pron = " i | you | he | she | they | we | me | him | her | them | us | my | his | your | their | this | that | these | those | myself | himself | themselves | who | what | which | some | many | both | that | whom | whose | whatever | whichever | whoever | whomever | it | how ";
		String others = " ll | b | c | d | e | f | g | h | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z | ve ";
		
		int n = 3;
		while (n > 0) {
			wordStr = wordStr.replaceAll(art, " ");            // delete the art
			wordStr = wordStr.replaceAll(prep, " ");           // delete the prep
			wordStr = wordStr.replaceAll(pron, " ");           // delete the pron
			wordStr = wordStr.replaceAll(others, " ");         // delete some other words such as single character
			wordStr = wordStr.replaceAll(" +", " ");           // delete unnecessary space
			n--;
		}
		
		return wordStr;
	}
}

class Node {
	char content;                               // single character
	boolean isEnd;                              // if it is the last node
	int count;
	LinkedList<Node> childList;
	LastNode has1 = new LastNode(false, 0);     // last node to store if the word exists in file1 and the frequency
	LastNode has2 = new LastNode(false, 0);     // last node to store if the word exists in file2 and the frequency
	LastNode has3 = new LastNode(false, 0);     // last node to store if the word exists in file3 and the frequency
	LastNode has4 = new LastNode(false, 0);     // last node to store if the word exists in file4 and the frequency
	LastNode has5 = new LastNode(false, 0);     // last node to store if the word exists in file5 and the frequency
	
	// construction
	public Node(char c) {
		childList = new LinkedList<Node>();
		isEnd = false;
		content = c;
		count = 0;
	}
	
	// child node
	public Node subNode(char c) {
		if(childList != null) {
			for(Node eachChild : childList) {
				if(eachChild.content == c) {
					return eachChild;
				}
			}
		}
		return null;
	}
}

class Trie {
	private Node root;
	
	// construction
	public Trie() {
    	root = new Node(' ');
	}
	
	// insert the word with frequency
	public void insert(String word, int freq, String filename) {
		Node current = root;
		for(int i = 0; i < word.length(); i++) {
			Node child = current.subNode(word.charAt(i));
			if(child != null) {
				current = child;
			}
			else {
				current.childList.add(new Node(word.charAt(i)));
				current = current.subNode(word.charAt(i));
			}
			current.count++;
		}
		current.isEnd = true;
		
		// set the last node
		if (filename == "1.txt") {
			current.has1.hasWord = true;
			current.has1.freq = freq;
		}
		if (filename == "2.txt") {
			current.has2.hasWord = true;
			current.has2.freq = freq;
		}
		if (filename == "3.txt") {
			current.has3.hasWord = true;
			current.has3.freq = freq;
		}
		if (filename == "4.txt") {
			current.has4.hasWord = true;
			current.has4.freq = freq;
		}
		if (filename == "5.txt") {
			current.has5.hasWord = true;
			current.has5.freq = freq;
		}
	}
    
	// search the word
	public boolean search(String word) {
		Node current = root;
		for(int i = 0; i < word.length(); i++) {
			if(current.subNode(word.charAt(i)) == null) {
				return false;
			}
			else {
				current = current.subNode(word.charAt(i));
			}
		}
		if (current.isEnd == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// check the word
	public boolean checkWord(String word, int[][] result, int index) {
		Node current = root;
		for(int i = 0; i < word.length(); i++) {
			if(current.subNode(word.charAt(i)) == null) {
				return false;
			}
			else {
				current = current.subNode(word.charAt(i));
			}
		}
		if (current.isEnd == true) {
			// get the frequency of the word in each file
			result[0][index + 2] = current.has1.freq;
			result[1][index + 2] = current.has2.freq;
			result[2][index + 2] = current.has3.freq;
			result[3][index + 2] = current.has4.freq;
			result[4][index + 2] = current.has5.freq;
			return true;
		}
		else {
			return false;
		}
	}
}

class Freq {
	String word;
	int freq;
	
	// construction
	public Freq(String word, int freq) {
		this.word = word;
		this.freq = freq;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getFreq() {
		return freq;
	}
}

class LastNode {
	boolean hasWord;
	int freq;
	
	// construction
	public LastNode(boolean hasWord, int freq) {
		this.hasWord = hasWord;
		this.freq = freq;
	}
	
	public boolean getHasWord() {
		return hasWord;
	}
	
	public int getFreq() {
		return freq;
	}
}
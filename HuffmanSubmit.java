//Jiayi Gu
//31525890
// Import any package as required

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
class Node_huff {
	 
    int data;
    char c;
 
    Node_huff left;
    Node_huff right;
}
class MyComparator implements Comparator<Node_huff> {
    public int compare(Node_huff a, Node_huff b)
    {
        return a.data - b.data;
    }
}
public class HuffmanSubmit implements Huffman {
  
	// Feel free to add more methods and variables as required.
	HashMap<Character, Integer> freq_table;	
	HashMap<Character, String> code_char;
	HashMap<String, Character> code_char_swap;
   public HuffmanSubmit() {
	   freq_table = new HashMap<>();
	   code_char = new HashMap<>();
	   code_char_swap = new HashMap<>();
	   
   }
	public void encode(String inputFile, String outputFile, String freqFile){
		freq_table.clear();
		code_char.clear();
		BinaryIn in = new BinaryIn(inputFile);
		BinaryOut out = new BinaryOut(freqFile);
		BinaryOut encode = new BinaryOut(outputFile);
		  // change the file into hashmap of the frequency table
		  while (!in.isEmpty()) {
		   char cha = in.readChar();
		   if (freq_table.containsKey(cha)) {
		    freq_table.put(cha, freq_table.get(cha)+1);
		   }
		   else freq_table.put(cha, 1);
		  }

		  for (char key:freq_table.keySet()) {
		   String key_ASCII = Integer.toBinaryString(key);
		   while (key_ASCII.length()<8) {
			   key_ASCII="0"+key_ASCII;
		   }
		   out.write(key_ASCII + ":" + freq_table.get(key)+"\n");
		  }
		  out.flush();
		  out.close();
		  Node_huff root = map_to_tree(freq_table);       
          root_to_code(root, "");
          // change the txt to encode file
          BinaryIn file_for_output = new BinaryIn(inputFile);
          
          while (!file_for_output.isEmpty()) {
   		   char ch = file_for_output.readChar();
   		   String binary_code = code_char.get(ch);
   		   char[] Binary_Arr = binary_code.toCharArray();
   		   for (int i = 0; i < Binary_Arr.length; i++) {
   			   if (Binary_Arr[i] == '0') {
   				   encode.write(false);
   			   }
   			   else if (Binary_Arr[i] == '1') {
   				   encode.write(true);
   		   }
          }
          }
          encode.flush();
          encode.close();
   }
	private void root_to_code(Node_huff root, String s){
		
		if (root.left == null && root.right == null) {
			code_char.put(root.c, s);
			code_char_swap.put(s, root.c);
			return;
		}
		root_to_code(root.left, s + "0");
		root_to_code(root.right, s + "1");		
	}
	//convert hashmap to binary tree with root node
	private Node_huff map_to_tree(HashMap<Character, Integer> map) {
		 int size_freq = map.size();
		  // change frequency to priority queue 
		  PriorityQueue<Node_huff> NodeQue
         = new PriorityQueue<Node_huff>(size_freq, new MyComparator());
         for (char key:map.keySet()) {
       	  Node_huff node = new Node_huff();
       	  node.c = key;
       	  node.data = map.get(key);
       	  node.left = null;
       	  node.right = null;
       	  NodeQue.add(node);
         }
         // build binary tree
         while (NodeQue.size() > 1) {
       	  Node_huff smallest = NodeQue.poll();
       	  Node_huff smallest_2 = NodeQue.poll();
       	  Node_huff newNode = new Node_huff();
       	  newNode.c = '~';
       	  newNode.data = smallest.data + smallest_2.data;
       	  newNode.left = smallest;
       	  newNode.right = smallest_2;
       	  NodeQue.add(newNode);
         }          	
         return NodeQue.poll();
	}

   public void decode(String inputFile, String outputFile, String freqFile){
		// read frequency table first
	   freq_table.clear();
	   BinaryIn freq = new BinaryIn(freqFile);
	   BinaryIn encoding_file = new BinaryIn(inputFile);
	   BinaryOut output = new BinaryOut(outputFile);
	   String frequency_input = freq.readString();
	   String[] freq_line = frequency_input.split("\n");
	   
	   for (int i = 0; i< freq_line.length; i++) {
		String[] freq_map =  freq_line[i].split(":");
		int parseInt = Integer.parseInt(freq_map[0], 2);
		
		char c = (char)parseInt;

		int frequency = Integer.parseInt(freq_map[1]);
		freq_table.put(c, frequency);
	   }
	   Node_huff root = map_to_tree(freq_table);	   
	   System.out.println(root.left.data);
	   root_to_code(root,"");
	   String s = "";
//	   System.out.println(curr_node.data);
	   while (!encoding_file.isEmpty()) {
		   boolean a = encoding_file.readBoolean();
		   if(a==true) {
				s= s+"1";
			
			}else if(a==false) {
				s=s+"0";
			}
		   if (code_char_swap.containsKey(s)) {
			   char decode = code_char_swap.get(s);
			   s = "";
			   output.write(decode);
		   }		
		   
	   }
	   output.flush();
		   
	   }


   public static void main(String[] args) {
	   Huffman  huffman = new HuffmanSubmit();

		huffman.encode("ur.jpg", "ur.enc", "freq.txt");

		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");

		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }
   

}

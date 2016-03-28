import java.util.*;
import java.io.*;

/*
 * This class encodes an inputted text file into another text file while also
 * outputting the encodings for each character
 * @author Joe Jensen
 * 
 */ 
public class HuffmanCompressor
{
  /*
   * This ArrayList of type HuffmanNode stores all the characters in the text file with their frequencies
   */ 
  private static ArrayList<HuffmanNode> myList = new ArrayList<HuffmanNode>(); 
  /*I chose array list rather than linked list because I am more comfortable with the implementation*/
  
  /*
   * This ArrayList of HuffmanNode type is a copy of myList
   */ 
  private static ArrayList<HuffmanNode> copiedList = new ArrayList<HuffmanNode>();
  
  /*
   * This HuffmanNodes is the root of the Huffman Tree
   */ 
  public static HuffmanNode root = null; // root of tree
  
  /*
   * this String array stores the character with matched encoding
   */ 
  private static String [] myTable = new String [1000]; // encoding table
  
  /*
   * This integer stores the number of space saving bits this encoding has compared to ascii encoding
   */ 
  private static int spaceSavings = 0; // total space saving bits
  
  public static void main (String args []) throws IOException
  {
    System.out.println(huffmanCoder(args[0],args[1]));
  }
  
  /*
   * This method returns OK if the inputFileName was valid and an error if not valid
   * It also calls helper methods to create an encoded text file from a given text file
   * @param String the input file name to be encoded
   * @param String the output file name of the encoded text
   * @return String whether the input file name was valid or not
   */ 
  public static String huffmanCoder(String inputFileName, String outputFileName) throws IOException
  {
    try
    {
      String myFile = readFile(inputFileName); // returns String of file
      myList = makeList(myFile); // turns String into ArrayList
      copyList(); // puts myList into copiedList
      makeTree(myList); // turns ArrayList into Tree
      makeTable(root, ""); // Generates encoding table from Tree
      String output = outputString(myFile); // Makes the output String with encodings
      makeFile(outputFileName, output); // makes the output file with desired name
      makeFile("EncodingTable.txt", makeEncodingString()); // makes output encoding table file
    }
   catch(FileNotFoundException x)
   {
     return "File Not Found";
   }
    return "OK"; // have to output OK or file error etc. See documentation
  }
  
  /*
   * Reads the input file and creates a String
   * @param String the input file name
   * @return String the created string of the text file
   */ 
  public static String readFile(String inputFileName) throws IOException
  {
    String output = "";
    FileReader in = new FileReader(inputFileName);
    BufferedReader br = new BufferedReader(in);
    String myLine = br.readLine();
    while (myLine != null) 
    {
      output += myLine + "\n"; // adds each line into the output String
      myLine = br.readLine(); // increments
    }
    in.close();
    return output;
  }
  
  /*
   * This method makes a list of all characters and their frequencies in the text file
   * @param String the string of the text file
   * @return ArrayList the list of the characters and frequencies
   */ 
  public static ArrayList<HuffmanNode> makeList(String myFile)
  {
    ArrayList<HuffmanNode> temp = new ArrayList<HuffmanNode>();
    
    for (int i = 0; i < myFile.length(); i++) // loops through inputted String
    {
       Character myC = myFile.charAt(i);
       boolean inList = false;
       for (int j = 0; j < temp.size(); j++) // loops through list
       {
         if (myC.equals(temp.get(j).inChar)) // if myC is same as a character in the list
         {
           temp.get(j).frequency++; 
           inList = true;
           if (j < temp.size()-1 && temp.get(j).frequency > temp.get(j+1).frequency) // if next element has less frequency, swap
           {
             HuffmanNode myTemp = temp.get(j);
             temp.set(j,temp.get(j+1));
             temp.set(j+1,myTemp);
             j++;
           }
         }
       }
       if (!inList) // if myC was not already in the list
       {
         temp.add(0, new HuffmanNode(myC, 1, null, null)); // adding myC to the list
       }
    }
    return temp;
  }
  
  /*
   * This method copies myList into copiedList
   */ 
  public static void copyList()
  {
    for (int i = 0; i < myList.size(); i++)
    {
      copiedList.add(myList.get(i));
    }
  }
  
  /*
   * This method makes a huffman tree from myList
   * @param ArrayList the list of characters and frequencies used to make the tree
   */ 
  public static void makeTree(ArrayList<HuffmanNode> list)
  {
    while (list.size()>1)
    {
      HuffmanNode leftChild = list.remove(0);
      HuffmanNode rightChild = list.remove(0);
      int parentFreq = leftChild.frequency + rightChild.frequency; // new parent frequency
      HuffmanNode newParent = new HuffmanNode(null, parentFreq, leftChild, rightChild); // make parent node
      list.add(0,newParent); // add parent to the beginning of list
      boolean inserted = false;
      int i = 0; // i is always the index of the new parent
      while (!inserted) // loop until new parent is in correct place
      {
        if (i != list.size()-1 && list.get(i).frequency > list.get(i+1).frequency)
        {
          // swap, implement i by 1
          HuffmanNode temp = list.get(i);
          list.set(i, list.get(i+1));
          list.set(i+1, temp);
          i++;
        }
        else
          inserted = true;
      }
    }
    root = list.get(0); // sets root to the lone value in the list
  }
  
  /*
   * This method makes the encoding table
   * @param HuffmanNode the root node of the huffman tree
   * @param String the encoding of each character passed through recursion
   */ 
  public static void makeTable(HuffmanNode myNode, String code)
  {
    if (myNode.inChar!=null)
      myTable[myNode.inChar] = code;
    if (myNode.left != null)
      makeTable(myNode.left, code + "0");
    if (myNode.right != null)
      makeTable(myNode.right, code + "1");
  }
  
  /*
   * This method converts the characters into their encodings and calculates space savings
   * @param String the inputted text file string
   * @return String the new encoding string
   */ 
  public static String outputString(String input)
  {
    String out = "";
    for (int i = 0; i < input.length(); i++)
    {
      spaceSavings += (8 - myTable[input.charAt(i)].length());
      out += myTable[input.charAt(i)];
    }   
    return out;
  }
  
  /*
   * This method makes a file with a given file name and String
   * @param String the desired name of the created file
   * @param text the String to be placed in the file
   */ 
  public static void makeFile(String fileName, String text) throws IOException
  {
    File outFile = new File(fileName);
    PrintStream stream = new PrintStream(outFile);
    for (int i = 0; i < text.length(); i++)
    {
      if (text.charAt(i) == '\n')
        stream.println();
      else
        stream.print(text.charAt(i));
    }
    stream.close();
  }
  
  /*
   * This method makes the encoding table
   * @return String the encoding table in String form
   */ 
  public static String makeEncodingString()
  {
    String output = "";
    for (int i = 0; i < myTable.length; i++) // loops through encoding table
    {
      if (myTable[i] != null)
      {
        Character c = (char)i; // change i from ascii to character
        int myFreq = 0;
        for (int j = 0; j < copiedList.size(); j++) // loops through copiedList to get frequency
        {
          if (copiedList.get(j).inChar.equals(c))
            myFreq = copiedList.get(j).frequency;
        }
        output += c + " : " + myFreq + " : " + myTable[i] + "\n";
        // outputs "character : frequency : encoding"
      }
    } 
    output += "\nTotal Space Savings: " + spaceSavings;    
    return output;
  }
}

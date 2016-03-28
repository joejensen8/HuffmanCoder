/**
 * This class is a Node class for a huffman encoding node 
 * @author Joe Jensen
 * 
 */ 
public class HuffmanNode
{
  /*
   * This Character is the character of the node
   */ 
  public Character inChar;
  
  /*
   * This int is the frequency of the character in the text
   */ 
  public int frequency;
  
  /*
   * This HuffmanNode is the left child of the node
   */ 
  public HuffmanNode left;
  
  /*
   * This HuffmanNode is the right child of the node
   */ 
  public HuffmanNode right;
  
  /*
   * Initializes all of the HuffmanNode fields.
   */ 
  public HuffmanNode()
  {
    inChar = null;
    frequency = 0;
    left = null;
    right = null;
  }
  
  /*
   * Initializes all of the HuffmanNode fields.
   */ 
  public HuffmanNode(Character myChar, int myFreq, HuffmanNode myLeft, HuffmanNode myRight)
  {
    inChar = myChar;
    frequency = myFreq;
    left = myLeft;
    right = myRight;
  }
}
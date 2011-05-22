/*********************************************************************
 * File path:     /share/JavaDev/ptscharts/src/ptscharts/datacheck.java
 * Version:       
 * Description:   From http://www.cs.utk.edu/~cs365/examples/datacheck.html
 * Author:        
 * Created at:    Sun Dec  5 13:43:40 2010
 * Modified at:   Tue Dec 14 12:00:15 2010
 ********************************************************************/

package ptscharts;

import java.util.Scanner;

class Ptsdatacheck {
  static public void main(String args[]) {
    Scanner console = new Scanner(System.in);
    Scanner lineTokenizer;
    int lineNum = 0;

    while (console.hasNextLine()) {
      lineTokenizer = new Scanner(console.nextLine());
      lineNum++;
      // determine if the line has a name field
      if (lineTokenizer.hasNext()) {
        lineTokenizer.next();  // consume the valid token
      }
      else {
        System.out.printf("Line %d: line must have the format 'name age singleness'\n", lineNum);
        continue;  // proceed to the next line of input
      }
      // determine if the line has a second field, and if so, whether that
      // field is an integer
      if (lineTokenizer.hasNext()) {
        if (lineTokenizer.hasNextInt()) {
          lineTokenizer.nextInt(); // consume the valid token
        }
        else
          System.out.printf("line %d - %s: age should be an integer\n", 
                            lineNum, lineTokenizer.next());
      }
      else {
        System.out.printf("line %d: must have fields for age and singleness\n",
                          lineNum);
        continue; // proceed to the next line of input
      }
      // determine if the line has a third field, and if so, whether that
      // field is a boolean
      if (lineTokenizer.hasNext()) {
        if (lineTokenizer.hasNextBoolean())
          lineTokenizer.nextBoolean(); // consume the valid token
        else {
          System.out.printf("line %d - %s: singleness should be a boolean\n", 
                            lineNum, lineTokenizer.next());
          continue; // proceed to the next line of input
        }
      }
      else {
        System.out.printf("line %d: must have a field for singleness\n", lineNum);
        continue; // proceed to the next line of input
      }
      lineTokenizer.close(); // discard this line
    }
  }
}

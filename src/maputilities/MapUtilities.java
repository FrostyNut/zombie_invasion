package maputilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A map file must obey these following conventions.
 * <ul>
 * <li>
 *      The map must have ones surrounding the border. This is so the player
 *      does not go outside the map area.
 * </li>
 * <li>
 *      Building off the previous convention, the first and last lines in a map
 *      must only contain ones. All lines between must start and end with a one.
 * </li>
 * <li>
 *      Each row of the map must be equal in length. If a map has a row of length
 *      10, all rows must be of length 10; the map will not be allowed to have a
 *      single row with a length of 11.
 * </li>
 * <li>
 *      The map must only contain ones and zeros. A map that contains any number
 *      greater than one will be considered invalid.
 * </li>
 * </ul>
 * 
 * If one of the above conventions are not met, the map will be considered 
 * invalid and all processes related to the map will be stopped.
 *
 * @author Jackie Chan
 * Apr 22, 2016
 */
public class MapUtilities {

    
    /** Contains the errors in the map. */
    private static final List<String> STACK = new ArrayList<>();
    
    
    /** Error code for violation of convention 2. */
    private static final int ERROR2 = 2;       
    
    
    /** Error code for violation of convention 4. */
    private static final int ERROR4 = 4;
    
    
    /** Will determine if the line contains a value greater than one. */
    private static final Pattern RECOGNIZEERROR4 = Pattern.compile("[2-9]+");
    
    
    /**
     * Matches "101" one or more times within a String. 
     */
    private static final Pattern P101 = Pattern.compile("(101((01){1,})?)+");
    
    
    /**
     * Matches "1" two or more times within a String.
     */
    private static final Pattern P111 = Pattern.compile("1{2,}");
    
    
    /** A private constructor method so this class cannot be instantiated.*/
    private MapUtilities(){};   
    
    
    /**
     * Checks the syntax contained inside of a map file. If the map is valid, 
     * it will return true, else return false.
     * 
     * @param content   The content in the map file.
     * @return          True if the map is valid; otherwise return false.
     */
    private static boolean performSyntaxChecks(List<String> content) {        
        STACK.clear();
                
        final int specifiedLineLength = content.get(0).length();
        
        for(int i = 0; i < content.size(); i++) {
            String line = content.get(i);
            
            // Check line lengths.
            if(line.length() != specifiedLineLength) 
                addLengthError(i, line.length(), specifiedLineLength);
            
            // Check the line formats.
            if(!(line.startsWith("1") && line.endsWith("1"))) 
                addLineFormatError(ERROR2, i);
            
            Matcher m = RECOGNIZEERROR4.matcher(line);
            
            // Find any number in the line greater than 1.
            while(m.find()) addLineFormatError(ERROR4, i);
        }
        
        // Print the stack.
        if(STACK.isEmpty()) {
            System.out.println("The map is valid.");
            return true;
        } else {
            STACK.stream().forEach((s) -> {
                System.out.println("Error: "+s);
            });
            System.out.println("Map compression aborted.");
            return false;
        }
    }
    
    
    /**
     * Adds a line length error to the STACK. A line length error occurs when a
     * uncompressed line's length is not that of the first line's length.
     * 
     * @param line          The line number that the error occurred on.
     * @param provLength    The length of the line.
     * @param specLength    The length of the first line.
     */
    private static void addLengthError(int line, int provLength, int specLength) {
        STACK.add("LineLengthError at line "
                + line+": Length Specified: " 
                + specLength+" Provided: "+provLength);
    }
    

    /**
     * Adds a line format error to the STACK. A line format error can occur when
     * a line does not start and end with a one. One will also occur when the
     * first or last line contain a number other than one.
     * 
     * @param type          The type of error that occurred.
     * @param lineNumber    The number the error occurred on.
     */
    private static void addLineFormatError(int type, int lineNumber) {
        switch(type) {
            
            case 2:
                STACK.add("LineFormatError at line "
                        + lineNumber+": First and last lines must only contain ones. "
                        + "Lines must start and end with ones."); 
                break;
                
            case 4:
                STACK.add("LineFormatError at line "
                        + lineNumber+": Map must only contain ones and zeros.");
                break;
                
            default:
                break;
        }
    }       

    
    /**
     * Will compress a map file that is not compressed. 
     * 
     * @param filePath      The path to the map file.
     * @throws IOException 
     */
    public static void loadAndCompressMap(String filePath) throws IOException {
        BufferedReader mapReader    = new BufferedReader(new FileReader(filePath));
        PrintWriter mapWriter       = new PrintWriter(filePath+".cmpre");
        
        List<String> content = new ArrayList<>();
        
        String line;
        
        while((line = mapReader.readLine()) != null) {
            content.add(line);
        }
        
        if(!content.isEmpty() && performSyntaxChecks(content)) {
            List<String> compressedContent = compressMapFile(content);            
            
            compressedContent.stream().forEach((s) -> {
                System.out.println(s);
                mapWriter.println(s);
            });
            
            mapWriter.close();
        } else {
            System.out.println("Map file is empty.");
        }
    }
    
    
    /**
     * The main method for map compression. Returns a List with the map in
     * compressed form.
     * 
     * @param content       The map's decompressed content.
     * @return              The map's compressed content.
     * @throws IOException 
     */
    public static List<String> compressMapFile(List<String> content) throws IOException {
        int currentAmount = 1;
        
        List<String> compressedContent = new ArrayList<>();
        
        while(content.size() > 0) {
            String decompressedLine = content.remove(0);
            
            while(content.size() > 0 && content.get(0).equals(decompressedLine)) {
                content.remove(0);
                currentAmount++;
            }
            
            compressedContent.add(getCompressedLine(decompressedLine, currentAmount));
            currentAmount = 1;
        }               
        
        return compressedContent;
    }    
    
    
    /**
     * Will compress a decompressed line and return it.
     * 
     * @param lineToCompress    The line to compress.
     * @param amount            The amount of times that line occurs.
     * @return                  The compressed line.
     */
    private static String getCompressedLine(String lineToCompress, int amount) {
        String compressedLine = "";        
        
        int index = 1;
        
        while(lineToCompress.length() > 0) {
            
            Matcher one01M = P101.matcher(lineToCompress);
            Matcher one11M = P111.matcher(lineToCompress);
            
            /*
                Eat the line. That means only check for conditions at position
                zero then shrink the line. 
            */
            if(one01M.find() && one01M.start() == 0) {
                
                /*
                    end()+index-1 is so in a line that has 10 ones, the compression
                    utility does not make it 11 ones. 
                */
                compressedLine += (one01M.start()+index)+";"+(one01M.end()+index-1)+",";
                index += one01M.end();
                lineToCompress = lineToCompress.substring(one01M.end());
            } else if(one11M.find() && one11M.start() == 0) {
                compressedLine += (one11M.start()+index)+"-"+(one11M.end()+index-1)+",";
                index += one11M.end();
                lineToCompress = lineToCompress.substring(one11M.end());
            } else if(lineToCompress.charAt(0) == '1') {
                compressedLine += index+",";
                lineToCompress = lineToCompress.substring(1);
                index++;
            } else {
                lineToCompress = lineToCompress.substring(1);
                index++;
            }
        }
        
        return compressedLine.substring(0, compressedLine.length()-1)+"+"+amount;
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zad3;


import java.io.File;
import java.util.Scanner;
import org.pcj.NodesDescription;
import org.pcj.PCJ;
import org.pcj.StartPoint;
import org.pcj.RegisterStorage;
import org.pcj.Storage; 

@RegisterStorage(ZAD3.Shared.class)
public class ZAD3 implements StartPoint {
    
    @Storage(ZAD3.class)
    enum Shared {
        vec_b,
    };
    int vec_b_length = 100;
    int vec_b[] = new int[vec_b_length];
    int tab_file[];
    int tab_file_lenght = 0;
    
    
    public void ReadFile() {
        try{
            Scanner scanner = new Scanner(new File("in.txt"));
            int value = 0;

            int line_count = 0;
            while(scanner.hasNextInt()) {
                line_count = line_count + 1;
                value = scanner.nextInt();
            }
            scanner.close();
            
            tab_file_lenght = line_count;
            tab_file = new int[tab_file_lenght];

            
            scanner = new Scanner(new File("in.txt"));
            line_count=0;
            while(scanner.hasNextInt()) {
                value = scanner.nextInt();
                tab_file[line_count] = value;
                line_count = line_count + 1; 
            }
            scanner.close();
        }
        catch(Exception exc){
            if(tab_file_lenght == 0) {
                System.out.println("READING of file failed:");
                System.out.println(exc.toString());
            }
        }
    }
    
    public ZAD3(){

        for(int i = 0; i<vec_b_length; i++) {
            vec_b[i] = 0;
        }
    }
    
    public static void main(String[] args) throws Throwable {
        
        String[] nodes = {"localhost","localhost", "localhost", "localhost"}; 
        NodesDescription nd = new NodesDescription(nodes);
        PCJ.deploy(ZAD3.class, nd);
    }
    
    @Override
    public void main() throws Throwable {

        ReadFile();

        int part = tab_file_lenght / PCJ.threadCount();
        int index_start = PCJ.myId() * part;
        int index_end = (PCJ.myId() +1 ) * part;
        if(PCJ.myId() ==  PCJ.threadCount() - 1) {
            index_end = tab_file_lenght;
        }
        
        for(int i = index_start; i < index_end; i++) {
            int vec_b_index = tab_file[i];
            if((vec_b_index >= 0 ) && (vec_b_index < vec_b_length)) { 
                vec_b[vec_b_index] = vec_b[vec_b_index] + 1;
            }
        }
        
        PCJ.barrier(); 
        
        if (PCJ.myId()==0) {
           for (int th = 1; th < PCJ.threadCount(); th++) {               
               int th_array[] = (int[]) PCJ.get(th, Shared.vec_b); 
                          
               for(int i = 0; i<vec_b_length; i++) {
                 vec_b[i] = vec_b[i] + th_array[i];
               }
               
           }
           
           for(int i = 0; i<vec_b_length; i++) {
                System.out.println("B["+i+"]: " + vec_b[i]);
           }
        }
    }  
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zad2;

import java.util.Random;
import org.pcj.NodesDescription;
import org.pcj.PCJ;
import org.pcj.StartPoint;
import org.pcj.RegisterStorage;
import org.pcj.Storage; 

@RegisterStorage(ZAD2.Shared.class)
public class ZAD2 implements StartPoint {
    @Storage(ZAD2.class)
    enum Shared {
        sum,
    };
    
    int n = 1024;
    int sum = 0;
    int tab_a[] = new int[n];
    Random rand = new Random();
    
    public ZAD2() {
        for(int i = 0; i< n; i++) {
            tab_a[i] = rand.nextInt(2);
        }
    }
    
    public static void main(String[] args) throws Throwable {
        
        String[] nodes = {"localhost", "localhost"}; 
        NodesDescription nd = new NodesDescription(nodes);
        PCJ.deploy(ZAD2.class, nd);
    }
    
    @Override
    public void main() throws Throwable {
        int part = n / PCJ.threadCount();
        int index_start = PCJ.myId() * part;
        int index_end = (PCJ.myId() +1 ) * part;
        if(PCJ.myId() ==  PCJ.threadCount() - 1) {
            index_end = n;
        }
        
        for(int i = index_start; i < index_end; i++) {
            sum = sum + tab_a[i]; 
        }
        
        PCJ.barrier();
        
        if (PCJ.myId()==0) {
           for (int th = 1; th < PCJ.threadCount(); th++) {
               int th_sum = (int) PCJ.get(th, Shared.sum);
               sum = sum + th_sum;
               
           }
          System.out.println("Sum of array a  " + sum);
        }      
    }  
}
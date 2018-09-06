/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zad1;

import java.util.Random;
import org.pcj.NodesDescription;
import org.pcj.PCJ;
import org.pcj.StartPoint;
import org.pcj.RegisterStorage;
import org.pcj.Storage; 

@RegisterStorage(ZAD1.Shared.class)
public class ZAD1 implements StartPoint {
    @Storage(ZAD1.class)            
    enum Shared {
        vec_a,
    };
    
    int n = 1048576;
    int x = 42;
    int vec_a[] = new int[n];
    int vec_b[] = new int[n];
    int vec_c[] = new int[n];
    Random rand = new Random();
    
    public ZAD1() {
        for(int i = 0; i< n; i++) {
            vec_b[i] = rand.nextInt(2);
            vec_c[i] = rand.nextInt(2);
        }
    }
    
    public static void main(String[] args) throws Throwable {
        
        String[] nodes = {"localhost", "localhost", "localhost", "localhost"}; 
        NodesDescription nd = new NodesDescription(nodes);
        PCJ.deploy(ZAD1.class, nd);
    }
    
    @Override
    public void main() throws Throwable {
        
        long startTime = System.currentTimeMillis();
        
        int part = n / PCJ.threadCount();
        int index_start = PCJ.myId() * part;
        int index_end = (PCJ.myId() +1 ) * part;
        if(PCJ.myId() ==  PCJ.threadCount() - 1) {
            index_end = n;
        }
        
        for(int i = index_start; i < index_end; i++) {
            vec_a[i] = (vec_b[i] * x) - vec_c[i];
        }
        
        PCJ.barrier(); 
        
        if (PCJ.myId()==0) {
           for (int th = 1; th < PCJ.threadCount(); th++) {
               
               int th_array[] = (int[]) PCJ.get(th, Shared.vec_a);
   
               index_start = th * part;
               index_end = (th + 1) * part;
               
               for(int i = index_start; i < index_end; i++) {
                 vec_a[i] = th_array[i]; 
               }
               
           }
          System.out.println("A[0]: " + vec_a[0] + "; A[n]:" + vec_a[n-1]);
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Thread: " + PCJ.myId() + "ended in:  " +  elapsedTime + "ms");
    }  
}

/* Time 4 threads: 
Thread: 1ended in:  32ms
Thread: 3ended in:  32ms
Thread: 2ended in:  32ms
Thread: 0ended in:  156ms
*/

/* Time 2 threads:
Thread: 1ended in:  16ms
Thread: 0ended in:  78ms
*/

/* Time 1 thread:
Thread: 0ended in:  16ms
*/